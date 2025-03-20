package org.jeecg.modules.flowable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.flowable.entity.FlowDeployOnline;
import org.jeecg.modules.flowable.service.IFlowDeployOnlineService;

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
 * @Description: flow_deploy_online
 * @Author: nbacheng
 * @Date:   2022-10-21
 * @Version: V1.0
 */
@Api(tags="flow_deploy_online")
@RestController
@RequestMapping("/flowable/flowDeployOnline")
@Slf4j
public class FlowDeployOnlineController extends JeecgController<FlowDeployOnline, IFlowDeployOnlineService> {
	@Autowired
	private IFlowDeployOnlineService flowDeployOnlineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param flowDeployOnline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-分页列表查询")
	@ApiOperation(value="flow_deploy_online-分页列表查询", notes="flow_deploy_online-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FlowDeployOnline flowDeployOnline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowDeployOnline> queryWrapper = QueryGenerator.initQueryWrapper(flowDeployOnline, req.getParameterMap());
		Page<FlowDeployOnline> page = new Page<FlowDeployOnline>(pageNo, pageSize);
		IPage<FlowDeployOnline> pageList = flowDeployOnlineService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param flowDeployOnline
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-添加")
	@ApiOperation(value="flow_deploy_online-添加", notes="flow_deploy_online-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlowDeployOnline flowDeployOnline) {
		flowDeployOnlineService.save(flowDeployOnline);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowDeployOnline
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-编辑")
	@ApiOperation(value="flow_deploy_online-编辑", notes="flow_deploy_online-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlowDeployOnline flowDeployOnline) {
		flowDeployOnlineService.updateById(flowDeployOnline);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-通过id删除")
	@ApiOperation(value="flow_deploy_online-通过id删除", notes="flow_deploy_online-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flowDeployOnlineService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-批量删除")
	@ApiOperation(value="flow_deploy_online-批量删除", notes="flow_deploy_online-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowDeployOnlineService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_deploy_online-通过id查询")
	@ApiOperation(value="flow_deploy_online-通过id查询", notes="flow_deploy_online-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FlowDeployOnline flowDeployOnline = flowDeployOnlineService.getById(id);
		if(flowDeployOnline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowDeployOnline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowDeployOnline
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowDeployOnline flowDeployOnline) {
        return super.exportXls(request, flowDeployOnline, FlowDeployOnline.class, "flow_deploy_online");
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
        return super.importExcel(request, response, FlowDeployOnline.class);
    }

}
