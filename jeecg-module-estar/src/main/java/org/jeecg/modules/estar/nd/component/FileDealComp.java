package org.jeecg.modules.estar.nd.component;

import cn.hutool.core.util.IdUtil;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.bs.util.FileUtil;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdMusic;
import org.jeecg.modules.estar.nd.entity.NdShare;
import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.file.DownloadFile;
import org.jeecg.modules.estar.nd.file.Downloader;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.file.WriteFile;
import org.jeecg.modules.estar.nd.file.Writer;
import org.jeecg.modules.estar.nd.mapper.NdFileMapper;
import org.jeecg.modules.estar.nd.mapper.NdMusicMapper;
import org.jeecg.modules.estar.nd.mapper.NdShareMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdShareService;
import org.jeecg.modules.estar.nd.service.INdSharefileService;
import org.jeecg.modules.estar.nd.util.NdFileUtil;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.MusicUtils;
import org.jeecg.modules.estar.nd.util.TreeNode;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 文件逻辑处理组件
 */
@Slf4j
@Component
@Lazy  //延迟加载，否则发布生产时会出错 by nbacheng
public class FileDealComp {
	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;
	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String uploadType;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
    @Resource
    private NdUserfileMapper userFileMapper;
    @Resource
    private NdFileMapper fileMapper;
    //@Resource
    //private INdShareService shareService;
    @Resource
	NdShareMapper shareMapper;
    @Resource
    private INdSharefileService shareFileService;
    @Resource
    private NdMusicMapper musicMapper;
    @Resource
    private NDFactory ndFactory;

    public static Executor exec = Executors.newFixedThreadPool(10);

    /**
     * 获取重复文件名
     *
     * 场景1: 文件还原时，在 savefilePath 路径下，保存 测试.txt 文件重名，则会生成 测试(1).txt
     * 场景2： 上传文件时，在 savefilePath 路径下，保存 测试.txt 文件重名，则会生成 测试(1).txt
     *
     * @param userFile
     * @param savefilePath
     * @return
     */
    public String getRepeatFileName(NdUserfile userFile, String savefilePath) {
        String fileName = userFile.getFilename();
        String extendName = userFile.getExtendname();
        Integer deleteFlag = userFile.getDeleteflag();
        String userId = userFile.getCreateBy();
        int isDir = userFile.getIsdir();
        LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFilepath, savefilePath)
                .eq(NdUserfile::getDeleteflag, deleteFlag)
                .eq(NdUserfile::getCreateBy, userId)
                .eq(NdUserfile::getFilename, fileName)
                .eq(NdUserfile::getIsdir, isDir);
        if (userFile.getIsdir() == 0) {
            lambdaQueryWrapper.eq(NdUserfile::getExtendname, extendName);
        }
        List<NdUserfile> list = userFileMapper.selectList(lambdaQueryWrapper);
        if (list == null) {
            return fileName;
        }
        if (list.isEmpty()) {
            return fileName;
        }
        int i = 0;

        while (list != null && !list.isEmpty()) {
            i++;
            LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(NdUserfile::getFilepath, savefilePath)
                    .eq(NdUserfile::getDeleteflag, deleteFlag)
                    .eq(NdUserfile::getCreateBy, userId)
                    .eq(NdUserfile::getFilename, fileName + "(" + i + ")")
                    .eq(NdUserfile::getIsdir, isDir);
            if (userFile.getIsdir() == 0) {
                lambdaQueryWrapper1.eq(NdUserfile::getExtendname, extendName);
            }
            list = userFileMapper.selectList(lambdaQueryWrapper1);
        }

