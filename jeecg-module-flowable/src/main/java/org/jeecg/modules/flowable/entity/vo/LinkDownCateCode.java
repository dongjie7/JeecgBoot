package org.jeecg.modules.flowable.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-10-04 14:33
 * @description:
 **/

@Data
public class LinkDownCateCode {

    /**
     * id
     */
    private String id;

    /**
     * 分类名称
     */
    private String name;

    private List<LinkDownCateCode> children;
}
