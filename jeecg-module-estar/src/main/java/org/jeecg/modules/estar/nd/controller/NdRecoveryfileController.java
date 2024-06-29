package org.jeecg.modules.estar.nd.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.AsyncTaskComp;
import org.jeecg.modules.estar.nd.dto.BatchDeleteRecoveryFileDTO;
import org.jeecg.modules.estar.nd.dto.DeleteRecoveryFileDTO;
import org.jeecg.modules.estar.nd.dto.RestoreFileDTO;
import org.jeecg.modules.estar.nd.entity.NdRecoveryfile;
import org.jeecg.modules.estar.nd.service.INdRecoveryfileService;
import org.jeecg.modules.estar.nd.vo.RecoveryFileListVo;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: nd_recoveryfile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Api(tags="nd_recoveryfile")
@RestController
@RequestMapping("/nd/ndRecoveryfile")
@Slf4j
public class NdRecoveryfileController extends JeecgController<NdRecoveryfile, INdRecoveryfileService> {
	@Autowired
	private INdRecoveryfileService ndRecoveryfileService;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Resource
    AsyncTaskComp asyncTaskComp;
	
	/**
	 * 分页列表查询
	 *
	 * @param ndRecoveryfile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	/*@AutoLog(value = "nd_recoveryfile-分页列表查询")
	@ApiOperation(value="nd_recoveryfile-分页列表查询", notes="nd_recoveryfile-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NdRecoveryfile ndRecoveryfile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NdRecoveryfile> queryWrapper = QueryGenerator.initQueryWrapper(ndRecoveryfile, req.getParameterMap());
		Page<NdRecoveryfile> page = new Page<NdRecoveryfile>(pageNo, pageSize);
		IPage<NdRecoveryfile> pageList = ndRecoveryfileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}*/

	 /**
	  * 回收列表查询
	  * @return
	  */
	@AutoLog(value = "nd_recoveryfile-回收列表查询")
	@ApiOperation(value="nd_recoveryfile-回收列表查询", notes="nd_recoveryfile-回收列表查询")
	@GetMapping(value = "/list")
	public Result<?> getRecoveryFileList()  {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		List<RecoveryFileListVo> recoveryFileList = ndRecoveryfileService.selectRecoveryFileList(loginUser.getUsername());
		return Result.OK(recoveryFileList);
	}
	
	/**
	 *   还原文件
	 *
	 * @param restoreFileDto
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-还原文件")
	@ApiOperation(value="nd_recoveryfile-还原文件", notes="nd_recoveryfile-还原文件")
	@PostMapping(value = "/restorefile")
	public Result<?> restoreFile(@RequestBody RestoreFileDTO restoreFileDto) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		ndRecoveryfileService.restorefile(restoreFileDto.getDeleteBatchNum(), restoreFileDto.getFilePath(), loginUser.getUsername());
        return Result.OK("还原成功！");
	}
	
	/**
	 *   删除回收文件
	 *
	 * @param deleteRecoveryFileDTO
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-删除回收文件")
	@ApiOperation(value="nd_recoveryfile-删除回收文件", notes="nd_recoveryfile-删除回收文件")
	@PostMapping(value = "/deleterecoveryfile")
	public Result<?> deleteRecoveryFile(@RequestBody DeleteRecoveryFileDTO deleteRecoveryFileDTO) {
		NdRecoveryfile recoveryFile = ndRecoveryfileService.getOne(new QueryWrapper<NdRecoveryfile>().lambda().eq(NdRecoveryfile::getUserfileid, deleteRecoveryFileDTO.getUserFileId()));

        asyncTaskComp.deleteUserFile(recoveryFile.getUserfileid());

        ndRecoveryfileService.removeById(recoveryFile.getId());
        return Result.OK("删除成功");
	}
	
	/**
	 *   批量删除回收文件
	 *
	 * @param batchDeleteRecoveryFileDTO
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-批量删除回收文件")
	@ApiOperation(value="nd_recoveryfile-批量删除回收文件", notes="nd_recoveryfile-批量删除回收文件")
	@PostMapping(value = "/batchdelete")
	public Result<?> batchDeleteRecoveryFile(@RequestBody BatchDeleteRecoveryFileDTO batchDeleteRecoveryFileDTO) {
        String userFileIds = batchDeleteRecoveryFileDTO.getUserFileIds();
        String[] userFileIdList = userFileIds.split(",");
        for (String userFileId : userFileIdList) {
        	NdRecoveryfile recoveryFile = ndRecoveryfileService.getOne(new QueryWrapper<NdRecoveryfile>().lambda().eq(NdRecoveryfile::getUserfileid, userFileId));

            if (recoveryFile != null) {
                asyncTaskComp.deleteUserFile(recoveryFile.getUserfileid());

                ndRecoveryfileService.removeById(recoveryFile.getId());
            }

        }
        return Result.OK("批量删除成功");
    }

	
	/**
	 *   添加
	 *
	 * @param ndRecoveryfile
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-添加")
	@ApiOperation(value="nd_recoveryfile-添加", notes="nd_recoveryfile-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NdRecoveryfile ndRecoveryfile) {
		ndRecoveryfileService.save(ndRecoveryfile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ndRecoveryfile
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-编辑")
	@ApiOperation(value="nd_recoveryfile-编辑", notes="nd_recoveryfile-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NdRecoveryfile ndRecoveryfile) {
		ndRecoveryfileService.updateById(ndRecoveryfile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-通过id删除")
	@ApiOperation(value="nd_recoveryfile-通过id删除", notes="nd_recoveryfile-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ndRecoveryfileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-批量删除")
	@ApiOperation(value="nd_recoveryfile-批量删除", notes="nd_recoveryfile-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ndRecoveryfileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "nd_recoveryfile-通过id查询")
	@ApiOperation(value="nd_recoveryfile-通过id查询", notes="nd_recoveryfile-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NdRecoveryfile ndRecoveryfile = ndRecoveryfileService.getById(id);
		if(ndRecoveryfile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ndRecoveryfile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ndRecoveryfile
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NdRecoveryfile ndRecoveryfile) {
        return super.exportXls(request, ndRecoveryfile, NdRecoveryfile.class, "nd_recoveryfile");
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
        return super.importExcel(request, response, NdRecoveryfile.class);
    }

}
