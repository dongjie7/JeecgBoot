package org.jeecg.modules.estar.bs.constant;


import java.util.ArrayList;
import java.util.List;

/**
 * Exp常量
 * @author nbacheng
 * @since 2023-03-16
 */
public class ExpConstant {

    public static final String[] FUNCTION = new String[]{"=SUM(", "=AVERAGE(", "=MAX(", "=MIN(", "=IF(", "=AND(", "=OR(", "=CONCAT("};

    public static List<Integer> getExpFunction(String e) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < FUNCTION.length; i++) {
            if(e.contains(FUNCTION[i])){
                counts.add(i);
            }
        }

        return counts;
    }

}
