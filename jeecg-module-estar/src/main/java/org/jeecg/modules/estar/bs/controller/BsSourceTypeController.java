package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.entity.BsSourceType;
import org.jeecg.modules.estar.bs.service.IBsSourceTypeService;

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
 * @Description: bs_source_type
 * @Author: nbacheng
 * @Date:   2023-03-14
 * @Version: V1.0
 */
@Api(tags="bs_source_type")
@RestController
@RequestMapping("/bs/bsSourceType")
@Slf4j
public class BsSourceTypeController extends JeecgController<BsSourceType, IBsSourceTypeService> {
	@Autowired
	private IBsSourceTypeService bsSourceTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsSourceType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bs_source_type-分页列表查询")
	@ApiOperation(value="bs_source_type-分页列表查询", notes="bs_source_type-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsSourceType bsSourceType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsSourceType> queryWrapper = QueryGenerator.initQueryWrapper(bsSourceType, req.getParameterMap());
		Page<BsSourceType> page = new Page<BsSourceType>(pageNo, pageSize);
		IPage<BsSourceType> pageList = bsSourceTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsSourceType
	 * @return
	 */
	@AutoLog(value = "bs_source_type-添加")
	@ApiOperation(value="bs_source_type-添加", notes="bs_source_type-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsSourceType bsSourceType) {
		bsSourceTypeService.save(bsSourceType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsSourceType
	 * @return
	 */
	@AutoLog(value = "bs_source_type-编辑")
	@ApiOperation(value="bs_source_type-编辑", notes="bs_source_type-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsSourceType bsSourceType) {
		bsSourceTypeService.updateById(bsSourceType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_source_type-通过id删除")
	@ApiOperation(value="bs_source_type-通过id删除", notes="bs_source_type-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsSourceTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bs_source_type-批量删除")
	@ApiOperation(value="bs_source_type-批量删除", notes="bs_source_type-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsSourceTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_source_type-通过id查询")
	@ApiOperation(value="bs_source_type-通过id查询", notes="bs_source_type-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsSourceType bsSourceType = bsSourceTypeService.getById(id);
		if(bsSourceType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsSourceType);
	}
	
	/**
	 * 通过code查询
	 *
	 * @param code
	 * @return
	 */
	@AutoLog(value = "bs_source_type-通过code查询")
	@ApiOperation(value="bs_source_type-通过code查询", notes="bs_source_type-通过code查询")
	@GetMapping(value = "/queryByCode")
	public Result<?> queryByCode(@RequestParam(name="code",required=true) String code) {
		Result<BsSourceType> result = new Result<BsSourceType>();
        QueryWrapper<BsSourceType> queryWrapper = new QueryWrapper<BsSourceType>();
        queryWrapper.eq("code",code);
        BsSourceType bsSourceType = bsSourceTypeService.getOne(queryWrapper);
        if (bsSourceType == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(bsSourceType);
            result.setSuccess(true);
        }
        return result;
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsSourceType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsSourceType bsSourceType) {
        return super.exportXls(request, bsSourceType, BsSourceType.class, "bs_source_type");
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
        return super.importExcel(request, response, BsSourceType.class);
    }

}