        return fileName + "(" + i + ")";

    }

    /**
     * 还原父文件路径
     *
     * 1、回收站文件还原操作会将文件恢复到原来的路径下,当还原文件的时候，如果父目录已经不存在了，则需要把父母录给还原
     * 2、上传目录
     *
     * @param sessionUserId
     */
    public void restoreParentFilePath(EstarFile estarFile, String sessionUserId) {

        if (estarFile.isFile()) {
            estarFile = estarFile.getParentFile();
        }
        while(estarFile.getParent() != null) {
            String fileName = estarFile.getName();
            String parentFilePath = estarFile.getParent();

            LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(NdUserfile::getFilepath, parentFilePath)
                    .eq(NdUserfile::getFilename, fileName)
                    .eq(NdUserfile::getDeleteflag, 0)
                    .eq(NdUserfile::getIsdir, 1)
                    .eq(NdUserfile::getCreateBy, sessionUserId);
            List<NdUserfile> userFileList = userFileMapper.selectList(lambdaQueryWrapper);
            if (userFileList.size() == 0) {
            	NdUserfile userFile = NdFileUtil.getQiwenDir(sessionUserId, parentFilePath, fileName);
                try {
                    userFileMapper.insert(userFile);
                } catch (Exception e) {
                    if (e.getMessage().contains("Duplicate entry")) {
                        //ignore
                    } else {
                        log.error(e.getMessage());
                    }
                }
            }
            estarFile = new EstarFile(parentFilePath, true);
        }
    }


    /**
     * 删除重复的子目录文件
     *
     * 当还原目录的时候，如果其子目录在文件系统中已存在，则还原之后进行去重操作
     * @param filePath
     * @param sessionUserId
     */
    public void deleteRepeatSubDirFile(String filePath, String sessionUserId) {
        log.debug("删除子目录："+filePath);
        LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.select(NdUserfile::getFilename, NdUserfile::getFilepath)
                .likeRight(NdUserfile::getFilepath, NdFileUtil.formatLikePath(filePath))
                .eq(NdUserfile::getIsdir, 1)
                .eq(NdUserfile::getDeleteflag, 0)
                .eq(NdUserfile::getCreateBy, sessionUserId)
                .groupBy(NdUserfile::getFilepath, NdUserfile::getFilename)
                .having("count(fileName) >= 2");
        List<NdUserfile> repeatList = userFileMapper.selectList(lambdaQueryWrapper);

        for (NdUserfile userFile : repeatList) {
            LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(NdUserfile::getFilepath, userFile.getFilepath())
                    .eq(NdUserfile::getFilename, userFile.getFilename())
                    .eq(NdUserfile::getDeleteflag, "0");
            List<NdUserfile> userFiles = userFileMapper.selectList(lambdaQueryWrapper1);
            for (int i = 0; i < userFiles.size() - 1; i ++) {
                userFileMapper.deleteById(userFiles.get(i).getId());
            }
        }
    }

    /**
     * 组织一个树目录节点，文件移动的时候使用
     * @param treeNode
     * @param id
     * @param filePath
     * @param nodeNameQueue
     * @return
     */
    public TreeNode insertTreeNode(TreeNode treeNode, long id, String filePath, Queue<String> nodeNameQueue){

        List<TreeNode> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null){
            return treeNode;
        }

        EstarFile estarFile = new EstarFile(filePath, currentNodeName, true);
        filePath = estarFile.getPath();

        if (!isExistPath(childrenTreeNodes, currentNodeName)){  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNode resultTreeNode = new TreeNode();

            resultTreeNode.setFilePath(filePath);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            resultTreeNode.setId(++id);

            childrenTreeNodes.add(resultTreeNode);

        }else{  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {

                TreeNode childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())){
                    childrenTreeNode = insertTreeNode(childrenTreeNode, id * 10, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }

            }
        }else{
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    /**
     * 判断该路径在树节点中是否已经存在
     * @param childrenTreeNodes
     * @param path
     * @return
     */
    public boolean isExistPath(List<TreeNode> childrenTreeNodes, String path){
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++){
                if (path.equals(childrenTreeNodes.get(i).getLabel())){
                    isExistPath = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return isExistPath;
    }


    public void uploadESByUserFileId(String userFileId) {

        try {

            Map<String, Object> param = new HashMap<>();
            param.put("id", userFileId);
            List<NdUserfile> userfileResult = userFileMapper.selectByMap(param);
            if (userfileResult != null && userfileResult.size() > 0) {
               
                
            }
        } catch (Exception e) {
            log.debug("ES更新操作失败，请检查配置");
        }

    }

    public void deleteESByUserFileId(String userFileId) {
        exec.execute(()->{
            try {
               
            } catch (Exception e) {
                log.debug("ES删除操作失败，请检查配置");
            }
        });


    }

    /**
     * 根据用户传入的参数，判断是否有下载或者预览权限
     * @return
     */
    public boolean checkAuthDownloadAndPreview(String shareBatchNum,
                                               String extractionCode,
                                               String token,
                                               String userFileId,
                                               Integer platform) {
        log.debug("权限检查开始：shareBatchNum:{}, extractionCode:{}, token:{}, userFileId{}" , shareBatchNum, extractionCode, token, userFileId);
        if (platform != null && platform == 2) {
            return true;
        }
        NdUserfile userFile = userFileMapper.selectById(userFileId);
        log.debug(JSON.toJSONString(userFile));
        if ("undefined".equals(shareBatchNum)  || StringUtils.isEmpty(shareBatchNum)) {

        	SysUser loginUser = iEstarThirdService.getLoginUser();
            String userId = iEstarThirdService.getUserNameByToken(token);
            log.debug(JSON.toJSONString("当前登录session用户id：" + userId));
            if (userId == null) {
                return false;
            }
            log.debug("文件所属用户id：" + userFile.getCreateBy());
            log.debug("登录用户id:" + userId);
            if (!userFile.getCreateBy().equals(userId)) {
                log.info("用户id不一致，权限校验失败");
                return false;
            }
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("shareBatchNum", shareBatchNum);
            //List<NdShare> shareList = shareService.listByMap(param);
            List<NdShare> shareList = shareMapper.selectByMap(param);
            //判断批次号
            if (shareList.size() <= 0) {
                log.info("分享批次号不存在，权限校验失败");
                return false;
            }
            Integer shareType = shareList.get(0).getSharetype();
            if (1 == shareType) {
                //判断提取码
                if (!shareList.get(0).getExtractioncode().equals(extractionCode)) {
                    log.info("提取码错误，权限校验失败");
                    return false;
                }
            }
            param.put("userFileId", userFileId);
            List<NdSharefile> shareFileList = shareFileService.listByMap(param);
            if (shareFileList.size() <= 0) {
                log.info("用户id和分享批次号不匹配，权限校验失败");
                return false;
            }

        }
        return true;
    }

    /**
     * 拷贝文件
     * 场景：修改的文件被多处引用时，需要重新拷贝一份，然后在新的基础上修改
     * @param fileBean
     * @param userFile
     * @return
     */
    public String copyFile(NdFile ndFile, NdUserfile userFile) {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(ndFile.getFileurl());
        File file = new File(downloadFile.getFileUrl());
        String fileUrl;
        if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
            fileUrl = CommonUtils.uploadLocal(FileUtil.getMultipartFile(file),"/nd",uploadpath);
                  
          }else{
            fileUrl = CommonUtils.upload(FileUtil.getMultipartFile(file), "/nd", uploadType);
          }
        if (downloadFile.getOssClient() != null) {
            downloadFile.getOssClient().shutdown();
        }
        ndFile.setFileurl(fileUrl);
        ndFile.setId(IdUtil.getSnowflakeNextIdStr());
        fileMapper.insert(ndFile);
        userFile.setId(ndFile.getId());
        userFile.setCreateTime(new Date());
        userFileMapper.updateById(userFile);
        return fileUrl;
    }

    public String getIdentifierByFile(String fileUrl, String storageType) throws IOException {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(fileUrl);
        InputStream inputStream = ndFactory.getDownloader(storageType).getInputStream(downloadFile);
        String md5Str = DigestUtils.md5Hex(inputStream);
        return md5Str;
    }

    public void saveFileInputStream(String storageType, String fileUrl, InputStream inputStream) throws IOException {
        Writer writer1 = ndFactory.getWriter(storageType);
        WriteFile writeFile = new WriteFile();
        writeFile.setFileUrl(fileUrl);
        int fileSize = inputStream.available();
        writeFile.setFileSize(fileSize);
        writer1.write(inputStream, writeFile);
    }
    
    public boolean isDirExist(String fileName, String filePath, String userId){
        LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFilename, fileName)
                .eq(NdUserfile::getFilepath, EstarFile.formatPath(filePath))
                .eq(NdUserfile::getCreateBy, userId)
                .eq(NdUserfile::getDeleteflag, 0)
                .eq(NdUserfile::getIsdir, 1);
        List<NdUserfile> list = userFileMapper.selectList(lambdaQueryWrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }


    public void parseMusicFile(String extendName, String storageType, String fileUrl, String fileId) {
        File outFile = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if ("mp3".equalsIgnoreCase(extendName) || "flac".equalsIgnoreCase(extendName)) {
                Downloader downloader = ndFactory.getDownloader(storageType);
                DownloadFile downloadFile = new DownloadFile();
                downloadFile.setFileUrl(fileUrl);
                inputStream = downloader.getInputStream(downloadFile);
                outFile = EstarUtils.getTempFile(fileUrl);
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                fileOutputStream = new FileOutputStream(outFile);
                IOUtils.copy(inputStream, fileOutputStream);
                NdMusic music = new NdMusic();
                music.setId(IdUtil.getSnowflakeNextIdStr());
                music.setFileid(fileId);

                Tag tag = null;
                AudioHeader audioHeader = null;
                if ("mp3".equalsIgnoreCase(extendName)) {
                    MP3File f = (MP3File) AudioFileIO.read(outFile);
                    tag = f.getTag();
                    audioHeader = f.getAudioHeader();
                    MP3File mp3file = new MP3File(outFile);
                    if (mp3file.hasID3v2Tag()) {
                        AbstractID3v2Tag id3v2Tag = mp3file.getID3v2TagAsv24();
                        AbstractID3v2Frame frame = (AbstractID3v2Frame) id3v2Tag.getFrame("APIC");
                        FrameBodyAPIC body;
                        if (frame != null && !frame.isEmpty()) {
                            body = (FrameBodyAPIC) frame.getBody();
                            byte[] imageData = body.getImageData();
                            music.setAlbumimage(Base64.getEncoder().encodeToString(imageData));
                        }
                        if (tag != null) {
                            music.setArtist(tag.getFirst(FieldKey.ARTIST));
                            music.setTitle(tag.getFirst(FieldKey.TITLE));
                            music.setAlbum(tag.getFirst(FieldKey.ALBUM));
                            music.setYear(tag.getFirst(FieldKey.YEAR));
                            try {
                                music.setTrack(tag.getFirst(FieldKey.TRACK));
                            } catch (Exception e) {
                                // ignore
                            }

                            music.setGenre(tag.getFirst(FieldKey.GENRE));
                            music.setComment(tag.getFirst(FieldKey.COMMENT));
                            music.setLyrics(tag.getFirst(FieldKey.LYRICS));
                            music.setComposer(tag.getFirst(FieldKey.COMPOSER));
                            music.setAlbumartist(tag.getFirst(FieldKey.ALBUM_ARTIST));
                            music.setEncoder(tag.getFirst(FieldKey.ENCODER));
                        }
                    }
                } else if ("flac".equalsIgnoreCase(extendName)) {
                    AudioFile f = new FlacFileReader().read(outFile);
                    tag = f.getTag();
                    audioHeader = f.getAudioHeader();
                    if (tag != null) {
                        music.setArtist(StringUtils.join(tag.getFields(FieldKey.ARTIST), ","));
                        music.setTitle(StringUtils.join(tag.getFields(FieldKey.TITLE), ","));
                        music.setAlbum(StringUtils.join(tag.getFields(FieldKey.ALBUM), ","));
                        music.setYear(StringUtils.join(tag.getFields(FieldKey.YEAR), ","));
                        music.setTrack(StringUtils.join(tag.getFields(FieldKey.TRACK), ","));
                        music.setGenre(StringUtils.join(tag.getFields(FieldKey.GENRE), ","));
                        music.setComment(StringUtils.join(tag.getFields(FieldKey.COMMENT), ","));
                        music.setLyrics(StringUtils.join(tag.getFields(FieldKey.LYRICS), ","));
                        music.setComposer(StringUtils.join(tag.getFields(FieldKey.COMPOSER), ","));
                        music.setAlbumartist(StringUtils.join(tag.getFields(FieldKey.ALBUM_ARTIST), ","));
                        music.setEncoder(StringUtils.join(tag.getFields(FieldKey.ENCODER), ","));
                        List<Artwork> artworkList = tag.getArtworkList();
                        if (artworkList != null && !artworkList.isEmpty()) {
                            Artwork artwork = artworkList.get(0);
                            byte[] binaryData = artwork.getBinaryData();
                            music.setAlbumimage(Base64.getEncoder().encodeToString(binaryData));
                        }
                    }

                }

                if (audioHeader != null) {
                    music.setTracklength(Double.parseDouble(audioHeader.getTrackLength() + ""));
                }

                if (StringUtils.isEmpty(music.getLyrics())) {
                    try {
                        
                        String lyc = MusicUtils.getLyc(music.getArtist(), music.getTitle(), music.getAlbum());
                        music.setLyrics(lyc);
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }
                musicMapper.insert(music);
            }
        } catch (Exception e) {
            log.error("解析音乐信息失败！", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fileOutputStream);
            if (outFile != null) {
                if (outFile.exists()) {
                    outFile.delete();
                }
            }
        }
    }

}
