package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.bs.entity.BsFile;
import org.jeecg.modules.estar.bs.service.IBsFileService;

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
 * @Description: 大屏文件
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="大屏文件")
@RestController
@RequestMapping("/bs/bsFile")
@Slf4j
public class BsFileController extends JeecgController<BsFile, IBsFileService> {
	@Autowired
	private IBsFileService bsFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "大屏文件-分页列表查询")
	@ApiOperation(value="大屏文件-分页列表查询", notes="大屏文件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsFile bsFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsFile> queryWrapper = QueryGenerator.initQueryWrapper(bsFile, req.getParameterMap());
		Page<BsFile> page = new Page<BsFile>(pageNo, pageSize);
		IPage<BsFile> pageList = bsFileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsFile
	 * @return
	 */
	@AutoLog(value = "大屏文件-添加")
	@ApiOperation(value="大屏文件-添加", notes="大屏文件-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsFile bsFile) {
		bsFileService.save(bsFile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsFile
	 * @return
	 */
	@AutoLog(value = "大屏文件-编辑")
	@ApiOperation(value="大屏文件-编辑", notes="大屏文件-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsFile bsFile) {
		bsFileService.updateById(bsFile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏文件-通过id删除")
	@ApiOperation(value="大屏文件-通过id删除", notes="大屏文件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsFileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "大屏文件-批量删除")
	@ApiOperation(value="大屏文件-批量删除", notes="大屏文件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏文件-通过id查询")
	@ApiOperation(value="大屏文件-通过id查询", notes="大屏文件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsFile bsFile = bsFileService.getById(id);
		if(bsFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsFile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsFile bsFile) {
        return super.exportXls(request, bsFile, BsFile.class, "大屏文件");
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
        return super.importExcel(request, response, BsFile.class);
    }

}
