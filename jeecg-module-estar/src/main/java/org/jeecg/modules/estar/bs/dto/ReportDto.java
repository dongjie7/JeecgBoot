package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;

/**
 *
 * @author nbacheng
 * @date 2023-03-23
 */
@Data
public class ReportDto extends JeecgEntity implements Serializable {

    /** 报表名称 */
    private String reportName;

    /** 报表编码 */
    private String reportCode;

    /**数据集编码，以|分割*/
    private String setCodes;

    /** 分组 */
    private String reportGroup;

    /** 备注 */
    private String reportDesc;

    /** 数据集查询参数 */
    private String setParam;

    /** 报表json字符串 */
    private String jsonStr;

    /** 报表类型 */
    private String reportType;

    /** 数据总计 */
    private long total;

    /** 报表缩略图 */
    private String reportImage;

    /** 0--禁用 1--启用 */
    private String status;


    /** 报表作者 */
    private String reportAuthor;

    /** 下载次数 */
    private Long downloadCount;

}
