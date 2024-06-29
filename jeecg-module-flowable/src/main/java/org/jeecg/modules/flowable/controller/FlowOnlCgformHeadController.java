package org.jeecg.modules.flowable.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.b;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlineJoinQueryService;
import org.jeecg.modules.online.cgform.service.IOnlineService;
import org.jeecg.modules.online.config.exception.BusinessException;

import org.jeecg.modules.flowable.entity.FlowOnlCgformHead;
import org.jeecg.modules.flowable.entity.vo.OnlCgformDataVo;
import org.jeecg.modules.flowable.service.IFlowOnlCgformHeadService;
import org.jeecg.modules.flowable.utils.onlineUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: flow_onl_cgform_head
 * @Author: nbacheng
 * @Date:   2022-10-22
 * @Version: V1.0
 */
@Api(tags="flow_onl_cgform_head")
@RestController
@RequestMapping("/flowable/onlCgformHead")
@Slf4j
public class FlowOnlCgformHeadController extends JeecgController<FlowOnlCgformHead, IFlowOnlCgformHeadService> {
		
	@Autowired
	private IFlowOnlCgformHeadService flowOnlCgformHeadService;
	
	@Autowired
	private IOnlCgformHeadService onlCgformHeadService;
	
	@Autowired
	private IOnlineService onlineService;
	
	@Autowired
	IOnlineJoinQueryService onlineJoinQueryService;
	
