package org.jeecg.modules.erp.purchase.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import org.jeecg.modules.erp.purchase.entity.ErpInSheet;
import org.jeecg.modules.erp.purchase.vo.ErpInSheetPage;
import org.jeecg.modules.erp.purchase.service.IErpInSheetService;
import org.jeecg.modules.erp.purchase.service.IErpInSheetDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: erp_in_sheet
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@Api(tags="erp_in_sheet")
@RestController
@RequestMapping("/purchase/erpInSheet")
@Slf4j
public class ErpInSheetController {
	@Autowired
	private IErpInSheetService erpInSheetService;
	@Autowired
	private IErpInSheetDetailService erpInSheetDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpInSheet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购入库单-分页列表查询")
	@ApiOperation(value="采购入库单-分页列表查询", notes="采购入库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpInSheet erpInSheet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpInSheet> queryWrapper = QueryGenerator.initQueryWrapper(erpInSheet, req.getParameterMap());
		Page<ErpInSheet> page = new Page<ErpInSheet>(pageNo, pageSize);
		IPage<ErpInSheet> pageList = erpInSheetService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpInSheetPage
	 * @return
	 */
	@AutoLog(value = "采购入库单-添加")
	@ApiOperation(value="采购入库单-添加", notes="采购入库单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpInSheetPage erpInSheetPage) {
		ErpInSheet erpInSheet = new ErpInSheet();
		BeanUtils.copyProperties(erpInSheetPage, erpInSheet);
		erpInSheetService.saveMain(erpInSheet, erpInSheetPage.getErpInSheetDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *   审核
	 *
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@AutoLog(value = "采购入库单-审核通过")
	@ApiOperation(value="采购入库单-审核通过", notes="采购入库单-审核通过")
	@PostMapping(value = "/approvePass")
	public Result<?> approvePass(@RequestBody String id) throws Exception {
		return erpInSheetService.approvePass(id);
	}
	
	/**
	 *  编辑
	 *
	 * @param erpInSheetPage
	 * @return
	 */
	@AutoLog(value = "采购入库单-编辑")
	@ApiOperation(value="采购入库单-编辑", notes="采购入库单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpInSheetPage erpInSheetPage) {
		ErpInSheet erpInSheet = new ErpInSheet();
		BeanUtils.copyProperties(erpInSheetPage, erpInSheet);
		ErpInSheet erpInSheetEntity = erpInSheetService.getById(erpInSheet.getId());
		if(erpInSheetEntity==null) {
			return Result.error("未找到对应数据");
		}
		erpInSheetService.updateMain(erpInSheet, erpInSheetPage.getErpInSheetDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购入库单-通过id删除")
	@ApiOperation(value="采购入库单-通过id删除", notes="采购入库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpInSheetService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购入库单-批量删除")
	@ApiOperation(value="采购入库单-批量删除", notes="采购入库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpInSheetService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购入库单-通过id查询")
	@ApiOperation(value="采购入库单-通过id查询", notes="采购入库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpInSheet erpInSheet = erpInSheetService.getById(id);
		if(erpInSheet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpInSheet);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购入库单明细表通过主表ID查询")
	@ApiOperation(value="采购入库单明细表-主表ID查询", notes="采购入库单明细表-通主表ID查询")
	@GetMapping(value = "/queryErpInSheetDetailByMainId")
	public Result<?> queryErpInSheetDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ErpInSheetDetail> erpInSheetDetailList = erpInSheetDetailService.selectByMainId(id);
		return Result.OK(erpInSheetDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpInSheet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpInSheet erpInSheet) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ErpInSheet> queryWrapper = QueryGenerator.initQueryWrapper(erpInSheet, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ErpInSheet> queryList = erpInSheetService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<ErpInSheet> erpInSheetList = new ArrayList<ErpInSheet>();
      if(oConvertUtils.isEmpty(selections)) {
          erpInSheetList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          erpInSheetList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ErpInSheetPage> pageList = new ArrayList<ErpInSheetPage>();
      for (ErpInSheet main : erpInSheetList) {
          ErpInSheetPage vo = new ErpInSheetPage();
          BeanUtils.copyProperties(main, vo);
          List<ErpInSheetDetail> erpInSheetDetailList = erpInSheetDetailService.selectByMainId(main.getId());
          vo.setErpInSheetDetailList(erpInSheetDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "erp_in_sheet列表");
      mv.addObject(NormalExcelConstants.CLASS, ErpInSheetPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("erp_in_sheet数据", "导出人:"+sysUser.getRealname(), "erp_in_sheet"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ErpInSheetPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ErpInSheetPage.class, params);
              for (ErpInSheetPage page : list) {
                  ErpInSheet po = new ErpInSheet();
                  BeanUtils.copyProperties(page, po);
                  erpInSheetService.saveMain(po, page.getErpInSheetDetailList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
