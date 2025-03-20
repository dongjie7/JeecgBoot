package org.jeecg.modules.estar.bs.enums;

/**
 * 报表类型枚举
 * @author nbacheng
 * @since 2023-03-23
 */
public enum ReportTypeEnum {

    /**report_screen*/
    report_screen("report_screen", "大屏报表"),
    /**report_excel*/
    report_excel("report_excel", "excel报表"),
    ;

    private String codeValue;
    private String codeDesc;

    ReportTypeEnum() {
    }

    private ReportTypeEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

}

