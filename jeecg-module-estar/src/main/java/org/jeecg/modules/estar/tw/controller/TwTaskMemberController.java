package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwTaskMember;
import org.jeecg.modules.estar.tw.service.ITwTaskMemberService;
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
 * @Description: 项目任务团队表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="项目任务团队表")
@RestController
@RequestMapping("/tw/twTaskMember")
@Slf4j
public class TwTaskMemberController extends JeecgController<TwTaskMember, ITwTaskMemberService> {
	@Autowired
	private ITwTaskMemberService twTaskMemberService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twTaskMember
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-分页列表查询")
	@ApiOperation(value="项目任务团队表-分页列表查询", notes="项目任务团队表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwTaskMember twTaskMember,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwTaskMember> queryWrapper = QueryGenerator.initQueryWrapper(twTaskMember, req.getParameterMap());
		Page<TwTaskMember> page = new Page<TwTaskMember>(pageNo, pageSize);
		IPage<TwTaskMember> pageList = twTaskMemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	@PostMapping("/inviteMemberBatch")
    @ResponseBody
    public Result<?> inviteMemberBatch(@RequestParam Map<String,Object> mmap) {
		return twTaskMemberService.inviteMemberBatch(mmap);
    }
	
	/**
	 *   添加
	 *
	 * @param twTaskMember
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-添加")
	@ApiOperation(value="项目任务团队表-添加", notes="项目任务团队表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwTaskMember twTaskMember) {
		twTaskMemberService.save(twTaskMember);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twTaskMember
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-编辑")
	@ApiOperation(value="项目任务团队表-编辑", notes="项目任务团队表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwTaskMember twTaskMember) {
		twTaskMemberService.updateById(twTaskMember);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-通过id删除")
	@ApiOperation(value="项目任务团队表-通过id删除", notes="项目任务团队表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twTaskMemberService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-批量删除")
	@ApiOperation(value="项目任务团队表-批量删除", notes="项目任务团队表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twTaskMemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目任务团队表-通过id查询")
	@ApiOperation(value="项目任务团队表-通过id查询", notes="项目任务团队表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwTaskMember twTaskMember = twTaskMemberService.getById(id);
		if(twTaskMember==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twTaskMember);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twTaskMember
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwTaskMember twTaskMember) {
        return super.exportXls(request, twTaskMember, TwTaskMember.class, "项目任务团队表");
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
        return super.importExcel(request, response, TwTaskMember.class);
    }

}
