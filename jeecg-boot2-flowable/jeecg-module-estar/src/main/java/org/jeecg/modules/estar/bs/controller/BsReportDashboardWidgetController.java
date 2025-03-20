package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.bs.entity.BsReportDashboardWidget;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardWidgetService;

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
 * @Description: 大屏组件表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="大屏组件表")
@RestController
@RequestMapping("/bs/bsReportDashboardWidget")
@Slf4j
public class BsReportDashboardWidgetController extends JeecgController<BsReportDashboardWidget, IBsReportDashboardWidgetService> {
	@Autowired
	private IBsReportDashboardWidgetService bsReportDashboardWidgetService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsReportDashboardWidget
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "大屏组件表-分页列表查询")
	@ApiOperation(value="大屏组件表-分页列表查询", notes="大屏组件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsReportDashboardWidget bsReportDashboardWidget,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsReportDashboardWidget> queryWrapper = QueryGenerator.initQueryWrapper(bsReportDashboardWidget, req.getParameterMap());
		Page<BsReportDashboardWidget> page = new Page<BsReportDashboardWidget>(pageNo, pageSize);
		IPage<BsReportDashboardWidget> pageList = bsReportDashboardWidgetService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsReportDashboardWidget
	 * @return
	 */
	@AutoLog(value = "大屏组件表-添加")
	@ApiOperation(value="大屏组件表-添加", notes="大屏组件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsReportDashboardWidget bsReportDashboardWidget) {
		bsReportDashboardWidgetService.save(bsReportDashboardWidget);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsReportDashboardWidget
	 * @return
	 */
	@AutoLog(value = "大屏组件表-编辑")
	@ApiOperation(value="大屏组件表-编辑", notes="大屏组件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsReportDashboardWidget bsReportDashboardWidget) {
		bsReportDashboardWidgetService.updateById(bsReportDashboardWidget);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏组件表-通过id删除")
	@ApiOperation(value="大屏组件表-通过id删除", notes="大屏组件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsReportDashboardWidgetService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "大屏组件表-批量删除")
	@ApiOperation(value="大屏组件表-批量删除", notes="大屏组件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsReportDashboardWidgetService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏组件表-通过id查询")
	@ApiOperation(value="大屏组件表-通过id查询", notes="大屏组件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsReportDashboardWidget bsReportDashboardWidget = bsReportDashboardWidgetService.getById(id);
		if(bsReportDashboardWidget==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsReportDashboardWidget);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsReportDashboardWidget
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsReportDashboardWidget bsReportDashboardWidget) {
        return super.exportXls(request, bsReportDashboardWidget, BsReportDashboardWidget.class, "大屏组件表");
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
        return super.importExcel(request, response, BsReportDashboardWidget.class);
    }

}
