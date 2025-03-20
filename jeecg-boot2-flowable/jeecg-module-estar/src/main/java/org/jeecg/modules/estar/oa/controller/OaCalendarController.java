package org.jeecg.modules.estar.oa.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.oa.entity.OaCalendar;
import org.jeecg.modules.estar.oa.service.IOaCalendarService;
import org.jeecg.modules.estar.oa.util.JsonUtil;
import org.jeecg.modules.estar.oa.vo.CalendarListVo;
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
 * @Description: OA日历表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Api(tags="OA日历表")
@RestController
@RequestMapping("/oa/oaCalendar")
@Slf4j
public class OaCalendarController extends JeecgController<OaCalendar, IOaCalendarService> {
	@Autowired
	private IOaCalendarService oaCalendarService;
	
	/**
	 * 分页列表查询
	 *
	 * @param oaCalendar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "OA日历表-分页列表查询")
	@ApiOperation(value="OA日历表-分页列表查询", notes="OA日历表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OaCalendar oaCalendar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OaCalendar> queryWrapper = QueryGenerator.initQueryWrapper(oaCalendar, req.getParameterMap());
		Page<OaCalendar> page = new Page<OaCalendar>(pageNo, pageSize);
		IPage<OaCalendar> pageList = oaCalendarService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 获取日历列表
	 *
	 * @param 
	 * @param req
	 * @return
	 */
	@AutoLog(value = "OA日程表-获取日历列表")
	@ApiOperation(value="OA日程表-获取日历列表", notes="OA日程表-获取日历列表")
	@GetMapping(value = "/getlist")
	public Result<?> getList() {
		List<OaCalendar> dataList = oaCalendarService.getList();
		List<CalendarListVo> listvo = JsonUtil.getJsonToList(dataList, CalendarListVo.class);
		return Result.OK(listvo);
	}
	
	/**
	 *   添加
	 *
	 * @param oaCalendar
	 * @return
	 */
	@AutoLog(value = "OA日历表-添加")
	@ApiOperation(value="OA日历表-添加", notes="OA日历表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OaCalendar oaCalendar) {
		oaCalendarService.save(oaCalendar);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param oaCalendar
	 * @return
	 */
	@AutoLog(value = "OA日历表-编辑")
	@ApiOperation(value="OA日历表-编辑", notes="OA日历表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OaCalendar oaCalendar) {
		oaCalendarService.updateById(oaCalendar);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "OA日历表-通过id删除")
	@ApiOperation(value="OA日历表-通过id删除", notes="OA日历表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		oaCalendarService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "OA日历表-批量删除")
	@ApiOperation(value="OA日历表-批量删除", notes="OA日历表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.oaCalendarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "OA日历表-通过id查询")
	@ApiOperation(value="OA日历表-通过id查询", notes="OA日历表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OaCalendar oaCalendar = oaCalendarService.getById(id);
		if(oaCalendar==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(oaCalendar);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param oaCalendar
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OaCalendar oaCalendar) {
        return super.exportXls(request, oaCalendar, OaCalendar.class, "OA日历表");
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
        return super.importExcel(request, response, OaCalendar.class);
    }

}
