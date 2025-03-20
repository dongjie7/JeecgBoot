package org.jeecg.modules.estar.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class OaSalaryTotal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**depname*/
    private String depname;
	/**rs*/
    private Integer rs;
	/**bysfgz*/
    private Double bysfgz;
	/**byjbf*/
    private Double byjbf;
	/**bysfjj*/
    private Double bysfjj;
	/**sysfgz*/
    private Double sysfgz;
	/**syjbf*/
    private Double syjbf;
	/**sysfjj*/
    private Double sysfjj;
}

