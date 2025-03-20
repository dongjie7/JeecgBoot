package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwTaskStages;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;

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

 /**
 * @Description: 任务列表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Api(tags="任务列表")
@RestController
@RequestMapping("/tw/twTaskStages")
@Slf4j
public class TwTaskStagesController extends JeecgController<TwTaskStages, ITwTaskStagesService> {
	@Autowired
	private ITwTaskStagesService twTaskStagesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTaskStages
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务列表-分页列表查询")
	@ApiOperation(value="任务列表-分页列表查询", notes="任务列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTaskStages twTaskStages,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTaskStages> queryWrapper = QueryGenerator.initQueryWrapper(twTaskStages, req.getParameterMap());
		Page<TwTaskStages> page = new Page<TwTaskStages>(pageNo, pageSize);
		IPage<TwTaskStages> pageList = twTaskStagesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   根据projectId 项目设置 任务流转 创建规则打开
	 *
	 * @param projectId
	 * @return
	 */
	@AutoLog(value = "任务列表-获取任务列表")
	@ApiOperation(value="任务列表-获取任务列表", notes="任务列表-获取任务列表")
	@PostMapping(value = "/getStages")
	@ResponseBody
	public Result<?> getTaskStages(@RequestParam Map<String,Object> mmap) {
		return twTaskStagesService.getTaskStages(mmap);
	}
	
	/**
	 *   我的项目打开后的任务清单
	 *
	 * @param tasksmap
	 * @return
	 */
	@AutoLog(value = "任务列表-获取任务列表")
	@ApiOperation(value="任务列表-获取任务列表", notes="任务列表-获取任务列表")
	@PostMapping(value = "/getTasks")
	public Result<?> getStagesTasks(@RequestParam Map<String,Object> tasksmap) {
		return twTaskStagesService.getStagesTasks(tasksmap);
	}
	
	/**
	 *   已做任务
	 *
	 * @param Map<String,Object> mmap
	 * @return
	 */
	@AutoLog(value = "项目任务表-已做任务")
	@ApiOperation(value="项目任务表-已做任务", notes="项目任务表-已做任务")
	@PostMapping(value = "/taskDone")
	public Result<?> taskDone(@RequestParam Map<String,Object> mmap) {
		return twTaskStagesService.taskDone(mmap);
	}
	
	/**
	 *   任务排序
	 *
	 * @param Map<String,Object> mmap
	 * @return
	 */
	@AutoLog(value = "项目任务表-任务排序")
	@ApiOperation(value="项目任务表-任务排序", notes="项目任务表-任务排序")
	@PostMapping(value = "/sortTask")
	public Result<?> sortTask(@RequestParam Map<String,Object> mmap) {
		return twTaskStagesService.sortTask(mmap);
	}
	
	/**
	 *   添加
	 *
	 * @param twTaskStages
	 * @return
	 */
	@AutoLog(value = "任务列表-添加")
	@ApiOperation(value="任务列表-添加", notes="任务列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTaskStages twTaskStages) {
		twTaskStagesService.save(twTaskStages);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twTaskStages
	 * @return
	 */
	@AutoLog(value = "任务列表-编辑")
	@ApiOperation(value="任务列表-编辑", notes="任务列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTaskStages twTaskStages) {
		twTaskStagesService.updateById(twTaskStages);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务列表-通过id删除")
	@ApiOperation(value="任务列表-通过id删除", notes="任务列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskStagesService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务列表-批量删除")
	@ApiOperation(value="任务列表-批量删除", notes="任务列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskStagesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务列表-通过id查询")
	@ApiOperation(value="任务列表-通过id查询", notes="任务列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTaskStages twTaskStages = twTaskStagesService.getById(id);
		if(twTaskStages==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTaskStages);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTaskStages
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTaskStages twTaskStages) {
        return super.exportXls(request, twTaskStages, TwTaskStages.class, "任务列表");
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
        return super.importExcel(request, response, TwTaskStages.class);
    }

}
