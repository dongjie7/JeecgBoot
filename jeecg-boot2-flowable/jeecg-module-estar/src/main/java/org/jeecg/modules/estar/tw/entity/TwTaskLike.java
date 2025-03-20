package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 任务点赞表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_task_like")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_task_like对象", description="任务点赞表")
public class TwTaskLike implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15)
    @ApiModelProperty(value = "任务ID")
    private String taskId;
	/**成员id*/
	@Excel(name = "成员id", width = 15)
    @ApiModelProperty(value = "成员id")
    private String memberId;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
}
