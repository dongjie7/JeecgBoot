package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.bs.util.FileUtil;
import org.jeecg.modules.estar.tw.entity.TwFile;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;
import org.jeecg.modules.estar.tw.entity.TwSourceLink;
import org.jeecg.modules.estar.tw.mapper.TwFileMapper;
import org.jeecg.modules.estar.tw.mapper.TwSourceLinkMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwFileService;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @Description: 项目文件表
 * @Author: nbacheng
 * @Date:   2023-07-11
 * @Version: V1.0
 */
@Service
public class TwFileServiceImpl extends ServiceImpl<TwFileMapper, TwFile> implements ITwFileService {

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;

	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String uploadType;
	
	@Autowired
	TwSourceLinkMapper sourceLinkMapper;
	@Autowired
	ITwProjectLogService projectLogService;
	@Autowired
	ITwAccountService accountService;
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	/**
     * 每一个上传块都会包含如下分块信息：
     * chunkNumber: 当前块的次序，第一个块是 1，注意不是从 0 开始的。
     * totalChunks: 文件被分成块的总数。
     * chunkSize: 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
     * currentChunkSize: 当前块的大小，实际大小。
     * totalSize: 文件总大小。
     * identifier: 这个就是每个文件的唯一标示。
     * filename: 文件名。
     * relativePath: 文件夹上传的时候文件的相对路径属性。
     * 一个分块可以被上传多次，当然这肯定不是标准行为，但是在实际上传过程中是可能发生这种事情的，这种重传也是本库的特性之一。
     *
     * 根据响应码认为成功或失败的：
     * 200 文件上传完成
     * 201 文加快上传成功
     * 500 第一块上传失败，取消整个文件上传
     * 507 服务器出错自动重试该文件块上传
     * 
     * 此处仍不完善，未处理断点续传加密、秒传处理等。
     */
	
