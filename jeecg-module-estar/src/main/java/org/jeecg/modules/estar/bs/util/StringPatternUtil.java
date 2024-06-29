package org.jeecg.modules.estar.bs.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class StringPatternUtil {
    private static final String STRING_STYLE_RED = "<span class='infoDanger'>%s</span>";
    private static final String UNDERLINE = "_";

    public StringPatternUtil() {
    }

    public static boolean StringMatch(String sourceStr, String pattern) {
        boolean result = false;

        try {
            if (StringUtils.isEmpty(sourceStr) || StringUtils.isEmpty(pattern)) {
                return result;
            }

            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            if (m.find()) {
                result = true;
            }
        } catch (Exception var5) {
            result = false;
        }

        return result;
    }

    public static boolean StringMatchIgnoreCase(String sourceStr, String pattern) {
        boolean result = false;

        try {
            if (StringUtils.isEmpty(sourceStr) || StringUtils.isEmpty(pattern)) {
                return result;
            }

            sourceStr = sourceStr.toLowerCase();
            pattern = pattern.toLowerCase();
            result = StringMatch(sourceStr, pattern);
        } catch (Exception var4) {
            result = false;
        }

        return result;
    }

    public static String StringFind(String sourceStr, String pattern) {
        String result = "";

        try {
            if (StringUtils.isEmpty(sourceStr) || StringUtils.isEmpty(pattern)) {
                return result;
            }

            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            if (m.find()) {
                result = m.group(0);
            }
        } catch (Exception var5) {
            result = "";
        }

        return result;
    }

    public static String replace(String sourceStr, String pattern, String replaceStr) {
        String result = "";

        try {
            if (StringUtils.isEmpty(sourceStr) || StringUtils.isEmpty(pattern)) {
                return result;
            }

            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            result = m.replaceAll(replaceStr);
        } catch (Exception var6) {
            result = "";
        }

        return result;
    }
}
