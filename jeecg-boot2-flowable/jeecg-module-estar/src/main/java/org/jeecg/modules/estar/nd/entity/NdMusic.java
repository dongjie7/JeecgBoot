package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 网盘音乐表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Data
@TableName("nd_music")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_music对象", description="网盘音乐表")
public class NdMusic implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**专辑*/
	@Excel(name = "专辑", width = 15)
    @ApiModelProperty(value = "专辑")
    private String album;
	/**专辑艺术家*/
	@Excel(name = "专辑艺术家", width = 15)
    @ApiModelProperty(value = "专辑艺术家")
    private String albumartist;
	/**专辑图片*/
	@Excel(name = "专辑图片", width = 15)
    private transient String albumimageString;

	@Column(columnDefinition = "mediumblob")
    private String albumimage;
	/**艺术家*/
	@Excel(name = "艺术家", width = 15)
    @ApiModelProperty(value = "艺术家")
    private String artist;
	/**评论*/
	@Excel(name = "评论", width = 15)
    @ApiModelProperty(value = "评论")
    private String comment;
	/**创作者*/
	@Excel(name = "创作者", width = 15)
    @ApiModelProperty(value = "创作者")
    private String composer;
	/**版权*/
	@Excel(name = "版权", width = 15)
    @ApiModelProperty(value = "版权")
    private String copyright;
	/**编码器*/
	@Excel(name = "编码器", width = 15)
    @ApiModelProperty(value = "编码器")
    private String encoder;
	/**文件ID*/
	@Excel(name = "文件ID", width = 15)
    @ApiModelProperty(value = "文件ID")
    private String fileid;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String genre;
	/**歌词*/
	@Excel(name = "歌词", width = 15)
    @ApiModelProperty(value = "歌词")
    private String lyrics;
	/**原创艺术家*/
	@Excel(name = "原创艺术家", width = 15)
    @ApiModelProperty(value = "原创艺术家")
    private String originalartist;
	/**公众*/
	@Excel(name = "公众", width = 15)
    @ApiModelProperty(value = "公众")
    private String publicer;
	/**题目*/
	@Excel(name = "题目", width = 15)
    @ApiModelProperty(value = "题目")
    private String title;
	/**声道*/
	@Excel(name = "声道", width = 15)
    @ApiModelProperty(value = "声道")
    private String track;
	/**声道长度*/
	@Excel(name = "声道长度", width = 15)
    @ApiModelProperty(value = "声道长度")
    private Double tracklength;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String url;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private String year;
}
