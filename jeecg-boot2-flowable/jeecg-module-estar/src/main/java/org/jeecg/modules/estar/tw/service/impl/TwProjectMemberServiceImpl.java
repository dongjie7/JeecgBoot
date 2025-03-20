package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwProjectMember;
import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.mapper.TwProjectMemberMapper;
import org.jeecg.modules.estar.tw.mapper.TwAccountMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectMemberService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目成员
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TwProjectMemberServiceImpl extends ServiceImpl<TwProjectMemberMapper, TwProjectMember> implements ITwProjectMemberService {

	//@Autowired
	//ITwProjectService projectService;
	@Autowired
	TwProjectMapper projectMapper;
	@Autowired
	TwProjectMemberMapper projectMemberMapper;
	@Autowired
	ITwProjectLogService projectLogService;
	@Autowired
	ITwAccountService accountService;
	@Autowired
	TwAccountMapper accountMapper;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	@Transactional
	public void addMember(String projectId, String userId) {
		TwProjectMember projectMember = new TwProjectMember();
		projectMember.setProjectId(projectId);
		projectMember.setIsOwner(0);
		projectMember.setUserId(userId);
		projectMemberMapper.insert(projectMember);
		
	}

	@Override
	@Transactional
	public void removeMember(String projectId, String userId) {
		LambdaQueryWrapper<TwProjectMember> lambdaQueryWrapper=new LambdaQueryWrapper<TwProjectMember>();
        lambdaQueryWrapper.eq(TwProjectMember::getProjectId,projectId);
        lambdaQueryWrapper.eq(TwProjectMember::getUserId,userId);
        baseMapper.delete(lambdaQueryWrapper);
	}

	@Override
	public List<Map> listForAdd(String projectId, String organizationId) {
		List<TwAccount> listaccount = accountService.lambdaQuery().eq(TwAccount::getOrganizationId, organizationId).list();
		List<Map> resultData = new ArrayList<Map>();
		if(!CollectionUtils.isEmpty(listaccount)){
			Map tmpMap = null;
            for(TwAccount account:listaccount){
                tmpMap = new HashMap();
                SysUser sysUser = iEstarThirdService.getUserByUsername(account.getUserId());
                tmpMap.put("userId",account.getUserId());
                tmpMap.put("status",account.getStatus());
                tmpMap.put("avatar",sysUser.getAvatar());
                tmpMap.put("name",sysUser.getRealname());
                tmpMap.put("email",sysUser.getEmail());
                tmpMap.put("joined",isProjectMember(projectId,account.getUserId()));
                resultData.add(tmpMap);
            }
        }
		return resultData;
	}

	@Override
	public boolean isProjectMember(String projectId, String userId) {
		List<TwProjectMember> list = lambdaQuery().eq(TwProjectMember::getUserId,userId)
	                .eq(TwProjectMember::getProjectId,projectId).list();
	    if(!CollectionUtils.isEmpty(list)){
	            return true;
	    }
		return false;
	}

	@Override
	public List<Map> searchMember(String projectId, String organizationId, String keyword) {
		List<Map> listaccount = accountMapper.getMemberByName(organizationId, keyword);
		List<Map> resultData = new ArrayList<Map>();
		if(!CollectionUtils.isEmpty(listaccount)){
			Map tmpMap = null;
            for(Map account:listaccount){
                tmpMap = new HashMap();
                SysUser sysUser = iEstarThirdService.getUserByUsername((account.get("userId").toString()));
                tmpMap.put("userId",(account.get("userId").toString()));
                tmpMap.put("status",(account.get("status").toString()));
                tmpMap.put("avatar",sysUser.getAvatar());
                tmpMap.put("name",sysUser.getRealname());
                tmpMap.put("email",sysUser.getEmail());
                tmpMap.put("joined",isProjectMember(projectId,(account.get("userId")).toString()));
                resultData.add(tmpMap);
            }
        }
		return resultData;
	}

	@SuppressWarnings("serial")
	@Override
	public List<Map> listByProjectId(String projectId) {
		QueryWrapper<TwProjectMember> queryWrapper =  new QueryWrapper<>();
		Page<TwProjectMember> page = new Page<TwProjectMember>(1, 100);//获取一页100条记录
		queryWrapper.eq("project_id",projectId);
		queryWrapper.orderByDesc("is_owner");
		IPage<TwProjectMember> memberpage = page(page,queryWrapper);
		if(ObjectUtil.isNotEmpty(memberpage) && !CollectionUtils.isEmpty(memberpage.getRecords())) {
			 List<TwProjectMember> lismember = memberpage.getRecords();
			 List<Map> mapList = new ArrayList<>();
			 lismember.forEach(member -> {
				 SysUser sysuser = iEstarThirdService.getUserByUsername(member.getUserId());
				 if(ObjectUtil.isNotEmpty(sysuser)) {
					 mapList.add(new HashMap(){{
							put("name",sysuser.getRealname());
	                        put("avatar",sysuser.getAvatar());
	                        put("id",sysuser.getId());
	                        put("username",sysuser.getUsername());
	                        put("email",sysuser.getEmail());
	                        put("is_owner",member.getIsOwner());
					 }});
				 }
			 });
			 return mapList;
		 }
		return null;
	}

	@Override
	public Map gettMemberIdAndNameByProjectId(String id) {
		return baseMapper.selectMemberIdAndNameByProjectId(id);
	}

	@Override
	@Transactional
	public Result<?> inviteMember(String memberId, String projectId, Integer isOwner) {
		//TwProject project = projectService.lambdaQuery().eq(TwProject::getId,projectId).one();
		TwProject project = projectMapper.selectById(projectId);
        if(ObjectUtil.isEmpty(project)){
        	Result.error("该项目已失效！");
        }
        TwProjectMember projectMember = lambdaQuery().eq(TwProjectMember::getUserId,memberId)
                .eq(TwProjectMember::getProjectId,projectId)
                .one();
        if(ObjectUtil.isNotEmpty(projectMember)){
            return Result.OK(projectMember);
        }
        projectMember = new TwProjectMember();
        projectMember.setUserId(memberId);
        projectMember.setProjectId(projectId);
        projectMember.setIsOwner(isOwner);
        save(projectMember);
        
        TwAccount account = new TwAccount();
        account.setUserId(memberId);
        account.setOrganizationId(project.getOrganizationId());
        accountService.inviteMember(account);

        projectLogService.run(new HashMap(){{
            put("member_id",memberId);
            put("source_id",project.getId());
            put("type","inviteMember");
            put("to_member_id",memberId);
            put("is_comment",0);
            put("content","");
            put("project_id",project.getId());
        }});
        return Result.OK(projectMember);
	}

	@Override
	public Result<?> listForInvite(Map<String, Object> mmap) {
        String projectId = MapUtils.getString(mmap, "projectId");
        if(StringUtils.isEmpty(projectId)){
        	Result.error("请先选择项目");
        }
        SysUser loginUser = iEstarThirdService.getLoginUser();
		String organizationId = accountService.selectOrgIdByUserId(loginUser.getUsername());
        if(StringUtils.isEmpty(organizationId)){
        	Result.error("项目不存在组织");
        }
        List<TwAccount> listAccounts = accountService.lambdaQuery()
                .eq(TwAccount::getOrganizationId, organizationId)
                .list();
        List<Map> resultData = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listAccounts)){
            Map tmpMap = null;
            for(TwAccount cm:listAccounts){
            	SysUser user = iEstarThirdService.getUserByUsername(cm.getUserId());
                tmpMap = new HashMap();
                tmpMap.put("memberId",cm.getUserId());
                tmpMap.put("status",cm.getStatus());
                tmpMap.put("avatar",user.getAvatar());
                tmpMap.put("name",cm.getName());
                tmpMap.put("email",cm.getEmail());
                tmpMap.put("joined",isProjectMember(projectId,cm.getUserId()));
                resultData.add(tmpMap);
            }
        }
        return Result.OK(resultData);
	}

	@Override
	public Result<?> searchInviteMember(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String projectId = MapUtils.getString(mmap, "projectId");
        
        String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
        String keyword = MapUtils.getString(mmap,"keyword");

        List<Map> listMemberAccounts = getMemberCountByOrgIdAndMemberName(orgId,keyword);
        List<Map> resultData = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listMemberAccounts)){
            Map tmpMap = null;
            for(Map cm:listMemberAccounts){
                tmpMap = new HashMap();
                tmpMap.put("memberId",MapUtils.getString(cm, "member_id",""));
                tmpMap.put("status",MapUtils.getString(cm, "status",""));
                tmpMap.put("avatar",MapUtils.getString(cm, "avatar",""));
                tmpMap.put("name",MapUtils.getString(cm, "name",""));
                tmpMap.put("email",MapUtils.getString(cm, "email",""));
                tmpMap.put("joined",isProjectMember(projectId,MapUtils.getString(cm, "member_id","")));
                resultData.add(tmpMap);
            }
        }
        return Result.OK(resultData);
	}

	private List<Map> getMemberCountByOrgIdAndMemberName(String orgId, String keyword) {
		return baseMapper.getMemberCountByOrgIdAndMemberName(orgId, keyword);
	}

	



}
