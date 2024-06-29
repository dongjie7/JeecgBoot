package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.estar.tw.entity.TwTaskWorkflow;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 任务工作流表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
@Api(tags="任务工作流表")
@RestController
@RequestMapping("/tw/twTaskWorkflow")
@Slf4j
public class TwTaskWorkflowController extends JeecgController<TwTaskWorkflow, ITwTaskWorkflowService> {
	@Autowired
	private ITwTaskWorkflowService twTaskWorkflowService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTaskWorkflow
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务工作流表-分页列表查询")
	@ApiOperation(value="任务工作流表-分页列表查询", notes="任务工作流表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTaskWorkflow twTaskWorkflow,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTaskWorkflow> queryWrapper = QueryGenerator.initQueryWrapper(twTaskWorkflow, req.getParameterMap());
		Page<TwTaskWorkflow> page = new Page<TwTaskWorkflow>(pageNo, pageSize);
		IPage<TwTaskWorkflow> pageList = twTaskWorkflowService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   根据projectId获取任务工作流列表
	 *
	 * @param twTaskWorkflow
	 * @return
	 */
	@AutoLog(value = "任务工作流表-获取任务工作流列表")
	@ApiOperation(value="任务工作流表-获取任务工作流列表", notes="任务工作流表-获取任务工作流列表")
	@PostMapping(value = "/getlist")
	public Result<?> getList(@RequestParam(name="projectId",required=true) String projectId) {
		return Result.OK(twTaskWorkflowService.getList(projectId));
	}
	
	/**
	 *   保存并添加规则
	 *
	 * @param 
	 * @return
	 */
	@AutoLog(value = "任务工作流表-保存并添加规则")
	@ApiOperation(value="任务工作流表-保存并添加规则", notes="任务工作流表-保存并添加规则")
	@PostMapping(value = "/saveandrules")
	public Result<?> saveAndRules(@RequestParam(name="projectId",required=true) String projectId,
			@RequestParam(name="organizationId",required=true) String organizationId,
			@RequestParam(name="taskWorkflowName",required=true) String taskWorkflowName,
			@RequestParam(name="taskWorkflowRules",required=true) String taskWorkflowRules) {
		if(twTaskWorkflowService.saveAndRules(projectId, organizationId, taskWorkflowName, taskWorkflowRules)) {
			return Result.OK("保存并添加规则成功！");
		}
		else {
			return Result.OK("保存并添加规则失败！");
		}
		
	}
	
	/**
	 *   编辑并修改规则
	 *
	 * @param 
	 * @return
	 */
	@AutoLog(value = "任务工作流表-编辑并修改规则")
	@ApiOperation(value="任务工作流表-编辑并修改规则", notes="任务工作流表-编辑并修改规则")
	@PostMapping(value = "/editandrules")
	public Result<?> editAndRules(@RequestParam(name="id",required=true) String id,
			@RequestParam(name="taskWorkflowName",required=true) String taskWorkflowName,
			@RequestParam(name="taskWorkflowRules",required=true) String taskWorkflowRules) {
		if(twTaskWorkflowService.editAndRules(id, taskWorkflowName, taskWorkflowRules)) {
			return Result.OK("编辑并修改规则成功！");
		}
		else {
			return Result.OK("编辑并修改规则失败！");
		}
		
	}
	
	/**
	 *   删除并移除规则
	 *
	 * @param 
	 * @return
	 */
	@AutoLog(value = "任务工作流表-删除并移除规则")
	@ApiOperation(value="任务工作流表-删除并移除规则", notes="任务工作流表-删除并移除规则")
	@PostMapping(value = "/removeandrules")
	public Result<?> removeAndRules(@RequestParam(name="id",required=true) String id) {
		if(twTaskWorkflowService.removeAndRules(id)) {
			return Result.OK("删除并移除规则成功！");
		}
		else {
			return Result.OK("删除并移除规则失败！");
		}
		
	}
	
	/**
	 *   添加
	 *
	 * @param twTaskWorkflow
	 * @return
	 */
	@AutoLog(value = "任务工作流表-添加")
	@ApiOperation(value="任务工作流表-添加", notes="任务工作流表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTaskWorkflow twTaskWorkflow) {
		twTaskWorkflowService.save(twTaskWorkflow);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twTaskWorkflow
	 * @return
	 */
	@AutoLog(value = "任务工作流表-编辑")
	@ApiOperation(value="任务工作流表-编辑", notes="任务工作流表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTaskWorkflow twTaskWorkflow) {
		twTaskWorkflowService.updateById(twTaskWorkflow);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务工作流表-通过id删除")
	@ApiOperation(value="任务工作流表-通过id删除", notes="任务工作流表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskWorkflowService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务工作流表-批量删除")
	@ApiOperation(value="任务工作流表-批量删除", notes="任务工作流表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskWorkflowService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务工作流表-通过id查询")
	@ApiOperation(value="任务工作流表-通过id查询", notes="任务工作流表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTaskWorkflow twTaskWorkflow = twTaskWorkflowService.getById(id);
		if(twTaskWorkflow==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTaskWorkflow);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTaskWorkflow
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTaskWorkflow twTaskWorkflow) {
        return super.exportXls(request, twTaskWorkflow, TwTaskWorkflow.class, "任务工作流表");
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
        return super.importExcel(request, response, TwTaskWorkflow.class);
    }

}
