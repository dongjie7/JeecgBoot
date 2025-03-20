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
import org.jeecg.modules.estar.tw.entity.TwSourceLink;
import org.jeecg.modules.estar.tw.service.ITwSourceLinkService;

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
 * @Description: 项目资源关联表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="项目资源关联表")
@RestController
@RequestMapping("/tw/twSourceLink")
@Slf4j
public class TwSourceLinkController extends JeecgController<TwSourceLink, ITwSourceLinkService> {
	@Autowired
	private ITwSourceLinkService twSourceLinkService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twSourceLink
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-分页列表查询")
	@ApiOperation(value="项目资源关联表-分页列表查询", notes="项目资源关联表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwSourceLink twSourceLink,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwSourceLink> queryWrapper = QueryGenerator.initQueryWrapper(twSourceLink, req.getParameterMap());
		Page<TwSourceLink> page = new Page<TwSourceLink>(pageNo, pageSize);
		IPage<TwSourceLink> pageList = twSourceLinkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twSourceLink
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-添加")
	@ApiOperation(value="项目资源关联表-添加", notes="项目资源关联表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwSourceLink twSourceLink) {
		twSourceLinkService.save(twSourceLink);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twSourceLink
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-编辑")
	@ApiOperation(value="项目资源关联表-编辑", notes="项目资源关联表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwSourceLink twSourceLink) {
		twSourceLinkService.updateById(twSourceLink);
		return Result.OK("编辑成功!");
	}
	
	
	@PostMapping("/sourceDel")
    @ResponseBody
    public Result<?> sourceDel(@RequestParam Map<String,Object> mmap){
		return twSourceLinkService.sourceDel(mmap);
	}
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-通过id删除")
	@ApiOperation(value="项目资源关联表-通过id删除", notes="项目资源关联表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twSourceLinkService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-批量删除")
	@ApiOperation(value="项目资源关联表-批量删除", notes="项目资源关联表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twSourceLinkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目资源关联表-通过id查询")
	@ApiOperation(value="项目资源关联表-通过id查询", notes="项目资源关联表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwSourceLink twSourceLink = twSourceLinkService.getById(id);
		if(twSourceLink==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twSourceLink);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twSourceLink
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwSourceLink twSourceLink) {
        return super.exportXls(request, twSourceLink, TwSourceLink.class, "项目资源关联表");
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
        return super.importExcel(request, response, TwSourceLink.class);
    }

}
