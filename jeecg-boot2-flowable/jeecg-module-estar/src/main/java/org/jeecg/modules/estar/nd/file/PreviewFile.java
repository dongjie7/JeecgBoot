package org.jeecg.modules.estar.nd.file;

import com.aliyun.oss.OSS;
import lombok.Data;

@Data
public class PreviewFile {
    private String fileUrl;
    private OSS ossClient;
}
