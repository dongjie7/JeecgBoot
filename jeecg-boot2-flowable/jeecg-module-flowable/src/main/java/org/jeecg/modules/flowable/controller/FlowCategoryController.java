package org.jeecg.modules.flowable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.flowable.entity.FlowCategory;
import org.jeecg.modules.flowable.service.IFlowCategoryService;

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
 * @Description: 流程分类表
 * @Author: nbacheng
 * @Date:   2022-10-06
 * @Version: V1.0
 */
@Api(tags="流程分类表")
@RestController
@RequestMapping("/flowable/flowCategory")
@Slf4j
public class FlowCategoryController extends JeecgController<FlowCategory, IFlowCategoryService> {
	@Autowired
	private IFlowCategoryService flowCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param flowCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "流程分类表-分页列表查询")
	@ApiOperation(value="流程分类表-分页列表查询", notes="流程分类表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FlowCategory flowCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowCategory> queryWrapper = QueryGenerator.initQueryWrapper(flowCategory, req.getParameterMap());
		Page<FlowCategory> page = new Page<FlowCategory>(pageNo, pageSize);
		IPage<FlowCategory> pageList = flowCategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param flowCategory
	 * @return
	 */
	@AutoLog(value = "流程分类表-添加")
	@ApiOperation(value="流程分类表-添加", notes="流程分类表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlowCategory flowCategory) {
		flowCategoryService.save(flowCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowCategory
	 * @return
	 */
	@AutoLog(value = "流程分类表-编辑")
	@ApiOperation(value="流程分类表-编辑", notes="流程分类表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlowCategory flowCategory) {
		flowCategoryService.updateById(flowCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流程分类表-通过id删除")
	@ApiOperation(value="流程分类表-通过id删除", notes="流程分类表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flowCategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "流程分类表-批量删除")
	@ApiOperation(value="流程分类表-批量删除", notes="流程分类表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流程分类表-通过id查询")
	@ApiOperation(value="流程分类表-通过id查询", notes="流程分类表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FlowCategory flowCategory = flowCategoryService.getById(id);
		if(flowCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowCategory flowCategory) {
        return super.exportXls(request, flowCategory, FlowCategory.class, "流程分类表");
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
        return super.importExcel(request, response, FlowCategory.class);
    }

}
