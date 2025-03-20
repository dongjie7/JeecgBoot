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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.dto.ProjectDto;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectCollect;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectCollectService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
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
 * @Description: 项目表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Api(tags="项目表")
@RestController
@RequestMapping("/tw/twProject")
@Slf4j
public class TwProjectController extends JeecgController<TwProject, ITwProjectService> {
	@Autowired
	private ITwProjectService twProjectService;
	
	@Autowired
	ITwProjectCollectService  ProjectCollectService;
	
	@Autowired
	TwProjectMapper ProjectMapper;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目表-分页列表查询")
	@ApiOperation(value="项目表-分页列表查询", notes="项目表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProject twProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProject> queryWrapper = QueryGenerator.initQueryWrapper(twProject, req.getParameterMap());
		Page<TwProject> page = new Page<TwProject>(pageNo, pageSize);
		queryWrapper.eq("deleted", 0);
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		
		IPage<TwProject> pageList = twProjectService.page(page, queryWrapper);
		List<TwProject> projectlist = pageList.getRecords();
		List<TwProject> resultpjlist = new ArrayList<TwProject>();
		if(!CollectionUtils.isEmpty(projectlist)){
			for(TwProject project : projectlist) {
				TwProjectCollect twProjectCollect = ProjectCollectService.queryByProjectIdAndUserId(project.getId(),userId);
				if (twProjectCollect!=null) {
					project.setCollected(1);
				}
				else {
					project.setCollected(0);
				}
				resultpjlist.add(project);
			}
		}
		pageList.setRecords(resultpjlist);
		return Result.OK(pageList);
	}
	
	/**
	 * 收藏分页列表查询
	 *
	 * @param twProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目表-收藏分页列表查询")
	@ApiOperation(value="项目表-收藏分页列表查询", notes="项目表-收藏分页列表查询")
	@GetMapping(value = "/collectlist")
	public Result<?> queryCollectList(TwProject twProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		Page<TwProject> page = new Page<TwProject>(pageNo, pageSize);
		IPage<TwProject> pageList = twProjectService.queryCollectList(page, userId);
		List<TwProject> projectlist = pageList.getRecords();
		List<TwProject> resultpjlist = new ArrayList<TwProject>();
		if(!CollectionUtils.isEmpty(projectlist)){
			for(TwProject project : projectlist) {
				TwProjectCollect twProjectCollect = ProjectCollectService.queryByProjectIdAndUserId(project.getId(),userId);
				if (twProjectCollect!=null) {
					project.setCollected(1);
				}
				else {
					project.setCollected(0);
				}
				resultpjlist.add(project);
			}
		}
		pageList.setRecords(resultpjlist);
		return Result.OK(pageList);
	}
	
	/**
	 * 归档分页列表查询
	 *
	 * @param twProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目表-归档分页列表查询")
	@ApiOperation(value="项目表-归档分页列表查询", notes="项目表-归档分页列表查询")
	@GetMapping(value = "/archivelist")
	public Result<?> archivePageList(TwProject twProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProject> queryWrapper = QueryGenerator.initQueryWrapper(twProject, req.getParameterMap());
		Page<TwProject> page = new Page<TwProject>(pageNo, pageSize);
		queryWrapper.eq("archive", 1);
		IPage<TwProject> pageList = twProjectService.page(page, queryWrapper);	
		return Result.OK(pageList);
	}
	
	/**
	 * 回收站分页列表查询
	 *
	 * @param twProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目表-回收站分页列表查询")
	@ApiOperation(value="项目表-回收站分页列表查询", notes="项目表-回收站分页列表查询")
	@GetMapping(value = "/recyclelist")
	public Result<?> recyclePageList(TwProject twProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProject> queryWrapper = QueryGenerator.initQueryWrapper(twProject, req.getParameterMap());
		Page<TwProject> page = new Page<TwProject>(pageNo, pageSize);
		queryWrapper.eq("deleted", 1);
		IPage<TwProject> pageList = twProjectService.page(page, queryWrapper);	
		return Result.OK(pageList);
	}
	
	/**
	 * 分页列表查询 我的项目详细页面初始化
	 *
	 * @param twProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目表-初始化分页列表查询")
	@ApiOperation(value="项目表-初始化分页列表查询", notes="项目表-初始化分页列表查询")
	@PostMapping(value = "/selfList")
	public Result<?> querySelfList(@RequestBody ProjectDto projectDto) {	
		return twProjectService.querySelfList(projectDto);
	}
	
	/**
	 *   项目设置获取
	 *
	 * @param projectId
	 * @return
	 */
	@AutoLog(value = "项目表-项目设置获取")
	@ApiOperation(value="项目表-项目设置获取", notes="项目表-项目设置获取")
	@PostMapping(value = "/projectset")
	public Result<?> projectset(@RequestParam(name="id",required=true) String id) {
		return Result.OK(twProjectService.projectSet(id));
	}
	
