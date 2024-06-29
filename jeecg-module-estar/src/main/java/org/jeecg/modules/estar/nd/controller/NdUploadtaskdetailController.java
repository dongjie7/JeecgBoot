package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.nd.entity.NdUploadtaskdetail;
import org.jeecg.modules.estar.nd.service.INdUploadtaskdetailService;

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
 * @Description: nd_uploadtaskdetail
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Api(tags="nd_uploadtaskdetail")
@RestController
@RequestMapping("/nd/ndUploadtaskdetail")
@Slf4j
public class NdUploadtaskdetailController extends JeecgController<NdUploadtaskdetail, INdUploadtaskdetailService> {
	@Autowired
	private INdUploadtaskdetailService ndUploadtaskdetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndUploadtaskdetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-分页列表查询")
	@ApiOperation(value="nd_uploadtaskdetail-分页列表查询", notes="nd_uploadtaskdetail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdUploadtaskdetail ndUploadtaskdetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdUploadtaskdetail> queryWrapper = QueryGenerator.initQueryWrapper(ndUploadtaskdetail, req.getParameterMap());
		Page<NdUploadtaskdetail> page = new Page<NdUploadtaskdetail>(pageNo, pageSize);
		IPage<NdUploadtaskdetail> pageList = ndUploadtaskdetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ndUploadtaskdetail
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-添加")
	@ApiOperation(value="nd_uploadtaskdetail-添加", notes="nd_uploadtaskdetail-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdUploadtaskdetail ndUploadtaskdetail) {
		ndUploadtaskdetailService.save(ndUploadtaskdetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndUploadtaskdetail
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-编辑")
	@ApiOperation(value="nd_uploadtaskdetail-编辑", notes="nd_uploadtaskdetail-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdUploadtaskdetail ndUploadtaskdetail) {
		ndUploadtaskdetailService.updateById(ndUploadtaskdetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-通过id删除")
	@ApiOperation(value="nd_uploadtaskdetail-通过id删除", notes="nd_uploadtaskdetail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndUploadtaskdetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-批量删除")
	@ApiOperation(value="nd_uploadtaskdetail-批量删除", notes="nd_uploadtaskdetail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndUploadtaskdetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_uploadtaskdetail-通过id查询")
	@ApiOperation(value="nd_uploadtaskdetail-通过id查询", notes="nd_uploadtaskdetail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdUploadtaskdetail ndUploadtaskdetail = ndUploadtaskdetailService.getById(id);
		if(ndUploadtaskdetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndUploadtaskdetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndUploadtaskdetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdUploadtaskdetail ndUploadtaskdetail) {
        return super.exportXls(request, ndUploadtaskdetail, NdUploadtaskdetail.class, "nd_uploadtaskdetail");
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
        return super.importExcel(request, response, NdUploadtaskdetail.class);
    }

}
