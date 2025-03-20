package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.entity.TwProjectVersion;
import org.jeecg.modules.estar.tw.entity.TwProjectVersionLog;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.mapper.TwProjectVersionLogMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectVersionMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwProjectFeaturesService;
import org.jeecg.modules.estar.tw.service.ITwProjectVersionLogService;
import org.jeecg.modules.estar.tw.service.ITwProjectVersionService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.util.BeanMapUtils;
import org.jeecg.modules.estar.tw.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 项目版本表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TwProjectVersionServiceImpl extends ServiceImpl<TwProjectVersionMapper, TwProjectVersion> implements ITwProjectVersionService {

	//@Autowired
	//ITwTaskService taskService;
	@Autowired
	TwTaskMapper taskMapper;
	@Autowired
	ITwAccountService accountService;
	@Autowired
	ITwProjectFeaturesService projectFeaturesService;
	@Autowired
    TwProjectVersionLogMapper projectVersionLogMapper;
	@Autowired
	ITwProjectVersionLogService projectVersionLogService;
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	@Override
	public void updateSchedule(String versionId) {
		TwProjectVersion pv = getPVById(versionId);
        /*List<Map> listTaskMap = taskService.getTaskListByVersionAndDelete(new HashMap(){{
            put("versionId",versionId);
            put("deleted",0);
        }});*/
		List<Map> listTaskMap = taskMapper.selectTaskListByVersionAndDelete(new HashMap(){{
            put("versionId",versionId);
            put("deleted",0);
        }});
        Integer doneTotal = 0;
        if(CollectionUtils.isNotEmpty(listTaskMap)){
            for(Map map:listTaskMap){
                if(MapUtils.getInteger(map,"done",0)>0){
                    doneTotal++;
                }
            }
            int size = listTaskMap.size();
            size = size>0?size:1;
            int schedule = (int)Math.floor(doneTotal/size * 100);
            float f1 = (float)((float)doneTotal/(float)size)*100;
            pv.setSchedule((int)f1);
            updateById(pv);
        }
		
	}
	@Override
	public TwProjectVersion getPVById(String versionId) {
		LambdaQueryWrapper<TwProjectVersion> pvQw = new LambdaQueryWrapper<>();
        pvQw.eq(TwProjectVersion::getId,versionId);
        return baseMapper.selectOne(pvQw);
	}
	@Override
	public Result<?> save(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
        String name = MapUtils.getString(mmap,"name");
        String description = MapUtils.getString(mmap,"description");
        String startTime = MapUtils.getString(mmap,"startTime");
        String planPublishTime = MapUtils.getString(mmap,"planPublishTime");
        String featuresId = MapUtils.getString(mmap,"featuresId");

        if(StringUtils.isEmpty(name)){
            return Result.error("请填写版本名称");
        }

        Map m = projectFeaturesService.getProjectFeaturesById(featuresId);
        if(MapUtils.isEmpty(m)){
            return Result.error("该版本库已失效");
        }
        m = gettProjectVersionByNameAndFeaturesId(name,featuresId);
        if(MapUtils.isNotEmpty(m)){
            return Result.error("该版本已名称存在");
        }

        TwProjectVersion pv = new TwProjectVersion();
        TwProjectVersionLog pvl = new TwProjectVersionLog();
        pv.setFeaturesId(featuresId);
        pv.setStartTime(DateUtil.stringToDate(startTime));
        pv.setPlanPublishTime(DateUtil.stringToDate(planPublishTime));
        pv.setDescription(description);
        pv.setName(name);
        pv.setOrganizationId(orgId);
        pvl.setMemberId(MapUtils.getString(mmap,"memberCountId"));
        pvl.setSourceId(pv.getId());
        pvl.setRemark("创建了新版本");
        pvl.setVerType("create");
        pvl.setContent(name);
        pvl.setFeaturesId(featuresId);
        pvl.setIcon("plus");
        Integer i = addProjectVersionAndVersionLog(pv,pvl);
        if(i == 2){
            Map pvMap = getProjectVersionById(pv.getId());
            return Result.OK(pv);
        }
        return Result.error("保存失败");
	}
	private Map getProjectVersionById(String id) {
		return baseMapper.selectProjectVersionById(id);
	}
	private Map gettProjectVersionByNameAndFeaturesId(String name, String featuresId) {
		return baseMapper.selectProjectVersionByNameAndFeaturesId(name, featuresId);
	}
	@Transactional
    public Integer addProjectVersionAndVersionLog(TwProjectVersion pv, TwProjectVersionLog pvl){
        Integer i = baseMapper.insert(pv);
        Integer j = projectVersionLogMapper.insert(pvl);
        return i+j;
    }
	@Override
	public Result<?> edit(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap,"versionId");
        String name = MapUtils.getString(mmap,"name");
        String description = MapUtils.getString(mmap,"description");
        String start_time = MapUtils.getString(mmap,"start_time");
        String plan_publish_time = MapUtils.getString(mmap,"plan_publish_time");
        if(StringUtils.isEmpty(versionId)){
            return Result.error("请选择一个版本");
        }
        Map versionMap = getProjectVersionById(versionId);
        if(MapUtils.isEmpty(versionMap)){
            return Result.error("该版本已失效");
        }
        if(!StringUtils.isEmpty(name)){
            Map proVerMap = gettProjectVersionByNameAndFeaturesId(name,MapUtils.getString(versionMap,"features_code"));
            if(MapUtils.isNotEmpty(proVerMap)){
                return Result.error("该版本名称已存在");
            }
        }
        String type = "name";
        TwProjectVersion upProjectVersion = new TwProjectVersion();
        upProjectVersion.setId(MapUtils.getString(versionMap,"id"));
        if(!StringUtils.isEmpty(name)) {
            upProjectVersion.setName(name);
        }
        if(!StringUtils.isEmpty(description)) {
            upProjectVersion.setDescription(description);
        }
        if(!StringUtils.isEmpty(start_time)) {
            upProjectVersion.setStartTime(DateUtil.stringToDate(start_time));
        }
        if(!StringUtils.isEmpty(plan_publish_time)) {
            upProjectVersion.setPlanPublishTime(DateUtil.stringToDate(plan_publish_time));
        }
        boolean bo =  updateById(upProjectVersion);
        TwProjectVersionLog pvl = new TwProjectVersionLog();
        String remark = "";
        if(null != name){
            type="name";
            remark = "更新名称为 " + name + " ";
        }
        if(null != description){
            type="content";
            remark = "更新描述为 " + description + " ";
            if("".equals(description)){
                type="clearContent";
                remark = " 清空描述内容 ";
            }
        }
        if(null != start_time){
            type="setStartTime";
            remark = "更新开始时间为 " + start_time + " ";
            if("".equals(start_time)){
                type="clearStartTime";
                remark = " 清空开始时间 ";
            }
        }
        if(null != plan_publish_time){
            type="setPlanPublishTime";
            remark = "更新计划发布时间为 "+ plan_publish_time;
            if("".equals(plan_publish_time)){
                type="clearPlanPublishTime";
                remark = "清除计划发布时间";
            }
        }

        SysUser loginUser = iEstarThirdService.getLoginUser();
        pvl.setMemberId(loginUser.getUsername());
        pvl.setSourceId(versionId).setRemark(remark);
        pvl.setVerType("status").setContent("").setCreateTime(new Date());
        pvl.setFeaturesId(MapUtils.getString(versionMap,"features_id")).setIcon("check-square");
        projectVersionLogService.save(pvl);
        return Result.OK(bo);
	}
	@Override
	public Result<?> delete(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap,"versionId");
        if(StringUtils.isEmpty(versionId)){
            return Result.error("请选择一个版本");
        }
        return Result.OK(delProjectVersion(versionId));
	}
	@Transactional
    public Integer delProjectVersion(String versionId){
        Integer i1 = baseMapper.deleteProjectVersionById(versionId);
        Integer i2 = taskMapper.updateTaskFeaAndVerByVerId(versionId);
        return  i1+i2;
    }
	@Override
	public Result<?> listIndex(Map<String, Object> mmap) {
		String projectFeaturesId = MapUtils.getString(mmap,"projectFeaturesId");
        if(StringUtils.isEmpty(projectFeaturesId)){
            return Result.error("请选择一个版本库");
        }
        return Result.OK(getProjectVersion(projectFeaturesId));
	}
	private List<Map> getProjectVersion(String projectFeaturesId) {
		return baseMapper.selectProjectVersionByFeaturesId(projectFeaturesId);
	}
	@Override
	public Result<?> changeStatus(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap, "versionId");
        Integer status = MapUtils.getInteger(mmap, "status",-1);
        String publishTime = MapUtils.getString(mmap, "publishTime");
        
        if (StringUtils.isEmpty(versionId)) {
            return Result.error("请选择一个版本");
        }
        Map versionMap = getProjectVersionById(versionId);
        TwProjectVersion pv = new TwProjectVersion();
        pv.setId(MapUtils.getString(versionMap,"id"));
        pv.setStatus(status);
        if(status == 3){
            pv.setPublishTime(DateUtil.stringToDate(publishTime));
        }
        boolean i = updateById(pv);
        TwProjectVersionLog pvl = new TwProjectVersionLog();
        SysUser loginUser = iEstarThirdService.getLoginUser();
        pvl.setMemberId(loginUser.getUsername());
        pvl.setSourceId(versionId).setRemark("更新了状态为"+ getStatusTextAttr(String.valueOf(status)));
        pvl.setVerType("status").setContent("").setCreateTime(new Date());
        pvl.setFeaturesId(MapUtils.getString(versionMap,"features_id")).setIcon("check-square");
        projectVersionLogService.save(pvl);
        return Result.OK(i);
	}
	
	public String getStatusTextAttr(String status){
        //状态。0：未开始，1：进行中，2：延期发布，3：已发布
        if(null == status){
            return "-";
        }
        switch (Integer.parseInt(status)){
            case 0:
                return "未开始";
            case 1:
                return "进行中";
            case 2:
                return "延期发布";
            case 3:
                return "已发布";
        }
        return "-";
    }
	@Override
	public Result<?> getVersionTask(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap,"versionId");
        if(StringUtils.isEmpty(versionId)){
            return Result.error("请选择一个版本");
        }
        Map param = new HashMap(){{
            put("versionId",versionId);
            put("deleted",0);
        }};
        //List<Map>  taskList = taskService.getTaskListByVersionAndDelete(param);
        List<Map>  taskList = taskMapper.selectTaskListByVersionAndDelete(param);
        List<Map> resultList = new ArrayList<>();
        Map memberMap = null;
        for(Map m:taskList){
        	SysUser sysuser = iEstarThirdService.getUserByUsername(MapUtils.getString(m,"assign_to"));
            memberMap.put("name", sysuser.getRealname());
            memberMap.put("avatar", sysuser.getAvatar());
            m.put("executor",memberMap);
            resultList.add(m);
        }
        return Result.OK(resultList);
	}
	@Override
	public Result<?> getVersionLog(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap,"versionId");
        Integer showAll = MapUtils.getInteger(mmap,"all",0);

        List<Map> selList = new ArrayList<>();
        List<Map> listResult = new ArrayList<>();
        Map resultData = new HashMap();

        if(showAll == 0){
           selList = projectVersionLogService.getProjectVersionLogBySourceIdAll(versionId);
           if(selList == null) {
               selList = new ArrayList<>();
           }

           resultData.put("total",selList.size());
        }else{
            Integer pageSize = MapUtils.getInteger(mmap,"pageSize",1000);
            Integer page = MapUtils.getInteger(mmap,"page",1);
            IPage<Map> iPage = new Page<>();
            iPage.setCurrent(page);iPage.setSize(pageSize);
            iPage = projectVersionLogService.getProjectVersionBySourceId(iPage,versionId);
            selList = iPage.getRecords();
            resultData.put("total",iPage.getTotal());
            resultData.put("page",iPage.getCurrent());
        }
        if(!CollectionUtils.isEmpty(selList)){
            for(Map m:selList){
            	Map memberMap = new HashMap();
            	SysUser sysuser = iEstarThirdService.getUserByUsername(MapUtils.getString(m,"member_id"));
            	memberMap.put("id", sysuser.getUsername());
                memberMap.put("name", sysuser.getRealname());
                memberMap.put("avatar", sysuser.getAvatar());
                m.put("member",memberMap);;
                listResult.add(m);
            }
        }
        resultData.put("list",listResult);
        return Result.OK(resultData);
	}
	@Override
	public Result<?> getVersionInfo(Map<String, Object> mmap) {
		String versionId = MapUtils.getString(mmap,"versionId");
        if(StringUtils.isEmpty(versionId)){
            return Result.error("请选择一个版本");
        }
        Map versionMap = getProjectVersionById(versionId);
        versionMap.put("statusText",getStatusTextAttr(MapUtils.getString(versionMap,"status")));
        if(MapUtils.isNotEmpty(versionMap)){
            Map featureMap = projectFeaturesService.getProjectFeaturesById(MapUtils.getString(versionMap,"features_id"));
            versionMap.put("featureName",MapUtils.getString(featureMap,"name"));
            versionMap.put("projectId",MapUtils.getString(featureMap,"project_id"));
        }
        return Result.OK(versionMap);
	}
	@Override
	public Result<?> removeVersionTask(Map<String, Object> mmap) throws Exception {
		String taskId = MapUtils.getString(mmap,"taskId");
        
        //Map taskMap = taskService.getTaskMapById(taskId);
		Map taskMap = taskMapper.selectTaskById(taskId);
        TwTask task = BeanMapUtils.mapToBean(taskMap,TwTask.class);
        SysUser loginUser = iEstarThirdService.getLoginUser();
        if(MapUtils.isEmpty(taskMap)){
            return Result.error("该任务已被失效");
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(taskMap,"version_code"))){
            task.setVersionId("");
            task.setFeaturesId("");
            task = removeVersionTask(task,loginUser.getUsername(),MapUtils.getString(taskMap,"version_id"));
        }
        return Result.OK(task);
	}
	
	@Transactional
    public TwTask removeVersionTask(TwTask task,String memberId,String versionId){
        if(StringUtils.isNotEmpty(versionId)){
            task.setVersionId("");
            task.setFeaturesId("");
            //taskService.updateById(task);
            taskMapper.updateById(task);
            updateSchedule(versionId);
            run(new HashMap(){{
                put("memberId",memberId);
                put("versionId",versionId);
                put("type","removeVersionTask");
                put("data",task.getName());
            }});
        }
        return task;
    }
	
	/**
     * memberId
     * versionId
     * remark
     * type
     * content
     * @param map
     */
    public void run(Map map){
        TwProjectVersionLog pvl = new TwProjectVersionLog(); 
        pvl.setMemberId(MapUtils.getString(map,"memberId"));
        pvl.setSourceId(MapUtils.getString(map,"versionId"));
        pvl.setRemark(MapUtils.getString(map,"remark"));
        pvl.setVerType(MapUtils.getString(map,"type"));
        pvl.setContent(MapUtils.getString(map,"content"));
        pvl.setCreateTime(new Date());
        Map versionMap = getProjectVersionById(MapUtils.getString(map,"versionId"));
        pvl.setFeaturesId(MapUtils.getString(versionMap,"features_id"));
        String remark="",content="",icon = "";
        String type = MapUtils.getString(map,"type");
        if("create".equals(type)){
            icon = "plus";
            remark="创建了版本";
            content = MapUtils.getString(versionMap,"name");
        }else if("status".equals(type)){
            icon = "check-square";
            remark="更新了状态为"+getStatusTextAttr(MapUtils.getString(versionMap,"status"));
        }else if("publish".equals(type)){
            icon = "check-square";
            remark="完成版本时间为 "+MapUtils.getString(versionMap,"publish_time");
        }else if("name".equals(type)){
            icon = "edit";
            remark="更新了版本名";
            content = MapUtils.getString(versionMap,"name");
        }else if("content".equals(type)){
            icon = "file-text";
            remark="更新了备注";
            content = MapUtils.getString(versionMap,"description");
        }else if("clearContent".equals(type)){
            icon = "file-text";
            remark="清空了备注 ";
        }else if("setStartTime".equals(type)){
            icon = "calendar";
            remark="更新开始时间为 " + MapUtils.getString(versionMap,"start_time");
        }else if("clearStartTime".equals(type)){
            icon = "calendar";
            remark="清除了开始时间 ";
        }else if("setPlanPublishTime".equals(type)){
            icon = "calendar";
            remark="更新计划发布时间为 " + MapUtils.getString(versionMap,"plan_publish_time");
        }else if("clearPlanPublishTime".equals(type)){
            icon = "calendar";
            remark="清除了计划发布时间 ";
        }else if("delete".equals(type)){
            icon = "delete";
            remark="删除了版本 ";
        }else if("addVersionTask".equals(type)){
            List<String> list = (ArrayList)map.get("data");
            content = StringUtils.join(list,",");
            icon="link";
            remark = "添加了"+list.size()+"项发布内容";
        }else if("removeVersionTask".equals(type)){
            icon = "disconnect";
            remark="移除了发布内容";
            content = MapUtils.getString(map,"data");
        }else{
            icon = "plus";
            remark="创建了版本";
        }
        pvl.setIcon(icon);
        pvl.setRemark(remark);
        pvl.setContent(content);
        if(!StringUtils.isEmpty(MapUtils.getString(map,"remark"))){
            pvl.setRemark(remark);
        }
        if(!StringUtils.isEmpty(MapUtils.getString(map,"content"))){
            pvl.setContent(content);
        }
        projectVersionLogService.save(pvl);
    }
	@Override
	public Result<?> addVersionTask(Map<String, Object> mmap) throws Exception {
		String taskIdList = MapUtils.getString(mmap,"taskIdList");
        String versionId = MapUtils.getString(mmap,"versionId");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        
        Integer successTotal = 0;
        if(!StringUtils.isEmpty(taskIdList)){
            JSONArray jsonArray = JSON.parseArray(taskIdList);
            if(StringUtils.isNotEmpty(jsonArray)){
                List<String> taskListName = new ArrayList<>();
                for (Object obj : jsonArray) {
                    //Map taskMap = taskService.getTaskMapById(String.valueOf(obj));
                	Map taskMap = taskMapper.selectTaskById(String.valueOf(obj));
                    if(MapUtils.isEmpty(taskMap)){
                        return Result.error("该任务已被失效");
                    }
                    String versionIdTask = MapUtils.getString(taskMap,"version_id","0");
                    if(!"0".equals(versionIdTask) && StringUtils.isNotEmpty(versionIdTask)){
                        return Result.error("该任务已被关联");
                    }
                    Map versionMap = getProjectVersionById(versionId);
                    if(MapUtils.isEmpty(taskMap)){
                        return Result.error("该版本已被失效");
                    }
                    TwTask task = addVersionTask(taskMap,versionMap);
                    if(!ObjectUtils.isEmpty(task)){
                        taskListName.add(task.getName());
                        successTotal ++;
                    }
                }
                run(new HashMap(){{
                    put("memberId",loginUser.getUsername());
                    put("versionId",versionId);
                    put("type","addVersionTask");
                    put("data",taskListName);
                }});
            }
        }
        Map result = new HashMap();
        result.put("successTotal",successTotal);
        return Result.OK(result);
	}
	
	@Transactional
    public TwTask addVersionTask(Map taskMap, Map versionMap) throws Exception {
        String versionId = MapUtils.getString(versionMap,"id","0");
        TwTask task = BeanMapUtils.mapToBean(taskMap,TwTask.class);
        task.setVersionId(versionId);
        task.setFeaturesId(MapUtils.getString(versionMap,"features_id"));
        //taskService.updateById(task);
        taskMapper.updateById(task);
        updateSchedule(versionId);
        return task;
    }

}
