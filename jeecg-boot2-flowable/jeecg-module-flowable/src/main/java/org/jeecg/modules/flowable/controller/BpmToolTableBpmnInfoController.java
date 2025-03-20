package org.jeecg.modules.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.flowable.entity.BpmToolTableBpmn;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.entity.SysForm;
import org.jeecg.modules.flowable.entity.vo.BpmToolTableBpmnVO;
import org.jeecg.modules.flowable.service.IBpmToolTableBpmnInfoService;
import org.jeecg.modules.flowable.service.ISysFormService;
import org.jeecg.modules.flowable.service.impl.HanDongYZCommonServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: WangYuZhou
 * @create: 2022-08-27 15:14
 * @description:
 **/

@Api(tags="系统流程表单")
@RestController
@RequestMapping("/flowable/bpmToolTableBpmnInfo")
@Slf4j
public class BpmToolTableBpmnInfoController {

    @Autowired
    private IBpmToolTableBpmnInfoService bpmToolTableBpmnInfoService;


    @Autowired
    private ISysFormService sysFormService;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    private HanDongYZCommonServiceImpl hanDongYZCommonService;




    /**
     * 分页列表查询
     *
     * @param bpmToolTableBpmnInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "流程表单配置-分页列表查询")
    @ApiOperation(value="流程表单配置-分页列表查询", notes="流程表单配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BpmToolTableBpmn bpmToolTableBpmnInfo,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BpmToolTableBpmn> queryWrapper = QueryGenerator.initQueryWrapper(bpmToolTableBpmnInfo, req.getParameterMap());
        Page<BpmToolTableBpmn> page = new Page<BpmToolTableBpmn>(pageNo, pageSize);
        IPage<BpmToolTableBpmn> pageList = bpmToolTableBpmnInfoService.page(page, queryWrapper);
        List<BpmToolTableBpmn> recordsData = pageList.getRecords();
        ArrayList<BpmToolTableBpmnVO> resultData = new ArrayList<>();
        if(recordsData.size()>0){
            List<SysDictItem> bpmTableCateAll = hanDongYZCommonService.allSysDictItemInfo("bpm_table_cate");

            for(int a=0;a<recordsData.size();a++){
                BpmToolTableBpmnVO bpmToolTableBpmnInfoVO = new BpmToolTableBpmnVO();
                BeanUtils.copyProperties(recordsData.get(a), bpmToolTableBpmnInfoVO);
                //判断表单类型
                bpmToolTableBpmnInfoVO.setTableCate(String.valueOf(recordsData.get(a).getTableCate()));
                if(String.valueOf(recordsData.get(a).getTableCate()).equals("1")){
                    bpmToolTableBpmnInfoVO.setTableCode(String.valueOf(recordsData.get(a).getDesformTableCode()));
                    bpmToolTableBpmnInfoVO.setTableId(String.valueOf(recordsData.get(a).getDesformTableId()));
                    bpmToolTableBpmnInfoVO.setTableBpm(String.valueOf(recordsData.get(a).getDesformTableBpm()));
                    SysForm sysForm = sysFormService.selectSysFormById(String.valueOf(recordsData.get(a).getDesformTableId()));
                    if (Objects.nonNull(sysForm)) {
                        bpmToolTableBpmnInfoVO.setTableName(sysForm.getFormName());
                    }
                    bpmToolTableBpmnInfoVO.setWorkTitle(String.valueOf(recordsData.get(a).getWorkTitle()));
                }else if(String.valueOf(recordsData.get(a).getTableCate()).equals("2")){
                    bpmToolTableBpmnInfoVO.setTableCode(String.valueOf(recordsData.get(a).getOnlineTableCode()));
                    bpmToolTableBpmnInfoVO.setTableId(String.valueOf(recordsData.get(a).getOnlineTableId()));

                    OnlCgformHead onlCgformHead = onlCgformHeadService.getById(String.valueOf(recordsData.get(a).getOnlineTableId()));
                    if (Objects.nonNull(onlCgformHead)) {
                        bpmToolTableBpmnInfoVO.setTableName(onlCgformHead.getTableTxt());
                    }
                    //设置在线表单的流程字段信息
                    OnlCgformField fieldInfo = onlCgformFieldService.getById(String.valueOf(recordsData.get(a).getOnlineTableBpm()));
                    bpmToolTableBpmnInfoVO.setTableBpm(String.valueOf(fieldInfo.getDbFieldName()));

                    List<String> ids = Arrays.asList(String.valueOf(recordsData.get(a).getWorkTitle()).split(","));
                    List<OnlCgformField> fieldInfoAll = onlCgformFieldService.listByIds(ids);
                    String workTitleInfo = fieldInfoAll.stream().filter(Objects::nonNull).map(OnlCgformField::getDbFieldName).collect(Collectors.joining(","));
                    bpmToolTableBpmnInfoVO.setWorkTitle(workTitleInfo);
                }else{
                    bpmToolTableBpmnInfoVO.setTableCate("0");
                    bpmToolTableBpmnInfoVO.setWorkTitle("");
                }
                for(int b=0;b<bpmTableCateAll.size();b++){
                    if(String.valueOf(recordsData.get(a).getTableCate()).equals(String.valueOf(bpmTableCateAll.get(b).getItemValue()))){
                        bpmToolTableBpmnInfoVO.setTableCate(String.valueOf(bpmTableCateAll.get(b).getItemText()));
                    }
                }
                resultData.add(bpmToolTableBpmnInfoVO);
            }
        }
        Page<BpmToolTableBpmnVO> resultInfo = new Page<>();
        BeanUtils.copyProperties(pageList, resultInfo);
        resultInfo.setRecords(resultData);
        return Result.OK(resultInfo);
    }


    /**
     * 分页列表查询--- 针对某个流程查询
     *
     * @param bpmToolTableBpmnInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "流程表单配置-分页列表查询")
    @ApiOperation(value="流程表单配置-分页列表查询", notes="流程表单配置-分页列表查询")
    @GetMapping(value = "/listByBpmId")
    public Result<?> queryPageList(BpmToolTableBpmn bpmToolTableBpmnInfo,
                                   @RequestParam(name="bpmId", defaultValue="") String bpmId,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BpmToolTableBpmn> queryWrapper = QueryGenerator.initQueryWrapper(bpmToolTableBpmnInfo, req.getParameterMap());
        queryWrapper.eq("bpm_id",bpmId);
        Page<BpmToolTableBpmn> page = new Page<BpmToolTableBpmn>(pageNo, pageSize);
        IPage<BpmToolTableBpmn> pageList = bpmToolTableBpmnInfoService.page(page, queryWrapper);
        List<BpmToolTableBpmn> recordsData = pageList.getRecords();
        ArrayList<BpmToolTableBpmnVO> resultData = new ArrayList<>();
        if(recordsData.size()>0){
            List<SysDictItem> bpmTableCateAll = hanDongYZCommonService.allSysDictItemInfo("bpm_table_cate");

            for(int a=0;a<recordsData.size();a++){
                BpmToolTableBpmnVO bpmToolTableBpmnInfoVO = new BpmToolTableBpmnVO();
                BeanUtils.copyProperties(recordsData.get(a), bpmToolTableBpmnInfoVO);
                //判断表单类型
                bpmToolTableBpmnInfoVO.setTableCate(String.valueOf(recordsData.get(a).getTableCate()));
                if(String.valueOf(recordsData.get(a).getTableCate()).equals("1")){
                    bpmToolTableBpmnInfoVO.setTableCode(String.valueOf(recordsData.get(a).getDesformTableCode()));
                    bpmToolTableBpmnInfoVO.setTableId(String.valueOf(recordsData.get(a).getDesformTableId()));
                    bpmToolTableBpmnInfoVO.setTableBpm(String.valueOf(recordsData.get(a).getDesformTableBpm()));
                    SysForm sysForm = sysFormService.selectSysFormById(String.valueOf(recordsData.get(a).getDesformTableId()));
                    if (Objects.nonNull(sysForm)) {
                        bpmToolTableBpmnInfoVO.setTableName(sysForm.getFormName());
                    }
                    bpmToolTableBpmnInfoVO.setWorkTitle(String.valueOf(recordsData.get(a).getWorkTitle()));
                }else if(String.valueOf(recordsData.get(a).getTableCate()).equals("2")){
                    bpmToolTableBpmnInfoVO.setTableCode(String.valueOf(recordsData.get(a).getOnlineTableCode()));
                    bpmToolTableBpmnInfoVO.setTableId(String.valueOf(recordsData.get(a).getOnlineTableId()));

                    OnlCgformHead onlCgformHead = onlCgformHeadService.getById(String.valueOf(recordsData.get(a).getOnlineTableId()));
                    if (Objects.nonNull(onlCgformHead)) {
                        bpmToolTableBpmnInfoVO.setTableName(onlCgformHead.getTableTxt());
                    }
                    //设置在线表单的流程字段信息
                    OnlCgformField fieldInfo = onlCgformFieldService.getById(String.valueOf(recordsData.get(a).getOnlineTableBpm()));
                    bpmToolTableBpmnInfoVO.setTableBpm(String.valueOf(fieldInfo.getDbFieldName()));

                    List<String> ids = Arrays.asList(String.valueOf(recordsData.get(a).getWorkTitle()).split(","));
                    List<OnlCgformField> fieldInfoAll = onlCgformFieldService.listByIds(ids);
                    String workTitleInfo = fieldInfoAll.stream().filter(Objects::nonNull).map(OnlCgformField::getDbFieldTxt).collect(Collectors.joining(","));
                    bpmToolTableBpmnInfoVO.setWorkTitle(workTitleInfo);
                }else{
                    bpmToolTableBpmnInfoVO.setTableCate("0");
                    bpmToolTableBpmnInfoVO.setWorkTitle("");
                }
                for(int b=0;b<bpmTableCateAll.size();b++){
                    if(String.valueOf(recordsData.get(a).getTableCate()).equals(String.valueOf(bpmTableCateAll.get(b).getItemValue()))){
                        bpmToolTableBpmnInfoVO.setTableCate(String.valueOf(bpmTableCateAll.get(b).getItemText()));
                    }
                }
                resultData.add(bpmToolTableBpmnInfoVO);
            }
        }
        Page<BpmToolTableBpmnVO> resultInfo = new Page<>();
        BeanUtils.copyProperties(pageList, resultInfo);
        resultInfo.setRecords(resultData);
        return Result.OK(resultInfo);
    }

    /**
     *   添加
     *
     * @param bpmToolTableBpmnInfoVO
     * @return
     */
    @AutoLog(value = "流程表单配置-添加")
    @ApiOperation(value="流程表单配置-添加", notes="流程表单配置-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BpmToolTableBpmnVO bpmToolTableBpmnInfoVO,HttpServletRequest req) {

        //查询当前的流程id
        QueryWrapper<BpmToolTableBpmn> bpmToolTableBpmnInfoQueryWrapper = new QueryWrapper<>();
        bpmToolTableBpmnInfoQueryWrapper.eq("bpm_id",String.valueOf(bpmToolTableBpmnInfoVO.getBpmId()));
        List<BpmToolTableBpmn> datalist = bpmToolTableBpmnInfoService.list(bpmToolTableBpmnInfoQueryWrapper);

        if(datalist.size()>0){
            return Result.error("当前流程已配置表单,无需再次配置",null);
        }

        BpmToolTableBpmn bpmToolTableBpmnInfo = new BpmToolTableBpmn();
        BeanUtils.copyProperties(bpmToolTableBpmnInfoVO, bpmToolTableBpmnInfo);
        if(String.valueOf(bpmToolTableBpmnInfoVO.getTableCate()).equals("1")){
            bpmToolTableBpmnInfo.setDesformTableId(String.valueOf(bpmToolTableBpmnInfoVO.getTableId()));
            bpmToolTableBpmnInfo.setDesformTableCode(String.valueOf(bpmToolTableBpmnInfoVO.getTableCode()));
            bpmToolTableBpmnInfo.setDesformTableBpm(String.valueOf(bpmToolTableBpmnInfoVO.getTableBpm()));
        }else if(String.valueOf(bpmToolTableBpmnInfoVO.getTableCate()).equals("2")){
            bpmToolTableBpmnInfo.setOnlineTableId(String.valueOf(bpmToolTableBpmnInfoVO.getTableId()));
            bpmToolTableBpmnInfo.setOnlineTableCode(String.valueOf(bpmToolTableBpmnInfoVO.getTableCode()));
            bpmToolTableBpmnInfo.setOnlineTableBpm(String.valueOf(bpmToolTableBpmnInfoVO.getTableBpm()));
        }else{
            bpmToolTableBpmnInfo.setTableCate("0");
        }
        bpmToolTableBpmnInfo.setCreateTime(new Date());
        bpmToolTableBpmnInfo.setPublishState("1");
        bpmToolTableBpmnInfoService.save(bpmToolTableBpmnInfo);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param bpmToolTableBpmnInfoVO
     * @return
     */
    @AutoLog(value = "流程表单配置-编辑")
    @ApiOperation(value="流程表单配置-编辑", notes="流程表单配置-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BpmToolTableBpmnVO bpmToolTableBpmnInfoVO,HttpServletRequest req) {

        BpmToolTableBpmn bpmToolTableBpmnInfo = new BpmToolTableBpmn();
        BeanUtils.copyProperties(bpmToolTableBpmnInfoVO, bpmToolTableBpmnInfo);
        if(String.valueOf(bpmToolTableBpmnInfoVO.getTableCate()).equals("1")){
            bpmToolTableBpmnInfo.setDesformTableId(String.valueOf(bpmToolTableBpmnInfoVO.getTableId()));
            bpmToolTableBpmnInfo.setDesformTableCode(String.valueOf(bpmToolTableBpmnInfoVO.getTableCode()));
            bpmToolTableBpmnInfo.setDesformTableBpm(String.valueOf(bpmToolTableBpmnInfoVO.getTableBpm()));
        }else if(String.valueOf(bpmToolTableBpmnInfoVO.getTableCate()).equals("2")){
            bpmToolTableBpmnInfo.setOnlineTableId(String.valueOf(bpmToolTableBpmnInfoVO.getTableId()));
            bpmToolTableBpmnInfo.setOnlineTableCode(String.valueOf(bpmToolTableBpmnInfoVO.getTableCode()));
            bpmToolTableBpmnInfo.setOnlineTableBpm(String.valueOf(bpmToolTableBpmnInfoVO.getTableBpm()));
        }else{
            bpmToolTableBpmnInfo.setTableCate("0");
        }
        bpmToolTableBpmnInfo.setUpdateTime(new Date());

        bpmToolTableBpmnInfoService.updateById(bpmToolTableBpmnInfo);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流程表单配置-通过id删除")
    @ApiOperation(value="流程表单配置-通过id删除", notes="流程表单配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        bpmToolTableBpmnInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "流程表单配置-通过id查询")
    @ApiOperation(value="流程表单配置-通过id查询", notes="流程表单配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        BpmToolTableBpmn bpmToolTableBpmnInfo = bpmToolTableBpmnInfoService.getById(id);
        if(bpmToolTableBpmnInfo==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bpmToolTableBpmnInfo);
    }

}
