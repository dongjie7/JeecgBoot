package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.dto.CheckEndTimeDTO;
import org.jeecg.modules.estar.nd.dto.CheckExtractionCodeDTO;
import org.jeecg.modules.estar.nd.dto.ShareFileDTO;
import org.jeecg.modules.estar.nd.dto.ShareFileListDTO;
import org.jeecg.modules.estar.nd.dto.ShareListDTO;
import org.jeecg.modules.estar.nd.dto.ShareTypeDTO;
import org.jeecg.modules.estar.nd.entity.NdShare;
import org.jeecg.modules.estar.nd.service.INdShareService;
import org.jeecg.modules.estar.nd.service.INdSharefileService;
import org.jeecg.modules.estar.nd.vo.ShareFileListVO;
import org.jeecg.modules.estar.nd.vo.ShareFileVO;
import org.jeecg.modules.estar.nd.vo.ShareListVO;
import org.jeecg.modules.estar.nd.vo.ShareTypeVO;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 网盘分享表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Api(tags="网盘分享表")
@RestController
@RequestMapping("/nd/ndShare")
@Slf4j
public class NdShareController extends JeecgController<NdShare, INdShareService> {
	@Autowired
	private INdShareService ndShareService;
	
	@Resource
    INdSharefileService shareFileService;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndShare
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	/*@AutoLog(value = "网盘分享表-分页列表查询")
	@ApiOperation(value="网盘分享表-分页列表查询", notes="网盘分享表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdShare ndShare,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdShare> queryWrapper = QueryGenerator.initQueryWrapper(ndShare, req.getParameterMap());
		Page<NdShare> page = new Page<NdShare>(pageNo, pageSize);
		IPage<NdShare> pageList = ndShareService.page(page, queryWrapper);
		return Result.OK(pageList);
	}*/
	
	/**
	 * 分享文件
	 *
	 * @param shareSecretDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-分享文件")
	@ApiOperation(value="网盘分享表-分享文件", notes="网盘分享表-分享文件")
	@PostMapping(value = "/shareFile")
	public Result<?> shareFile(@RequestBody ShareFileDTO  shareSecretDTO) {
		ShareFileVO shareSecretVO = new ShareFileVO();
		shareSecretVO = ndShareService.shareFile(shareSecretDTO);
		return Result.OK(shareSecretVO);
	}
	
	/**
	 * 查看已分享列表
	 *
	 * @param shareListDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-查看已分享列表")
	@ApiOperation(value="网盘分享表-查看已分享列表", notes="网盘分享表-查看已分享列表")
	@GetMapping(value = "/shareList")
	public Result<?> shareList(ShareListDTO shareListDTO) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		List<ShareListVO> shareList = ndShareService.selectShareList(shareListDTO, loginUser.getUsername());
		return Result.OK(shareList);
	}
	
	/**
	 * 分享文件列表
	 *
	 * @param shareFileListBySecretDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-分享文件列表")
	@ApiOperation(value="网盘分享表-分享文件列表", notes="网盘分享表-分享文件列表")
	@GetMapping(value = "/sharefileList")
	public Result<?> shareFileList(ShareFileListDTO shareFileListBySecretDTO) {
		 String shareBatchNum = shareFileListBySecretDTO.getShareBatchNum();
	        String shareFilePath = shareFileListBySecretDTO.getShareFilePath();
	        List<ShareFileListVO> list = shareFileService.selectShareFileList(shareBatchNum, shareFilePath);
	        for (ShareFileListVO shareFileListVO : list) {
	            shareFileListVO.setShareFilePath(shareFilePath);
	        }
	        return Result.OK(list);
	}
	
	/**
	 * 分享类型,可用此接口判断是否需要提取码
	 *
	 * @param shareTypeDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-查看已分享列表")
	@ApiOperation(value="网盘分享表-分享类型", notes="网盘分享表-可用此接口判断是否需要提取码")
	@GetMapping(value = "/sharetype")
	public Result<?> shareType(ShareTypeDTO shareTypeDTO) {
		 LambdaQueryWrapper<NdShare> lambdaQueryWrapper = new LambdaQueryWrapper<>();
	        lambdaQueryWrapper.eq(NdShare::getSharebatchnum, shareTypeDTO.getShareBatchNum());
	        NdShare share = ndShareService.getOne(lambdaQueryWrapper);
	        ShareTypeVO shareTypeVO = new ShareTypeVO();
	        shareTypeVO.setShareType(share.getSharetype());
	        return Result.OK(shareTypeVO);
	}
	
	/**
	 * 校验提取码
	 *
	 * @param checkExtractionCodeDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-校验提取码")
	@ApiOperation(value="网盘分享表-校验提取码", notes="网盘分享表-校验提取码")
	@GetMapping(value = "/checkextractioncode")
	public Result<?> checkExtractionCode(CheckExtractionCodeDTO checkExtractionCodeDTO) {
		LambdaQueryWrapper<NdShare> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdShare::getSharebatchnum, checkExtractionCodeDTO.getShareBatchNum())
                .eq(NdShare::getExtractioncode, checkExtractionCodeDTO.getExtractionCode());
        List<NdShare> list = ndShareService.list(lambdaQueryWrapper);
        if (list.isEmpty()) {
            return Result.error("校验失败");
        } else {
            return Result.OK("校验成功");
        }
	}
	
	/**
	 * 校验过期时间
	 *
	 * @param checkEndTimeDTO
	 * @return
	 */
	@AutoLog(value = "网盘分享表-校验过期时间")
	@ApiOperation(value="网盘分享表-校验过期时间", notes="网盘分享表-校验过期时间")
	@GetMapping(value = "/checkendtime")
	public Result<?> checkEndTime(CheckEndTimeDTO checkEndTimeDTO) {
		LambdaQueryWrapper<NdShare> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdShare::getSharebatchnum, checkEndTimeDTO.getShareBatchNum());
        NdShare share = ndShareService.getOne(lambdaQueryWrapper);
        if (share == null) {
            return Result.error("文件不存在！");
        }
        Date endTime = share.getEndtime();
        if (new Date().after(endTime))  {
            return Result.error("分享已过期");
        } else {
            return Result.OK("分享有效");
        }
	}
	
	/**
	 *   添加
	 *
	 * @param ndShare
	 * @return
	 */
	@AutoLog(value = "网盘分享表-添加")
	@ApiOperation(value="网盘分享表-添加", notes="网盘分享表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdShare ndShare) {
		ndShareService.save(ndShare);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndShare
	 * @return
	 */
	@AutoLog(value = "网盘分享表-编辑")
	@ApiOperation(value="网盘分享表-编辑", notes="网盘分享表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdShare ndShare) {
		ndShareService.updateById(ndShare);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘分享表-通过id删除")
	@ApiOperation(value="网盘分享表-通过id删除", notes="网盘分享表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndShareService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "网盘分享表-批量删除")
	@ApiOperation(value="网盘分享表-批量删除", notes="网盘分享表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndShareService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网盘分享表-通过id查询")
	@ApiOperation(value="网盘分享表-通过id查询", notes="网盘分享表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdShare ndShare = ndShareService.getById(id);
		if(ndShare==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndShare);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndShare
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdShare ndShare) {
        return super.exportXls(request, ndShare, NdShare.class, "网盘分享表");
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
        return super.importExcel(request, response, NdShare.class);
    }

}
