package org.jeecg.modules.erp.utils;

public class Assert extends cn.hutool.core.lang.Assert {
	public static void greaterThanZero(Number number) {
		notNull(number);
		isTrue(number.doubleValue() > 0.0D);
	}

	public static void greaterThanOrEqualToZero(Number number) {
		notNull(number);
		isTrue(number.doubleValue() >= 0.0D);
	}
}
