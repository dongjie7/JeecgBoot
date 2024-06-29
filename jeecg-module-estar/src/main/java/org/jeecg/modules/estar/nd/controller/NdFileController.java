package org.jeecg.modules.estar.nd.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.AsyncTaskComp;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.dto.BatchDeleteFileDTO;
import org.jeecg.modules.estar.nd.dto.BatchMoveFileDTO;
import org.jeecg.modules.estar.nd.dto.CopyFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFoldDTO;
import org.jeecg.modules.estar.nd.dto.DeleteFileDTO;
import org.jeecg.modules.estar.nd.dto.MoveFileDTO;
import org.jeecg.modules.estar.nd.dto.RenameFileDTO;
import org.jeecg.modules.estar.nd.dto.UnzipFileDTO;
import org.jeecg.modules.estar.nd.dto.UpdateFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.exception.EstarException;
import org.jeecg.modules.estar.nd.file.DownloadFile;
import org.jeecg.modules.estar.nd.file.Downloader;
import org.jeecg.modules.estar.nd.file.FileOperation;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.mapper.NdFileMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdFileService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.NdFileUtil;
import org.jeecg.modules.estar.nd.vo.FileDetailVO;
import org.jeecg.modules.estar.nd.vo.FileListVO;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 网盘文件表
 * @Author: nbacheng
 * @Date:   2023-04-05
 * @Version: V1.0
 */
@Api(tags="网盘文件表")
@RestController
@RequestMapping("/nd/ndFile")
@Slf4j
public class NdFileController extends JeecgController<NdFile, INdFileService> {
	@Autowired
	private INdFileService ndFileService;
	
	@Autowired
	private INdUserfileService userFileService;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Resource
	NdFileMapper fileMapper;
    @Resource
    NdUserfileMapper userFileMapper;
	
	@Resource
    NDFactory ndFactory;
	
	@Resource
    AsyncTaskComp asyncTaskComp;
	
