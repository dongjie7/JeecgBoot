package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.nd.entity.NdUploadtask;
import org.jeecg.modules.estar.nd.service.INdUploadtaskService;

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
 * @Description: nd_uploadtask
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Api(tags="nd_uploadtask")
@RestController
@RequestMapping("/nd/ndUploadtask")
@Slf4j
public class NdUploadtaskController extends JeecgController<NdUploadtask, INdUploadtaskService> {
	@Autowired
	private INdUploadtaskService ndUploadtaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndUploadtask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-分页列表查询")
	@ApiOperation(value="nd_uploadtask-分页列表查询", notes="nd_uploadtask-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdUploadtask ndUploadtask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdUploadtask> queryWrapper = QueryGenerator.initQueryWrapper(ndUploadtask, req.getParameterMap());
		Page<NdUploadtask> page = new Page<NdUploadtask>(pageNo, pageSize);
		IPage<NdUploadtask> pageList = ndUploadtaskService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ndUploadtask
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-添加")
	@ApiOperation(value="nd_uploadtask-添加", notes="nd_uploadtask-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdUploadtask ndUploadtask) {
		ndUploadtaskService.save(ndUploadtask);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndUploadtask
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-编辑")
	@ApiOperation(value="nd_uploadtask-编辑", notes="nd_uploadtask-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdUploadtask ndUploadtask) {
		ndUploadtaskService.updateById(ndUploadtask);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-通过id删除")
	@ApiOperation(value="nd_uploadtask-通过id删除", notes="nd_uploadtask-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndUploadtaskService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-批量删除")
	@ApiOperation(value="nd_uploadtask-批量删除", notes="nd_uploadtask-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndUploadtaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_uploadtask-通过id查询")
	@ApiOperation(value="nd_uploadtask-通过id查询", notes="nd_uploadtask-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdUploadtask ndUploadtask = ndUploadtaskService.getById(id);
		if(ndUploadtask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndUploadtask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndUploadtask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdUploadtask ndUploadtask) {
        return super.exportXls(request, ndUploadtask, NdUploadtask.class, "nd_uploadtask");
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
        return super.importExcel(request, response, NdUploadtask.class);
    }

}
