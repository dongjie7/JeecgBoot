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
@TableName("tw_project_node")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_project_node对象", description="项目端节点表")
public class TwProjectNode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;
	/**节点代码*/
	@Excel(name = "节点代码", width = 15)
    @ApiModelProperty(value = "节点代码")
    private String node;
	/**节点标题*/
	@Excel(name = "节点标题", width = 15)
    @ApiModelProperty(value = "节点标题")
    private String title;
	/**是否可设置为菜单*/
	@Excel(name = "是否可设置为菜单", width = 15)
    @ApiModelProperty(value = "是否可设置为菜单")
    private Integer isMenu;
	/**是否启动RBAC权限控制*/
	@Excel(name = "是否启动RBAC权限控制", width = 15)
    @ApiModelProperty(value = "是否启动RBAC权限控制")
    private Integer isAuth;
	/**是否启动登录控制*/
	@Excel(name = "是否启动登录控制", width = 15)
    @ApiModelProperty(value = "是否启动登录控制")
    private Integer isLogin;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
