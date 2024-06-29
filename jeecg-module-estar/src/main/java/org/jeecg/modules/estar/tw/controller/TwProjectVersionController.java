package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwProjectVersion;
import org.jeecg.modules.estar.tw.service.ITwProjectVersionService;

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
 * @Description: 项目版本表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="项目版本表")
@RestController
@RequestMapping("/tw/twProjectVersion")
@Slf4j
public class TwProjectVersionController extends JeecgController<TwProjectVersion, ITwProjectVersionService> {
	@Autowired
	private ITwProjectVersionService twProjectVersionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectVersion
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目版本表-分页列表查询")
	@ApiOperation(value="项目版本表-分页列表查询", notes="项目版本表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectVersion twProjectVersion,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectVersion> queryWrapper = QueryGenerator.initQueryWrapper(twProjectVersion, req.getParameterMap());
		Page<TwProjectVersion> page = new Page<TwProjectVersion>(pageNo, pageSize);
		IPage<TwProjectVersion> pageList = twProjectVersionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   列表
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-列表")
	@ApiOperation(value="项目版本表-列表", notes="项目版本表-列表")
	@PostMapping(value = "/listIndex")
	@ResponseBody
	public Result<?> listIndex(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.listIndex(mmap);
	}
	
	/**
	 *   添加
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-添加")
	@ApiOperation(value="项目版本表-添加", notes="项目版本表-添加")
	@PostMapping(value = "/save")
	@ResponseBody
	public Result<?> add(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.save(mmap);
	}
	
	/**
	 *  编辑
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-编辑")
	@ApiOperation(value="项目版本表-编辑", notes="项目版本表-编辑")
	@PostMapping(value = "/edit")
	@ResponseBody
	public Result<?> edit(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.edit(mmap);
	}
	
	/**
	 *   通过mmap删除
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-通过id删除")
	@ApiOperation(value="项目版本表-通过id删除", notes="项目版本表-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.delete(mmap);
	}
	
	/**
	 *   通过mmap更改版本状态
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-更改版本状态")
	@ApiOperation(value="项目版本表-更改版本状态", notes="项目版本表-更改版本状态")
	@PostMapping(value = "/changeStatus")
	public Result<?> changeStatus(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.changeStatus(mmap);
	}
	
	/**
	 *   通过mmap获取版本任务
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-获取版本任务")
	@ApiOperation(value="项目版本表-获取版本任务", notes="项目版本表-获取版本任务")
	@PostMapping(value = "/getVersionTask")
	public Result<?> getVersionTask(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.getVersionTask(mmap);
	}
	
	/**
	 *   通过mmap关联任务
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-关联任务")
	@ApiOperation(value="项目版本表-关联任务", notes="项目版本表-关联任务")
	@PostMapping(value = "/addVersionTask")
	public Result<?> addVersionTask(@RequestParam Map<String,Object> mmap) throws Exception {
		return twProjectVersionService.addVersionTask(mmap);
	}
	
	/**
	 *   通过mmap获取版本日志
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-获取版本日志")
	@ApiOperation(value="项目版本表-获取版本日志", notes="项目版本表-获取版本日志")
	@PostMapping(value = "/getVersionLog")
	public Result<?> getVersionLog(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.getVersionLog(mmap);
	}
	
	/**
	 *   通过mmap获取版本信息
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-获取版本信息")
	@ApiOperation(value="项目版本表-获取版本版本信息", notes="项目版本表-获取版本信息")
	@PostMapping(value = "/getVersionInfo")
	public Result<?> getVersionInfo(@RequestParam Map<String,Object> mmap) {
		return twProjectVersionService.getVersionInfo(mmap);
	}
	
	/**
	 *   通过mmap移除版本任务
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "项目版本表-移除版本任务")
	@ApiOperation(value="项目版本表-移除版本任务", notes="项目版本表-移除版本任务")
	@PostMapping(value = "/removeVersionTask")
	public Result<?> removeVersionTask(@RequestParam Map<String,Object> mmap) throws Exception {
		return twProjectVersionService.removeVersionTask(mmap);
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目版本表-批量删除")
	@ApiOperation(value="项目版本表-批量删除", notes="项目版本表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectVersionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目版本表-通过id查询")
	@ApiOperation(value="项目版本表-通过id查询", notes="项目版本表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectVersion twProjectVersion = twProjectVersionService.getById(id);
		if(twProjectVersion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectVersion);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectVersion
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectVersion twProjectVersion) {
        return super.exportXls(request, twProjectVersion, TwProjectVersion.class, "项目版本表");
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
        return super.importExcel(request, response, TwProjectVersion.class);
    }

}
