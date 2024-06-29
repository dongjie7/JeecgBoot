package org.jeecg.modules.estar.nd.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.constant.RegexConstant;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
@Component
public class UserDealComp {
	@Resource
    private IEstarThirdService iEstarThirdService;


    /**
     * 检测用户名是否存在
     *
     * @param sysUser
     */
    public Boolean isUserNameExit(SysUser sysUser) {
        String userName = sysUser.getUsername();
        SysUser getSysUser = iEstarThirdService.getUserByUsername(userName);
        if (getSysUser != null ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测手机号是否存在
     *
     * @param sysUser
     * @return
     */
   /* public Boolean isPhoneExit(SysUser sysUser) {

    	String phone = sysUser.getPhone();
        SysUser getSysUser = iEstarThirdService.getUserByPhone(phone);
        if (getSysUser != null ) {
            return true;
        } else {
            return false;
        }

    }*/

    public Boolean isPhoneFormatRight(String phone){
        boolean isRight = Pattern.matches(RegexConstant.PASSWORD_REGEX, phone);
        return isRight;
    }
}
