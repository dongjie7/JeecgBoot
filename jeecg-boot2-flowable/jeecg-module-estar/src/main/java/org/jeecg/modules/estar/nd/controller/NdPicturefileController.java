package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.nd.entity.NdPicturefile;
import org.jeecg.modules.estar.nd.service.INdPicturefileService;

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
 * @Description: nd_picturefile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Api(tags="nd_picturefile")
@RestController
@RequestMapping("/nd/ndPicturefile")
@Slf4j
public class NdPicturefileController extends JeecgController<NdPicturefile, INdPicturefileService> {
	@Autowired
	private INdPicturefileService ndPicturefileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndPicturefile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-分页列表查询")
	@ApiOperation(value="nd_picturefile-分页列表查询", notes="nd_picturefile-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdPicturefile ndPicturefile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdPicturefile> queryWrapper = QueryGenerator.initQueryWrapper(ndPicturefile, req.getParameterMap());
		Page<NdPicturefile> page = new Page<NdPicturefile>(pageNo, pageSize);
		IPage<NdPicturefile> pageList = ndPicturefileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ndPicturefile
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-添加")
	@ApiOperation(value="nd_picturefile-添加", notes="nd_picturefile-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdPicturefile ndPicturefile) {
		ndPicturefileService.save(ndPicturefile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndPicturefile
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-编辑")
	@ApiOperation(value="nd_picturefile-编辑", notes="nd_picturefile-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdPicturefile ndPicturefile) {
		ndPicturefileService.updateById(ndPicturefile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-通过id删除")
	@ApiOperation(value="nd_picturefile-通过id删除", notes="nd_picturefile-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndPicturefileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-批量删除")
	@ApiOperation(value="nd_picturefile-批量删除", notes="nd_picturefile-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndPicturefileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_picturefile-通过id查询")
	@ApiOperation(value="nd_picturefile-通过id查询", notes="nd_picturefile-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdPicturefile ndPicturefile = ndPicturefileService.getById(id);
		if(ndPicturefile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndPicturefile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndPicturefile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdPicturefile ndPicturefile) {
        return super.exportXls(request, ndPicturefile, NdPicturefile.class, "nd_picturefile");
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
        return super.importExcel(request, response, NdPicturefile.class);
    }

}