	/**
	 *   添加
	 *
	 * @param twProject
	 * @return
	 */
	@AutoLog(value = "项目表-添加")
	@ApiOperation(value="项目表-添加", notes="项目表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProject twProject) {
		twProjectService.saveProject(twProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twProject
	 * @return
	 */
	@AutoLog(value = "项目表-编辑")
	@ApiOperation(value="项目表-编辑", notes="项目表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProject twProject) {
		twProjectService.updateById(twProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   退出
	 *
	 * @param twProject
	 * @return
	 */
	@AutoLog(value = "项目表-添加")
	@ApiOperation(value="项目表-添加", notes="项目表-添加")
	@PostMapping(value = "/quit")
	public Result<?> quit(@RequestParam(name="id",required=true) String id) {
		if(twProjectService.quitProject(id)) {
			return Result.OK("退出成功！");
		}else {
			return Result.error("退出失败！");
		}
	}
	
	/**
	 *   通过id归档
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id归档")
	@ApiOperation(value="项目表-通过id归档", notes="项目表-通过id归档")
	@PostMapping(value = "/archive")
	public Result<?> archive(@RequestParam(name="id",required=true) String id) {
		twProjectService.archive(id);
		return Result.OK("归档成功!");
	}
	
	/**
	 *   通过id取消归档
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id取消归档")
	@ApiOperation(value="项目表-通过id取消归档", notes="项目表-通过id取消归档")
	@PostMapping(value = "/recoveryArchive")
	public Result<?> recoveryArchive(@RequestParam(name="id",required=true) String id) {
		twProjectService.recoveryArchive(id);
		return Result.OK("取消归档成功!");
	}
	
	/**
	 *   通过id放到回收站
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id放到回收站")
	@ApiOperation(value="项目表-通过id放到回收站", notes="项目表-通过id放到回收站")
	@PostMapping(value = "/recycle")
	public Result<?> recycle(@RequestParam(name="id",required=true) String id) {
		twProjectService.recycle(id);
		return Result.OK("放到回收站成功!");
	}
	
	/**
	 *   通过id恢复项目
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id恢复项目")
	@ApiOperation(value="项目表-通过id恢复项目", notes="项目表-通过id恢复项目")
	@PostMapping(value = "/recovery")
	public Result<?> recovery(@RequestParam(name="id",required=true) String id) {
		twProjectService.recovery(id);
		return Result.OK("恢复项目成功!");
	}
	
	/**
	 *   通过id收藏项目
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id收藏项目")
	@ApiOperation(value="项目表-通过id收藏项目", notes="项目表-通过id收藏项目")
	@PostMapping(value = "/collect")
	public Result<?> collect(@RequestParam(name="id",required=true) String id,
			                 @RequestParam(name="type",required=true) String type) {
		return twProjectService.collect(id,type);
	}
	
	/**
     * 得到自己的项目日志
     * @param
     * @return
     */
    @PostMapping("/getLogBySelfProject")
    @ResponseBody
    public Result<?> getLogBySelfProject(@RequestParam Map<String,Object> mmap){
    	return twProjectService.getLogBySelfProject(mmap);
    }

    /**
     * 得到自己的项目统计信息
     * @param
     * @return
     */
    @PostMapping("/projectStats")
    @ResponseBody
    public Result<?> projectStats(@RequestParam(name="projectId",required=true) String projectId)  throws Exception {
    	return twProjectService.projectStats(projectId);
    }

    /**
     * 得到自己的项目报表
     * @param
     * @return
     */
    @PostMapping("/getProjectReport")
    @ResponseBody
    public Result<?> getProjectReport(@RequestParam(name="projectId",required=true) String projectId)  throws Exception {
    	return twProjectService.getProjectReport(projectId);
    }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id删除")
	@ApiOperation(value="项目表-通过id删除", notes="项目表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目表-批量删除")
	@ApiOperation(value="项目表-批量删除", notes="项目表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目表-通过id查询")
	@ApiOperation(value="项目表-通过id查询", notes="项目表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProject twProject = twProjectService.getById(id);
		if(twProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProject
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProject twProject) {
        return super.exportXls(request, twProject, TwProject.class, "项目表");
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
        return super.importExcel(request, response, TwProject.class);
    }

}
