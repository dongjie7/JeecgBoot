package org.jeecg.modules.system.estar;

import cn.hutool.core.bean.BeanUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.config.URLConstant;
import org.jeecg.modules.estar.oa.vo.SysMessageVo;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.jeecg.modules.estar.config.URLConstant.URL_GET_TOKKEN;

/**
 * @Description: Estar接口实现表
 * @Author: nbacheng
 * @Date:   2022-06-06
 * @Version: V1.0
 */

@Slf4j
@Service
public class EstarThirdServiceImple implements IEstarThirdService {

	private static final Logger bizLogger = LoggerFactory.getLogger(EstarThirdServiceImple.class);

	@Autowired
    ISysBaseAPI sysBaseAPI;
	
	@Autowired
	ThirdAppConfig thirdAppConfig;
	
	@Autowired
	private ISysMessageService sysMessageService;
	
	//获取access_token
	@Override
	public String getToken() throws RuntimeException {
		try {
        	String appKey = thirdAppConfig.getDingtalk().getClientId();
    		String appSecret = thirdAppConfig.getDingtalk().getClientSecret();
            DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();

            request.setAppkey(appKey);
            request.setAppsecret(appSecret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            return accessToken;
        } catch (ApiException e) {
            bizLogger.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
	}

	//发送消息
	@Override
	public void sendMessageToOriginator(String processInstanceId) throws RuntimeException {
		try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_GET);
            OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
            request.setProcessInstanceId(processInstanceId);
            OapiProcessinstanceGetResponse response = client.execute(request, getToken());
            String recieverUserId = response.getProcessInstance().getOriginatorUserid();

            client = new DefaultDingTalkClient(URLConstant.MESSAGE_ASYNCSEND);

            OapiMessageCorpconversationAsyncsendV2Request messageRequest = new OapiMessageCorpconversationAsyncsendV2Request();
            messageRequest.setUseridList(recieverUserId);
            String agentId = thirdAppConfig.getDingtalk().getAgentId();
            messageRequest.setAgentId(Long.parseLong(agentId));
            messageRequest.setToAllUser(false);

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("text");
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent("出差申请通过了，快去订机票吧");
            messageRequest.setMsg(msg);

            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(messageRequest,getToken());
        } catch (ApiException e) {
            bizLogger.error("send message failed", e);
            throw new RuntimeException();
        }
	}

	@Override
	public SysUser getLoginUser() {
		LoginUser sysUser = null;
        SysUser copyProperties = null;
        try {
            sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            copyProperties = BeanUtil.copyProperties(sysUser, SysUser.class);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return copyProperties;
	}	
	
	@Override
    public SysUser getUserByUsername(String username) {
        LoginUser userByName = sysBaseAPI.getUserByName(username);
        return userByName==null?null:BeanUtil.copyProperties(userByName, SysUser.class);
    }

	@Override
	public String getUserNameByToken(String token) {
		return JwtUtil.getUsername(token);
	}

	@Override
	public String getLoginUserName() {
		LoginUser sysUser = null;
        try {
            sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return sysUser.getUsername();
	}

	@Override
	public String saveSysMessage(SysMessageVo sysMessageVo) {
		SysMessage sysMessage = new SysMessage();
		sysMessage.setEsContent(sysMessageVo.getEsContent());
		sysMessage.setEsReceiver(sysMessageVo.getEsReceiver());
		sysMessage.setEsSendNum(sysMessageVo.getEsSendNum());
		sysMessage.setEsSendStatus(sysMessageVo.getEsSendStatus());
		sysMessage.setEsTitle(sysMessageVo.getEsTitle());
		sysMessage.setEsSendTime(sysMessageVo.getEsSendTime());
		sysMessage.setEsType(sysMessageVo.getEsType());
		
		sysMessageService.save(sysMessage);
	    log.info("sysMessage getid="+sysMessage.getId());
	    return sysMessage.getId();
	}

	@Override
	public String getMsgSendStatus(String msgId) {
		SysMessage sysMessage = sysMessageService.getById(msgId);
		if(sysMessage !=null) {
			return sysMessage.getEsSendStatus();
		}
		return null;
	}

	@Override
	public void removeSysMessage(String msgId) {
		sysMessageService.removeById(msgId);
	}

	@Override
	public void updateSysMessage(SysMessageVo sysMessageVo) {
		SysMessage sysMessage = new SysMessage();
		sysMessage.setId(sysMessageVo.getId());
		sysMessage.setEsContent(sysMessageVo.getEsContent());
		sysMessage.setEsReceiver(sysMessageVo.getEsReceiver());
		sysMessage.setEsSendNum(sysMessageVo.getEsSendNum());
		sysMessage.setEsSendStatus(sysMessageVo.getEsSendStatus());
		sysMessage.setEsTitle(sysMessageVo.getEsTitle());
		sysMessage.setEsSendTime(sysMessageVo.getEsSendTime());
		sysMessage.setEsType(sysMessageVo.getEsType());
		
		sysMessageService.updateById(sysMessage);
		
	}

	@Override
	public boolean getMsgRecord(String msgId) {
		SysMessage sysMessage = sysMessageService.getById(msgId);
		if (sysMessage != null) {
			return true;
		}
		return false;
	}

}
