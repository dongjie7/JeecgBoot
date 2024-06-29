package org.jeecg.modules.estar.tw.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.tw.entity.TwProjectMember;
import org.jeecg.modules.estar.tw.service.ITwProjectMemberService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 项目成员
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Api(tags="项目成员")
@RestController
@RequestMapping("/tw/twProjectMember")
@Slf4j
public class TwProjectMemberController extends JeecgController<TwProjectMember, ITwProjectMemberService> {
	@Autowired
	private ITwProjectMemberService twProjectMemberService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectMember
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目成员-分页列表查询")
	@ApiOperation(value="项目成员-分页列表查询", notes="项目成员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectMember twProjectMember,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectMember> queryWrapper = QueryGenerator.initQueryWrapper(twProjectMember, req.getParameterMap());
		Page<TwProjectMember> page = new Page<TwProjectMember>(pageNo, pageSize);
		IPage<TwProjectMember> pageList = twProjectMemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *  根据projectid获取成员列表
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-获取成员列表")
	@ApiOperation(value="项目成员-获取成员列表", notes="项目成员-获取成员列表")
	@PostMapping(value = "/listByProjectId")
	public Result<?> listByProjectId(@RequestParam(name="projectId",required=true) String projectId) {
		return Result.OK(twProjectMemberService.listByProjectId(projectId));
	}
	
	/**
	 *  邀请新成员页面初始化
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-邀请新成员页面初始化")
	@ApiOperation(value="项目成员-邀请新成员页面初始化", notes="项目成员-邀请新成员页面初始化")
	@PostMapping(value = "/listForInvite")
	public Result<?> listForInvite(@RequestParam Map<String,Object> mmap) {
		return twProjectMemberService.listForInvite(mmap);
	}
	
	/**
	 *  项目添加成员列表
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-项目添加成员列表")
	@ApiOperation(value="项目成员-项目添加成员列表", notes="项目成员-项目添加成员列表")
	@PostMapping(value = "/listForAdd")
	public Result<?> listForAdd(@RequestParam(name="projectId",required=true) String projectId,
			@RequestParam(name="organizationId",required=true) String organizationId) {
		return Result.OK(twProjectMemberService.listForAdd(projectId,organizationId));
	}
	
	/**
	 *  项目添加新成员的模糊查询
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-项目添加新成员的模糊查询")
	@ApiOperation(value="项目成员-根据项目添加新成员的模糊查询", notes="项目成员-项目添加新成员的模糊查询")
	@PostMapping(value = "/searchMember")
	public Result<?> searchMember(@RequestParam(name="projectId",required=true) String projectId,
			@RequestParam(name="organizationId",required=true) String organizationId,
			@RequestParam(name="keyword") String keyword) {
		return Result.OK(twProjectMemberService.searchMember(projectId,organizationId,keyword));
	}
	
	@PostMapping("/inviteMember")
    @ResponseBody
    public Result<?> inviteMember(@RequestParam Map<String,Object> mmap)
    {
        String memberId = MapUtils.getString(mmap,"memberId");
        String projectId = MapUtils.getString(mmap,"projectId");
        if(StringUtils.isEmpty(memberId) || StringUtils.isEmpty(projectId)){
            return Result.error("数据异常！");
        }
        return twProjectMemberService.inviteMember(memberId,projectId,0);
    }
	
	/**
	 *   添加
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-添加")
	@ApiOperation(value="项目成员-添加", notes="项目成员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProjectMember twProjectMember) {
		twProjectMemberService.save(twProjectMember);
		return Result.OK("添加成功！");
	}
	
	/**
	 *   根据项目id和用户id添加成员
	 *
	 * @param projectId, userId
	 * @return
	 */
	@AutoLog(value = "项目成员-根据项目id添加")
	@ApiOperation(value="项目成员-根据项目id添加", notes="项目成员-根据项目id添加")
	@PostMapping(value = "/addMember")
	public Result<?> addMember(@RequestParam(name="projectId",required=true) String projectId,
			                   @RequestParam(name="userId",required=true) String userId) {
		twProjectMemberService.addMember(projectId,userId);
		return Result.OK("添加成功！");
	}
	
	/**
	 *   根据项目id和用户id移除成员
	 *
	 * @param projectId, userId
	 * @return
	 */
	@AutoLog(value = "项目成员-根据项目id移除成员")
	@ApiOperation(value="项目成员-根据项目id移除成员", notes="项目成员-根据项目id移除成员")
	@PostMapping(value = "/removeMember")
	public Result<?> removeMember(@RequestParam(name="projectId",required=true) String projectId,
			                   @RequestParam(name="userId",required=true) String userId) {
		twProjectMemberService.removeMember(projectId,userId);
		return Result.OK("添加成功！");
	}
	
	/**
     * 我的项目 邀请新成员 模糊查询
     * @param mmap
     * @return
     */
    @PostMapping("/searchInviteMember")
    @ResponseBody
    public Result<?> searchInviteMember(@RequestParam Map<String,Object> mmap) {
    	return twProjectMemberService.searchInviteMember(mmap);
    }
	
	/**
	 *  编辑
	 *
	 * @param twProjectMember
	 * @return
	 */
	@AutoLog(value = "项目成员-编辑")
	@ApiOperation(value="项目成员-编辑", notes="项目成员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProjectMember twProjectMember) {
		twProjectMemberService.updateById(twProjectMember);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目成员-通过id删除")
	@ApiOperation(value="项目成员-通过id删除", notes="项目成员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twProjectMemberService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目成员-批量删除")
	@ApiOperation(value="项目成员-批量删除", notes="项目成员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectMemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目成员-通过id查询")
	@ApiOperation(value="项目成员-通过id查询", notes="项目成员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectMember twProjectMember = twProjectMemberService.getById(id);
		if(twProjectMember==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectMember);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectMember
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectMember twProjectMember) {
        return super.exportXls(request, twProjectMember, TwProjectMember.class, "项目成员");
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
        return super.importExcel(request, response, TwProjectMember.class);
    }

}
