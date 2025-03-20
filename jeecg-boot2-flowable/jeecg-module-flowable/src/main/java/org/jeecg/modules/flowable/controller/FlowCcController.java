package org.jeecg.modules.flowable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.jeecg.modules.flowable.entity.FlowCc;
import org.jeecg.modules.flowable.service.IFlowCcService;
import org.jeecg.modules.flowable.service.IFlowTaskService;
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
 * @Description: 流程抄送表
 * @Author: nbacheng
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@Api(tags="流程抄送表")
@RestController
@RequestMapping("/flowable/flowCc")
@Slf4j
public class FlowCcController extends JeecgController<FlowCc, IFlowCcService> {
	
	@Autowired
	IFlowThirdService iFlowThirdService;
	
	@Autowired
	private IFlowCcService flowCcService;
	
	/**
	 * 分页列表查询
	 *
	 * @param flowCc
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "流程抄送表-分页列表查询")
	@ApiOperation(value="流程抄送表-分页列表查询", notes="流程抄送表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FlowCc flowCc,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowCc> queryWrapper = QueryGenerator.initQueryWrapper(flowCc, req.getParameterMap());
		
		queryWrapper.eq("username", iFlowThirdService.getLoginUser().getUsername());
		Page<FlowCc> page = new Page<FlowCc>(pageNo, pageSize);
		IPage<FlowCc> pageList = flowCcService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 我的抄送分页列表查询
	  *
	  * @param flowCc
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "我的抄送表-分页列表查询")
	 @ApiOperation(value="我的抄送表-分页列表查询", notes="我的抄送表-分页列表查询")
	 @GetMapping(value = "/myList")
	 public Result<?> queryPageLists(FlowCc flowCc,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<FlowCc> queryWrapper = QueryGenerator.initQueryWrapper(flowCc, req.getParameterMap());

		 queryWrapper.eq("initiator_username", iFlowThirdService.getLoginUser().getUsername());
		 Page<FlowCc> page = new Page<FlowCc>(pageNo, pageSize);
		 IPage<FlowCc> pageList = flowCcService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 //修改查看状态
	 @GetMapping(value = "/updateViewStatus")
	 public Result<?> updateStatus(@RequestParam(name = "id",required = false) String id){
	 	flowCcService.updateStatus(id);
		 return Result.OK("成功");
	 }
	
	/**
	 *   添加
	 *
	 * @param flowCc
	 * @return
	 */
	@AutoLog(value = "流程抄送表-添加")
	@ApiOperation(value="流程抄送表-添加", notes="流程抄送表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlowCc flowCc) {
		flowCcService.save(flowCc);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowCc
	 * @return
	 */
	@AutoLog(value = "流程抄送表-编辑")
	@ApiOperation(value="流程抄送表-编辑", notes="流程抄送表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlowCc flowCc) {
		flowCcService.updateById(flowCc);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流程抄送表-通过id删除")
	@ApiOperation(value="流程抄送表-通过id删除", notes="流程抄送表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flowCcService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "流程抄送表-批量删除")
	@ApiOperation(value="流程抄送表-批量删除", notes="流程抄送表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowCcService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流程抄送表-通过id查询")
	@ApiOperation(value="流程抄送表-通过id查询", notes="流程抄送表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FlowCc flowCc = flowCcService.getById(id);
		if(flowCc==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowCc);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowCc
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowCc flowCc) {
        return super.exportXls(request, flowCc, FlowCc.class, "流程抄送表");
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
        return super.importExcel(request, response, FlowCc.class);
    }

}
