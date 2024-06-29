package org.jeecg.modules.erp.goods.controller;

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
import org.jeecg.modules.erp.goods.entity.ErpGoodsCategory;
import org.jeecg.modules.erp.goods.service.IErpGoodsCategoryService;

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
 * @Description: erp_goods_category
 * @Author: nbacheng
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Api(tags="erp_goods_category")
@RestController
@RequestMapping("/goods/erpGoodsCategory")
@Slf4j
public class ErpGoodsCategoryController extends JeecgController<ErpGoodsCategory, IErpGoodsCategoryService>{
	@Autowired
	private IErpGoodsCategoryService erpGoodsCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpGoodsCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-分页列表查询")
	@ApiOperation(value="erp_goods_category-分页列表查询", notes="erp_goods_category-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(ErpGoodsCategory erpGoodsCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<ErpGoodsCategory> queryWrapper =  QueryGenerator.initQueryWrapper(erpGoodsCategory, req.getParameterMap());
            List<ErpGoodsCategory> list = erpGoodsCategoryService.queryTreeListNoPage(queryWrapper);
            IPage<ErpGoodsCategory> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = erpGoodsCategory.getParentId();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            erpGoodsCategory.setParentId(null);
            QueryWrapper<ErpGoodsCategory> queryWrapper = QueryGenerator.initQueryWrapper(erpGoodsCategory, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("parent_id", parentId);
            Page<ErpGoodsCategory> page = new Page<ErpGoodsCategory>(pageNo, pageSize);
            IPage<ErpGoodsCategory> pageList = erpGoodsCategoryService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * 获取子数据
      * @param erpGoodsCategory
      * @param req
      * @return
      */
	@AutoLog(value = "erp_goods_category-获取子数据")
	@ApiOperation(value="erp_goods_category-获取子数据", notes="erp_goods_category-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(ErpGoodsCategory erpGoodsCategory,HttpServletRequest req) {
		QueryWrapper<ErpGoodsCategory> queryWrapper = QueryGenerator.initQueryWrapper(erpGoodsCategory, req.getParameterMap());
		List<ErpGoodsCategory> list = erpGoodsCategoryService.list(queryWrapper);
		IPage<ErpGoodsCategory> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "erp_goods_category-批量获取子数据")
    @ApiOperation(value="erp_goods_category-批量获取子数据", notes="erp_goods_category-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<ErpGoodsCategory> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("parent_id", parentIdList);
            List<ErpGoodsCategory> list = erpGoodsCategoryService.list(queryWrapper);
            IPage<ErpGoodsCategory> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param erpGoodsCategory
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-添加")
	@ApiOperation(value="erp_goods_category-添加", notes="erp_goods_category-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpGoodsCategory erpGoodsCategory) {
		erpGoodsCategoryService.addErpGoodsCategory(erpGoodsCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpGoodsCategory
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-编辑")
	@ApiOperation(value="erp_goods_category-编辑", notes="erp_goods_category-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpGoodsCategory erpGoodsCategory) {
		erpGoodsCategoryService.updateErpGoodsCategory(erpGoodsCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-通过id删除")
	@ApiOperation(value="erp_goods_category-通过id删除", notes="erp_goods_category-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpGoodsCategoryService.deleteErpGoodsCategory(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-批量删除")
	@ApiOperation(value="erp_goods_category-批量删除", notes="erp_goods_category-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpGoodsCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_category-通过id查询")
	@ApiOperation(value="erp_goods_category-通过id查询", notes="erp_goods_category-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpGoodsCategory erpGoodsCategory = erpGoodsCategoryService.getById(id);
		if(erpGoodsCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpGoodsCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpGoodsCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpGoodsCategory erpGoodsCategory) {
		return super.exportXls(request, erpGoodsCategory, ErpGoodsCategory.class, "erp_goods_category");
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
		return super.importExcel(request, response, ErpGoodsCategory.class);
    }

}
