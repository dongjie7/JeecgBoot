package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.estar.tw.entity.TwFile;
import org.jeecg.modules.estar.tw.service.ITwFileService;
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
 * @Description: 项目文件表
 * @Author: nbacheng
 * @Date:   2023-07-11
 * @Version: V1.0
 */
@Api(tags="项目文件表")
@RestController
@RequestMapping("/tw/twFile")
@Slf4j
public class TwFileController extends JeecgController<TwFile, ITwFileService> {
	@Autowired
	private ITwFileService twFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目文件表-分页列表查询")
	@ApiOperation(value="项目文件表-分页列表查询", notes="项目文件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwFile twFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwFile> queryWrapper = QueryGenerator.initQueryWrapper(twFile, req.getParameterMap());
		Page<TwFile> page = new Page<TwFile>(pageNo, pageSize);
		IPage<TwFile> pageList = twFileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
     * 我的文件清单
     * @param
     * @return
     */
    @PostMapping("/fileList")
    @ResponseBody
    public Result<?> getProjectFile(@RequestParam Map<String,Object> mmap){
    	return twFileService.getProjectFile(mmap);
    }
    
    /**
     * 任务文件移入回收站
     * @param
     * @return
     */
    @PostMapping("/recycle")
    @ResponseBody
    public Result<?> projectFileRecycle(@RequestParam Map<String,Object> mmap){
        String fileId = MapUtils.getString(mmap,"fileId");
        return twFileService.FileRecycle(fileId);
    }
    
    /**
     * 恢复任务文件
     * @param
     * @return
     */
    @PostMapping("/recovery")
    @ResponseBody
    public Result<?> fileRecovery(@RequestParam Map<String,Object> mmap) {
        String fileId = MapUtils.getString(mmap,"fileId");
        return twFileService.recovery(fileId);
    }
	
	/**
	 *   上传文件
	 *
	 * @param twFile
	 * @return
	 */
	@AutoLog(value = "项目文件表-上传文件")
	@ApiOperation(value="项目文件表-添加", notes="项目文件表-上传文件")
	@PostMapping(value = "/uploadFiles")
	@ResponseBody
	public Result<?> uploadFiles(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile)  throws Exception{
		return twFileService.uploadFiles(request,multipartFile);
	}
	
	/**
	 *   添加
	 *
	 * @param twFile
	 * @return
	 */
	@AutoLog(value = "项目文件表-添加")
	@ApiOperation(value="项目文件表-添加", notes="项目文件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwFile twFile) {
		twFileService.save(twFile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twFile
	 * @return
	 */
	@AutoLog(value = "项目文件表-编辑")
	@ApiOperation(value="项目文件表-编辑", notes="项目文件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwFile twFile) {
		twFileService.updateById(twFile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目文件表-通过id删除")
	@ApiOperation(value="项目文件表-通过id删除", notes="项目文件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twFileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目文件表-批量删除")
	@ApiOperation(value="项目文件表-批量删除", notes="项目文件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目文件表-通过id查询")
	@ApiOperation(value="项目文件表-通过id查询", notes="项目文件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwFile twFile = twFileService.getById(id);
		if(twFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twFile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwFile twFile) {
        return super.exportXls(request, twFile, TwFile.class, "项目文件表");
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
        return super.importExcel(request, response, TwFile.class);
    }

}
