package org.jeecg.modules.system.flow;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.flowable.apithird.entity.FlowCategory;
import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.apithird.entity.SysRole;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.impl.SysRoleServiceImpl;
import org.jeecg.modules.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * flowable模块必需实现类
 *@author nbacheng
 *@date 2022/05/06
 *@version 1.0
 */
@Service
public class FlowThirdServiceImpl implements IFlowThirdService {
	
	@Value("${flowable.message-base-url}")
	private String msgBaseUrl;
	
    @Autowired
    ISysBaseAPI sysBaseAPI;
    @Autowired
    SysUserServiceImpl sysUserService;
    @Autowired
    SysRoleServiceImpl sysRoleService;
    @Autowired
	private ISysUserRoleService sysUserRoleService;
    
    
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
    public List<SysUser> getAllUser() {
        List<org.jeecg.modules.system.entity.SysUser> list = sysUserService.list();
        List<SysUser> userList = list.stream().map(o -> BeanUtil.copyProperties(o, SysUser.class)).collect(Collectors.toList());
        return userList;
    }

    @Override
    public List<SysUser> getUsersByRoleId(String roleId) {
        Page<org.jeecg.modules.system.entity.SysUser> page = new Page<>(1,Integer.MAX_VALUE);
        IPage<org.jeecg.modules.system.entity.SysUser> userByRoleId = sysUserService.getUserByRoleId(page, roleId, null);
        List<org.jeecg.modules.system.entity.SysUser> records = userByRoleId.getRecords();
        List<SysUser> userList = records.stream().map(o -> BeanUtil.copyProperties(o, SysUser.class)).collect(Collectors.toList());
        return userList;
    }


    @Override
    public SysUser getUserByUsername(String username) {
        LoginUser userByName = sysBaseAPI.getUserByName(username);
        return userByName==null?null:BeanUtil.copyProperties(userByName, SysUser.class);
    }

    @Override
    public List<SysRole> getAllRole() {
        List<org.jeecg.modules.system.entity.SysRole> list = sysRoleService.list();
        List<SysRole> roleList = list.stream().map(o -> BeanUtil.copyProperties(o, SysRole.class)).collect(Collectors.toList());
        return roleList;
    }
    
    @Override
    public List<String> getUserRole(String userid) {
    	List<String> list = new ArrayList<String>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userid));
        if (userRole == null || userRole.size() <= 0) {
            return null;
        } else {
            for (SysUserRole sysUserRole : userRole) {
                list.add(sysUserRole.getRoleId());
            }
        }
        return list;
    }
	@Override
    public List<SysCategory> getAllCategory() {
        // todo 获取流程分类信息，此处为例子
		Set keySet = FlowCategory.flowcategory.keySet();   
		Iterator itfc = keySet.iterator();   
		ArrayList<SysCategory> sysCategories = new  ArrayList<SysCategory>();
        while (itfc.hasNext()) {   
            String key = (String) itfc.next();   
            // 有了键 可以通过map集合的get方法获取其对应的值。   
            String value = (String) FlowCategory.flowcategory.get(key);   
            SysCategory category = new SysCategory();
            category.setId(key);
            category.setName(value);
            sysCategories.add(category);
        }   
        return sysCategories;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        List<String> departNamesByUsername = sysBaseAPI.getDepartNamesByUsername(username);
        return departNamesByUsername;
    }

	@Override
	public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent,
			String setMsgCategory) {
		// TODO Auto-generated method stub
		MessageDTO messagedto = new MessageDTO();
		messagedto.setContent(msgContent);
		messagedto.setCategory(setMsgCategory);
		messagedto.setFromUser(fromUser);
		messagedto.setTitle(title);
		messagedto.setToUser(toUser);
		sysBaseAPI.sendSysAnnouncement(messagedto);
	}

	@Override
	public String getBaseUrl() {
		// TODO Auto-generated method stub
		return msgBaseUrl;
	}

	@Override
	public List<String> getDeptHeadByDepId(String deptId) {
		return sysBaseAPI.getDeptHeadByDepId(deptId);
	}

	@Override
	public List<String> getDepartIdsByUsername(String username) {
		return sysBaseAPI.getDepartIdsByUsername(username);
	}

}
