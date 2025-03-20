package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.apithird.entity.SysUser;
import org.jeecg.modules.im.domain.ImUserFriend;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-12-31
 */
public interface IImUserFriendService extends IService<ImUserFriend> {

    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     *
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    List<SysUser> getUserFriends(String userName);
}
