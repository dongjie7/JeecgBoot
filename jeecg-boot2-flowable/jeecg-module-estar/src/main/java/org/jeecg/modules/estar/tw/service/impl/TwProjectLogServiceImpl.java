package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;
import org.jeecg.modules.estar.tw.mapper.TwProjectLogMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.util.StringUtils;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 项目日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwProjectLogServiceImpl extends ServiceImpl<TwProjectLogMapper, TwProjectLog> implements ITwProjectLogService {

	//@Autowired
	//ITwProjectService projectService;
	@Autowired
	TwProjectMapper projectMapper;
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public TwProject run(Map param) {
		TwProjectLog projectLog = new TwProjectLog();
		projectLog.setActionType(MapUtils.getString(param,"action_type"));
		projectLog.setToMemberId(MapUtils.getString(param,"to_member_id"));
		projectLog.setIsComment(MapUtils.getInteger(param,"is_comment"));
		projectLog.setContent(MapUtils.getString(param,"content",""));
		projectLog.setOpeType(MapUtils.getString(param,"type"));
		projectLog.setSourceId(MapUtils.getString(param,"source_id"));
		projectLog.setMemberId(MapUtils.getString(param,"member_id"));
		projectLog.setProjectId(MapUtils.getString(param,"project_id"));
		
        //TwProject project = projectService.getProjectById(projectLog.getProjectId());
        TwProject project = projectMapper.selectById(projectLog.getProjectId());
        projectLog.setProjectId(project.getId());
        SysUser toMember = new SysUser();
        if(StringUtils.isNotEmpty(projectLog.getToMemberId())){
            toMember = iEstarThirdService.getUserByUsername(projectLog.getToMemberId());
        }
        String type = projectLog.getOpeType();
        if("create".equals(type)){
            projectLog.setIcon("plus");
            projectLog.setRemark("创建了项目");
            projectLog.setContent(project.getName());
        }else if("edit".equals(type)){
            projectLog.setIcon("edit");
            projectLog.setRemark("编辑了项目");
            projectLog.setContent(project.getName());
        }else if("name".equals(type)){
            projectLog.setIcon("edit");
            projectLog.setRemark("修改了项目名称");
            projectLog.setContent(project.getName());
        }else if("content".equals(type)){
            projectLog.setIcon("file-text");
            projectLog.setRemark("更新了备注");
            projectLog.setContent(project.getDescription());
        }else if("clearContent".equals(type)){
            projectLog.setIcon("file-text");
            projectLog.setRemark("清空了备注");
        }else if("inviteMember".equals(type)){
            projectLog.setIcon("user-add");
            projectLog.setRemark("邀请"+toMember.getRealname()+"加入项目");
            projectLog.setContent(toMember.getRealname());
        }else if("removeMember".equals(type)){
            projectLog.setIcon("user-delete");
            projectLog.setRemark("移除了成员"+toMember.getRealname());
            projectLog.setContent(toMember.getRealname());
        }else if("recycle".equals(type)){
            projectLog.setIcon("delete");
            projectLog.setRemark("把项目移到了回收站");
        }else if("recovery".equals(type)){
            projectLog.setIcon("undo");
            projectLog.setRemark("恢复了项目");
        }else if("archive".equals(type)){
            projectLog.setIcon("delete");
            projectLog.setRemark("归档了项目");
        }else if("recoveryArchive".equals(type)){
            projectLog.setIcon("undo");
            projectLog.setRemark("恢复了项目");
        }else if("uploadFile".equals(type)){
            projectLog.setIcon("link");
            projectLog.setRemark("上传了文件文件");
            projectLog.setContent("<a target=\"_blank\" class=\"muted\" href=\""+MapUtils.getString(param,"url")+" \">\""+MapUtils.getString(param,"title")+"</a>");
        }else if("deleteFile".equals(type)){
            projectLog.setIcon("disconnect");
            projectLog.setRemark("删除了文件");
            projectLog.setContent("<a target=\"_blank\" class=\"muted\" href=\""+MapUtils.getString(param,"url")+" \">\""+MapUtils.getString(param,"title")+"</a>");
        }else{
            projectLog.setIcon("plus");
            projectLog.setRemark("创建了文件");
        }
        baseMapper.insert(projectLog);
        return project;
	}

}
