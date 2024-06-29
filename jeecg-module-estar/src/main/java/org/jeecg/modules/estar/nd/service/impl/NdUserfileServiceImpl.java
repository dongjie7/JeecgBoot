package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.entity.NdRecoveryfile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.file.FileConstant;
import org.jeecg.modules.estar.nd.mapper.NdRecoveryfileMapper;
import org.jeecg.modules.estar.nd.mapper.NdStorageMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.vo.FileListVO;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 网盘用户文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class NdUserfileServiceImpl extends ServiceImpl<NdUserfileMapper, NdUserfile> implements INdUserfileService {

	@Resource
	NdUserfileMapper userFileMapper;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Resource
	NdRecoveryfileMapper recoveryFileMapper;
	
	@Resource
    FileDealComp fileDealComp;

	
	public static Executor executor = Executors.newFixedThreadPool(20);
	
	@Override
	public List<NdUserfile> selectSameUserFile(String fileName, String filePath, String extendName, String userId) {
		LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFilename, fileName)
                .eq(NdUserfile::getFilepath, filePath)
                .eq(NdUserfile::getCreateBy, userId)
                .eq(NdUserfile::getExtendname, extendName)
                .eq(NdUserfile::getDeleteflag, "0");
        return userFileMapper.selectList(lambdaQueryWrapper);
	}

	@Override
	public IPage<FileListVO> userFileList(String userId, String filePath, Integer currentPage, Integer pageCount) {
		Page<FileListVO> page = new Page<>(currentPage, pageCount);
		NdUserfile userFile = new NdUserfile();
		SysUser loginUser = iEstarThirdService.getLoginUser();
        if (userId == null) {
            userFile.setCreateBy(loginUser.getUsername());
        } else {
            userFile.setCreateBy(userId);
        }

        userFile.setFilepath(URLDecoder.decodeForPath(filePath, StandardCharsets.UTF_8));

        return userFileMapper.selectPageVo(page, userFile, null);
	}

	@Override
	public IPage<FileListVO> getFileByFileType(Integer fileTypeId, Integer currentPage, Integer pageCount,
			String userId) {
		 Page<FileListVO> page = new Page<>(currentPage, pageCount);

		 NdUserfile userFile = new NdUserfile();
	     userFile.setCreateBy(userId);
	     return userFileMapper.selectPageVo(page, userFile, fileTypeId);
	}

	@Override
	public void deleteUserFile(String userFileId, String userId) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		NdUserfile userFile = userFileMapper.selectById(userFileId);
        String uuid = UUID.randomUUID().toString();
        if (userFile.getIsdir() == 1) {
            LambdaUpdateWrapper<NdUserfile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<NdUserfile>();
            userFileLambdaUpdateWrapper.set(NdUserfile::getDeleteflag, RandomUtil.randomInt(FileConstant.deleteFileRandomSize))
                    .set(NdUserfile::getDeletebatchnum, uuid)
                    .set(NdUserfile::getDeletetime, new Date())
                    .eq(NdUserfile::getId, userFileId);
            userFileMapper.update(null, userFileLambdaUpdateWrapper);

            String filePath = new EstarFile(userFile.getFilepath(), userFile.getFilename(), true).getPath();
            updateFileDeleteStateByFilePath(filePath, uuid, loginUser.getUsername());

        } else {
        	NdUserfile userFileTemp = userFileMapper.selectById(userFileId);
            LambdaUpdateWrapper<NdUserfile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper.set(NdUserfile::getDeleteflag, RandomUtil.randomInt(1, FileConstant.deleteFileRandomSize))
                    .set(NdUserfile::getDeletetime, new Date())
                    .set(NdUserfile::getDeletebatchnum, uuid)
                    .eq(NdUserfile::getId, userFileTemp.getId());
            userFileMapper.update(null, userFileLambdaUpdateWrapper);
        }

        NdRecoveryfile recoveryFile = new NdRecoveryfile();
        recoveryFile.setUserfileid(userFileId);
        recoveryFile.setDeletetime(new Date());
        recoveryFile.setDeletebatchnum(uuid);
        recoveryFileMapper.insert(recoveryFile);

	}
	
	private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, String userId) {
        executor.execute(() -> {
            List<NdUserfile> fileList = selectUserFileByLikeRightFilePath(filePath, userId);
            for (int i = 0; i < fileList.size(); i++) {
            	NdUserfile userFileTemp = fileList.get(i);
                //标记删除标志
                LambdaUpdateWrapper<NdUserfile> userFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                userFileLambdaUpdateWrapper1.set(NdUserfile::getDeleteflag, RandomUtil.randomInt(FileConstant.deleteFileRandomSize))
                        .set(NdUserfile::getDeletetime, new Date())
                        .set(NdUserfile::getDeletebatchnum, deleteBatchNum)
                        .eq(NdUserfile::getId, userFileTemp.getId())
                        .eq(NdUserfile::getDeleteflag, 0);
                userFileMapper.update(null, userFileLambdaUpdateWrapper1);

            }
        });
    }

	@Override
	public List<NdUserfile> selectUserFileByLikeRightFilePath(String filePath, String userId) {
		return userFileMapper.selectUserFileByLikeRightFilePath(filePath, userId);
	}

	@Override
	public void userFileCopy(String userId, String userFileId, String newfilePath) {
		NdUserfile userFile = userFileMapper.selectById(userFileId);
        String oldfilePath = userFile.getFilepath();
        String oldUserId = userFile.getCreateBy();
        String fileName = userFile.getFilename();

        userFile.setFilepath(newfilePath);
        userFile.setCreateBy(userId);
        userFile.setId(IdUtil.getSnowflakeNextIdStr());
        if (userFile.getIsdir() == 0) {
            String repeatFileName = fileDealComp.getRepeatFileName(userFile, userFile.getFilepath());
            userFile.setFilename(repeatFileName);
        }
        try {
            userFileMapper.insert(userFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        oldfilePath = new EstarFile(oldfilePath, fileName, true).getPath();
        newfilePath = new EstarFile(newfilePath, fileName, true).getPath();


        if (userFile.isDirectory()) {
            List<NdUserfile> subUserFileList = userFileMapper.selectUserFileByLikeRightFilePath(oldfilePath, oldUserId);

            for (NdUserfile newUserFile : subUserFileList) {
                newUserFile.setFilepath(newUserFile.getFilepath().replaceFirst(oldfilePath, newfilePath));
                newUserFile.setId(IdUtil.getSnowflakeNextIdStr());
                if (newUserFile.isDirectory()) {
                    String repeatFileName = fileDealComp.getRepeatFileName(newUserFile, newUserFile.getFilepath());
                    newUserFile.setFilename(repeatFileName);
                }
                newUserFile.setCreateBy(userId);
                try {
                    userFileMapper.insert(newUserFile);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }
	}

	@Override
	public void updateFilepathByUserFileId(String userFileId, String newfilePath, String userId) {
		NdUserfile userFile = userFileMapper.selectById(userFileId);
        String oldfilePath = userFile.getFilepath();
        String fileName = userFile.getFilename();

        userFile.setFilepath(newfilePath);
        if (userFile.getIsdir() == 0) {
            String repeatFileName = fileDealComp.getRepeatFileName(userFile, userFile.getFilepath());
            userFile.setFilename(repeatFileName);
        }
        try {
            userFileMapper.updateById(userFile);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        //移动子目录
        oldfilePath = new EstarFile(oldfilePath, fileName, true).getPath();
        newfilePath = new EstarFile(newfilePath, fileName, true).getPath();

        if (userFile.isDirectory()) { //如果是目录，则需要移动子目录
            List<NdUserfile> list = selectUserFileByLikeRightFilePath(oldfilePath, userId);

            for (NdUserfile newUserFile : list) {
                newUserFile.setFilepath(newUserFile.getFilepath().replaceFirst(oldfilePath, newfilePath));
                if (newUserFile.getIsdir() == 0) {
                    String repeatFileName = fileDealComp.getRepeatFileName(newUserFile, newUserFile.getFilepath());
                    newUserFile.setFilename(repeatFileName);
                }
                try {
                    userFileMapper.updateById(newUserFile);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }

	}

	@Override
	public List<NdUserfile> selectFilePathTreeByUserId(String userId) {
		LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getCreateBy, userId)
                .eq(NdUserfile::getIsdir, 1)
                .eq(NdUserfile::getDeleteflag, 0);
        return userFileMapper.selectList(lambdaQueryWrapper);
	}

	@Override
	public List<NdUserfile> selectUserFileByNameAndPath(String fileName, String filePath, String userId) {
		LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFilename, fileName)
                .eq(NdUserfile::getFilepath, filePath)
                .eq(NdUserfile::getCreateBy, userId)
                .eq(NdUserfile::getDeleteflag, 0);
        return userFileMapper.selectList(lambdaQueryWrapper);
	}

	@Override
	public IPage<FileListVO> getFileByFileName(String fileName, String filePath, Integer currentPage, Integer pageCount) {
		Page<FileListVO> page = new Page<>(currentPage, pageCount);
		NdUserfile userFile = new NdUserfile();
		SysUser loginUser = iEstarThirdService.getLoginUser();
        userFile.setCreateBy(loginUser.getUsername());
        userFile.setFilename(fileName);
        userFile.setFilepath(URLDecoder.decodeForPath(filePath, StandardCharsets.UTF_8));
	    return userFileMapper.selectPageVoByName(page, userFile, fileName);
	}

}
