package org.jeecg.modules.estar.oa.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.oa.entity.OaSchedule;
import org.jeecg.modules.estar.oa.service.IOaScheduleService;
import org.jeecg.modules.estar.oa.util.JsonUtil;
import org.jeecg.modules.estar.oa.vo.ScheduleListVO;
import org.jeecg.modules.estar.oa.vo.ScheduleTime;
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
 * @Description: OA日程表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Api(tags="OA日程表")
@RestController
@RequestMapping("/oa/oaSchedule")
@Slf4j
public class OaScheduleController extends JeecgController<OaSchedule, IOaScheduleService> {
	@Autowired
	private IOaScheduleService oaScheduleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param oaSchedule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "OA日程表-分页列表查询")
	@ApiOperation(value="OA日程表-分页列表查询", notes="OA日程表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OaSchedule oaSchedule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OaSchedule> queryWrapper = QueryGenerator.initQueryWrapper(oaSchedule, req.getParameterMap());
		Page<OaSchedule> page = new Page<OaSchedule>(pageNo, pageSize);
		IPage<OaSchedule> pageList = oaScheduleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 获取日程安排列表
	 *
	 * @param ScheduleTime
	 * @param req
	 * @return
	 */
	@AutoLog(value = "OA日程表-获取日程安排列表")
	@ApiOperation(value="OA日程表-获取日程安排列表", notes="OA日程表-获取日程安排列表")
	@GetMapping(value = "/getlist")
	public Result<?> getList(ScheduleTime scheduleTime) {
		List<OaSchedule> dataList = oaScheduleService.getList(scheduleTime);
		List<ScheduleListVO> listvo = JsonUtil.getJsonToList(dataList, ScheduleListVO.class);
		return Result.OK(listvo);
	}
	
	/**
	 *   添加
	 *
	 * @param oaSchedule
	 * @return
	 */
	@AutoLog(value = "OA日程表-添加")
	@ApiOperation(value="OA日程表-添加", notes="OA日程表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OaSchedule oaSchedule) {
		//oaScheduleService.save(oaSchedule);
		oaScheduleService.saveAndAddSchedule(oaSchedule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param oaSchedule
	 * @return
	 */
	@AutoLog(value = "OA日程表-编辑")
	@ApiOperation(value="OA日程表-编辑", notes="OA日程表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OaSchedule oaSchedule) {
		//oaScheduleService.updateById(oaSchedule);
		
		if(oaScheduleService.updateSchedule(oaSchedule)){
			return Result.OK("编辑成功!");
		}
		else {
			return Result.error("日程已经发送,编辑失败!");
		}
		
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "OA日程表-通过id删除")
	@ApiOperation(value="OA日程表-通过id删除", notes="OA日程表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//oaScheduleService.removeById(id);
		if(oaScheduleService.removeSchedule(id)) {
			return Result.OK("删除成功!");
		}
		else {
			return Result.error("消息已经处理,删除失败!");
		}
		
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "OA日程表-批量删除")
	@ApiOperation(value="OA日程表-批量删除", notes="OA日程表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.oaScheduleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "OA日程表-通过id查询")
	@ApiOperation(value="OA日程表-通过id查询", notes="OA日程表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OaSchedule oaSchedule = oaScheduleService.getById(id);
		if(oaSchedule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(oaSchedule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param oaSchedule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OaSchedule oaSchedule) {
        return super.exportXls(request, oaSchedule, OaSchedule.class, "OA日程表");
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
        return super.importExcel(request, response, OaSchedule.class);
    }

}
