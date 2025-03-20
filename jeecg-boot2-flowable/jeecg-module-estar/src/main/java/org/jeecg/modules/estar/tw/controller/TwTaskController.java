package org.jeecg.modules.estar.tw.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.service.ITwTaskLikeService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.service.ITwTaskTagService;
import org.jeecg.modules.estar.tw.service.ITwTaskToTagService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkTimeService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.util.ObjectUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.estar.tw.entity.TwTaskLike;
import org.jeecg.modules.estar.tw.entity.TwTaskWorkTime;
import org.jeecg.modules.estar.tw.entity.TwTaskToTag;

 /**
 * @Description: 项目任务表
 * @Author: nbacheng
 * @Date:   2023-07-01
 * @Version: V1.0
 */
@Api(tags="项目任务表")
@RestController
@RequestMapping("/tw/twTask")
@Slf4j
public class TwTaskController extends JeecgController<TwTask, ITwTaskService> {
	@Autowired
	private ITwTaskService twTaskService;
	@Autowired
	ITwTaskTagService taskTagService;
	@Autowired
	ITwTaskLikeService taskLikeService;
	@Autowired
	ITwTaskWorkTimeService taskWorkTimeService;
	@Autowired
	ITwTaskToTagService taskToTagService;
	
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目任务表-分页列表查询")
	@ApiOperation(value="项目任务表-分页列表查询", notes="项目任务表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTask twTask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTask> queryWrapper = QueryGenerator.initQueryWrapper(twTask, req.getParameterMap());
		Page<TwTask> page = new Page<TwTask>(pageNo, pageSize);
		IPage<TwTask> pageList = twTaskService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	@PostMapping("/listByMember")
    @ResponseBody
    public Result<?> taskIndex(@RequestParam Map<String,Object> mmap)  throws Exception
    {
        SysUser loginUser = iEstarThirdService.getLoginUser();
        
        IPage<Map> page = Constant.createPage(mmap);
        mmap.put("memberId",loginUser.getUsername());
        page = twTaskService.taskIndex(page,mmap);
        return Result.OK(Constant.createPageResultMap(page));
    }
	//点赞
	@PostMapping("/like")
    @ResponseBody
    public Result<?> like(@RequestParam Map<String,Object> mmap)  throws Exception {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        Integer data = MapUtils.getInteger(mmap,"like");
        String id = MapUtils.getString(mmap,"taskId");
        if(StringUtils.isEmpty(id)){
            return Result.error("请选择一个任务！");
        }
        Map taskMap = twTaskService.getTaskMapById(id);
        if(MapUtils.isEmpty(taskMap)){
            return Result.error("该任务已失效！");
        }
        Map taskMapNoDel = twTaskService.getTaskByIdNoDel(id);
        if(MapUtils.isEmpty(taskMapNoDel)){
            return Result.error("该任务在回收站中不能点赞！");
        }
        taskLikeService.like(taskMap,loginUser.getUsername(),data);
        return Result.OK();
    }
	//收藏
	@PostMapping("/star")
    @ResponseBody
    public Result<?> star(@RequestParam Map<String,Object> mmap)  throws Exception {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        Integer data = MapUtils.getInteger(mmap,"star");
        String id = MapUtils.getString(mmap,"taskId");
        if(StringUtils.isEmpty(id)){
            return Result.error("请选择一个任务！");
        }
        Map taskMap = twTaskService.getTaskMapById(id);
        if(MapUtils.isEmpty(taskMap)){
            return Result.error("该任务已失效！");
        }
        Map taskMapNoDel = twTaskService.getTaskByIdNoDel(id);
        if(MapUtils.isEmpty(taskMapNoDel)){
            return Result.error("该任务在回收站中不能收藏！");
        }
        twTaskService.star(taskMap,loginUser.getUsername(),data);
        return Result.OK();
    }
	
	 /**
     * 打开任务详情 编辑工时
     * @param mmap
     * @return
     */
    @PostMapping("/editTaskWorkTime")
    @ResponseBody
    public Result<?> taskEditTaskWorkTime(@RequestParam Map<String,Object> mmap)  throws Exception {
    	String beginTime = MapUtils.getString(mmap, "beginTime");
        Integer num = MapUtils.getInteger(mmap, "num");
        String content = MapUtils.getString(mmap, "content");
        String taskId = MapUtils.getString(mmap, "taskId");
        String id = MapUtils.getString(mmap, "id");
        Map taskWorkTimeMap = taskWorkTimeService.getTaskWorkTimeById(id);
        TwTaskWorkTime taskWorkTime = new TwTaskWorkTime();
        taskWorkTime.setId(id);
        taskWorkTime.setBeginTime(DateUtil.stringToDate(beginTime));
        taskWorkTime.setContent(content);
        taskWorkTime.setCreateTime(DateUtil.stringToDate(MapUtils.getString(taskWorkTimeMap,"create_time")));
        taskWorkTime.setMemberId(MapUtils.getString(taskWorkTimeMap,"memberId"));
        taskWorkTime.setNum(num);taskWorkTime.setTaskId(taskId);
        return Result.OK(taskWorkTimeService.updateById(taskWorkTime));
    }

    /**
     * 打开任务详情 添加工时
     * @param mmap
     * @return
     */
    @PostMapping("/saveTaskWorkTime")
    @ResponseBody
    public Result<?> taskSaveTaskWorkTime(@RequestParam Map<String,Object> mmap)  throws Exception {
        SysUser loginUser = iEstarThirdService.getLoginUser();
        String beginTime = MapUtils.getString(mmap, "beginTime");
        Integer num = MapUtils.getInteger(mmap, "num");
        String content = MapUtils.getString(mmap, "content");
        String taskId = MapUtils.getString(mmap, "taskId");


        TwTaskWorkTime taskWorkTime = new TwTaskWorkTime();
        taskWorkTime.setBeginTime(DateUtil.stringHHmmToDate(beginTime));
        taskWorkTime.setContent(content);
        taskWorkTime.setCreateTime(new Date());
        taskWorkTime.setMemberId(loginUser.getUsername());
        taskWorkTime.setNum(num);taskWorkTime.setTaskId(taskId);
        return  Result.OK(taskWorkTimeService.save(taskWorkTime));
    }
    
    /**
     *打开任务详情 编辑工时
     * @param mmap
     * @return
     */
    @PostMapping("/delTaskWorkTime")
    @ResponseBody
    public Result<?> taskDelTaskWorkTime(@RequestParam Map<String,Object> mmap)  throws Exception {
        String id = MapUtils.getString(mmap, "id");
        return Result.OK(taskWorkTimeService.delTaskWorkTimeById(id));
    }
	
	/**
     * 打开任务详情
     * @param mmap
     * @return
     */
    @PostMapping("/taskSources")
    @ResponseBody
    public Result<?>  taskSources(@RequestParam Map<String,Object> mmap)  throws Exception
    {
    	return Result.OK(twTaskService.taskSources(mmap));
    }


    /**
     * 打开任务详情
     * @param mmap
     * @return
     */
    @PostMapping("/taskWorkTimeList")
    @ResponseBody
    public Result<?>  taskWorkTimeList(@RequestParam Map<String,Object> mmap)  throws Exception
    {
    	return Result.OK(twTaskService.taskWorkTimeList(mmap));
    }
	
	@PostMapping("/recovery")
    @ResponseBody
    public Result<?> recovery(@RequestParam Map<String,Object> mmap)  throws Exception {
        String taskId = MapUtils.getString(mmap,"taskId");
        return twTaskService.recovery(taskId);
    }
	
	@PostMapping("/taskRead")
    @ResponseBody
    public Result<?> taskRead(@RequestParam Map<String,Object> mmap)  throws Exception
    {
        return Result.OK(twTaskService.readTask(mmap));
    }
	
	/**
    *
    * 打开任务详情 设置任务结束时间、备注、优先级
    * @param mmap
    * @return
    */
	@PostMapping("/getTaskMembers")
    @ResponseBody
    public Result<?> getTaskMembers(@RequestParam Map<String,Object> mmap) 
    {
        return twTaskService.getTaskMembers(mmap);
    }
	
	@PostMapping("/batchAssignTask")
    @ResponseBody
    public Result<?> batchAssignTask(@RequestParam Map<String,Object> mmap) 
    {
        return twTaskService.batchAssignTask(mmap);
    }
	
	@PostMapping("/assignTask")
    @ResponseBody
    public Result<?> assignTask(@RequestParam Map<String,Object> mmap) 
    {
		String taskId = MapUtils.getString(mmap,"taskId");
        String executorId = MapUtils.getString(mmap,"executorId");
        if(StringUtils.isEmpty(taskId)){
            return Result.error("请选择任务");
        }
        return twTaskService.assignTask(mmap);
    }
	
	@PostMapping("/taskStagesSave")
    @ResponseBody
    public Result<?> taskStagesSave(@RequestParam Map<String,Object> mmap)
    {	
		return twTaskService.taskStagesSave(mmap);
    }
	
	@PostMapping("/taskStagesEdit")
    @ResponseBody
    public Result<?> taskStagesEdit(@RequestParam Map<String,Object> mmap)
    {	
		return twTaskService.taskStagesEdit(mmap);
    }
	
	@PostMapping("/taskStagesDel")
    @ResponseBody
    public Result<?> taskStagesDel(@RequestParam Map<String,Object> mmap)
    {	
		return twTaskService.taskStagesDel(mmap);
    }
	
	@PostMapping("/taskRecycle")
    @ResponseBody
    public Result<?> taskRecycle(@RequestParam Map<String,Object> mmap)  throws Exception {
        String taskId = MapUtils.getString(mmap,"taskId");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        twTaskService.taskRecycle(taskId,loginUser.getUsername());
        return  Result.OK("恢复任务成功");
    }
	
	@PostMapping("/setPrivate")
    @ResponseBody
    public Result<?> setPrivate(@RequestParam Map<String,Object> mmap)  throws Exception {
        Integer privated=MapUtils.getInteger(mmap,"privated");
        String taskId = MapUtils.getString(mmap,"taskId");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        if( 0==privated || 1==privated){
            TwTask task = twTaskService.getTaskById(taskId);
            twTaskService.edit(task,loginUser.getUsername());
        }
        return  Result.OK("设置私有成功");
    }
	
	/**
     * 任务详情 新建标签保存
     * @param mmap
     * @return
     */
    @PostMapping("/taskSetTag")
    @ResponseBody
    public Result<?> taskSetTag(@RequestParam Map<String,Object> mmap)  throws Exception {
        String taskId = MapUtils.getString(mmap, "taskCode");
        String tagId = MapUtils.getString(mmap, "tagCode");
        TwTaskToTag taskToTag = new TwTaskToTag();
        taskToTag.setTaskId(taskId);
        taskToTag.setTagId(tagId);
        Map taskToTagMap = taskToTagService.getTaskToTagByTagIdAndTaskId(tagId,taskId);
        boolean bo = true;
        if(MapUtils.isEmpty(taskToTagMap)){
            bo = taskToTagService.save(taskToTag);
        }else{
            bo = taskToTagService.removeById(MapUtils.getString(taskToTagMap,"id"));
        }
        return  Result.OK(bo);
    }
	
	@PostMapping("/recycleBatch")
    @ResponseBody
    public Result<?> recycleBatch(@RequestParam Map<String,Object> mmap)  throws Exception {
        String stageId = MapUtils.getString(mmap,"stageId");
        return twTaskService.recycleBatch(stageId);
    }
	
	@PostMapping("/taskSave")
    @ResponseBody
    public Result<?> taskSave(@RequestParam Map<String,Object> mmap)  throws Exception {
        return twTaskService.taskSave(mmap);
    }
	
	@PostMapping("/getListByTaskTag")
    @ResponseBody
    public Result<?> getListByTaskTag(@RequestParam Map<String,Object> mmap)  throws Exception {
        String taskTagId = MapUtils.getString(mmap, "taskTagId");
        IPage<Map> page = Constant.createPage(mmap);
        page = taskTagService.selectListByTaskTag(page,taskTagId);
        return Result.OK(Constant.createPageResultMap(page));
    }
	
	/**
	 *   添加
	 *
	 * @param twTask
	 * @return
	 */
	@AutoLog(value = "项目任务表-添加")
	@ApiOperation(value="项目任务表-添加", notes="项目任务表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTask twTask) {
		twTaskService.save(twTask);
		return Result.OK("添加成功！");
	}
	
	
	/**
    *
    * 任务详情 设置任务结束时间、备注、优先级
    * @param mmap
    * @return
    */
   @PostMapping("/taskEdit")
   @ResponseBody
   public Result<?> taskEdit(@RequestParam Map<String,Object> mmap)   {

       String taskId = MapUtils.getString(mmap,"taskId");

       if(StringUtils.isEmpty(taskId)){
           return Result.error("请选择一个任务");
       }

       TwTask task = twTaskService.lambdaQuery().eq(TwTask::getId,taskId).one();
       if(ObjectUtil.isEmpty(task)){
           return Result.error("该任务已失效");
       }
       return twTaskService.edit(taskId,mmap);

   }
   
   /**
   *
   * 任务详情 添加评论
   * @param mmap
   * @return
   */
  @PostMapping("/createComment")
  @ResponseBody
  public Result<?> taskCreateComment(@RequestParam Map<String,Object> mmap)  throws Exception {
      return twTaskService.createComment(mmap);
  }
  
  /**
   * 任务清单 打开任务详情
   * @param mmap
   * @return
   */
  @PostMapping("/taskLog")
  @ResponseBody
  public Result<?> taskLog(@RequestParam Map<String,Object> mmap)  throws Exception
  {
	  return Result.OK(twTaskService.taskLog(mmap));

  }
  
  @PostMapping("/dateTotalForProject")
  @ResponseBody
  public Result<?> dateTotalForProject(@RequestParam Map<String,Object> mmap)  throws Exception {
	  return twTaskService.dateTotalForProject(mmap);
  }
  
  /**
   *打开任务详情 添加加标签初始化
   * @param mmap
   * @return
   */
  @PostMapping("/taskToTags")
  @ResponseBody
  public Result<?> taskToTags(@RequestParam Map<String,Object> mmap)  throws Exception
  {
	  return twTaskService.taskToTags(mmap);
  }
  
  /**
   * 甘特图任务列表
   * @param mmap
   * @return
   */
  @PostMapping("/taskGantt")
  @ResponseBody
  public Result<?> taskGantt(@RequestParam Map<String,Object> mmap)  throws Exception
  {
	  return twTaskService.taskGantt(mmap);

  }
	
	/**
	 *  编辑
	 *
	 * @param twTask
	 * @return
	 */
	@AutoLog(value = "项目任务表-编辑")
	@ApiOperation(value="项目任务表-编辑", notes="项目任务表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTask twTask) {
		twTaskService.updateById(twTask);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目任务表-通过id删除")
	@ApiOperation(value="项目任务表-通过id删除", notes="项目任务表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目任务表-批量删除")
	@ApiOperation(value="项目任务表-批量删除", notes="项目任务表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目任务表-通过id查询")
	@ApiOperation(value="项目任务表-通过id查询", notes="项目任务表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTask twTask = twTaskService.getById(id);
		if(twTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTask twTask) {
        return super.exportXls(request, twTask, TwTask.class, "项目任务表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TwTask.class);
    }

}
