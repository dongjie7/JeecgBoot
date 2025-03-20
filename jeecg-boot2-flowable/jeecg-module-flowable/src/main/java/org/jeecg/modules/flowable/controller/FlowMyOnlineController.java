package org.jeecg.modules.flowable.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.flowable.entity.FlowMyOnline;
import org.jeecg.modules.flowable.service.IFlowMyOnlineService;

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
 * @Description: flow_my_online
 * @Author: nbacheng
 * @Date:   2022-11-02
 * @Version: V1.0
 */
@Api(tags="flow_my_online")
@RestController
@RequestMapping("/flowable/flowMyOnline")
@Slf4j
public class FlowMyOnlineController extends JeecgController<FlowMyOnline, IFlowMyOnlineService> {
	@Autowired
	private IFlowMyOnlineService flowMyOnlineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param flowMyOnline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "flow_my_online-分页列表查询")
	@ApiOperation(value="flow_my_online-分页列表查询", notes="flow_my_online-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FlowMyOnline flowMyOnline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowMyOnline> queryWrapper = QueryGenerator.initQueryWrapper(flowMyOnline, req.getParameterMap());
		Page<FlowMyOnline> page = new Page<FlowMyOnline>(pageNo, pageSize);
		IPage<FlowMyOnline> pageList = flowMyOnlineService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param flowMyOnline
	 * @return
	 */
	@AutoLog(value = "flow_my_online-添加")
	@ApiOperation(value="flow_my_online-添加", notes="flow_my_online-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlowMyOnline flowMyOnline) {
		flowMyOnlineService.save(flowMyOnline);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowMyOnline
	 * @return
	 */
	@AutoLog(value = "flow_my_online-编辑")
	@ApiOperation(value="flow_my_online-编辑", notes="flow_my_online-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlowMyOnline flowMyOnline) {
		flowMyOnlineService.updateById(flowMyOnline);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_my_online-通过id删除")
	@ApiOperation(value="flow_my_online-通过id删除", notes="flow_my_online-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flowMyOnlineService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "flow_my_online-批量删除")
	@ApiOperation(value="flow_my_online-批量删除", notes="flow_my_online-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowMyOnlineService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_my_online-通过id查询")
	@ApiOperation(value="flow_my_online-通过id查询", notes="flow_my_online-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FlowMyOnline flowMyOnline = flowMyOnlineService.getById(id);
		if(flowMyOnline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowMyOnline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowMyOnline
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowMyOnline flowMyOnline) {
        return super.exportXls(request, flowMyOnline, FlowMyOnline.class, "flow_my_online");
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
        return super.importExcel(request, response, FlowMyOnline.class);
    }

}
