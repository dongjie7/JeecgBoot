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
import org.jeecg.modules.estar.tw.entity.TwProjectCollect;
import org.jeecg.modules.estar.tw.service.ITwProjectCollectService;

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
 * @Description: 项目收藏表
 * @Author: nbacheng
 * @Date:   2023-06-09
 * @Version: V1.0
 */
@Api(tags="项目收藏表")
@RestController
@RequestMapping("/tw/twProjectCollect")
@Slf4j
public class TwProjectCollectController extends JeecgController<TwProjectCollect, ITwProjectCollectService> {
	@Autowired
	private ITwProjectCollectService twProjectCollectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectCollect
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目收藏表-分页列表查询")
	@ApiOperation(value="项目收藏表-分页列表查询", notes="项目收藏表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectCollect twProjectCollect,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectCollect> queryWrapper = QueryGenerator.initQueryWrapper(twProjectCollect, req.getParameterMap());
		Page<TwProjectCollect> page = new Page<TwProjectCollect>(pageNo, pageSize);
		IPage<TwProjectCollect> pageList = twProjectCollectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twProjectCollect
	 * @return
	 */
	@AutoLog(value = "项目收藏表-添加")
	@ApiOperation(value="项目收藏表-添加", notes="项目收藏表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProjectCollect twProjectCollect) {
		twProjectCollectService.save(twProjectCollect);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twProjectCollect
	 * @return
	 */
	@AutoLog(value = "项目收藏表-编辑")
	@ApiOperation(value="项目收藏表-编辑", notes="项目收藏表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProjectCollect twProjectCollect) {
		twProjectCollectService.updateById(twProjectCollect);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目收藏表-通过id删除")
	@ApiOperation(value="项目收藏表-通过id删除", notes="项目收藏表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twProjectCollectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目收藏表-批量删除")
	@ApiOperation(value="项目收藏表-批量删除", notes="项目收藏表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectCollectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目收藏表-通过id查询")
	@ApiOperation(value="项目收藏表-通过id查询", notes="项目收藏表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectCollect twProjectCollect = twProjectCollectService.getById(id);
		if(twProjectCollect==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectCollect);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectCollect
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectCollect twProjectCollect) {
        return super.exportXls(request, twProjectCollect, TwProjectCollect.class, "项目收藏表");
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
        return super.importExcel(request, response, TwProjectCollect.class);
    }

}
