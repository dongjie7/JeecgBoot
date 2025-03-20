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
 * @Description: 项目端节点表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_lock")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_lock对象", description="项目端节点表")
public class TwLock implements Serializable {
    private static final long serialVersionUID = 1L;

	/**IP+TYPE*/
	@Excel(name = "IP+TYPE", width = 15)
    @ApiModelProperty(value = "IP+TYPE")
    private Integer pid;
	/**次数*/
	@Excel(name = "次数", width = 15)
    @ApiModelProperty(value = "次数")
    private Integer pvalue;
	/**锁定截止时间*/
	@Excel(name = "锁定截止时间", width = 15)
    @ApiModelProperty(value = "锁定截止时间")
    private Integer expiretime;
}
