package org.jeecg.modules.erp.enums;

import java.io.Serializable;

/**
 * @Description: Enum基类
 * @Author: nbacheng
 * @Date:   2022-11-25
 * @Version: V1.0
 */
public interface BaseEnum<T extends Serializable> extends Serializable {

	  /**
	   * 获取枚举值
	   *
	   * @return
	   */
	  T getCode();

	  /**
	   * 获取描述
	   *
	   * @return
	   */
	  String getDesc();
	}
