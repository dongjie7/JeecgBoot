package org.jeecg.modules.flowable.mapper;


import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.flowable.entity.SysCustomForm;
import org.jeecg.modules.flowable.entity.SysDeployForm;
import org.jeecg.modules.flowable.entity.SysForm;

/**
 * @Description: 流程实例关联表单
 * @Author: nbacheng
 * @Date:   2022-04-11
 * @Version: V1.0
 */
public interface SysDeployFormMapper extends BaseMapper<SysDeployForm> {

	/**
     * 查询关联表内容
     * @param formId
     * @return
     */
	SysDeployForm selectSysDeployFormByFormId(String formId);
	/**
     * 查询流程挂着的表单
     * @param deployId
     * @return
     */
    SysForm selectSysDeployFormByDeployId(String deployId);
    
    /**
     * 查询流程挂着的自定义表单
     * @param deployId
     * @return
     */
    SysCustomForm selectSysCustomFormByDeployId(String deployId);
    /**
     * 查询当前流程任务节点挂接的表单
     * @param formId,deployId,nodeKey
     * @return
     */
	SysForm selectCurSysDeployForm(@Param("formId") String formId, @Param("deployId") String deployId, @Param("nodeKey") String nodeKey);

}