	@Resource
    FileDealComp fileDealComp;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "网盘文件表-分页列表查询")
	@ApiOperation(value="网盘文件表-分页列表查询", notes="网盘文件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdFile ndFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdFile> queryWrapper = QueryGenerator.initQueryWrapper(ndFile, req.getParameterMap());
		Page<NdFile> page = new Page<NdFile>(pageNo, pageSize);
		IPage<NdFile> pageList = ndFileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 分页文件列表查询
	 *
	 * @param fileType
	 * @param filePath
	 * @param currentPage
	 * @param pageCount
	 * @param req
	 * @return
	 */
	@AutoLog(value = "网盘文件表-获取文件列表")
	@ApiOperation(value="网盘文件表-获取文件列表", notes="网盘文件表-获取文件列表")
	@GetMapping(value = "/getfilelist")
	public Result<?> getfilelist(
			                       @RequestParam(name="fileType", defaultValue="1") String fileType,
			                       @RequestParam(name="filePath", defaultValue="10") String filePath,
								   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
								   @RequestParam(name="pageCount", defaultValue="10") Integer pageCount,
								   HttpServletRequest req) {
		if ("0".equals(fileType)) {
            IPage<FileListVO> fileList = userFileService.userFileList(null, filePath, currentPage, pageCount);
            return Result.OK(fileList);
        } else {
        	SysUser loginUser = iEstarThirdService.getLoginUser();
            IPage<FileListVO> fileList = userFileService.getFileByFileType(Integer.valueOf(fileType), currentPage, pageCount, loginUser.getUsername());
            return Result.OK(fileList);
        }
	}
	
	/**
	 * 分页搜索文件列表-目前用mysql搜索，大数据也可以考虑用ES搜索
	 *
	 * @param fileName
	 * @param filePath
	 * @param currentPage
	 * @param pageCount
	 * @param req
	 * @return
	 */
	@AutoLog(value = "网盘文件表-搜索文件列表")
	@ApiOperation(value="网盘文件表-搜索文件列表", notes="网盘文件表-搜索文件列表")
	@GetMapping(value = "/search")
	public Result<?> getfilelistbyname(
			                       @RequestParam(name="fileName", defaultValue="%%") String fileName,
			                       @RequestParam(name="filePath", defaultValue="10") String filePath,
								   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
								   @RequestParam(name="pageCount", defaultValue="10") Integer pageCount,
								   HttpServletRequest req) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		if ("%%".equals(fileName)) {
            IPage<FileListVO> fileList = userFileService.getFileByFileName(fileName, filePath, currentPage, pageCount);
            return Result.OK(fileList);
        } else {
            IPage<FileListVO> fileList = userFileService.getFileByFileName("%" + fileName +"%", "%" + filePath +"%", currentPage, pageCount);
            return Result.OK(fileList);
        }
	}
	
	/**
	 *   文件重命名
	 *
	 * @param renameFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-文件重命名")
	@ApiOperation(value="网盘文件表-文件重命名", notes="网盘文件表-文件重命名")
	@PostMapping(value = "/renamefile")
	public Result<?> renameFile(@RequestBody RenameFileDTO renameFileDto) {
		return Result.OK(ndFileService.renameFile(renameFileDto));
	}
	
	/**
	 *   复制文件
	 *
	 * @param copyFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-复制文件")
	@ApiOperation(value="网盘文件表-复制文件", notes="网盘文件表-复制文件")
	@PostMapping(value = "/copyfile")
	public Result<?> copyFile(@RequestBody CopyFileDTO copyFileDTO) {
		return Result.OK(ndFileService.copyFile(copyFileDTO));
	}
	
	/**
	 *   移动文件
	 *
	 * @param moveFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-移动文件")
	@ApiOperation(value="网盘文件表-移动文件", notes="网盘文件表-移动文件")
	@PostMapping(value = "/movefile")
	public Result<?> moveFile(@RequestBody MoveFileDTO moveFileDto) {
		return Result.OK(ndFileService.moveFile(moveFileDto));
	}
	
	/**
	 *   批量移动文件
	 *
	 * @param batchMoveFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-批量移动文件")
	@ApiOperation(value="网盘文件表-批量移动文件", notes="网盘文件表-批量移动文件")
	@PostMapping(value = "/batchmovefile")
	public Result<?> batchMoveFile(@RequestBody BatchMoveFileDTO batchMoveFileDto) {
		return Result.OK(ndFileService.batchMoveFile(batchMoveFileDto));
	}
	
	/**
	 * 获取文件树
	 *
	 * @param 
	 * @return
	 */
	@AutoLog(value = "网盘文件表-获取文件树")
	@ApiOperation(value="网盘文件表-获取文件树", notes="网盘文件表-获取文件树")
	@GetMapping(value = "/getfiletree")
	public Result<?> getFileTree() {
		 return ndFileService.getFileTree();
	}

	
	/**
	 *   修改文件-只支持普通文本类文件的修改
	 *
	 * @param updateFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-修改文件")
	@ApiOperation(value="网盘文件表-修改文件", notes="网盘文件表-修改文件")
	@PostMapping(value = "/update")
	public Result<?> updateFile(@RequestBody UpdateFileDTO updateFileDTO) {
		return ndFileService.updateFile(updateFileDTO);
	}
	
	
	/**
	 *   添加
	 *
	 * @param ndFile
	 * @return
	 */
	@AutoLog(value = "网盘文件表-添加")
	@ApiOperation(value="网盘文件表-添加", notes="网盘文件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdFile ndFile) {
		ndFileService.save(ndFile);
		return Result.OK("添加成功！");
	}
	
	
	/**
	 *   创建文件
	 *
	 * @param createFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-创建文件")
	@ApiOperation(value="网盘文件表-创建文件", notes="网盘文件表-创建文件")
	@PostMapping(value = "/create")
	public Result<?> create(@RequestBody CreateFileDTO createFileDTO) {
		return ndFileService.create(createFileDTO);
	}
	
	/**
	 *   创建文件夹
	 *
	 * @param createFoldDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-创建文件夹")
	@ApiOperation(value="网盘文件表-创建文件夹", notes="网盘文件表-创建文件夹")
	@PostMapping(value = "/createFold")
	public Result<?> createFold(@RequestBody CreateFoldDTO createFoldDto) {
		return ndFileService.createFold(createFoldDto);
	}
	
	/**
	 *   创建文件
	 *
	 * @param createFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-创建文件")
	@ApiOperation(value="网盘文件表-创建文件", notes="网盘文件表-创建文件")
	@PostMapping(value = "/createFile")
	public Result<?> createFile(@Valid @RequestBody CreateFileDTO createFileDTO) {
		return ndFileService.createFile(createFileDTO);
	}
	
	/**
	 *   删除文件
	 *
	 * @param deleteFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-删除文件")
	@ApiOperation(value="网盘文件表-删除文件", notes="网盘文件表-删除文件")
	@PostMapping(value = "/deleteFile")
	public Result deleteFile(@RequestBody DeleteFileDTO deleteFileDto) {

		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        userFileService.deleteUserFile(deleteFileDto.getUserFileId(), userId);

        return Result.OK("删除成功");

    }
	
	/**
	 *   批量删除文件
	 *
	 * @param batchDeleteFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-批量删除文件")
	@ApiOperation(value="网盘文件表-删除文件", notes="网盘文件表-删除文件")
	@PostMapping(value = "/batchdeletefile")
	public Result batchDeleteFile(@RequestBody BatchDeleteFileDTO batchDeleteFileDto) {

		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        String userFileIds = batchDeleteFileDto.getUserFileIds();
        String[] userFileIdList = userFileIds.split(",");
        for (String userFileId : userFileIdList) {
            userFileService.deleteUserFile(userFileId, userId);
            fileDealComp.deleteESByUserFileId(userFileId);
        }

        return Result.OK("批量删除文件成功");

    }
	
	/**
	 *   解压文件
	 *
	 * @param unzipFileDto
	 * @return
	 */
	@AutoLog(value = "网盘文件表-删除文件")
	@ApiOperation(value="网盘文件表-删除文件", notes="网盘文件表-删除文件")
	@PostMapping(value = "/unzipfile")
	public Result unzipFile(@RequestBody UnzipFileDTO unzipFileDto) {

		try {
			String userFileId = unzipFileDto.getUserFileId();
			int unzipMode = unzipFileDto.getUnzipMode();
			String filePath = unzipFileDto.getFilePath();
			NdUserfile userFile = userFileMapper.selectById(userFileId);
			NdFile ndFile = fileMapper.selectById(userFile.getFileid());
	        File destFile = new File(EstarUtils.getStaticPath() + "temp" + File.separator + ndFile.getFileurl());


	        Downloader downloader = ndFactory.getDownloader(ndFile.getStoragetype());
	        DownloadFile downloadFile = new DownloadFile();
	        downloadFile.setFileUrl(ndFile.getFileurl());
	        InputStream inputStream = downloader.getInputStream(downloadFile);

	        try {
	            FileUtils.copyInputStreamToFile(inputStream, destFile);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }


	        String extendName = userFile.getExtendname();

	        String unzipUrl = EstarUtils.getTempFile(ndFile.getFileurl()).getAbsolutePath().replace("." + extendName, "");

	        List<String> fileEntryNameList = new ArrayList<>();

	        try {
	            fileEntryNameList = FileOperation.unzip(destFile, unzipUrl);
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error("解压失败" + e);
	            throw new EstarException(500001, "解压异常：" + e.getMessage());
	        }

	        if (destFile.exists()) {
	            destFile.delete();
	        }

	        if (!fileEntryNameList.isEmpty() && unzipMode == 1) {
	        	NdUserfile estarDir = NdFileUtil.getQiwenDir(userFile.getCreateBy(), userFile.getFilepath(), userFile.getFilename());
	            userFileMapper.insert(estarDir);
	        }
	        for (int i = 0; i < fileEntryNameList.size(); i++){
	            String entryName = fileEntryNameList.get(i);
	            asyncTaskComp.saveUnzipFile(userFile, ndFile, unzipMode, entryName, filePath);

	        }
        } catch (EstarException e) {
            return Result.error(e.getMessage());
        }

        return Result.OK("解压成功");

    }
	
	/**
	 * 查询文件详情
	 *
	 * @param userFileId
	 * @return
	 */
	@AutoLog(value = "网盘文件表-查询文件详情")
	@ApiOperation(value="网盘文件表-查询文件详情", notes="网盘文件表-查询文件详情")
	@GetMapping(value = "/detail")
	public Result<?> queryFileDetail(@RequestParam(name="userFileId", required = true) String userFileId){
        FileDetailVO vo = ndFileService.getFileDetail(userFileId);
        return Result.OK(vo);
	}
	
	
	/**
	 *  编辑
	 *
	 * @param ndFile
	 * @return
	 */
	@AutoLog(value = "网盘文件表-编辑")
	@ApiOperation(value="网盘文件表-编辑", notes="网盘文件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdFile ndFile) {
		ndFileService.updateById(ndFile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘文件表-通过id删除")
	@ApiOperation(value="网盘文件表-通过id删除", notes="网盘文件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndFileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "网盘文件表-批量删除")
	@ApiOperation(value="网盘文件表-批量删除", notes="网盘文件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘文件表-通过id查询")
	@ApiOperation(value="网盘文件表-通过id查询", notes="网盘文件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdFile ndFile = ndFileService.getById(id);
		if(ndFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndFile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdFile ndFile) {
        return super.exportXls(request, ndFile, NdFile.class, "网盘文件表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, NdFile.class);
    }

}
