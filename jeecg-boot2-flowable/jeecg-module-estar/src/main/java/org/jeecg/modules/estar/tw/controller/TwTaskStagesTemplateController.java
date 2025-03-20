package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwTaskStagesTemplate;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesTemplateMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesTemplateService;

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
 * @Description: 任务列表模板表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Api(tags="任务列表模板表")
@RestController
@RequestMapping("/tw/twTaskStagesTemplate")
@Slf4j
public class TwTaskStagesTemplateController extends JeecgController<TwTaskStagesTemplate, ITwTaskStagesTemplateService> {
	@Autowired
	private ITwTaskStagesTemplateService twTaskStagesTemplateService;
	@Autowired
	TwTaskStagesTemplateMapper taskStagesTemplateMapper;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTaskStagesTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-分页列表查询")
	@ApiOperation(value="任务列表模板表-分页列表查询", notes="任务列表模板表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTaskStagesTemplate twTaskStagesTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTaskStagesTemplate> queryWrapper = QueryGenerator.initQueryWrapper(twTaskStagesTemplate, req.getParameterMap());
		Page<TwTaskStagesTemplate> page = new Page<TwTaskStagesTemplate>(pageNo, pageSize);
		IPage<TwTaskStagesTemplate> pageList = twTaskStagesTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	
	/**
	 * 根据项目模板id分页列表查询
	 *
	 * @param templateId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-根据模板ID分页列表查询")
	@ApiOperation(value="任务列表模板表-根据模板ID分页列表查询", notes="任务列表模板表-根据模板ID分页列表查询")
	@GetMapping(value = "/listByTemplateId")
	public Result<?> listByTemplateId(@RequestParam(name="templateId",required=true) String templateId,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<TwTaskStagesTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("project_template_id",templateId);
		Page<TwTaskStagesTemplate> page = new Page<TwTaskStagesTemplate>(pageNo, pageSize);
		IPage<TwTaskStagesTemplate> pageList = twTaskStagesTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twTaskStagesTemplate
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-添加")
	@ApiOperation(value="任务列表模板表-添加", notes="任务列表模板表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTaskStagesTemplate twTaskStagesTemplate) {
		twTaskStagesTemplateService.save(twTaskStagesTemplate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twTaskStagesTemplate
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-编辑")
	@ApiOperation(value="任务列表模板表-编辑", notes="任务列表模板表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTaskStagesTemplate twTaskStagesTemplate) {
		twTaskStagesTemplateService.updateById(twTaskStagesTemplate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-通过id删除")
	@ApiOperation(value="任务列表模板表-通过id删除", notes="任务列表模板表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskStagesTemplateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-批量删除")
	@ApiOperation(value="任务列表模板表-批量删除", notes="任务列表模板表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskStagesTemplateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "任务列表模板表-通过id查询")
	@ApiOperation(value="任务列表模板表-通过id查询", notes="任务列表模板表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTaskStagesTemplate twTaskStagesTemplate = twTaskStagesTemplateService.getById(id);
		if(twTaskStagesTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTaskStagesTemplate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTaskStagesTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTaskStagesTemplate twTaskStagesTemplate) {
        return super.exportXls(request, twTaskStagesTemplate, TwTaskStagesTemplate.class, "任务列表模板表");
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
        return super.importExcel(request, response, TwTaskStagesTemplate.class);
    }

}
