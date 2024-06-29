package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.service.INdSharefileService;

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
 * @Description: 网盘分享文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Api(tags="网盘分享文件表")
@RestController
@RequestMapping("/nd/ndSharefile")
@Slf4j
public class NdSharefileController extends JeecgController<NdSharefile, INdSharefileService> {
	@Autowired
	private INdSharefileService ndSharefileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndSharefile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-分页列表查询")
	@ApiOperation(value="网盘分享文件表-分页列表查询", notes="网盘分享文件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdSharefile ndSharefile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdSharefile> queryWrapper = QueryGenerator.initQueryWrapper(ndSharefile, req.getParameterMap());
		Page<NdSharefile> page = new Page<NdSharefile>(pageNo, pageSize);
		IPage<NdSharefile> pageList = ndSharefileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ndSharefile
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-添加")
	@ApiOperation(value="网盘分享文件表-添加", notes="网盘分享文件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdSharefile ndSharefile) {
		ndSharefileService.save(ndSharefile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndSharefile
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-编辑")
	@ApiOperation(value="网盘分享文件表-编辑", notes="网盘分享文件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdSharefile ndSharefile) {
		ndSharefileService.updateById(ndSharefile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-通过id删除")
	@ApiOperation(value="网盘分享文件表-通过id删除", notes="网盘分享文件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndSharefileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-批量删除")
	@ApiOperation(value="网盘分享文件表-批量删除", notes="网盘分享文件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndSharefileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘分享文件表-通过id查询")
	@ApiOperation(value="网盘分享文件表-通过id查询", notes="网盘分享文件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdSharefile ndSharefile = ndSharefileService.getById(id);
		if(ndSharefile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndSharefile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndSharefile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdSharefile ndSharefile) {
        return super.exportXls(request, ndSharefile, NdSharefile.class, "网盘分享文件表");
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
        return super.importExcel(request, response, NdSharefile.class);
    }

}
