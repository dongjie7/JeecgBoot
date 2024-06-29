package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: nd_storage
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Data
@TableName("nd_storage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_storage对象", description="nd_storage")
public class NdStorage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**修改用户id*/
    @ApiModelProperty(value = "修改用户id")
    private String updateBy;
	/**占用存储大小*/
	@Excel(name = "占用存储大小", width = 15)
    @ApiModelProperty(value = "占用存储大小")
    private Long storagesize;
	/**总存储大小*/
	@Excel(name = "总存储大小", width = 15)
    @ApiModelProperty(value = "总存储大小")
    private Long totalstoragesize;
	/**userid*/
	@Excel(name = "userid", width = 15)
    @ApiModelProperty(value = "userid")
    private String userid;
	
	public NdStorage() {

    }

    public NdStorage(String userId) {
        this.userid = userId;
    }
}
