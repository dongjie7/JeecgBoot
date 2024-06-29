package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwProjectTemplate;
import org.jeecg.modules.estar.tw.service.ITwProjectTemplateService;

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
 * @Description: 项目模板表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Api(tags="项目模板表")
@RestController
@RequestMapping("/tw/twProjectTemplate")
@Slf4j
public class TwProjectTemplateController extends JeecgController<TwProjectTemplate, ITwProjectTemplateService> {
	@Autowired
	private ITwProjectTemplateService twProjectTemplateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目模板表-分页列表查询")
	@ApiOperation(value="项目模板表-分页列表查询", notes="项目模板表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectTemplate twProjectTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectTemplate> queryWrapper = QueryGenerator.initQueryWrapper(twProjectTemplate, req.getParameterMap());
		Page<TwProjectTemplate> page = new Page<TwProjectTemplate>(pageNo, pageSize);
		IPage<TwProjectTemplate> pageList = twProjectTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twProjectTemplate
	 * @return
	 */
	@AutoLog(value = "项目模板表-添加")
	@ApiOperation(value="项目模板表-添加", notes="项目模板表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProjectTemplate twProjectTemplate) {
		twProjectTemplateService.saveTemplate(twProjectTemplate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twProjectTemplate
	 * @return
	 */
	@AutoLog(value = "项目模板表-编辑")
	@ApiOperation(value="项目模板表-编辑", notes="项目模板表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProjectTemplate twProjectTemplate) {
		twProjectTemplateService.updateById(twProjectTemplate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目模板表-通过id删除")
	@ApiOperation(value="项目模板表-通过id删除", notes="项目模板表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//twProjectTemplateService.removeById(id);
		twProjectTemplateService.removeProjectTemplateAndTaskStagesTemplage(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目模板表-批量删除")
	@ApiOperation(value="项目模板表-批量删除", notes="项目模板表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectTemplateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目模板表-通过id查询")
	@ApiOperation(value="项目模板表-通过id查询", notes="项目模板表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectTemplate twProjectTemplate = twProjectTemplateService.getById(id);
		if(twProjectTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectTemplate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectTemplate twProjectTemplate) {
        return super.exportXls(request, twProjectTemplate, TwProjectTemplate.class, "项目模板表");
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
        return super.importExcel(request, response, TwProjectTemplate.class);
    }

}
