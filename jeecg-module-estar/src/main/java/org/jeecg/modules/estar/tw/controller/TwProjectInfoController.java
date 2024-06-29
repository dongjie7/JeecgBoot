package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwProjectInfo;
import org.jeecg.modules.estar.tw.service.ITwProjectInfoService;
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
 * @Description: 项目自定义信息表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="项目自定义信息表")
@RestController
@RequestMapping("/tw/twProjectInfo")
@Slf4j
public class TwProjectInfoController extends JeecgController<TwProjectInfo, ITwProjectInfoService> {
	@Autowired
	private ITwProjectInfoService twProjectInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-分页列表查询")
	@ApiOperation(value="项目自定义信息表-分页列表查询", notes="项目自定义信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectInfo twProjectInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectInfo> queryWrapper = QueryGenerator.initQueryWrapper(twProjectInfo, req.getParameterMap());
		Page<TwProjectInfo> page = new Page<TwProjectInfo>(pageNo, pageSize);
		IPage<TwProjectInfo> pageList = twProjectInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twProjectInfo
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-添加")
	@ApiOperation(value="项目自定义信息表-添加", notes="项目自定义信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProjectInfo twProjectInfo) {
		twProjectInfoService.save(twProjectInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twProjectInfo
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-编辑")
	@ApiOperation(value="项目自定义信息表-编辑", notes="项目自定义信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProjectInfo twProjectInfo) {
		twProjectInfoService.updateById(twProjectInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
     * 获取项目信息
     * @param
     * @return
     */
	@PostMapping("/infoList")
    @ResponseBody
    public Result<?> projectProjectInfo(@RequestParam Map<String,Object> mmap){
        String projectId = MapUtils.getString(mmap,"projectId");
        List<Map> projectInfoList = twProjectInfoService.getProjectInfoByProjectId(projectId);
        return Result.OK(projectInfoList);

    }
	
	@PostMapping("/infoDelete")
    @ResponseBody
    public Result<?> infoDelete(@RequestParam Map<String,Object> mmap)
    {
        String infoId = MapUtils.getString(mmap,"infoId");
        twProjectInfoService.lambdaUpdate().eq(TwProjectInfo::getId,infoId).remove();
        return Result.OK();
    }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-通过id删除")
	@ApiOperation(value="项目自定义信息表-通过id删除", notes="项目自定义信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twProjectInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-批量删除")
	@ApiOperation(value="项目自定义信息表-批量删除", notes="项目自定义信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目自定义信息表-通过id查询")
	@ApiOperation(value="项目自定义信息表-通过id查询", notes="项目自定义信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectInfo twProjectInfo = twProjectInfoService.getById(id);
		if(twProjectInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectInfo twProjectInfo) {
        return super.exportXls(request, twProjectInfo, TwProjectInfo.class, "项目自定义信息表");
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
        return super.importExcel(request, response, TwProjectInfo.class);
    }

}
