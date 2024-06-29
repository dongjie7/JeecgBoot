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
import org.jeecg.modules.estar.tw.entity.TwTaskWorkflowRule;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowRuleService;

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
 * @Description: 任务工作流规则表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
@Api(tags="任务工作流规则表")
@RestController
@RequestMapping("/tw/twTaskWorkflowRule")
@Slf4j
public class TwTaskWorkflowRuleController extends JeecgController<TwTaskWorkflowRule, ITwTaskWorkflowRuleService> {
	@Autowired
	private ITwTaskWorkflowRuleService twTaskWorkflowRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTaskWorkflowRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-分页列表查询")
	@ApiOperation(value="任务工作流规则表-分页列表查询", notes="任务工作流规则表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTaskWorkflowRule twTaskWorkflowRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTaskWorkflowRule> queryWrapper = QueryGenerator.initQueryWrapper(twTaskWorkflowRule, req.getParameterMap());
		Page<TwTaskWorkflowRule> page = new Page<TwTaskWorkflowRule>(pageNo, pageSize);
		IPage<TwTaskWorkflowRule> pageList = twTaskWorkflowRuleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   根据workflowId获取任务流程规则
	 *
	 * @param workflowId
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-添加")
	@ApiOperation(value="任务工作流规则表-添加", notes="任务工作流规则表-添加")
	@PostMapping(value = "/getrules")
	public Result<?> getRules(@RequestParam(name="workflowId",required=true) String workflowId) {
		return twTaskWorkflowRuleService.getRules(workflowId);
	}
	
	/**
	 *   添加
	 *
	 * @param twTaskWorkflowRule
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-添加")
	@ApiOperation(value="任务工作流规则表-添加", notes="任务工作流规则表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTaskWorkflowRule twTaskWorkflowRule) {
		twTaskWorkflowRuleService.save(twTaskWorkflowRule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twTaskWorkflowRule
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-编辑")
	@ApiOperation(value="任务工作流规则表-编辑", notes="任务工作流规则表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTaskWorkflowRule twTaskWorkflowRule) {
		twTaskWorkflowRuleService.updateById(twTaskWorkflowRule);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-通过id删除")
	@ApiOperation(value="任务工作流规则表-通过id删除", notes="任务工作流规则表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskWorkflowRuleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-批量删除")
	@ApiOperation(value="任务工作流规则表-批量删除", notes="任务工作流规则表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskWorkflowRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务工作流规则表-通过id查询")
	@ApiOperation(value="任务工作流规则表-通过id查询", notes="任务工作流规则表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTaskWorkflowRule twTaskWorkflowRule = twTaskWorkflowRuleService.getById(id);
		if(twTaskWorkflowRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTaskWorkflowRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTaskWorkflowRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTaskWorkflowRule twTaskWorkflowRule) {
        return super.exportXls(request, twTaskWorkflowRule, TwTaskWorkflowRule.class, "任务工作流规则表");
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
        return super.importExcel(request, response, TwTaskWorkflowRule.class);
    }

}
