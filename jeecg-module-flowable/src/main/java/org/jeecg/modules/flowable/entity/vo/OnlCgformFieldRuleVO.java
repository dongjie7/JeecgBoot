package org.jeecg.modules.flowable.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-09-19 16:09
 * @description: 每个字段的校验规则返回类信息
 **/

@Data
public class OnlCgformFieldRuleVO implements Serializable {


    private Boolean required;

    private String validator;

    private String trigger;
}
