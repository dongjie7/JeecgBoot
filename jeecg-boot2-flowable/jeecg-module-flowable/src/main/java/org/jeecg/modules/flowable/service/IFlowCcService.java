package org.jeecg.modules.flowable.service;

import org.jeecg.modules.flowable.domain.vo.FlowTaskVo;
import org.jeecg.modules.flowable.entity.FlowCc;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 流程抄送表
 * @Author: nbacheng
 * @Date:   2022-10-18
 * @Version: V1.0
 */
public interface IFlowCcService extends IService<FlowCc> {
	/**
     * 抄送
     * @param taskVo
     * @return
     */
    Boolean flowCc(FlowTaskVo taskVo);
    /**
     * 批量插入(包含限制条数，目前是100条)
     * @param deployId
     * @return
     */
    boolean insertBatch(List<FlowCc> flowCcList);

    public void updateStatus(@Param("id") String id);
}
