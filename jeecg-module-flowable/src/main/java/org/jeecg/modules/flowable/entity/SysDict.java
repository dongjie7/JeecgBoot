package org.jeecg.modules.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:wangYuZhou
 * @Date:2021/4/16 0016 10:24
 * @Content:
 */
@Data
@TableName("sys_dict")
public class SysDict implements Serializable {

    /**
     * id 唯一标识
     * */
    @TableField("id")
    private String id;

    /**
     * 字典名称
     * */
    @TableField("dict_name")
    private String dictName;


    /**
     * 字典编码
     * */
    @TableField("dict_code")
    private String dictCode;


    /**
     * 字典描述
     * */
    @TableField("description")
    private String description;

}
