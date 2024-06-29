package org.jeecg.modules.estar.nd.util;

import java.util.Date;

import org.jeecg.modules.estar.nd.entity.NdUserfile;

import cn.hutool.core.util.IdUtil;

public class NdFileUtil {


    public static NdUserfile getQiwenDir(String userId, String filePath, String fileName) {
        NdUserfile userFile = new NdUserfile();
        userFile.setId(IdUtil.getSnowflakeNextIdStr());
        userFile.setCreateBy(userId);
        userFile.setFileid(null);
        userFile.setFilename(fileName);
        userFile.setFilepath(EstarFile.formatPath(filePath));
        userFile.setExtendname(null);
        userFile.setIsdir(1);
        userFile.setCreateTime(new Date());
        userFile.setDeleteflag(0);
        userFile.setDeletebatchnum(null);
        return userFile;
    }

    public static NdUserfile getQiwenFile(String userId, String fileId, String filePath, String fileName, String extendName) {
        NdUserfile userFile = new NdUserfile();
        userFile.setId(IdUtil.getSnowflakeNextIdStr());
        userFile.setCreateBy(userId);
        userFile.setFileid(fileId);
        userFile.setFilename(fileName);
        userFile.setFilepath(EstarFile.formatPath(filePath));
        userFile.setExtendname(extendName);
        userFile.setIsdir(0);
        userFile.setCreateTime(new Date());
        userFile.setDeleteflag(0);
        userFile.setDeletebatchnum(null);
        return userFile;
    }

    public static NdUserfile searchQiwenFileParam(NdUserfile userFile) {
        NdUserfile param = new NdUserfile();
        param.setFilepath(EstarFile.formatPath(userFile.getFilepath()));
        param.setFilename(userFile.getFilename());
        param.setExtendname(userFile.getExtendname());
        param.setDeleteflag(0);
        param.setCreateBy(userFile.getCreateBy());
        param.setIsdir(0);
        return param;
    }

    public static String formatLikePath(String filePath) {
        String newFilePath = filePath.replace("'", "\\'");
        newFilePath = newFilePath.replace("%", "\\%");
        newFilePath = newFilePath.replace("_", "\\_");
        return newFilePath;
    }

}
