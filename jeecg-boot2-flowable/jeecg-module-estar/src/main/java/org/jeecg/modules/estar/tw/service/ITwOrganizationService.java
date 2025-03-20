package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwOrganization;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目组织表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
public interface ITwOrganizationService extends IService<TwOrganization> {

	void saveAddCcount(TwOrganization twOrganization);

	boolean delRemoveAccount(String id);

}
