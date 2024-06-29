
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

import org.jeecg.common.system.base.entity.JeecgEntity;


/**
*
* @description 数据源 dto
* @author nbacheng
* @date 2023-03-16 
**/
@Data
public class DataSourceDto extends JeecgEntity implements Serializable {
    /** 数据源编码 */
     private String code;

    /** 数据源名称 */
     private String name;

    /** 数据源描述 */
     private String remark;

    /** 数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单 */
     private String type;

    /** 数据源连接配置json：关系库{ jdbcUrl:'', username:'', password:'','driverName':''}ES-sql{ apiUrl:'http://127.0.0.1:9092/_xpack/sql?format=json','method':'POST','body':'{"query":"select 1"}' }  接口{ apiUrl:'http://ip:port/url', method:'' } javaBean{ beanNamw:'xxx' } */
     private String config;
     

    /**************************************************************/
    /**关系型数据库jdbcUrl */
    private String jdbcUrl;

    /** 关系型数据库用户名 */
    private String username;

    /** 关系型数据库密码 */
    private String password;

    /** 关系型数据库驱动类 */
    private String driverName;

    /** 关系型数据库sql */
    private String sql;

    /** http requestUrl */
    private String apiUrl;

    /** http method */
    private String method;

    /** http header */
    private String header;

    /** http 请求体 */
    private String body;

    /** 动态查询sql或者接口中的请求体 */
    private String dynSentence;

    /** 传入的自定义参数，解决url中存在的动态参数*/
    private Map<String, Object> contextData;

}
