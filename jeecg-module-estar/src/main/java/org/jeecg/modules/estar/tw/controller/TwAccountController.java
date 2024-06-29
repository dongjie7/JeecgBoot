package org.jeecg.modules.estar.tw.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
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
 * @Description: 团队成员
 * @Author: nbacheng
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Api(tags="团队成员")
@RestController
@RequestMapping("/tw/twAccount")
@Slf4j
public class TwAccountController extends JeecgController<TwAccount, ITwAccountService> {
	@Autowired
	private ITwAccountService twAccountService;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "团队成员-分页列表查询")
	@ApiOperation(value="团队成员-分页列表查询", notes="团队成员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwAccount twAccount,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwAccount> queryWrapper = QueryGenerator.initQueryWrapper(twAccount, req.getParameterMap());
		Page<TwAccount> page = new Page<TwAccount>(pageNo, pageSize);
		IPage<TwAccount> pageList = twAccountService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twAccount
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@AutoLog(value = "团队成员-添加")
	@ApiOperation(value="团队成员-添加", notes="团队成员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwAccount twAccount)  {
		return twAccountService.accountAdd(twAccount);
	}
	
	/**
	 *  编辑
	 *
	 * @param twAccount
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@AutoLog(value = "团队成员-编辑")
	@ApiOperation(value="团队成员-编辑", notes="团队成员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwAccount twAccount) throws IllegalAccessException, InvocationTargetException {
		return twAccountService.accountEdit(twAccount);
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "团队成员-通过id删除")
	@ApiOperation(value="团队成员-通过id删除", notes="团队成员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twAccountService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "团队成员-批量删除")
	@ApiOperation(value="团队成员-批量删除", notes="团队成员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "团队成员-通过id查询")
	@ApiOperation(value="团队成员-通过id查询", notes="团队成员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwAccount twAccount = twAccountService.getById(id);
		if(twAccount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twAccount);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twAccount
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwAccount twAccount) {
        return super.exportXls(request, twAccount, TwAccount.class, "团队成员");
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
        return super.importExcel(request, response, TwAccount.class);
    }

}
