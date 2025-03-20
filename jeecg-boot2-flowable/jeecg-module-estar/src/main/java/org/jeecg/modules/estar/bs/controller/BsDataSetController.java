package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.entity.BsDataSet;
import org.jeecg.modules.estar.bs.param.DataSetTestTransformParam;
import org.jeecg.modules.estar.bs.service.IBsDataSetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: bs_data_set
 * @Author: nbacheng
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Api(tags="bs_data_set")
@RestController
@RequestMapping("/bs/bsDataSet")
@Slf4j
public class BsDataSetController extends JeecgController<BsDataSet, IBsDataSetService> {
	@Autowired
	private IBsDataSetService bsDataSetService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsDataSet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bs_data_set-分页列表查询")
	@ApiOperation(value="bs_data_set-分页列表查询", notes="bs_data_set-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsDataSet bsDataSet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsDataSet> queryWrapper = QueryGenerator.initQueryWrapper(bsDataSet, req.getParameterMap());
		Page<BsDataSet> page = new Page<BsDataSet>(pageNo, pageSize);
		IPage<BsDataSet> pageList = bsDataSetService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
     * 获取所有数据集
     * @return
     */
    @GetMapping("/queryAllDataSet")
    public Result<?> queryAllDataSet() {
        return Result.OK(bsDataSetService.queryAllDataSet());
    }
	
    @AutoLog(value = "bs_data_set-明细")
    @GetMapping("/detailBysetId/{id}")
    public Result<?> detailBysetId(@PathVariable("id") Long id) {
        return Result.OK(bsDataSetService.detailSet(id));
    }
    
    @AutoLog(value = "bs_data_set-明细")
    @GetMapping("/detailBysetCode/{setCode}")
    public Result<?> detailBysetCode(@PathVariable("setCode") String setCode) {
        return Result.OK(bsDataSetService.detailSet(setCode));
    }
    
	/**
	 *   添加
	 *
	 * @param bsDataSet
	 * @return
	 */
	@AutoLog(value = "bs_data_set-添加")
	@ApiOperation(value="bs_data_set-添加", notes="bs_data_set-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsDataSet bsDataSet) {
		bsDataSetService.save(bsDataSet);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsDataSet
	 * @return
	 */
	@AutoLog(value = "bs_data_set-编辑")
	@ApiOperation(value="bs_data_set-编辑", notes="bs_data_set-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsDataSet bsDataSet) {
		bsDataSetService.updateById(bsDataSet);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_set-通过id删除")
	@ApiOperation(value="bs_data_set-通过id删除", notes="bs_data_set-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsDataSetService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bs_data_set-批量删除")
	@ApiOperation(value="bs_data_set-批量删除", notes="bs_data_set-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsDataSetService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_set-通过id查询")
	@ApiOperation(value="bs_data_set-通过id查询", notes="bs_data_set-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsDataSet bsDataSet = bsDataSetService.getById(id);
		if(bsDataSet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsDataSet);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsDataSet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsDataSet bsDataSet) {
        return super.exportXls(request, bsDataSet, BsDataSet.class, "bs_data_set");
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
        return super.importExcel(request, response, BsDataSet.class);
    }
    
    /**
     * 测试 数据转换是否正确
     * @param param
     * @return
     */
    @PostMapping("/testTransform")
    public Result<?> testTransform(@Validated @RequestBody DataSetTestTransformParam param) {
        DataSetDto dto = new DataSetDto();
        BeanUtils.copyProperties(param, dto);
        return Result.OK(bsDataSetService.testTransform(dto));
    }

}
