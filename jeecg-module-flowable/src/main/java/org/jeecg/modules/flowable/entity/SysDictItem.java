package org.jeecg.modules.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:wangYuZhou
 * @Date:2021/4/16 0016 10:25
 * @Content:
 */
@Data
@TableName("sys_dict_item")
public class SysDictItem implements Serializable {
    /**
     * id
     * */
    @TableField("id")
    private String  id;


    /**
     *
     * dict_id字典id
     * */
    @TableField("dict_id")
    private String dictId;


    /**
     *item_text   字典项文本
     * */
    @TableField("item_text")
    private String itemText;


    /**
     * item_value 字典项值
     * */
    @TableField("item_value")
    private String itemValue;


    /**
     *
     * description 描述
     * */
    @TableField("description")
    private String description;


    /**
     *
     * sort_order 排序
     * */
    @TableField("sort_order")
    private Integer sortOrder;


    /**
     * status  状态    0不启用     1启用
     * */
    @TableField("status")
    private Integer status;
}
