package org.jeecg.modules.flowable.entity;

import java.io.Serializable;
import java.util.Date;

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
 * @Description: onl_cgform_head
 * @Author: nbacheng
 * @Date:   2022-10-22
 * @Version: V1.0
 */
@Data
@TableName("onl_cgform_head")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="onl_cgform_head对象", description="onl_cgform_head")
public class FlowOnlCgformHead implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
	/**表名*/
	@Excel(name = "表名", width = 15)
    @ApiModelProperty(value = "表名")
    private String tableName;
	/**表类型: 0单表、1主表、2附表*/
	@Excel(name = "表类型: 0单表、1主表、2附表", width = 15)
    @ApiModelProperty(value = "表类型: 0单表、1主表、2附表")
    private Integer tableType;
	/**表版本*/
	@Excel(name = "表版本", width = 15)
    @ApiModelProperty(value = "表版本")
    private Integer tableVersion;
	/**表说明*/
	@Excel(name = "表说明", width = 15)
    @ApiModelProperty(value = "表说明")
    private String tableTxt;
	/**是否带checkbox*/
	@Excel(name = "是否带checkbox", width = 15)
    @ApiModelProperty(value = "是否带checkbox")
    private String isCheckbox;
	/**同步数据库状态*/
	@Excel(name = "同步数据库状态", width = 15)
    @ApiModelProperty(value = "同步数据库状态")
    private String isDbSynch;
	/**是否分页*/
	@Excel(name = "是否分页", width = 15)
    @ApiModelProperty(value = "是否分页")
    private String isPage;
	/**是否是树*/
	@Excel(name = "是否是树", width = 15)
    @ApiModelProperty(value = "是否是树")
    private String isTree;
	/**主键生成序列*/
	@Excel(name = "主键生成序列", width = 15)
    @ApiModelProperty(value = "主键生成序列")
    private String idSequence;
	/**主键类型*/
	@Excel(name = "主键类型", width = 15)
    @ApiModelProperty(value = "主键类型")
    private String idType;
	/**查询模式*/
	@Excel(name = "查询模式", width = 15)
    @ApiModelProperty(value = "查询模式")
    private String queryMode;
	/**映射关系 0一对多  1一对一*/
	@Excel(name = "映射关系 0一对多  1一对一", width = 15)
    @ApiModelProperty(value = "映射关系 0一对多  1一对一")
    private Integer relationType;
	/**子表*/
	@Excel(name = "子表", width = 15)
    @ApiModelProperty(value = "子表")
    private String subTableStr;
	/**附表排序序号*/
	@Excel(name = "附表排序序号", width = 15)
    @ApiModelProperty(value = "附表排序序号")
    private Integer tabOrderNum;
	/**树形表单父id*/
	@Excel(name = "树形表单父id", width = 15)
    @ApiModelProperty(value = "树形表单父id")
    private String treeParentIdField;
	/**树表主键字段*/
	@Excel(name = "树表主键字段", width = 15)
    @ApiModelProperty(value = "树表主键字段")
    private String treeIdField;
	/**树开表单列字段*/
	@Excel(name = "树开表单列字段", width = 15)
    @ApiModelProperty(value = "树开表单列字段")
    private String treeFieldname;
	/**表单分类*/
	@Excel(name = "表单分类", width = 15)
    @ApiModelProperty(value = "表单分类")
    private String formCategory;
	/**PC表单模板*/
	@Excel(name = "PC表单模板", width = 15)
    @ApiModelProperty(value = "PC表单模板")
    private String formTemplate;
	/**表单模板样式(移动端)*/
	@Excel(name = "表单模板样式(移动端)", width = 15)
    @ApiModelProperty(value = "表单模板样式(移动端)")
    private String formTemplateMobile;
	/**是否有横向滚动条*/
	@Excel(name = "是否有横向滚动条", width = 15)
    @ApiModelProperty(value = "是否有横向滚动条")
    private Integer scroll;
	/**复制版本号*/
	@Excel(name = "复制版本号", width = 15)
    @ApiModelProperty(value = "复制版本号")
    private Integer copyVersion;
	/**复制表类型1为复制表 0为原始表*/
	@Excel(name = "复制表类型1为复制表 0为原始表", width = 15)
    @ApiModelProperty(value = "复制表类型1为复制表 0为原始表")
    private Integer copyType;
	/**原始表ID*/
	@Excel(name = "原始表ID", width = 15)
    @ApiModelProperty(value = "原始表ID")
    private String physicId;
	/**扩展JSON*/
	@Excel(name = "扩展JSON", width = 15)
    @ApiModelProperty(value = "扩展JSON")
    private String extConfigJson;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**主题模板*/
	@Excel(name = "主题模板", width = 15)
    @ApiModelProperty(value = "主题模板")
    private String themeTemplate;
	/**是否用设计器表单*/
	@Excel(name = "是否用设计器表单", width = 15)
    @ApiModelProperty(value = "是否用设计器表单")
    private String isDesForm;
	/**设计器表单编码*/
	@Excel(name = "设计器表单编码", width = 15)
    @ApiModelProperty(value = "设计器表单编码")
    private String desFormCode;
	/**关联的应用ID*/
	@Excel(name = "关联的应用ID", width = 15)
    @ApiModelProperty(value = "关联的应用ID")
    private String lowAppId;
}
