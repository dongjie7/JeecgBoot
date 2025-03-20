package org.jeecg.modules.estar.oa.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.entity.OaSchedule;
import org.jeecg.modules.estar.oa.mapper.OaScheduleMapper;
import org.jeecg.modules.estar.oa.service.IOaScheduleService;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.oa.vo.ScheduleTime;
import org.jeecg.modules.estar.oa.vo.SysMessageVo;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: OA日程表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Service
public class OaScheduleServiceImpl extends ServiceImpl<OaScheduleMapper, OaSchedule> implements IOaScheduleService {

	@Resource
    private IEstarThirdService iEstarThirdService;
	//@Autowired
	//private IOaScheduleService oaScheduleService;
	@Autowired
	OaScheduleMapper scheduleMapper;
	
	@Override
	public List<OaSchedule> getList(ScheduleTime scheduleTime) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		QueryWrapper<OaSchedule> queryWrapper = new QueryWrapper<>();
        Date startTimes = DateUtil.stringToDates(scheduleTime.getStartTime());
        Date endTimes = DateUtil.stringToDates(scheduleTime.getEndTime());
        List<String>  idlist= Arrays.asList(scheduleTime.getIds().split(","));
        queryWrapper.lambda().eq(OaSchedule::getCreateBy,userId)
                .in(OaSchedule::getCalId, idlist)
                .ge(OaSchedule::getStartTime,startTimes)
                .le(OaSchedule::getEndTime,endTimes)
                .orderByAsc(OaSchedule::getCreateTime);
        return this.list(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveAndAddSchedule(OaSchedule oaSchedule) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        if(StringUtils.isEmpty(oaSchedule.getTaker())){
        	oaSchedule.setTaker(userId);
        }
        if(StringUtils.isEmpty(oaSchedule.getOwner())){
        	oaSchedule.setOwner(userId);
        }
		//oaScheduleService.save(oaSchedule);
		scheduleMapper.insert(oaSchedule);
		if (oaSchedule.getRemind() != 0) {//要提醒目前只支持消息提醒
			SysMessageVo sysMessageVo= new SysMessageVo();
			sysMessageVo.setEsContent(oaSchedule.getContent());
			sysMessageVo.setEsTitle(oaSchedule.getTitle());
			sysMessageVo.setEsReceiver(oaSchedule.getTaker());
			sysMessageVo.setEsType(oaSchedule.getRemindType());
			String reminddate = DateUtil.getPreTime(DateUtil.getDateString(oaSchedule.getStartTime(), "yyyy-MM-dd HH:mm:ss") ,String.valueOf(0-oaSchedule.getRemind()));
			sysMessageVo.setEsSendTime(DateUtil.stringToDate(reminddate));
			sysMessageVo.setEsSendStatus("0"); //SendMsgStatusEnum.WAIT.getCode()
			sysMessageVo.setEsSendNum(0); 	
			String sysMsgId = iEstarThirdService.saveSysMessage(sysMessageVo);
			oaSchedule.setMsgId(sysMsgId);
			//oaScheduleService.updateById(oaSchedule);
			scheduleMapper.updateById(oaSchedule);
		}
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateSchedule(OaSchedule oaSchedule) {
		String msgId = oaSchedule.getMsgId();
		String sendStatus = iEstarThirdService.getMsgSendStatus(msgId);
		if (oaSchedule.getRemind() != 0) {//有消息提醒
			if (iEstarThirdService.getMsgRecord(msgId)) {//原来有消息记录
				if(StringUtils.equals(sendStatus,"0")) {//还没发送消息,可以更新
					updateScheduleById(oaSchedule);
					SysMessageVo sysMessageVo= new SysMessageVo();
					sysMessageVo.setId(msgId);
					sysMessageVo.setEsContent(oaSchedule.getContent());
					sysMessageVo.setEsTitle(oaSchedule.getTitle());
					sysMessageVo.setEsReceiver(oaSchedule.getTaker());
					sysMessageVo.setEsType(oaSchedule.getRemindType());
					String reminddate = DateUtil.getPreTime(DateUtil.getDateString(oaSchedule.getStartTime(), "yyyy-MM-dd HH:mm:ss") ,String.valueOf(0-oaSchedule.getRemind()));
					sysMessageVo.setEsSendTime(DateUtil.stringToDate(reminddate));
					iEstarThirdService.updateSysMessage(sysMessageVo);	
					return true;
				}
				else {//已经发送消息了就不再更新了
					return false;
				}
			 }else {//原来没有就插入
				updateScheduleById(oaSchedule);
				SysMessageVo sysMessageVo= new SysMessageVo();
				sysMessageVo.setEsContent(oaSchedule.getContent());
				sysMessageVo.setEsTitle(oaSchedule.getTitle());
				sysMessageVo.setEsReceiver(oaSchedule.getTaker());
				sysMessageVo.setEsType(oaSchedule.getRemindType());
				String reminddate = DateUtil.getPreTime(DateUtil.getDateString(oaSchedule.getStartTime(), "yyyy-MM-dd HH:mm:ss") ,String.valueOf(0-oaSchedule.getRemind()));
				sysMessageVo.setEsSendTime(DateUtil.stringToDate(reminddate));
				sysMessageVo.setEsSendStatus("0"); //SendMsgStatusEnum.WAIT.getCode()
				sysMessageVo.setEsSendNum(0); 	
				String sysMsgId = iEstarThirdService.saveSysMessage(sysMessageVo);
				oaSchedule.setMsgId(sysMsgId);
				//oaScheduleService.updateById(oaSchedule);
				scheduleMapper.updateById(oaSchedule);
				return true;
			 }
		}
		else {
			updateScheduleById(oaSchedule);
			return true;
		}
	}
	
	void updateScheduleById(OaSchedule oaSchedule) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        if(StringUtils.isEmpty(oaSchedule.getTaker())){
        	oaSchedule.setTaker(userId);
        }
        if(StringUtils.isEmpty(oaSchedule.getOwner())){
        	oaSchedule.setOwner(userId);
        }
		//oaScheduleService.updateById(oaSchedule);
        scheduleMapper.updateById(oaSchedule);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean removeSchedule(String id) {
		//OaSchedule oaSchedule = oaScheduleService.getById(id);
		OaSchedule oaSchedule = scheduleMapper.selectById(id);
		String msgId = oaSchedule.getMsgId();
		String sendStatus = iEstarThirdService.getMsgSendStatus(msgId);
		if (oaSchedule.getRemind() != 0 ) {//有消息提醒
			if(StringUtils.equals(sendStatus,"0")) {
				//oaScheduleService.removeById(id);
				scheduleMapper.deleteById(id);
				iEstarThirdService.removeSysMessage(msgId);
				return true;
			}
			else {
				return false;
			}	
		}
		else {
			//oaScheduleService.removeById(id);
			scheduleMapper.deleteById(id);
			return true;
		}
		
	}

}
