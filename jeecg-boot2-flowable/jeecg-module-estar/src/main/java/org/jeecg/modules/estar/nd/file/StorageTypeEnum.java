package org.jeecg.modules.estar.nd.file;

public enum StorageTypeEnum {//对于jeecgboot的上传文件类型nbacheng
    LOCAL(0, "local"),
    ALIYUN_OSS(1, "alioss"),
    MINIO(3, "minio");
    private final int code;
    private final String name;

    StorageTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }


    public String getName() {
        return name;
    }

}