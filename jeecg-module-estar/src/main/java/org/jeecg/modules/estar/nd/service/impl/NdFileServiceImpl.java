package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.dto.BatchMoveFileDTO;
import org.jeecg.modules.estar.nd.dto.CopyFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFoldDTO;
import org.jeecg.modules.estar.nd.dto.MoveFileDTO;
import org.jeecg.modules.estar.nd.dto.RenameFileDTO;
import org.jeecg.modules.estar.nd.dto.UpdateFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdImage;
import org.jeecg.modules.estar.nd.entity.NdMusic;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.file.Copier;
import org.jeecg.modules.estar.nd.file.CopyFile;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.mapper.NdFileMapper;
import org.jeecg.modules.estar.nd.mapper.NdImageMapper;
import org.jeecg.modules.estar.nd.mapper.NdMusicMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdFileService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.util.NdFileUtil;
import org.jeecg.modules.estar.nd.util.TreeNode;
import org.jeecg.modules.estar.nd.vo.FileDetailVO;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 网盘文件表
 * @Author: nbacheng
 * @Date:   2023-04-05
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class NdFileServiceImpl extends ServiceImpl<NdFileMapper, NdFile> implements INdFileService {

	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String storageType;
	
	@Resource
	NdFileMapper fileMapper;
    @Resource
    NdUserfileMapper userFileMapper;
	
	//@Resource
	//INdFileService fileService;
	
	@Resource
	INdUserfileService userFileService;
	
	@Resource
    NDFactory ndFactory;
	
	@Resource
    NdMusicMapper musicMapper;
	
    @Resource
    NdImageMapper imageMapper;
	
	@Resource
    FileDealComp fileDealComp;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public Result<?> create(CreateFileDTO createFileDTO) {
		try {

			SysUser loginUser = iEstarThirdService.getLoginUser();
            String userId = loginUser.getUsername();
            String filePath = createFileDTO.getFilePath();
            String fileName = createFileDTO.getFileName();
            String extendName = createFileDTO.getExtendName();
            List<NdUserfile> userFiles = userFileService.selectSameUserFile(fileName, filePath, extendName, userId);
            if (userFiles != null && !userFiles.isEmpty()) {
                return Result.error("同名文件已存在");
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            String templateFilePath = "";
            if ("docx".equals(extendName)) {
                templateFilePath = "template/Word.docx";
            } else if ("xlsx".equals(extendName)) {
                templateFilePath = "template/Excel.xlsx";
            } else if ("pptx".equals(extendName)) {
                templateFilePath = "template/PowerPoint.pptx";
            } else if ("txt".equals(extendName)) {
                templateFilePath = "template/Text.txt";
            } else if ("drawio".equals(extendName)) {
                templateFilePath = "template/Drawio.drawio";
            }
            String url2 = ClassUtils.getDefaultClassLoader().getResource("static/" + templateFilePath).getPath();
            url2 = URLDecoder.decode(url2, "UTF-8");
            FileInputStream fileInputStream = new FileInputStream(url2);
            Copier copier = ndFactory.getCopier();
            CopyFile copyFile = new CopyFile();
            copyFile.setExtendName(extendName);
            String fileUrl = copier.copy(fileInputStream, copyFile);
            NdFile ndFile = new NdFile();
            ndFile.setId(IdUtil.getSnowflakeNextIdStr());
            ndFile.setFilesize(0L);
            ndFile.setFileurl(fileUrl);
            ndFile.setStoragetype(storageType);
            ndFile.setIdentifier(uuid);
            ndFile.setCreateTime(new Date());
            ndFile.setCreateBy(loginUser.getUsername());
            ndFile.setFilestatus(1);
            boolean saveFlag = save(ndFile);
            NdUserfile userFile = new NdUserfile();
            if (saveFlag) {
                userFile.setId(IdUtil.getSnowflakeNextIdStr());
                userFile.setCreateBy(userId);
                userFile.setFilename(fileName);
                userFile.setFilepath(filePath);
                userFile.setDeleteflag(0);
                userFile.setIsdir(0);
                userFile.setExtendname(extendName);
                userFile.setCreateTime(new Date());
                userFile.setFileid(ndFile.getId());

                userFileService.save(userFile);
            }
            return Result.OK("文件创建成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
	}

	@Override
	public Result<?> createFold(CreateFoldDTO createFoldDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        String filePath = createFoldDto.getFilePath();


        boolean isDirExist = fileDealComp.isDirExist(createFoldDto.getFileName(), createFoldDto.getFilePath(), userId);

        if (isDirExist) {
            return Result.error("同名文件夹已存在");
        }

        NdUserfile userFile = NdFileUtil.getQiwenDir(userId, filePath, createFoldDto.getFileName());

        userFileService.save(userFile);
        fileDealComp.uploadESByUserFileId(userFile.getId());
        return Result.OK();
	}

	@Override
	public String copyFile(CopyFileDTO copyFileDTO) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		String filePath = copyFileDTO.getFilePath();
        String userFileIds = copyFileDTO.getUserFileIds();
        String[] userFileIdArr = userFileIds.split(",");
        for (String userFileId : userFileIdArr) {
        	NdUserfile userFile = userFileService.getById(userFileId);
            String oldfilePath = userFile.getFilepath();
            String fileName = userFile.getFilename();
            if (userFile.isDirectory()) {
                EstarFile estarFile = new EstarFile(oldfilePath, fileName, true);
                if (filePath.startsWith(estarFile.getPath() + EstarFile.separator) || filePath.equals(estarFile.getPath())) {
                    return "原路径与目标路径冲突，不能复制";
                }
            }

            userFileService.userFileCopy(userId, userFileId, filePath);
            fileDealComp.deleteRepeatSubDirFile(filePath, userId);
        }

        return "复制成功";
	}

	@Override
	public String moveFile(MoveFileDTO moveFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		NdUserfile userFile = userFileService.getById(moveFileDto.getUserFileId());
        String oldfilePath = userFile.getFilepath();
        String newfilePath = moveFileDto.getFilePath();
        String fileName = userFile.getFilename();
        String extendName = userFile.getExtendname();
        if (StringUtils.isEmpty(extendName)) {
        	EstarFile estarFile = new EstarFile(oldfilePath, fileName, true);
            if (newfilePath.startsWith(estarFile.getPath() + EstarFile.separator) || newfilePath.equals(estarFile.getPath())) {
                return "原路径与目标路径冲突，不能移动";
            }
        }

        userFileService.updateFilepathByUserFileId(moveFileDto.getUserFileId(), newfilePath, userId);

        fileDealComp.deleteRepeatSubDirFile(newfilePath, userId);
        return "移动成功";
	}

	@Override
	public Result<?> getFileTree() {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		Result<TreeNode> result = new Result<TreeNode>();

        List<NdUserfile> userFileList = userFileService.selectFilePathTreeByUserId(userId);
        TreeNode resultTreeNode = new TreeNode();
        resultTreeNode.setLabel(EstarFile.separator);
        resultTreeNode.setId(0L);
        long id = 1;
        for (int i = 0; i < userFileList.size(); i++){
        	NdUserfile userFile = userFileList.get(i);
            EstarFile estarFile = new EstarFile(userFile.getFilepath(), userFile.getFilename(), false);
            String filePath = estarFile.getPath();

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split(EstarFile.separator);
            for (int j = 0; j < strArr.length; j++){
                if (!"".equals(strArr[j]) && strArr[j] != null){
                    queue.add(strArr[j]);
                }

            }
            if (queue.size() == 0){
                continue;
            }

            resultTreeNode = fileDealComp.insertTreeNode(resultTreeNode, id++, EstarFile.separator, queue);


        }
        List<TreeNode> treeNodeList = resultTreeNode.getChildren();
        Collections.sort(treeNodeList, (o1, o2) -> {
            long i = o1.getId() - o2.getId();
            return (int) i;
        });
        result.setSuccess(true);
        result.setResult(resultTreeNode);
        return result;
	}

	@Override
	public String renameFile(RenameFileDTO renameFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		NdUserfile userFile = userFileService.getById(renameFileDto.getUserFileId());

	        List<NdUserfile> userFiles = userFileService.selectUserFileByNameAndPath(renameFileDto.getFileName(), userFile.getFilepath(), userId);
	        if (userFiles != null && !userFiles.isEmpty()) {
	            return "同名文件已存在";
	        }

	        LambdaUpdateWrapper<NdUserfile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
	        lambdaUpdateWrapper.set(NdUserfile::getFilename, renameFileDto.getFileName())
	                .set(NdUserfile::getCreateTime, new Date())
	                .eq(NdUserfile::getId, renameFileDto.getUserFileId());
	        userFileService.update(lambdaUpdateWrapper);
	        if (1 == userFile.getIsdir()) {
	            List<NdUserfile> list = userFileService.selectUserFileByLikeRightFilePath(new EstarFile(userFile.getFilepath(), userFile.getFilename(), true).getPath(), userId);

	            for (NdUserfile newUserFile : list) {
	                newUserFile.setFilepath(newUserFile.getFilepath().replaceFirst(new EstarFile(userFile.getFilepath(), userFile.getFilename(), userFile.getIsdir() == 1).getPath(),
	                        new EstarFile(userFile.getFilepath(), renameFileDto.getFileName(), userFile.getIsdir() == 1).getPath()));
	                userFileService.updateById(newUserFile);
	            }
	        }
	        fileDealComp.uploadESByUserFileId(renameFileDto.getUserFileId());
	        return "改名成功";
	}

	@Override
	public Result<?> updateFile(UpdateFileDTO updateFileDTO) {
		NdUserfile userFile = userFileService.getById(updateFileDTO.getUserFileId());
		//NdFile ndFile = fileService.getById(userFile.getFileid());
        //Long pointCount = fileService.getFilePointCount(userFile.getFileid());
		NdFile ndFile =  fileMapper.selectById(userFile.getFileid());
		Long pointCount = getFilePointCount(userFile.getFileid());
        String fileUrl = ndFile.getFileurl();
        if (pointCount > 1) {
            fileUrl = fileDealComp.copyFile(ndFile, userFile);
        }
        String content = updateFileDTO.getFileContent();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        try {
            int fileSize = byteArrayInputStream.available();
            fileDealComp.saveFileInputStream(ndFile.getStoragetype(), fileUrl, byteArrayInputStream);

            String md5Str = fileDealComp.getIdentifierByFile(fileUrl, ndFile.getStoragetype());

            updateFileDetail(userFile.getId(), md5Str, fileSize);


        } catch (Exception e) {
        	return Result.error("修改文件异常");
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.OK("修改文件成功");
	}

	@Override
	public Long getFilePointCount(String fileId) {
		LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFileid, fileId);
        long count = userFileMapper.selectCount(lambdaQueryWrapper);
        return count;
	}

	@Override
	public void updateFileDetail(String userFileId, String identifier, long fileSize) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		NdUserfile userFile = userFileMapper.selectById(userFileId);
        NdFile ndFile = new NdFile();
        ndFile.setIdentifier(identifier);
        ndFile.setFilesize(fileSize);
        ndFile.setUpdateTime(new Date());
        ndFile.setUpdateBy(userId);
        ndFile.setId(userFile.getFileid());
        fileMapper.updateById(ndFile);
        userFile.setCreateTime(new Date());
        userFileMapper.updateById(userFile);
	}

	@Override
	public String batchMoveFile(BatchMoveFileDTO batchMoveFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		String newfilePath = batchMoveFileDto.getFilePath();

        String userFileIds = batchMoveFileDto.getUserFileIds();
        String[] userFileIdArr = userFileIds.split(",");

        for (String userFileId : userFileIdArr) {
        	NdUserfile userFile = userFileService.getById(userFileId);
            if (StringUtils.isEmpty(userFile.getExtendname())) {
            	EstarFile estarFile = new EstarFile(userFile.getFilepath(), userFile.getFilename(), true);
                if (newfilePath.startsWith(estarFile.getPath() + EstarFile.separator) || newfilePath.equals(estarFile.getPath())) {
                    return ("原路径与目标路径冲突，不能移动");
                }
            }
            userFileService.updateFilepathByUserFileId(userFile.getId(), newfilePath, userId);
        }

        return ("批量移动文件成功");
	}

	@Override
	public Result<?> createFile(@Valid CreateFileDTO createFileDTO) {
		try {
			SysUser loginUser = iEstarThirdService.getLoginUser();
	        String userId = loginUser.getUsername();
            String filePath = createFileDTO.getFilePath();
            String fileName = createFileDTO.getFileName();
            String extendName = createFileDTO.getExtendName();
            List<NdUserfile> userFiles = userFileService.selectSameUserFile(fileName, filePath, extendName, userId);
            if (userFiles != null && !userFiles.isEmpty()) {
                return Result.error("同名文件已存在");
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            String templateFilePath = "";
            if ("docx".equals(extendName)) {
                templateFilePath = "template/Word.docx";
            } else if ("xlsx".equals(extendName)) {
                templateFilePath = "template/Excel.xlsx";
            } else if ("pptx".equals(extendName)) {
                templateFilePath = "template/PowerPoint.pptx";
            } else if ("txt".equals(extendName)) {
                templateFilePath = "template/Text.txt";
            } else if ("drawio".equals(extendName)) {
                templateFilePath = "template/Drawio.drawio";
            }
            String url2 = ClassUtils.getDefaultClassLoader().getResource("static/" + templateFilePath).getPath();
            url2 = URLDecoder.decode(url2, "UTF-8");
            FileInputStream fileInputStream = new FileInputStream(url2);
            Copier copier = ndFactory.getCopier();
            CopyFile copyFile = new CopyFile();
            copyFile.setExtendName(extendName);
            String fileUrl = copier.copy(fileInputStream, copyFile);

            NdFile ndFile = new NdFile();
            ndFile.setId(IdUtil.getSnowflakeNextIdStr());
            ndFile.setFilesize(0L);
            ndFile.setFileurl(fileUrl);
            ndFile.setStoragetype(storageType);
            ndFile.setIdentifier(uuid);
            ndFile.setCreateTime(new Date());
            ndFile.setCreateBy(userId);
            ndFile.setFilestatus(1);
            //boolean saveFlag = fileService.save(ndFile);
            int saveFlag = fileMapper.insert(ndFile);
            NdUserfile userFile = new NdUserfile();
            if (saveFlag>0) {
                userFile.setId(IdUtil.getSnowflakeNextIdStr());
                userFile.setCreateBy(userId);
                userFile.setFilename(fileName);
                userFile.setFilepath(filePath);
                userFile.setDeleteflag(0);
                userFile.setIsdir(0);
                userFile.setExtendname(extendName);
                userFile.setCreateTime(new Date());
                userFile.setFileid(ndFile.getId());

                userFileService.save(userFile);
            }
            return Result.OK("文件创建成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
	}

	@Override
	public FileDetailVO getFileDetail(String userFileId) {
		NdUserfile userFile = userFileMapper.selectById(userFileId);
		NdFile ndFile = fileMapper.selectById(userFile.getFileid());
        NdMusic music = musicMapper.selectOne(new QueryWrapper<NdMusic>().eq("fileId", userFile.getFileid()));
        NdImage image = imageMapper.selectOne(new QueryWrapper<NdImage>().eq("fileId", userFile.getFileid()));

        if ("mp3".equalsIgnoreCase(userFile.getExtendname()) || "flac".equalsIgnoreCase(userFile.getExtendname())) {
            if (music == null) {
                fileDealComp.parseMusicFile(userFile.getExtendname(), ndFile.getStoragetype(), ndFile.getFileurl(), ndFile.getId());
                music = musicMapper.selectOne(new QueryWrapper<NdMusic>().eq("fileId", userFile.getFileid()));
            }
        }

        FileDetailVO fileDetailVO = new FileDetailVO();
        BeanUtil.copyProperties(userFile, fileDetailVO);
        BeanUtil.copyProperties(ndFile, fileDetailVO);
        fileDetailVO.setMusic(music);
        fileDetailVO.setImage(image);
        return fileDetailVO;
	}

}
