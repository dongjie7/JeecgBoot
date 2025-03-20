package org.jeecg.modules.estar.nd.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.dto.BatchDownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.DownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.PreviewDTO;
import org.jeecg.modules.estar.nd.dto.UploadFileDTO;
import org.jeecg.modules.estar.nd.entity.NdStorage;
import org.jeecg.modules.estar.nd.service.INdStorageService;
import org.jeecg.modules.estar.nd.vo.UploadFileVo;
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
 * @Description: nd_storage
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Api(tags="nd_storage")
@RestController
@RequestMapping("/nd/ndStorage")
@Slf4j
public class NdStorageController extends JeecgController<NdStorage, INdStorageService> {
	@Autowired
	private INdStorageService ndStorageService;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndStorage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "nd_storage-分页列表查询")
	@ApiOperation(value="nd_storage-分页列表查询", notes="nd_storage-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdStorage ndStorage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdStorage> queryWrapper = QueryGenerator.initQueryWrapper(ndStorage, req.getParameterMap());
		Page<NdStorage> page = new Page<NdStorage>(pageNo, pageSize);
		IPage<NdStorage> pageList = ndStorageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *  极速上传
	 *  校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法"
	 * @param uploadFileDto
	 * @return
	 */
	@AutoLog(value = "nd_storage-极速上传")
	@ApiOperation(value="nd_storage-极速上传", notes="nd_storage-极速上传")
	@GetMapping(value = "/uploadfile")
	 public Result<?>  uploadFileSpeed(UploadFileDTO uploadFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        boolean isCheckSuccess = ndStorageService.checkStorage(userId, uploadFileDto.getTotalSize());
        if (!isCheckSuccess) {
            return Result.error("存储空间不足");
        }
        UploadFileVo uploadFileVo = ndStorageService.uploadFileSpeed(uploadFileDto);
        return Result.OK(uploadFileVo);

    }
	
	/**
	 *  极速上传
	 *  
	 * @param uploadFileDto
	 * @return
	 */
	@AutoLog(value = "nd_storage-上传文件")
	@ApiOperation(value="nd_storage-上传文件", notes="nd_storage-上传文件")
	@PostMapping(value = "/uploadfile")
	 public Result<?>  uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        ndStorageService.uploadFile(request, uploadFileDto, userId);
        UploadFileVo uploadFileVo = new UploadFileVo();
        return Result.OK(uploadFileVo);

    }
	
	/**
	 *   添加
	 *
	 * @param ndStorage
	 * @return
	 */
	@AutoLog(value = "nd_storage-添加")
	@ApiOperation(value="nd_storage-添加", notes="nd_storage-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdStorage ndStorage) {
		ndStorageService.save(ndStorage);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndStorage
	 * @return
	 */
	@AutoLog(value = "nd_storage-编辑")
	@ApiOperation(value="nd_storage-编辑", notes="nd_storage-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdStorage ndStorage) {
		ndStorageService.updateById(ndStorage);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_storage-通过id删除")
	@ApiOperation(value="nd_storage-通过id删除", notes="nd_storage-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndStorageService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "nd_storage-批量删除")
	@ApiOperation(value="nd_storage-批量删除", notes="nd_storage-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndStorageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_storage-通过id查询")
	@ApiOperation(value="nd_storage-通过id查询", notes="nd_storage-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdStorage ndStorage = ndStorageService.getById(id);
		if(ndStorage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndStorage);
	}
	
	/**
	 * 下载文件
	 *
	 * @param downloadFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-下载文件")
	@ApiOperation(value="网盘文件表-下载文件", notes="网盘文件表-下载文件")
	@GetMapping(value = "/downloadfile")
	public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO) {
		ndStorageService.downloadFile(httpServletRequest, httpServletResponse, downloadFileDTO);
		
	}
	
	
	/**
	 * 批量下载文件
	 *
	 * @param batchDownloadFileDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-下载文件")
	@ApiOperation(value="网盘文件表-下载文件", notes="网盘文件表-下载文件")
	@GetMapping(value = "/batchDownloadFile")
	public void batchDownloadFile(HttpServletResponse httpServletResponse, BatchDownloadFileDTO batchDownloadFileDTO) {
		ndStorageService.batchDownloadFile(httpServletResponse, batchDownloadFileDTO);
		
	}
	
	
	/**
	 * 预览文件-用于文件预览
	 *
	 * @param previewDTO
	 * @return
	 */
	@AutoLog(value = "网盘文件表-预览文件")
	@ApiOperation(value="网盘文件表-预览文件", notes="网盘文件表-预览文件")
	@GetMapping(value = "/preview")
	public void preview(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,  PreviewDTO previewDTO) throws IOException {
		ndStorageService.preview(httpServletRequest, httpServletResponse, previewDTO);
		
	}
	
	/**
	 * 通过id查询
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "nd_storage-获取存储信息")
	@ApiOperation(value="nd_storage-获取存储信息", notes="nd_storage-获取存储信息")
	@GetMapping(value = "/getstorage")
	public Result<?> getStorage() {
		NdStorage ndStorage = ndStorageService.getStorage();
		if(ndStorage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndStorage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndStorage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdStorage ndStorage) {
        return super.exportXls(request, ndStorage, NdStorage.class, "nd_storage");
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
        return super.importExcel(request, response, NdStorage.class);
    }

}