	@Autowired
	private IOnlCgformFieldService onlCgformFieldService;
	
	
	/**
	 * 分页列表查询
	 *
	 * @param flowOnlCgformHead
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-分页列表查询")
	@ApiOperation(value="flow_onl_cgform_head-分页列表查询", notes="flow_onl_cgform_head-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FlowOnlCgformHead flowOnlCgformHead,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FlowOnlCgformHead> queryWrapper = QueryGenerator.initQueryWrapper(flowOnlCgformHead, req.getParameterMap());
		queryWrapper.orderByDesc("create_time");
		Page<FlowOnlCgformHead> page = new Page<FlowOnlCgformHead>(pageNo, pageSize);
		IPage<FlowOnlCgformHead> pageList = flowOnlCgformHeadService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param flowOnlCgformHead
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-添加")
	@ApiOperation(value="flow_onl_cgform_head-添加", notes="flow_onl_cgform_head-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlowOnlCgformHead flowOnlCgformHead) {
		flowOnlCgformHeadService.save(flowOnlCgformHead);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param flowOnlCgformHead
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-编辑")
	@ApiOperation(value="flow_onl_cgform_head-编辑", notes="flow_onl_cgform_head-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlowOnlCgformHead flowOnlCgformHead) {
		flowOnlCgformHeadService.updateById(flowOnlCgformHead);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-通过id删除")
	@ApiOperation(value="flow_onl_cgform_head-通过id删除", notes="flow_onl_cgform_head-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flowOnlCgformHeadService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-批量删除")
	@ApiOperation(value="flow_onl_cgform_head-批量删除", notes="flow_onl_cgform_head-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flowOnlCgformHeadService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-通过id查询")
	@ApiOperation(value="flow_onl_cgform_head-通过id查询", notes="flow_onl_cgform_head-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FlowOnlCgformHead flowOnlCgformHead = flowOnlCgformHeadService.getById(id);
		if(flowOnlCgformHead==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowOnlCgformHead);
	}
	
	/**
	 * 通过formId查询
	 *
	 * @param formId
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-通过formId查询")
	@ApiOperation(value="flow_onl_cgform_head-通过formId查询", notes="flow_onl_cgform_head-通过formId查询")
	@GetMapping(value = "/queryByFormId/{formId}")
	public Result<?> queryByFormId(@PathVariable("formId")  String formId) {
		Map<String, Object> flowOnlCgformHeadMap = flowOnlCgformHeadService.getOnlCgformHeadByFormId(formId);
		if(flowOnlCgformHeadMap==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(flowOnlCgformHeadMap);
	}
	
	@AutoLog(value = "getColumns-通过code查询获取online表单列表信息")
	@ApiOperation(value="getColumns-通过code查询获取online表单列表信息", notes="getColumns-通过code查询获取online表单列表信息")
	@GetMapping({"/getColumns/{code}"})
	public Result<b> getColumns(@PathVariable("code") String code) {
        Result result = new Result();
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.onlCgformHeadService.getById(code);
        if (onlCgformHead == null) {
        	result.error500("实体不存在");
            return result;
        } else {
            LoginUser loginuser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            b onlComplexModel = this.onlineService.queryOnlineConfig(onlCgformHead, loginuser.getUsername());
            onlComplexModel.setIsDesForm(onlCgformHead.getIsDesForm());
            onlComplexModel.setDesFormCode(onlCgformHead.getDesFormCode());
            result.setResult(onlComplexModel);
            result.setOnlTable(onlCgformHead.getTableName());
            return result;
        }
    }
	
	@AutoLog(value = "getData-通过code查询获取online表单数据")
	@ApiOperation(value="getData-通过code查询获取online表单数据", notes="getData-通过code查询获取online表单数据")
	@GetMapping({"/getData/{code}"})
	public Result<Map<String, Object>> getData(@PathVariable("code") String code, HttpServletRequest request) {
		Result result = new Result();
		OnlCgformHead onlCgformHead = (OnlCgformHead) this.onlCgformHeadService.getById(code);
		if (onlCgformHead == null) {
			result.error500("实体不存在");
			return result;
		} else {
			Map map = null;

			try {
				Map objectmap = onlineUtils.objectHashMap(request);
				boolean bJoinQuery = onlineUtils.isJoinQuery(onlCgformHead);
				if (bJoinQuery) {
					map = this.onlineJoinQueryService.pageList(onlCgformHead, objectmap);
				} else {
					map = this.onlCgformFieldService.queryAutolistPage(onlCgformHead, objectmap, (List) null);
				}

				this.getEnhanceList(onlCgformHead, map);
				result.setResult(map);
			} catch (Exception except) {
				log.error(except.getMessage(), except);
				result.error500("数据库查询失败，" + except.getMessage());
			}

			result.setOnlTable(onlCgformHead.getTableName());
			return result;
		}
	}

	
	@AutoLog(value = "getFormItem-通过code查询获取子表单数据")
	@ApiOperation(value="getFormItem-通过code查询获取子表单数据", notes="getFormItem-通过code查询获取子表单数据")
	@GetMapping({"/getFormItem/{code}"})
	public Result<?> getFormItem(@PathVariable("code") String code, HttpServletRequest var2) {
		OnlCgformHead onlCgformHead = (OnlCgformHead) this.onlCgformHeadService.getById(code);
		if (onlCgformHead == null) {
			Result.error("表不存在");
		}

		Result result = new Result();
		LoginUser loginuser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		JSONObject formItemJsonObject = this.onlineService.queryOnlineFormItem(onlCgformHead, loginuser.getUsername());
		result.setResult(onlineUtils.resultOnlineFormItem(formItemJsonObject));
		result.setOnlTable(onlCgformHead.getTableName());
		return result;
	}
	
	/**
	 *   添加
	 *
	 * @param flowOnlCgformHead
	 * @return
	 */
	@AutoLog(value = "flow_onl_cgform_head-保存")
	@ApiOperation(value="flow_onl_cgform_head-保存", notes="flow_onl_cgform_head-保存")
	@PostMapping(value = "/save")
	public Result<?> save(@RequestBody OnlCgformDataVo onlCgformDataVo) {
		try {
			flowOnlCgformHeadService.save(onlCgformDataVo);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Result.OK("保存成功！");
	}

    /**
    * 导出excel
    *
    * @param request
    * @param flowOnlCgformHead
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FlowOnlCgformHead flowOnlCgformHead) {
        return super.exportXls(request, flowOnlCgformHead, FlowOnlCgformHead.class, "test_onl_cgform_head");
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
        return super.importExcel(request, response, FlowOnlCgformHead.class);
    }
    
    private void getEnhanceList(OnlCgformHead onlCgformHead, Map<String, Object> map) throws BusinessException {
		List recordlist = (List) map.get("records");
		this.onlCgformHeadService.executeEnhanceList(onlCgformHead, "query", recordlist);
	}

}
