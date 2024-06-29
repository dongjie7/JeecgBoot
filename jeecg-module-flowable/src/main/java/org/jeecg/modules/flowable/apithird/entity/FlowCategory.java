package org.jeecg.modules.flowable.apithird.entity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public interface FlowCategory {
	/**
	 * 常量
	 */
	public  enum Category {
		oa,
		cw,
		ddxz,
		ddrs,
		zdyyw,
		online;
	}
	static final Map<String, String> flowcategory = ImmutableMap.<String, String>builder()
            .put(Category.oa.name(), "OA")
            .put(Category.cw.name(), "财务")
            .put(Category.ddxz.name(), "钉钉薪资")
            .put(Category.ddrs.name(), "钉钉人事")
            .put(Category.zdyyw.name(), "自定义业务")
            .put(Category.online.name(), "online表单")
            .build();
}
