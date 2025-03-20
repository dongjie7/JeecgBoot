package org.jeecg.modules.demo.qinjia.controller;

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
import org.jeecg.modules.demo.qinjia.entity.QinjiaDan;
import org.jeecg.modules.demo.qinjia.service.IQinjiaDanService;

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
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 请假单
 * @Author: jeecg-boot
 * @Date:   2024-06-29
 * @Version: V1.0
 */
@Api(tags="请假单")
@RestController
@RequestMapping("/qinjia/qinjiaDan")
@Slf4j
public class QinjiaDanController extends JeecgController<QinjiaDan, IQinjiaDanService> {
	@Autowired
	private IQinjiaDanService qinjiaDanService;
	
	/**
	 * 分页列表查询
	 *
	 * @param qinjiaDan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "请假单-分页列表查询")
	@ApiOperation(value="请假单-分页列表查询", notes="请假单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<QinjiaDan>> queryPageList(QinjiaDan qinjiaDan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<QinjiaDan> queryWrapper = QueryGenerator.initQueryWrapper(qinjiaDan, req.getParameterMap());
		Page<QinjiaDan> page = new Page<QinjiaDan>(pageNo, pageSize);
		IPage<QinjiaDan> pageList = qinjiaDanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param qinjiaDan
	 * @return
	 */
	@AutoLog(value = "请假单-添加")
	@ApiOperation(value="请假单-添加", notes="请假单-添加")
	@RequiresPermissions("qinjia:qinjia_dan:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody QinjiaDan qinjiaDan) {
		qinjiaDanService.save(qinjiaDan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param qinjiaDan
	 * @return
	 */
	@AutoLog(value = "请假单-编辑")
	@ApiOperation(value="请假单-编辑", notes="请假单-编辑")
	@RequiresPermissions("qinjia:qinjia_dan:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody QinjiaDan qinjiaDan) {
		qinjiaDanService.updateById(qinjiaDan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "请假单-通过id删除")
	@ApiOperation(value="请假单-通过id删除", notes="请假单-通过id删除")
	@RequiresPermissions("qinjia:qinjia_dan:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		qinjiaDanService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "请假单-批量删除")
	@ApiOperation(value="请假单-批量删除", notes="请假单-批量删除")
	@RequiresPermissions("qinjia:qinjia_dan:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.qinjiaDanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "请假单-通过id查询")
	@ApiOperation(value="请假单-通过id查询", notes="请假单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<QinjiaDan> queryById(@RequestParam(name="id",required=true) String id) {
		QinjiaDan qinjiaDan = qinjiaDanService.getById(id);
		if(qinjiaDan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(qinjiaDan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param qinjiaDan
    */
    @RequiresPermissions("qinjia:qinjia_dan:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QinjiaDan qinjiaDan) {
        return super.exportXls(request, qinjiaDan, QinjiaDan.class, "请假单");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("qinjia:qinjia_dan:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, QinjiaDan.class);
    }

}
