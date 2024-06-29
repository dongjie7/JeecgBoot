package org.jeecg.modules.erp.base.controller;

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
import org.jeecg.modules.erp.base.entity.ErpMember;
import org.jeecg.modules.erp.base.service.IErpMemberService;

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
 * @Description: erp_member
 * @Author: nbacheng
 * @Date:   2022-08-27
 * @Version: V1.0
 */
@Api(tags="erp_member")
@RestController
@RequestMapping("/base/erpMember")
@Slf4j
public class ErpMemberController extends JeecgController<ErpMember, IErpMemberService> {
	@Autowired
	private IErpMemberService erpMemberService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpMember
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_member-分页列表查询")
	@ApiOperation(value="erp_member-分页列表查询", notes="erp_member-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpMember erpMember,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpMember> queryWrapper = QueryGenerator.initQueryWrapper(erpMember, req.getParameterMap());
		Page<ErpMember> page = new Page<ErpMember>(pageNo, pageSize);
		IPage<ErpMember> pageList = erpMemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpMember
	 * @return
	 */
	@AutoLog(value = "erp_member-添加")
	@ApiOperation(value="erp_member-添加", notes="erp_member-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpMember erpMember) {
		erpMemberService.save(erpMember);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpMember
	 * @return
	 */
	@AutoLog(value = "erp_member-编辑")
	@ApiOperation(value="erp_member-编辑", notes="erp_member-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpMember erpMember) {
		erpMemberService.updateById(erpMember);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_member-通过id删除")
	@ApiOperation(value="erp_member-通过id删除", notes="erp_member-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpMemberService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "erp_member-批量删除")
	@ApiOperation(value="erp_member-批量删除", notes="erp_member-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpMemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_member-通过id查询")
	@ApiOperation(value="erp_member-通过id查询", notes="erp_member-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpMember erpMember = erpMemberService.getById(id);
		if(erpMember==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpMember);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpMember
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpMember erpMember) {
        return super.exportXls(request, erpMember, ErpMember.class, "erp_member");
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
        return super.importExcel(request, response, ErpMember.class);
    }

}
