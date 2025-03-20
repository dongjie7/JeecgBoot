package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.entity.NdRecoveryfile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.mapper.NdRecoveryfileMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdRecoveryfileService;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.vo.RecoveryFileListVo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: nd_recoveryfile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class NdRecoveryfileServiceImpl extends ServiceImpl<NdRecoveryfileMapper, NdRecoveryfile> implements INdRecoveryfileService {

	@Resource
	NdUserfileMapper userFileMapper;
	
	@Resource
	NdRecoveryfileMapper recoveryFileMapper;
	
	@Autowired
    FileDealComp fileDealComp;
	
	@Override
	public void deleteUserFileByDeleteBatchNum(String deleteBatchNum) {
		 LambdaQueryWrapper<NdUserfile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
	     userFileLambdaQueryWrapper.eq(NdUserfile::getDeletebatchnum, deleteBatchNum);
	     userFileMapper.delete(userFileLambdaQueryWrapper);
	}

	@Override
	public List<RecoveryFileListVo> selectRecoveryFileList(String username) {
		return recoveryFileMapper.selectRecoveryFileList(username);
	}

	@Override
	public void restorefile(String deleteBatchNum, String filePath, String username) {
		LambdaUpdateWrapper<NdUserfile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userFileLambdaUpdateWrapper.set(NdUserfile::getDeleteflag, 0)
                .set(NdUserfile::getDeletebatchnum, "")
                .eq(NdUserfile::getDeletebatchnum, deleteBatchNum);
        userFileMapper.update(null, userFileLambdaUpdateWrapper);
        EstarFile estarFile = new EstarFile(filePath, true);
        fileDealComp.restoreParentFilePath(estarFile, username);

        fileDealComp.deleteRepeatSubDirFile(filePath, username);
        // TODO 如果被还原的文件已存在，暂未实现

        LambdaQueryWrapper<NdRecoveryfile> recoveryFileServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recoveryFileServiceLambdaQueryWrapper.eq(NdRecoveryfile::getDeletebatchnum, deleteBatchNum);
        recoveryFileMapper.delete(recoveryFileServiceLambdaQueryWrapper);
	}

}
