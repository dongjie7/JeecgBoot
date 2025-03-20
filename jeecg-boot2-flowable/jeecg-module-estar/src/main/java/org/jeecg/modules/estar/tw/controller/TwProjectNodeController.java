package org.jeecg.modules.estar.tw.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.estar.tw.entity.TwProjectNode;
import org.jeecg.modules.estar.tw.service.ITwProjectNodeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 项目端节点表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="项目端节点表")
@RestController
@RequestMapping("/tw/twProjectNode")
@Slf4j
public class TwProjectNodeController extends JeecgController<TwProjectNode, ITwProjectNodeService> {
	@Autowired
	private ITwProjectNodeService twProjectNodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectNode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目端节点表-分页列表查询")
	@ApiOperation(value="项目端节点表-分页列表查询", notes="项目端节点表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectNode twProjectNode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectNode> queryWrapper = QueryGenerator.initQueryWrapper(twProjectNode, req.getParameterMap());
		Page<TwProjectNode> page = new Page<TwProjectNode>(pageNo, pageSize);
		IPage<TwProjectNode> pageList = twProjectNodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param twProjectNode
	 * @return
	 */
	@AutoLog(value = "项目端节点表-添加")
	@ApiOperation(value="项目端节点表-添加", notes="项目端节点表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TwProjectNode twProjectNode) {
		twProjectNodeService.save(twProjectNode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param twProjectNode
	 * @return
	 */
	@AutoLog(value = "项目端节点表-编辑")
	@ApiOperation(value="项目端节点表-编辑", notes="项目端节点表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TwProjectNode twProjectNode) {
		twProjectNodeService.updateById(twProjectNode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目端节点表-通过id删除")
	@ApiOperation(value="项目端节点表-通过id删除", notes="项目端节点表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		twProjectNodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目端节点表-批量删除")
	@ApiOperation(value="项目端节点表-批量删除", notes="项目端节点表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectNodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目端节点表-通过id查询")
	@ApiOperation(value="项目端节点表-通过id查询", notes="项目端节点表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectNode twProjectNode = twProjectNodeService.getById(id);
		if(twProjectNode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectNode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectNode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectNode twProjectNode) {
        return super.exportXls(request, twProjectNode, TwProjectNode.class, "项目端节点表");
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
        return super.importExcel(request, response, TwProjectNode.class);
    }

}
