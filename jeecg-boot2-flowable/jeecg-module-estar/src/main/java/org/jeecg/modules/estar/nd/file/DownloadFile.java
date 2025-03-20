package org.jeecg.modules.estar.nd.file;

import com.aliyun.oss.OSS;
import lombok.Data;

@Data
public class DownloadFile {
    private String fileUrl;
    private OSS ossClient;
    private Range range;
}
