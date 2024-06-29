package org.jeecg.modules.estar.apithird.service;

import org.jeecg.modules.estar.oa.vo.SysMessageVo;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 钉钉接口类
 * @Author: nbacheng
 * @Date:   2022-06-06
 * @Version: V1.0
 */
public interface IEstarThirdService {
	/**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     */
    public SysUser getLoginUser();
    public String getLoginUserName();
	String getToken();
	void sendMessageToOriginator(String processInstanceId);
	/**
     * 根据用户username查询用户信息
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);
    
    String getUserNameByToken(String token);
    /**
     * 写入系统定时发送消息表里
     *
     * @return 
     */
    String saveSysMessage(SysMessageVo sysMessageVo);
    String getMsgSendStatus(String msgId);  //返回发送消息状态
    boolean getMsgRecord(String msgId);//返回消息记录数
    void updateSysMessage(SysMessageVo sysMessageVo);
    void removeSysMessage(String msgId);
    
}
