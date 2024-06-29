package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportShare;
import org.jeecg.modules.estar.bs.service.IBsReportShareService;
import org.jeecg.modules.estar.bs.util.EstarBeanUtils;
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
 * @Description: 大屏分享表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="大屏分享表")
@RestController
@RequestMapping("/bs/bsReportShare")
@Slf4j
public class BsReportShareController extends JeecgController<BsReportShare, IBsReportShareService> {
	@Autowired
	private IBsReportShareService bsReportShareService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsReportShare
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "大屏分享表-分页列表查询")
	@ApiOperation(value="大屏分享表-分页列表查询", notes="大屏分享表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsReportShare bsReportShare,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsReportShare> queryWrapper = QueryGenerator.initQueryWrapper(bsReportShare, req.getParameterMap());
		Page<BsReportShare> page = new Page<BsReportShare>(pageNo, pageSize);
		IPage<BsReportShare> pageList = bsReportShareService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsReportShare
	 * @return
	 */
	@AutoLog(value = "大屏分享表-添加")
	@ApiOperation(value="大屏分享表-添加", notes="大屏分享表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsReportShare bsReportShare) {
		bsReportShareService.save(bsReportShare);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsReportShare
	 * @return
	 */
	@AutoLog(value = "大屏分享表-编辑")
	@ApiOperation(value="大屏分享表-编辑", notes="大屏分享表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsReportShare bsReportShare) {
		bsReportShareService.updateById(bsReportShare);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏分享表-通过id删除")
	@ApiOperation(value="大屏分享表-通过id删除", notes="大屏分享表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsReportShareService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "大屏分享表-批量删除")
	@ApiOperation(value="大屏分享表-批量删除", notes="大屏分享表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsReportShareService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏分享表-通过id查询")
	@ApiOperation(value="大屏分享表-通过id查询", notes="大屏分享表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsReportShare bsReportShare = bsReportShareService.getById(id);
		if(bsReportShare==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsReportShare);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsReportShare
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsReportShare bsReportShare) {
        return super.exportXls(request, bsReportShare, BsReportShare.class, "大屏分享表");
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
        return super.importExcel(request, response, BsReportShare.class);
    }
    
    @GetMapping({"/{id}"})
    public Result<?> detail(@PathVariable("id") Long id) {
        BsReportShare result = bsReportShareService.getDetail(id);
        ReportShareDto dto = new ReportShareDto();
        EstarBeanUtils.copyAndFormatter(result, dto);
        return Result.OK(dto);
    }

    @GetMapping({"/detailByCode"})
    public Result<?> detailByCode(@RequestParam("shareCode") String shareCode) {
        return Result.OK(bsReportShareService.detailByCode(shareCode));
    }

    @PostMapping({"/shareDelay"})
    public Result<?> shareDelay(@RequestBody ReportShareDto dto) {
    	bsReportShareService.shareDelay(dto);
        return Result.OK("分享成功！");
    }

}
