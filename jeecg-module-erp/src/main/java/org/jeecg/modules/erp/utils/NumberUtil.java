package org.jeecg.modules.erp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
	public static boolean isNumberPrecision(Number value, int precision) {
		if (value == null) {
			return false;
		} else if (precision < 0) {
			return false;
		} else {
			String str = BigDecimal.valueOf(value.doubleValue()).toPlainString();
			if (!str.contains(".")) {
				return true;
			} else {
				while ("0".equals(str.substring(str.length() - 1))) {
					str = str.substring(0, str.length() - 1);
				}

				if (".".equals(str.substring(str.length() - 1))) {
					return true;
				} else {
					return str.substring(str.indexOf(".")).length() - 1 <= precision;
				}
			}
		}
	}

	public static BigDecimal add(Number... numbers) {
		BigDecimal result = new BigDecimal(0);
		Number[] var2 = numbers;
		int var3 = numbers.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			Number number = var2[var4];
			result = result.add(getNumber(number));
		}

		return result;
	}

	public static BigDecimal sub(Number n1, Number... numbers) {
		Assert.notNull(n1);
		Assert.notEmpty(numbers);
		BigDecimal result = getNumber(n1);
		Number[] var3 = numbers;
		int var4 = numbers.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Number number = var3[var5];
			BigDecimal tmp = getNumber(number);
			result = result.subtract(tmp);
		}

		return result;
	}

	public static BigDecimal mul(Number... numbers) {
		BigDecimal result = new BigDecimal(1);
		Number[] var2 = numbers;
		int var3 = numbers.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			Number number = var2[var4];
			result = result.multiply(getNumber(number));
		}

		return result;
	}

	public static BigDecimal div(Number n1, Number... numbers) throws Exception {
		return div(RoundingMode.HALF_UP, n1, numbers);
	}

	public static BigDecimal div(RoundingMode mode, Number n1, Number... numbers) throws Exception {
		Assert.notNull(n1);
		Assert.notEmpty(numbers);
		BigDecimal result = getNumber(n1);
		Number[] var4 = numbers;
		int var5 = numbers.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			Number number = var4[var6];
			BigDecimal tmp = getNumber(number);
			if (equal(tmp, 0)) {
				throw new Exception("除数不能等于0");
			}

			result = result.divide(tmp, 16, mode);
		}

		return result;
	}

	public static boolean gt(Number n1, Number n2) {
		return getNumber(n1).compareTo(getNumber(n2)) > 0;
	}

	public static boolean lt(Number n1, Number n2) {
		return getNumber(n1).compareTo(getNumber(n2)) < 0;
	}

	public static boolean ge(Number n1, Number n2) {
		return getNumber(n1).compareTo(getNumber(n2)) >= 0;
	}

	public static boolean le(Number n1, Number n2) {
		return getNumber(n1).compareTo(getNumber(n2)) <= 0;
	}

	public static boolean equal(Number n1, Number n2) {
		return getNumber(n1).compareTo(getNumber(n2)) == 0;
	}

	public static BigDecimal calcTaxPrice(Number unTaxPrice, Number taxRate) throws Exception {
		return mul(unTaxPrice, add(div(taxRate, 100), BigDecimal.ONE));
	}

	public static BigDecimal calcUnTaxPrice(Number taxPrice, Number taxRate) throws Exception {
		return div(taxPrice, add(div(taxRate, 100), BigDecimal.ONE));
	}

	public static BigDecimal getNumber(Number number, int precision) {
		precision = Math.max(0, precision);
		BigDecimal result = getNumber(number).setScale(precision, 4);
		return result;
	}

	private static BigDecimal getNumber(Number number) {
		Assert.notNull(number);
		return number instanceof BigDecimal ? (BigDecimal) number : BigDecimal.valueOf(number.doubleValue());
	}
}
