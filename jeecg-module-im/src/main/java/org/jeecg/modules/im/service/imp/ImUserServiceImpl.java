package org.jeecg.modules.im.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.apithird.entity.SysUser;
import org.jeecg.modules.im.apithird.service.IImThirdService;
import org.jeecg.modules.im.domain.ImChatGroup;

import org.jeecg.modules.im.mapper.ImUserMapper;
import org.jeecg.modules.im.service.IImChatGroupUserService;
import org.jeecg.modules.im.service.IImUserFriendService;
import org.jeecg.modules.im.service.IImUserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.List;

/**
 * 服务实现类
 *
 * @author nbacheng
 * @since 2018-10-07
 */
@Service
@Qualifier(value = "imUserService")
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, SysUser> implements IImUserService {

    @Resource
    @Qualifier(value = "imUserFriendService")
    private IImUserFriendService imUserFriendService;

    @Resource
    @Qualifier(value = "imChatGroupUserService")
    private IImChatGroupUserService imChatGroupUserService;
    
    @Resource
    private IImThirdService iImThirdService;
    
    @Override
    public SysUser getByName(String username) {
    	return iImThirdService.getUserByName(username);
    }

    @Override
    public List<ImChatGroup> getChatGroups(String userName) {
        return baseMapper.getUserGroups(userName);
    }

    @Override
    public List<SysUser> getChatUserList(String chatId) {
        return baseMapper.getChatUserList(chatId);
    }
}
