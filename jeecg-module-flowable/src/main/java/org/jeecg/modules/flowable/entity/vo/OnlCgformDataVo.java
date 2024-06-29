package org.jeecg.modules.flowable.entity.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;

import lombok.Data;

/**
 * @author: nbacheng
 * @create: 2022-10-31
 * @description: online表单保存数据格式
 **/
@Data
public class OnlCgformDataVo implements Serializable {
	String formId;
	OnlCgformHead head;
	List<OnlCgformField> fieldList;
	List<Map<String, Object>> dataList;
}