	@Override
	@Transactional
	public Result<?> uploadFiles(HttpServletRequest request, MultipartFile multipartFile) throws Exception{
		String  fileName= request.getParameter("identifier");
        String  orgFileName= request.getParameter("filename");
        int  chunkNumber= request.getParameter("chunkNumber") == null ?0:new Integer(request.getParameter("chunkNumber"));
        int  totalChunks= request.getParameter("totalChunks") == null ?0:new Integer(request.getParameter("totalChunks"));

        String  taskId= request.getParameter("taskId");
        String projectId = request.getParameter("projectId");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        
        String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
        String memberId = loginUser.getUsername();
        if (multipartFile.isEmpty()) {
            return  Result.error("文件不能为空！");
        } else {
            String date = DateUtil.format(new Date(), "yyyyMMdd");
            // 文件原名称
            String originFileName = multipartFile.getOriginalFilename().toString();
            // 上传文件重命名
            String uploadFileName = IdUtil.fastSimpleUUID()+"-"+originFileName;
            
            String file_url = uploadpath + "/temp/"+memberId+"/"+date+"/";
            String base_url = uploadpath + "/temp/"+memberId+"/"+date+"/"+uploadFileName;
            String downloadUrl = uploadpath + "/common/download?filePathName="+base_url+"&realFileName="+originFileName;
            // 这里使用Apache的FileUtils方法来进行保存
            File tempFile= new File(file_url, originFileName);
            Long fileSize = 0L;
            //第一个块,则新建文件
            if(1==chunkNumber && !tempFile.exists()){
            	FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tempFile);
            }else{
                //进行写文件操作
                try(
                        //将块文件写入文件中
                        InputStream fos=multipartFile.getInputStream();
                        RandomAccessFile raf =new RandomAccessFile(tempFile,"rw")
                ) {
                    int len=-1;
                    byte[] buffer=new byte[1024];
                    raf.seek((chunkNumber-1)*1024*1024);
                    while((len=fos.read(buffer))!=-1){
                        raf.write(buffer,0,len);
                    }
                    //文件大小
                    fileSize = raf.length();
                } catch (IOException e) {
                    e.printStackTrace();
                    if(chunkNumber==1) {
                    	tempFile.delete();
                    }
                    return  Result.error("读写文件错误!");
                }
            }

            
            if(chunkNumber == totalChunks){
                //分片读写结束
            	//重命名，以免重复
            	//File newFile  = new File(file_url, uploadFileName);
            	//tempFile.renameTo(newFile);
            	String uploadUrl;
            	if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
            		uploadUrl = CommonUtils.uploadLocal(multipartFile,"/tw",uploadpath);
                    
                }else{
                	uploadUrl = CommonUtils.upload(multipartFile, "/tw", uploadType);
                }
            	tempFile.delete();
                TwFile file = new TwFile();
                file.setFileSize(fileSize);
                //file.setFileUrl(file_url+uploadFileName);
                file.setFileUrl(uploadUrl);
                file.setTitle(originFileName.substring(0,originFileName.lastIndexOf(".")));
                file.setFileType("text/plain");
                file.setOrganizationId(orgId);
                file.setProjectId(projectId);
                file.setCreateBy(memberId);
                file.setDeleted(0);
                file.setDownloads(0L);
                file.setTaskId(taskId);
                file.setExtension(originFileName.substring(originFileName.lastIndexOf(".")+1));
                TwProject project = uploadTwFiles(file,memberId,projectId);
                Map result = new HashMap();
                result.put("key",file.getPathName());
                result.put("url",file.getFileUrl());
                result.put("projectName",project.getName());
                return Result.OK(result);
            }else {
                //正常返回
                return  Result.OK("上传成功!");
            }
        }
	}
	
    public TwProject uploadTwFiles(TwFile file,String memberId,String projectId){
        file.setProjectId(projectId);
        baseMapper.insert(file);
        if(StringUtils.isNotEmpty(file.getTaskId())){

            TwSourceLink sourceLink = new  TwSourceLink();
            sourceLink.setSourceType("file");
            sourceLink.setOrganizationId(file.getOrganizationId());
            sourceLink.setLinkId(file.getTaskId());
            sourceLink.setLinkType("task");
            sourceLink.setSourceId(file.getId());
            sourceLink.setSourceType("file");
            sourceLink.setSort(0);
            sourceLinkMapper.insert(sourceLink);
        }
        TwProjectLog projectLog = new TwProjectLog();
        projectLog.setProjectId(file.getProjectId());
        projectLog.setMemberId(memberId);
        projectLog.setOpeType("uploadFile");
        projectLog.setToMemberId("");
        projectLog.setIsComment(0);
        projectLog.setRemark("");
        projectLog.setContent("");
        
        TwProject project = projectLogService.run(new HashMap(){{
            put("is_comment",0);
            put("to_member_id","");
            put("content","");
            put("type","uploadFile");
            put("source_id",file.getTaskId());
            put("member_id",memberId);
            put("action_type","task");
            put("url",file.getFileUrl());
            put("title",file.getTitle());
            put("project_id",projectId);
        }});
        return project;
    }

	@Override
	public Map getFileById(String fileId) {
		return baseMapper.selectFileById(fileId);
	}

	@Override
	public Result<?> getProjectFile(Map<String, Object> mmap) {
		String projectId = MapUtils.getString(mmap,"projectId");
        Integer deleted = MapUtils.getInteger(mmap,"deleted",0);
        Map params = new HashMap(){{
            put("projectId",projectId);
            put("deleted",deleted);
        }};
        IPage<TwFile> ipage = Constant.createPage(new Page<TwFile>(),mmap);
        ipage=lambdaQuery().eq(TwFile::getProjectId,projectId).eq(TwFile::getDeleted,0).page(ipage);
        List<TwFile> resultList = new ArrayList<>();
        for(int i=0;ipage !=null && ipage.getRecords() !=null && i<ipage.getRecords().size();i++){
            TwFile file = ipage.getRecords().get(i);
            SysUser user = iEstarThirdService.getUserByUsername(file.getCreateBy());  
            file.setCreatorName(user.getRealname());
            file.setFullName(file.getTitle()+"."+file.getExtension());
            resultList.add(file);
        }
        ipage.setRecords(resultList);
        Map data = Constant.createPageResultMap(ipage);
        return Result.OK(data);
	}

	@Override
	public Result<?> recovery(String fileId) {
		 TwFile file = lambdaQuery().eq(TwFile::getId,fileId).one();
	        if(ObjectUtils.isEmpty(file)){
	        	return  Result.error("文件不存在");
	        }
	        if(file.getDeleted()==0){
	        	return  Result.error("文件已恢复");
	        }
	        lambdaUpdate().eq(TwFile::getId,fileId).set(TwFile::getDeleted,0).update();
		return Result.OK("恢复文件成功");
	}

	@Override
	public Result<?> FileRecycle(String fileId) {
		Map fileMap = getFileById(fileId);
        if(MapUtils.isEmpty(fileMap)){
            return  Result.error("文件不存在");
        }
        if(1== MapUtils.getInteger(fileMap,"deleted")){
            return  Result.error("文件已在回收站");
        }
        TwFile projectFile = new TwFile();
        projectFile.setId(MapUtils.getString(fileMap,"id"));
        projectFile.setDeleted(1);
        projectFile.setDeletedTime(new Date());
        return Result.OK(updateById(projectFile));
	}

}
