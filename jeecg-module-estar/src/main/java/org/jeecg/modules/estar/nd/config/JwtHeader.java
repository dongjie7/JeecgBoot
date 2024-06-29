package org.jeecg.modules.estar.nd.config;

import lombok.Data;

@Data
public class JwtHeader {
    private String alg;
    private String typ;
}
