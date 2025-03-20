package org.jeecg.modules.estar.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;

import lombok.Data;

/**
 * @Author nbacheng
 * @Date 2022/4/2
 * @Description:
 * @Version 1.0
 */
@Data
public class ProcessUpdateVo {
	private String processInstanceId;
	
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date endTime;
	
	@Dict(dicCode = "bpm_status")
    private Integer processStatus;
}
