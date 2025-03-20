package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.entity.BsDataSource;
import org.jeecg.modules.estar.bs.param.ConnectionParam;
import org.jeecg.modules.estar.bs.service.IBsDataSourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: bs_data_source
 * @Author: nbacheng
 * @Date:   2023-03-14
 * @Version: V1.0
 */
@Api(tags="bs_data_source")
@RestController
@RequestMapping("/bs/bsDataSource")
@Slf4j
public class BsDataSourceController extends JeecgController<BsDataSource, IBsDataSourceService> {
	@Autowired
	private IBsDataSourceService bsDataSourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsDataSource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bs_data_source-分页列表查询")
	@ApiOperation(value="bs_data_source-分页列表查询", notes="bs_data_source-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsDataSource bsDataSource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsDataSource> queryWrapper = QueryGenerator.initQueryWrapper(bsDataSource, req.getParameterMap());
		Page<BsDataSource> page = new Page<BsDataSource>(pageNo, pageSize);
		IPage<BsDataSource> pageList = bsDataSourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsDataSource
	 * @return
	 */
	@AutoLog(value = "bs_data_source-添加")
	@ApiOperation(value="bs_data_source-添加", notes="bs_data_source-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsDataSource bsDataSource) {
		bsDataSourceService.save(bsDataSource);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsDataSource
	 * @return
	 */
	@AutoLog(value = "bs_data_source-编辑")
	@ApiOperation(value="bs_data_source-编辑", notes="bs_data_source-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsDataSource bsDataSource) {
		bsDataSourceService.updateById(bsDataSource);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_source-通过id删除")
	@ApiOperation(value="bs_data_source-通过id删除", notes="bs_data_source-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsDataSourceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bs_data_source-批量删除")
	@ApiOperation(value="bs_data_source-批量删除", notes="bs_data_source-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsDataSourceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_source-通过id查询")
	@ApiOperation(value="bs_data_source-通过id查询", notes="bs_data_source-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsDataSource bsDataSource = bsDataSourceService.getById(id);
		if(bsDataSource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsDataSource);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsDataSource
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsDataSource bsDataSource) {
        return super.exportXls(request, bsDataSource, BsDataSource.class, "bs_data_source");
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
        return super.importExcel(request, response, BsDataSource.class);
    }
    
    /**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    @AutoLog(value = "testConnection-测试数据源")
	@ApiOperation(value="testConnection-测试数据源", notes="testConnection-测试数据源")
    @PostMapping("/testConnection")
    public Result<?> testConnection(@Validated @RequestBody ConnectionParam connectionParam) {
        return bsDataSourceService.testConnection(connectionParam);
    }

}
