package org.jeecg.modules.flowable.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-09-30 10:36
 * @description: 分类字典返回类使用-带子集children
 **/

@Data
public class SysCateDictCode {

    /**
     * id
     */
    private String id;

    /**
     * 分类名称
     */
    private String label;

    private List<SysCateDictCode> children;
}
