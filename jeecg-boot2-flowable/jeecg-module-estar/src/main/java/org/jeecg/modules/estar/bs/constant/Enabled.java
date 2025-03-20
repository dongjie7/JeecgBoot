package org.jeecg.modules.estar.bs.constant;

import org.jeecg.modules.estar.bs.annotation.EnumInterface;

/**
 * 是否标识即0,1
 * @author nbacheng
 * @since 2023-03-23
 */
public enum Enabled implements EnumInterface<Integer> {

    YES(1),NO(0);

    private Integer value;

    Enabled(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


    @Override
    public Boolean exist(Integer value) {
        return this.getValue().equals(value);
    }}
