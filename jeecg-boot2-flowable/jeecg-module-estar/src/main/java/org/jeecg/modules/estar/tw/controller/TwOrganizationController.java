package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwOrganization;
import org.jeecg.modules.estar.tw.service.ITwOrganizationService;

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
 * @Description: 项目组织表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Api(tags="项目组织表")
@RestController
@RequestMapping("/tw/twOrganization")
@Slf4j
public class TwOrganizationController extends JeecgController<TwOrganization, ITwOrganizationService> {
	@Autowired
	private ITwOrganizationService twOrganizationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twOrganization
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目组织表-分页列表查询")
	@ApiOperation(value="项目组织表-分页列表查询", notes="项目组织表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwOrganization twOrganization,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwOrganization> queryWrapper = QueryGenerator.initQueryWrapper(twOrganization, req.getParameterMap());
		Page<TwOrganization> page = new Page<TwOrganization>(pageNo, pageSize);
		IPage<TwOrganization> pageList = twOrganizationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twOrganization
	 * @return
	 */
	@AutoLog(value = "项目组织表-添加")
	@ApiOperation(value="项目组织表-添加", notes="项目组织表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwOrganization twOrganization) {
		//twOrganizationService.save(twOrganization);
		twOrganizationService.saveAddCcount(twOrganization);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twOrganization
	 * @return
	 */
	@AutoLog(value = "项目组织表-编辑")
	@ApiOperation(value="项目组织表-编辑", notes="项目组织表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwOrganization twOrganization) {
		twOrganizationService.updateById(twOrganization);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目组织表-通过id删除")
	@ApiOperation(value="项目组织表-通过id删除", notes="项目组织表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//twOrganizationService.removeById(id);
		if (twOrganizationService.delRemoveAccount(id)) {
			return Result.OK("删除成功!");
		}
		else {
			return Result.error("有项目使用，删除失败!");
		}
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目组织表-批量删除")
	@ApiOperation(value="项目组织表-批量删除", notes="项目组织表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twOrganizationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目组织表-通过id查询")
	@ApiOperation(value="项目组织表-通过id查询", notes="项目组织表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwOrganization twOrganization = twOrganizationService.getById(id);
		if(twOrganization==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twOrganization);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twOrganization
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwOrganization twOrganization) {
        return super.exportXls(request, twOrganization, TwOrganization.class, "项目组织表");
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
        return super.importExcel(request, response, TwOrganization.class);
    }

}
