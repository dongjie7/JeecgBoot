package org.jeecg.modules.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.flowable.apithird.business.entity.FlowMyBusiness;
import org.jeecg.modules.flowable.apithird.business.service.impl.FlowMyBusinessServiceImpl;
import org.jeecg.modules.flowable.apithird.entity.ActStatus;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.entity.FlowCategory.Category;
import org.jeecg.modules.flowable.apithird.service.FlowCallBackServiceI;
import org.jeecg.modules.flowable.apithird.service.FlowCommonService;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.jeecg.modules.flowable.common.constant.ProcessConstants;
import org.jeecg.modules.flowable.common.enums.FlowComment;
import org.jeecg.modules.flowable.domain.dto.FlowNextDto;
import org.jeecg.modules.flowable.domain.dto.FlowProcDefDto;
import org.jeecg.modules.flowable.entity.FlowDeployOnline;
import org.jeecg.modules.flowable.entity.FlowMyOnline;
import org.jeecg.modules.flowable.entity.SysCustomForm;
import org.jeecg.modules.flowable.entity.SysDeployForm;
import org.jeecg.modules.flowable.entity.SysForm;
import org.jeecg.modules.flowable.factory.FlowServiceFactory;
import org.jeecg.modules.flowable.flow.ExpressionCmd;
import org.jeecg.modules.flowable.flow.FindNextNodeUtil;
import org.jeecg.modules.flowable.flow.FlowableUtils;
import org.jeecg.modules.flowable.mapper.SysDeployFormMapper;
import org.jeecg.modules.flowable.mapper.SysFormMapper;
import org.jeecg.modules.flowable.service.IFlowDefinitionService;
import org.jeecg.modules.flowable.service.IFlowDeployOnlineService;
import org.jeecg.modules.flowable.service.ISysCustomFormService;
import org.jeecg.modules.flowable.service.ISysDeployFormService;
import org.jeecg.modules.flowable.utils.flowExp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 流程定义
 * @Author: nbacheng
 * @Date:   2022-03-29
 * @Version: V1.0
 */
@Slf4j
@Service
public class FlowDefinitionServiceImpl extends FlowServiceFactory implements IFlowDefinitionService {
    @Autowired
    IFlowThirdService iFlowThirdService;
    @Autowired
    FlowMyBusinessServiceImpl flowMyBusinessService;
    @Autowired
    FlowTaskServiceImpl flowTaskService;
    @Autowired
    ManagementService managementService;
    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    private ISysDeployFormService sysDeployFormService;
    @Autowired
	private ISysCustomFormService sysCustomFormService;
    @Autowired
    FlowCommonService flowCommonService;
    @Autowired
    SysFormMapper sysFormMapper;
    @Autowired
    SysDeployFormMapper sysDeployFormMapper;
    @Autowired
    IFlowDeployOnlineService iFlowDeployOnlineService;
    @Autowired
    FlowMyOnlineServiceImpl flowMyOnlineService;


    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Override
    public boolean exist(String processDefinitionKey) {
        ProcessDefinitionQuery processDefinitionQuery
                = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);
        long count = processDefinitionQuery.count();
        return count > 0 ? true : false;
    }


    /**
     * 流程定义列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @param flowProcDefDto
     * modify by nbacheng
     * @return 流程定义分页列表数据
     */
    @Override
    public Page<FlowProcDefDto> list(Integer pageNum, Integer pageSize, FlowProcDefDto flowProcDefDto) {
        Page<FlowProcDefDto> page = new Page<>();
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery
                .latestVersion()   //获取最新的一个版本
                .orderByProcessDefinitionName().asc();
        /*=====参数=====*/
        if (StrUtil.isNotBlank(flowProcDefDto.getName())){
            processDefinitionQuery.processDefinitionNameLike("%"+flowProcDefDto.getName()+"%");
        }
        if (StrUtil.isNotBlank(flowProcDefDto.getCategory())){
            processDefinitionQuery.processDefinitionCategory(flowProcDefDto.getCategory());
        }
        if (flowProcDefDto.getSuspensionState() == 1){
            processDefinitionQuery.active();
        }
        /*============*/
        page.setTotal(processDefinitionQuery.count());
        //获取分页数据
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage((pageNum - 1) * pageSize, pageSize);

        List<FlowProcDefDto> dataList = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            FlowProcDefDto reProcDef = new FlowProcDefDto();
            BeanUtils.copyProperties(processDefinition, reProcDef);
            // 流程定义时间
            reProcDef.setDeploymentTime(deployment.getDeploymentTime());
            SysForm sysForm = sysDeployFormService.selectSysDeployFormByDeployId(reProcDef.getDeploymentId());
            if (Objects.nonNull(sysForm)) {
            	reProcDef.setFormName(sysForm.getFormName());
            	reProcDef.setFormId(sysForm.getId());
            }
            FlowDeployOnline flowDeployOnline = iFlowDeployOnlineService.selectFlowDeployOnlineByDeployId(reProcDef.getDeploymentId());
            if (Objects.nonNull(flowDeployOnline)) {
            	reProcDef.setFormName(flowDeployOnline.getTableName());
            	reProcDef.setFormId(flowDeployOnline.getId());
            }
            SysCustomForm sysCustomForm = sysDeployFormService.selectSysCustomFormByDeployId(reProcDef.getDeploymentId());
            if (Objects.nonNull(sysCustomForm)) {
            	reProcDef.setFormName(sysCustomForm.getBusinessName());
            	reProcDef.setFormId(sysCustomForm.getId());
            }
            dataList.add(reProcDef);
        }
        page.setRecords(dataList);
        return page;
    }
    
    /**@Description: 使用 Lists 工具类的 partition 分页
     * <br> 1. 页码边界值处理：小于等于0，情况处理； 页码大于 （总页数-1） 情况处理
     * <br> 2. 总页数-1 ，原因是：list集合索引是从0开始的。
     *  @param pageSize 每页显示的数量
     *  @param pageNum  当前页码
     * @see com.google.common.collect.Lists#partition(List, int)
     */
    private List<?> partition(List<?> totalDataList, int pageSize, int pageNum) {
        // 将 List 按照 PageSzie 拆分成多个List
        List<? extends List<?>> partition = Lists.partition(totalDataList, pageSize);
        // 总页数
        int pages = partition.size();
        pageNum = pageNum <= 0 ? 0 : (pageNum <= (pages - 1) ? pageNum : (pages - 1));
        return (List<?>) partition.get(pageNum);
    }

    /**
     * 导入流程文件
     *
     * @param name
     * @param category
     * @param in
     */
    @Override
    public String importFile(String name, String category, InputStream in) {
        Deployment deploy = repositoryService.createDeployment().addInputStream(name + BPMN_FILE_SUFFIX, in).name(name).category(category).deploy();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), category);
        return deploy.getId();

    }

    /**
     * 读取xml
     *
     * @param deployId
     * @return
     */
    @Override
    public Result readXml(String deployId) throws IOException {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        return Result.OK("", result);
    }

    @Override
    public Result readXmlByDataId(String dataId) throws IOException {
        LambdaQueryWrapper<FlowMyBusiness> flowMyBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowMyBusinessLambdaQueryWrapper.eq(FlowMyBusiness::getDataId,dataId)
        ;
        //如果保存数据前未调用必调的FlowCommonService.initActBusiness方法，就会有问题
        FlowMyBusiness business = flowMyBusinessService.getOne(flowMyBusinessLambdaQueryWrapper);
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(business.getProcessDefinitionId()).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        return Result.OK("", result);
    }

    @Override
    public Result readXmlByName(String processDefinitionName) throws IOException {
    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String processId;
        if(processDefinitionQuery.processDefinitionName(processDefinitionName)
        		           .latestVersion().active().list().size() > 0) {
        	processId = processDefinitionQuery.processDefinitionName(processDefinitionName).processDefinitionCategory(Category.ddxz.name())
        			    .latestVersion().active().list().get(0).getId();
	    	ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processId).singleResult();
	        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
	        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
	        return Result.OK("", result);
        }
        else {
        	return Result.OK("", null);
        }
    }
    
    /**
     * 读取xml 根据业务Id
     *
     * @param dataId
     * @return
     */
    @Override
    public InputStream readImageByDataId(String dataId) {
        FlowMyBusiness business = flowMyBusinessService.getByDataId(dataId);

        String processId = business.getProcessInstanceId();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //流程走完的 显示全图
        if (pi == null) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(business.getProcessDefinitionId()).singleResult();
            return this.readImage(processDefinition.getDeploymentId());
        }

        List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId).list();
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                flows.add(hi.getActivityId());
            } else if (activityType.equals("userTask") || activityType.equals("startEvent")) {
                activityIds.add(hi.getActivityId());
            }
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        for (Task task : tasks) {
            activityIds.add(task.getTaskDefinitionKey());
        }
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = engConf.getProcessDiagramGenerator();
        InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);
        return in;
    }
    /**
     * 读取xml
     *
     * @param deployId
     * @return
     */
    @Override
    public InputStream readImage(String deployId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        //获得图片流
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        //输出为图片
        return diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                Collections.emptyList(),
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体",
                null,
                1.0,
                false);

    }

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefKey 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    public Result startProcessInstanceByKey(String procDefKey, Map<String, Object> variables) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey)
                .latestVersion().singleResult();
        return startProcessInstanceById(processDefinition.getId(),variables);
    }
    /**
     * 根据流程定义ID启动流程实例，这个涉及业务dataid,必须要传入dataid
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    @Transactional
    public Result startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId)
                .latestVersion()
                .singleResult();
        if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
            return Result.error("流程已被挂起,请先激活流程");
        }
//           variables.put("skip", true);
//           variables.put(ProcessConstants.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
        // 设置流程发起人Id到流程中
        SysUser sysUser = iFlowThirdService.getLoginUser();
		setFlowVariables(sysUser, variables);
		Map<String, Object> variablesnew = variables;
		Map<String, Object> usermap = new HashMap<String, Object>();
        List<String> userlist = new ArrayList<String>();
        boolean bparallelGateway = false;
        boolean bapprovedEG = false;
        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
        
        //设置自定义表单dataid的数据 
        FlowMyBusiness flowmybusiness = flowMyBusinessService.getByDataId(variables.get("dataId").toString());
        String serviceImplName = flowmybusiness.getServiceImplName();
        FlowCallBackServiceI flowCallBackService = (FlowCallBackServiceI) SpringContextUtils.getBean(serviceImplName);
        if (flowCallBackService!=null){
          Object businessDataById = flowCallBackService.getBusinessDataById(variables.get("dataId").toString());
          variables.put("formData",businessDataById);
        }
        
        //设置部门负责人任务监听uel表达式
        flowExp flowexp = SpringContextUtils.getBean(flowExp.class);
        List<String> deptHeaList = flowexp.getInitiatorDepManagers(sysUser.getUsername());
        variables.put("DeptHeadList",deptHeaList);
        //获取下个节点信息
        getNextFlowInfo(processDefinition, variablesnew, usermap, variables, userlist);
        //取出两个特殊的变量
        if(variablesnew.containsKey("bparallelGateway")) {//并行网关
        	bparallelGateway = (boolean) variablesnew.get("bparallelGateway");
        	variablesnew.remove("bparallelGateway");
        }
        if(variablesnew.containsKey("bapprovedEG")) {//通用拒绝同意排它网关
        	bapprovedEG = (boolean) variablesnew.get("bapprovedEG");
        	variablesnew.remove("bapprovedEG");
        }
        //设置下一个节点审批人员
        variablesnew.put("category", "zdyyw");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables.get("dataId").toString(), variables);
        Result<?> result = setNextAssignee(processInstance, usermap, userlist, sysUser, variables, bparallelGateway,bapprovedEG);	
        // 流程发起后的自定义业务更新-需要考虑两种情况，第一个发起人审批或跳过
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().list();
        /*======================todo 启动之后  回调以及关键数据保存======================*/
        //业务数据id
        String dataId = variables.get("dataId").toString();
        //如果保存数据前未调用必调的FlowCommonService.initActBusiness方法，就会有问题
        if(tasks!=null) {
        	SysUser sysTaskUser = new SysUser();
        	List <String> listUser = new ArrayList<String>();
        	List <String> listId = new ArrayList<String>();
        	List <String> listName = new ArrayList<String>();
        	String taskUser = "";
        	String taskid = "";
        	String taskName = "";
        	int taskPriority = 0;
        	for(Task task : tasks) {
        		if(task.getAssignee() != null) {
            		sysTaskUser = iFlowThirdService.getUserByUsername(task.getAssignee());
            		listUser.add(sysTaskUser.getRealname());
            	}
        		listId.add(task.getId());
        		listName.add(task.getName());
        	    taskPriority = task.getPriority();
        	}
        	taskUser = listUser.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(","));
        	taskid = listId.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(","));
        	taskName = listName.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(","));
        	
	        FlowMyBusiness business = flowMyBusinessService.getByDataId(dataId);
	        business.setProcessDefinitionId(procDefId)
	                .setProcessInstanceId(processInstance.getProcessInstanceId())
	                .setActStatus(ActStatus.doing)
	                .setProposer(sysUser.getUsername())
	                .setTaskId(taskid)
	                .setTaskName(taskName)
	                .setTaskNameId(taskid)
	                .setPriority(String.valueOf(taskPriority))
	                .setDoneUsers("")
	                .setTodoUsers(taskUser);
	        flowMyBusinessService.updateById(business);
	        //spring容器类名
	        String serviceImplNameafter = business.getServiceImplName();
	        FlowCallBackServiceI flowCallBackServiceafter = (FlowCallBackServiceI) SpringContextUtils.getBean(serviceImplNameafter);
	        // 流程处理完后，进行回调业务层
	        business.setValues(variables);
	        if (flowCallBackServiceafter!=null)flowCallBackServiceafter.afterFlowHandle(business);
        }
        else {
        	FlowMyBusiness business = flowMyBusinessService.getByDataId(dataId);
	        business.setProcessDefinitionId(procDefId)
	                .setProcessInstanceId(processInstance.getProcessInstanceId())
	                .setActStatus(ActStatus.pass)
	                .setProposer(sysUser.getUsername())
	                .setTaskId("")
	                .setTaskName("")
	                .setTaskNameId("")
	                .setDoneUsers("")
	                .setTodoUsers("");
	        flowMyBusinessService.updateById(business);
	        //spring容器类名
	        String serviceImplNameafter = business.getServiceImplName();
	        FlowCallBackServiceI flowCallBackServiceafter = (FlowCallBackServiceI) SpringContextUtils.getBean(serviceImplNameafter);
	        // 流程处理完后，进行回调业务层
	        business.setValues(variables);
	        if (flowCallBackServiceafter!=null)flowCallBackServiceafter.afterFlowHandle(business);
        }
   
        return result;
    }
    
	/**
	 * 根据流程定义ID启动流程实例，这个与业务dataid无关，直接通过发布定义流程进行发起实例
	 *  add by nbacheng
	 * @param procDefId
	 *            流程定义Id
	 * @param variables
	 *            流程变量
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result startProcessInstanceByProcDefId(String procDefId, Map<String, Object> variables) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId).latestVersion().singleResult();
		if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
			return Result.error("流程已被挂起,请先激活流程");
		}
		// variables.put("skip", true);
		// variables.put(ProcessConstants.FLOWABLE_SKIP_EXPRESSION_ENABLED,
		// true);
		// 设置流程发起人Id到流程中
		SysUser sysUser = iFlowThirdService.getLoginUser();
		setFlowVariables(sysUser, variables);	
		
		Map<String, Object> variablesnew = variables;
		Map<String, Object> usermap = new HashMap<String, Object>();
        List<String> userlist = new ArrayList<String>();
        boolean bparallelGateway = false;
        boolean bapprovedEG = false;
        //获取下个节点信息
        getNextFlowInfo(processDefinition, variablesnew, usermap, variables, userlist);
        //取出两个特殊的变量
        if(variablesnew.containsKey("bparallelGateway")) {//并行网关
        	bparallelGateway = (boolean) variablesnew.get("bparallelGateway");
        	variablesnew.remove("bparallelGateway");
        }
        if(variablesnew.containsKey("bapprovedEG")) {//通用拒绝同意排它网关
        	bapprovedEG = (boolean) variablesnew.get("bapprovedEG");
        	variablesnew.remove("bapprovedEG");
        }
        //设置下一个节点审批人员
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variablesnew);
        return setNextAssignee(processInstance, usermap, userlist, sysUser, variables, bparallelGateway, bapprovedEG);	
	}
    
	/**
	 * 设置发起人变量
	 *  add by nbacheng
	 *           
	 * @param variables
	 *            流程变量
	 * @return
	 */
	private void setFlowVariables(SysUser sysUser, Map<String, Object> variables) {
        identityService.setAuthenticatedUserId(sysUser.getUsername());
        variables.put(ProcessConstants.PROCESS_INITIATOR, sysUser.getUsername());
        //设置部门负责人任务监听uel表达式
        flowExp flowexp = SpringContextUtils.getBean(flowExp.class);
        List<String> deptHeaList = flowexp.getInitiatorDepManagers(sysUser.getUsername());
        variables.put("DeptHeadList",deptHeaList);
	}
	
	/**
	 * 获取下个节点信息,对并行与排它网关做处理
	 *  add by nbacheng
	 *           
	 * @param processDefinition, variablesnew, usermap,
			   variables, userlist, bparallelGateway
	 *           
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void getNextFlowInfo(ProcessDefinition processDefinition, Map<String, Object> variablesnew, Map<String, Object> usermap,
			                     Map<String, Object> variables, List<String> userlist) {
		String definitionld = processDefinition.getId();        //获取bpm（模型）对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionld);
        //传节点定义key获取当前节点
        List<org.flowable.bpmn.model.Process> processes =  bpmnModel.getProcesses();
        //只处理发起人后面排它网关再后面是会签的情况，其它目前不考虑
        //List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
        List<FlowNode> flowNodes = processes.get(0).findFlowElementsOfType(FlowNode.class);
        List<SequenceFlow> outgoingFlows = flowNodes.get(1).getOutgoingFlows();
        //遍历返回下一个节点信息
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            //类型自己判断（获取下个节点是网关还是节点）
            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
            //下个是节点
           if(targetFlowElement instanceof ExclusiveGateway){// 下个出口是排它网关的话,后一个用户任务又是会签的情况下需要approval的赋值处理，否则报错
        	   usermap =  GetExclusiveGatewayUser(targetFlowElement,variables);//还是需要返回用户与是否并发，因为并发要做特殊处理
        	   if(usermap != null) {
        		 userlist = (ArrayList<String>) usermap.get("approval");
        	     variablesnew.put("approval", userlist);
        	   }
        	   if(FindNextNodeUtil.GetExclusiveGatewayExpression(targetFlowElement)) {//下个出口是通用拒绝同意排它网关
        		   variablesnew.put("bapprovedEG",true);
        	   }
        	   break;
            }
           if(targetFlowElement instanceof ParallelGateway){// 下个出口是并行网关的话,直接需要进行complete，否则报错
        	   variablesnew.put("bparallelGateway",true);
           }
        }
	}
	
	/**
	 * 设置下个节点信息处理人员
	 *  add by nbacheng
	 *           
	 * @param variablesnew, usermap,
	 *		  userlist, sysUser, variables,  bparallelGateway
	 *            
	 * @return
	 */
	private Result setNextAssignee(ProcessInstance processInstance, Map<String, Object> usermap,
			                       List<String> userlist, SysUser sysUser, Map<String, Object> variables,
			                       boolean bparallelGateway, boolean bapprovedEG) {
		// 给第一步申请人节点设置任务执行人和意见
		if((usermap.containsKey("isSequential")) && !(boolean)usermap.get("isSequential")) {//并发会签会出现2个以上需要特殊处理
			List<Task> nexttasklist = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().list();
			  int i=0;
			  for (Task nexttask : nexttasklist) {
				   String assignee = userlist.get(i).toString();	
				   taskService.addComment(nexttask.getId(), processInstance.getProcessInstanceId(),
							FlowComment.NORMAL.getType(), sysUser.getRealname() + "发起流程申请");
			       taskService.setAssignee(nexttask.getId(), assignee);
			       i++;
			  }
			  return Result.OK("多实例会签流程启动成功.");
 	    }
		else {// 给第一步申请人节点设置任务执行人和意见
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active()
				.singleResult();
			if (Objects.nonNull(task)) {
				taskService.addComment(task.getId(), processInstance.getProcessInstanceId(),
						FlowComment.NORMAL.getType(), sysUser.getRealname() + "发起流程申请");
				taskService.setAssignee(task.getId(), sysUser.getUsername());
				//taskService.complete(task.getId(), variables);
			}

			// 获取下一个节点数据及设置数据

			FlowNextDto	nextFlowNode = flowTaskService.getNextFlowNode(task.getId(), variables);
			if(Objects.nonNull(nextFlowNode)) {
				if (Objects.nonNull(task)) {
					Map<String, Object> nVariablesMap = taskService.getVariables(task.getId());
					if(nVariablesMap.containsKey("SetAssigneeTaskListener")) {//是否通过动态设置审批人的任务监听器
					  taskService.complete(task.getId(), variables);
					  Task nexttask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().singleResult();
					  taskService.setAssignee(nexttask.getId(), nVariablesMap.get("SetAssigneeTaskListener").toString());
					  return Result.OK("通过动态设置审批人的任务监听器流程启动成功.");
				    }
					if(nVariablesMap.containsKey("SetDeptHeadTaskListener")) {//是否通过动态设置发起人部门负责人的任务监听器
						  if(nextFlowNode.getType() == ProcessConstants.PROCESS_MULTI_INSTANCE ) { //对多实例会签做特殊处理
							  return Result.OK("流程启动成功给发起人,请到我的待办里进行流程的提交流转."); 
						  }
						  else {
							  taskService.complete(task.getId(), variables);
							  Task nexttask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().singleResult();
							  if(Objects.nonNull(nexttask)) {
								  if(Objects.nonNull((List<String>) nVariablesMap.get("SetDeptHeadTaskListener"))) {
									  if(((List<String>) nVariablesMap.get("SetDeptHeadTaskListener")).size() == 1) {//是否就一个人
										  taskService.setAssignee(nexttask.getId(), ((List<String>)nVariablesMap.get("SetDeptHeadTaskListener")).get(0).toString());
								          return Result.OK("设置发起人部门负责人的任务监听器流程启动成功.");
									  }
									  else {
										  for (String username : ((List<String>) nVariablesMap.get("SetDeptHeadTaskListener"))) {
		        							  taskService.addCandidateUser(nexttask.getId(), username);
		        						  }
							             return Result.OK("设置多个发起人部门负责人的任务监听器流程启动成功,目前用户可通过签收方式完成审批."); 
									  }
									  
								  }
								
							  }
						  }  
					}
				}
				if(Objects.nonNull(nextFlowNode.getUserList())) {
					if( nextFlowNode.getUserList().size() == 1 ) {
						if (nextFlowNode.getUserList().get(0) != null) {
							if(StringUtils.equalsAnyIgnoreCase(nextFlowNode.getUserList().get(0).getUsername(), "${INITIATOR}")) {//对发起人做特殊处理
								taskService.complete(task.getId(), variables);
								return Result.OK("流程启动成功给发起人.");
							}
							else if(nextFlowNode.getUserTask().getCandidateGroups().size()>0 && StringUtils.equalsAnyIgnoreCase(nextFlowNode.getUserTask().getCandidateGroups().get(0), "${DepManagerHandler.getUsers(execution)}")) {//对部门经理做特殊处理								
								//taskService.complete(task.getId(), variables);
								return Result.OK("流程启动成功给部门经理,请到我的待办里进行流程的提交流转.");
							}
							else {
								taskService.complete(task.getId(), variables);
							    return Result.OK("流程启动成功.");
							}
						}
						else {
							return Result.error("审批人不存在，流程启动失败!");
						}
						
					}
					else if(nextFlowNode.getType() == ProcessConstants.PROCESS_MULTI_INSTANCE ) {//对多实例会签做特殊处理或者以后在流程设计进行修改也可以
		                Map<String, Object> approvalmap = new HashMap<>();
		                List<String> sysuserlist = nextFlowNode.getUserList().stream().filter(Objects::nonNull).map(obj-> (String) obj.getUsername()).collect(Collectors.toList());
						approvalmap.put("approval", sysuserlist);
						taskService.complete(task.getId(), approvalmap);
						if(!nextFlowNode.isBisSequential()){//对并发会签进行assignee单独赋值
		  				  List<Task> nexttasklist = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().list();
		  				  int i=0;
		  				  for (Task nexttask : nexttasklist) {
		  					   String assignee = sysuserlist.get(i).toString();	
		      			       taskService.setAssignee(nexttask.getId(), assignee);
		      			       i++;
		  				  }
		  				 
		  				}
						return Result.OK("多实例会签流程启动成功.");
					}
					else if(nextFlowNode.getUserList().size() > 1) {
						if (bparallelGateway) {//后一个节点是并行网关的话
							taskService.complete(task.getId(), variables);
							return Result.OK("流程启动成功.");
						}
						else {
							return Result.OK("流程启动成功,请到我的待办里进行流程的提交流转.");
						}
					}
					else {
						return Result.OK("流程启动失败,请检查流程设置人员！");
					}
				}
				else {//对跳过流程做特殊处理
					List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasks(repositoryService, task, variables);
		            if (CollectionUtils.isNotEmpty(nextUserTask)) {
		            	List<FlowableListener> listlistener = nextUserTask.get(0).getTaskListeners();
		            	if(CollectionUtils.isNotEmpty(listlistener)) {
		            		String tasklistener =  listlistener.get(0).getImplementation();
		            		if(StringUtils.contains(tasklistener, "AutoSkipTaskListener")) {
			            		taskService.complete(task.getId(), variables);
			    				return Result.OK("流程启动成功.");
			            	}else {
				            	return Result.OK("流程启动失败,请检查流程设置人员！");
				            }
		            	}else {
			            	return Result.OK("流程启动失败,请检查流程设置人员！");
			            }
		            	
		            }
		            else {
		            	return Result.OK("流程启动失败,请检查流程设置人员！");
		            }
				}
			}
			else {
				if(bapprovedEG) {
					return Result.OK("通用拒绝同意流程启动成功,请到我的待办里进行流程的提交流转.");
				}
				taskService.complete(task.getId(), variables);
				return Result.OK("流程启动成功.");
			}
		}
	}
	
	/**
	 * 根据流程dataId,serviceName启动流程实例，主要是自定义业务表单发起流程使用
	 *  add by nbacheng
	 * @param dataId,serviceName
	 *           
	 * @param variables
	 *            流程变量
	 * @return
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startProcessInstanceByDataId(String dataId, String serviceName, Map<String, Object> variables) {
    	//提交审批的时候进行流程实例关联初始化
    	
        if (serviceName==null){
             return Result.error("未找到serviceName："+serviceName);
        }
        SysCustomForm sysCustomForm = sysCustomFormService.selectSysCustomFormByServiceName(serviceName);
        if(sysCustomForm ==null){
        	 return Result.error("未找到sysCustomForm："+serviceName);
        }
        //优先考虑自定义业务表是否关联流程，再看通用的表单流程关联表
        ProcessDefinition processDefinition;
        String deployId = sysCustomForm.getDeployId();
        if(StringUtils.isEmpty(deployId)) {
        	SysDeployForm sysDeployForm  = sysDeployFormService.selectSysDeployFormByFormId(sysCustomForm.getId());
            if(sysDeployForm ==null){          	
       	       return Result.error("自定义表单也没关联流程定义表,流程没定义关联自定义表单"+sysCustomForm.getId());
            }
            processDefinition = repositoryService.createProcessDefinitionQuery()
        		.parentDeploymentId(sysDeployForm.getDeployId()).latestVersion().singleResult();
        }
        else {
        	processDefinition = repositoryService.createProcessDefinitionQuery()
            		.parentDeploymentId(deployId).latestVersion().singleResult();
        }
        
        LambdaQueryWrapper<FlowMyBusiness> flowMyBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowMyBusinessLambdaQueryWrapper.eq(FlowMyBusiness::getDataId, dataId);
        FlowMyBusiness business = flowMyBusinessService.getOne(flowMyBusinessLambdaQueryWrapper);
        if (business==null){
        	if(processDefinition==null) {
        		return Result.error("自定义表单也没关联流程定义表,流程没定义关联自定义表单"+sysCustomForm.getId());
        	}
        	boolean binit = flowCommonService.initActBusiness(sysCustomForm.getBusinessName(), dataId, serviceName, 
        	processDefinition.getKey(), processDefinition.getId(), sysCustomForm.getRouteName());
        	if(!binit) {
        		return Result.error("自定义表单也没关联流程定义表,流程没定义关联自定义表单"+sysCustomForm.getId());
        	}
            FlowMyBusiness businessnew = flowMyBusinessService.getOne(flowMyBusinessLambdaQueryWrapper);
           //流程实例关联初始化结束
            if (StrUtil.isNotBlank(businessnew.getProcessDefinitionId())){
              return this.startProcessInstanceById(businessnew.getProcessDefinitionId(),variables);
            }
            return this.startProcessInstanceByKey(businessnew.getProcessDefinitionKey(),variables);
        }
        else {
        	 return Result.error("已经存在这个dataid实例，不要重复申请："+dataId);
        }
        
    }
    
    /**
	 * 根据流程dataId,onlineId启动流程实例，主要是online表单发起流程使用
	 *  add by nbacheng
	 * @param dataId,onlineId
	 *           
	 * @param variables
	 *            流程变量
	 * @return
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public Result startProcessInstanceByOnlineDataId(String dataId, String onlineId, String deployId, Map<String, Object> variables) {
       //提交审批的时候进行流程实例关联初始化
    	
        if (onlineId==null){
             return Result.error("未找到onlineId："+onlineId);
        }
        FlowDeployOnline flowDeployOnline = iFlowDeployOnlineService.selectFlowDeployOnlineByOnlineIdDeployId(onlineId,deployId);
        if(flowDeployOnline ==null){
        	 return Result.error("未找到FlowDeployOnline："+onlineId);
        }
        //优先考虑自定义业务表是否关联流程，再看通用的表单流程关联表
        ProcessDefinition processDefinition;
        if(StringUtils.isEmpty(deployId)) {       	
       	     return Result.error("online表单没关联流程"+flowDeployOnline.getId());
         
        }
        else {
        	processDefinition = repositoryService.createProcessDefinitionQuery()
            		.parentDeploymentId(deployId).latestVersion().singleResult();
        }
        
        LambdaQueryWrapper<FlowMyOnline> flowMyOnlineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowMyOnlineLambdaQueryWrapper.eq(FlowMyOnline::getDataId, dataId);//以后这里还要加上onlineId
        FlowMyOnline online = flowMyOnlineService.getOne(flowMyOnlineLambdaQueryWrapper);
        if (online==null){
        	boolean binit = flowCommonService.initActOnline(flowDeployOnline.getTableName(), dataId, onlineId, 
        	processDefinition.getKey(), processDefinition.getId());
        	if(!binit) {
        		return Result.error("online表单也没关联流程定义表"+flowDeployOnline.getId());
        	}
            FlowMyOnline onlinenew = flowMyOnlineService.getOne(flowMyOnlineLambdaQueryWrapper);
           //流程实例关联初始化结束
            if (StrUtil.isNotBlank(onlinenew.getProcessDefinitionId())){
              return this.startProcessInstanceByOnlineId(onlinenew.getProcessDefinitionId(),variables);
            }
            return this.startProcessInstanceByOnlineKey(onlinenew.getProcessDefinitionKey(),variables);	
        }
        else {
        	 return Result.error("已经存在这个dataid实例，不要重复申请："+dataId);
        }
	}

    
    /**
     * 根据流程定义ID启动流程实例,online表单申请使用
     *
     * @param procDefKey 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    public Result startProcessInstanceByOnlineKey(String procDefKey, Map<String, Object> variables) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey)
                .latestVersion().singleResult();
        return startProcessInstanceByOnlineId(processDefinition.getId(),variables);
    }
    /**
     * 根据流程定义ID启动流程实例，这个涉及,online表单数据dataid和onlineId,必须要传入dataid和onlineId，online表单申请使用
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    @Transactional
    public Result startProcessInstanceByOnlineId(String procDefId, Map<String, Object> variables) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId)
                .latestVersion()
                .singleResult();
        if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
            return Result.error("流程已被挂起,请先激活流程");
        }
//           variables.put("skip", true);
//           variables.put(ProcessConstants.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
        // 设置流程发起人Id到流程中
        SysUser sysUser = iFlowThirdService.getLoginUser();
		setFlowVariables(sysUser, variables);
		Map<String, Object> variablesnew = variables;
		Map<String, Object> usermap = new HashMap<String, Object>();
        List<String> userlist = new ArrayList<String>();
        boolean bparallelGateway = false;
        boolean bapprovedEG = false;
        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
        
        //设置online表单dataid的数据 
        FlowMyOnline flowmyonline = flowMyOnlineService.getByDataId(variables.get("dataId").toString());
        String onlineId = flowmyonline.getOnlineId();
        //获取下个节点信息
        getNextFlowInfo(processDefinition, variablesnew, usermap, variables, userlist);
        //设置下一个节点审批人员
        variablesnew.put("category", "online");  //设置流程类别，以便发送消息里包括这个类别，否则有问题
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variablesnew.get("dataId").toString(), variablesnew);
        // 给第一步申请人节点设置任务执行人和意见
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().singleResult();
            
        /*======================todo 启动之后  回调以及关键数据保存======================*/
        //online数据id
        String dataId = variables.get("dataId").toString();
        //如果保存数据前未调用必调的FlowCommonService.initActOnline方法，就会有问题
        FlowMyOnline online = flowMyOnlineService.getByDataId(dataId);
        online.setProcessDefinitionId(procDefId)
                .setProcessInstanceId(processInstance.getProcessInstanceId())
                .setActStatus(ActStatus.start)
                .setProposer(sysUser.getUsername())
                .setTaskId(task.getId())
                .setTaskName(task.getName())
                .setTaskNameId(task.getId())
                .setPriority(String.valueOf(task.getPriority()))
                .setDoneUsers("")
                .setTodoUsers(JSON.toJSONString(sysUser.getRealname()));
        flowMyOnlineService.updateById(online);
        
        Result<?> result = setNextAssignee(processInstance, usermap, userlist, sysUser, variables, bparallelGateway, bapprovedEG);	
        return result;
    }

    /**
     * 激活或挂起流程定义
     *
     * @param state    状态 激活1 挂起2
     * @param deployId 流程部署ID
     */
    @Override
    public void updateState(Integer state, String deployId) {
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        // 激活
        if (state == 1) {
            repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);
        }
        // 挂起
        if (state == 2) {
            repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);
        }
    }


    /**
     * 删除流程定义
     *
     * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
     */
    @Override
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
    }


    /**
     *获取流程定义的所有节点信息  add by nbacheng
     *
     * @param processDefinitionName 流程ID act_re_procdef 表中 name值
     */
	@Override
	public JSONArray ListAllNode(String processDefinitionName) {
		// TODO Auto-generated method stub

		// 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String processId;
        JSONArray jsonlist = new JSONArray();
        List<Object> taskList = new ArrayList<>();
        if(processDefinitionQuery.processDefinitionName(processDefinitionName).processDefinitionCategory(Category.ddxz.name())
        		           .latestVersion().active().list().size() > 0) {
        	processId = processDefinitionQuery.processDefinitionName(processDefinitionName)
        		           .latestVersion().active().list().get(0).getId();
        	List<org.flowable.bpmn.model.Process> processes = repositoryService.getBpmnModel(processId). getProcesses();
			log.info("processes size:" + processes.size());
			
			for (org.flowable.bpmn.model.Process process : processes) {
			    Collection<FlowElement> flowElements = process.getFlowElements();
			    getTaskList(processId, taskList, flowElements, null, null);
			}
         }
        int i=1;
        for (Object flowElement : taskList) {
        	JSONObject jsonobj = new JSONObject();  
        	UserTask userTask = (UserTask) flowElement;
        	SysUser sysUser  = iFlowThirdService.getUserByUsername(userTask.getAssignee());
        	jsonobj.put("NodeNo", i);
        	jsonobj.put("NodeName", userTask.getName());
        	jsonobj.put("Assignee", userTask.getAssignee());
        	jsonobj.put("RealName", sysUser.getRealname());
        	jsonlist.add(jsonobj);
        	i++;
        	log.info("UserTask：" + userTask.getName());
        	log.info("getAssignee：" + userTask.getAssignee());
        }
     
        return jsonlist;
	}
	/***
     * 根据bpmnmodel获取流程节点的顺序信息
     * @param processInstanceId
     * @param taskList
     * @param flowElements
     * @param workflowRequestFormData
     * @param curFlowElement
     */
    private void getTaskList(String processInstanceId, List<Object> taskList, Collection<FlowElement> flowElements, Map<String, Object> workflowRequestFormData, FlowElement curFlowElement) {
        if (curFlowElement == null && taskList.size() == 0) {
            // 获取第一个UserTask
            FlowElement startElement = flowElements.stream().filter(flowElement -> flowElement instanceof StartEvent).collect(Collectors.toList()).get(0);
            List<SequenceFlow> outgoingFlows = ((StartEvent) startElement).getOutgoingFlows();
            String targetRef = outgoingFlows.get(0).getTargetRef();
            // 根据ID找到FlowElement
            FlowElement targetElementOfStartElement = getFlowElement(flowElements, targetRef);
            if (targetElementOfStartElement instanceof UserTask) {
                this.getTaskList(processInstanceId, taskList, flowElements, workflowRequestFormData, targetElementOfStartElement);
            }

        } else if (curFlowElement instanceof UserTask) {
            // 只有Usertask才添加到列表中
            taskList.add(curFlowElement);
            String targetRef = "";
            List<SequenceFlow> outgoingFlows = ((UserTask) curFlowElement).getOutgoingFlows();
            if (outgoingFlows.size() == 1) {
                targetRef = outgoingFlows.get(0).getTargetRef();
            } else {
                // 找到表达式成立的sequenceFlow的
                SequenceFlow sequenceFlow = getSequenceFlow(workflowRequestFormData, outgoingFlows);
                if (sequenceFlow != null) {
                    targetRef = sequenceFlow.getTargetRef();
                }
            }
            // 根据ID找到FlowElement
            FlowElement targetElement = getFlowElement(flowElements, targetRef);

            this.getTaskList(processInstanceId, taskList, flowElements, workflowRequestFormData, targetElement);
        } else if (curFlowElement instanceof ExclusiveGateway) {
            String targetRef = "";
            // 如果为排他网关，获取符合条件的sequenceFlow的目标FlowElement
            List<SequenceFlow> exclusiveGatewayOutgoingFlows = ((ExclusiveGateway) curFlowElement).getOutgoingFlows();
            // 找到表达式成立的sequenceFlow的
            SequenceFlow sequenceFlow = getSequenceFlow(workflowRequestFormData, exclusiveGatewayOutgoingFlows);
            if (sequenceFlow != null) {
                targetRef = sequenceFlow.getTargetRef();
            }
            // 根据ID找到FlowElement
            FlowElement targetElement = getFlowElement(flowElements, targetRef);

            this.getTaskList(processInstanceId, taskList, flowElements, workflowRequestFormData, targetElement);
        }
    }
    
/***
     * 根据ID找到FlowElement
     * @param flowElements
     * @param targetRef
     * @return
     */
    private FlowElement getFlowElement(Collection<FlowElement> flowElements, String targetRef) {
        List<FlowElement> targetFlowElements = flowElements.stream().filter(
                flowElement -> !StringUtils.isEmpty(flowElement.getId()) && flowElement.getId().equals(targetRef)
        ).collect(Collectors.toList());
        if (targetFlowElements.size() > 0) {
            return targetFlowElements.get(0);
        }
        return null;
    }

  /***
     * 找到表达式成立的sequenceFlow的
     * @param workflowRequestFormData
     * @param outgoingFlows
     * @return
     */
    private SequenceFlow getSequenceFlow(Map workflowRequestFormData, List<SequenceFlow> outgoingFlows) {

        List<SequenceFlow> sequenceFlows = outgoingFlows.stream().filter(item -> {
            Object execConditionExpressionResult = null;
            boolean re = false;
            try {
                execConditionExpressionResult = this.getElValue(item.getConditionExpression(), workflowRequestFormData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (execConditionExpressionResult.equals("true")) {
                re = true;
            }
            return (Boolean) execConditionExpressionResult;
        }).collect(Collectors.toList());
        if (sequenceFlows.size() > 0) {
            return sequenceFlows.get(0);

        }
        return null;
    }
    private boolean getElValue(String exp, Map<String, Object> variableMap){
        return managementService.executeCommand(new ExpressionCmd(runtimeService, processEngineConfiguration, null, exp, variableMap));
    }
    /**
     * 获取排他网关分支名称、分支表达式、下一级任务节点
     * @param flowElement
     * @param data
     * add by nbacheng
     */
    private Map<String, Object> GetExclusiveGatewayUser(FlowElement flowElement,Map<String, Object> variables){
    	// 获取所有网关分支
        List<SequenceFlow> targetFlows=((ExclusiveGateway)flowElement).getOutgoingFlows();
        // 循环每个网关分支
        for(SequenceFlow sequenceFlow : targetFlows){
            // 获取下一个网关和节点数据
            FlowElement targetFlowElement=sequenceFlow.getTargetFlowElement();
            // 网关数据不为空
            if (StringUtils.isNotBlank(sequenceFlow.getConditionExpression())) {
                // 获取网关判断条件
            	String expression = sequenceFlow.getConditionExpression();
                if (expression == null ||Boolean.parseBoolean(
                                String.valueOf(
                                		FindNextNodeUtil.result(variables, expression.substring(expression.lastIndexOf("{") + 1, expression.lastIndexOf("}")))))) {
                	// 网关出线的下个节点是用户节点
                    if(targetFlowElement instanceof UserTask){
                        // 判断是否是会签
                        UserTask userTask = (UserTask) targetFlowElement;
                        MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                    	if (Objects.nonNull(multiInstance)) {//下个节点是会签节点
                    		Map<String, Object> approvalmap = new HashMap<>();
                    		List<String> getuserlist =  getmultiInstanceUsers(multiInstance,userTask);
                    		approvalmap.put("approval", getuserlist);
                    		if(multiInstance.isSequential()) {
                    			approvalmap.put("isSequential", true);
                    		}
                    		else {
                    			approvalmap.put("isSequential", false);
                    		}
                    		return approvalmap;
                    	}
                    }
                }
            }
        }
		return null;
    }
    
    /**
     * 获取多实例会签用户信息
     * @param userTask
     * @param multiInstance
     *
     **/
    List<String> getmultiInstanceUsers(MultiInstanceLoopCharacteristics multiInstance,UserTask userTask) {
    	List<String> sysuserlist = new ArrayList<>();
    	List<String> rolelist = new ArrayList<>();
        rolelist = userTask.getCandidateGroups();
    	List<String> userlist = new ArrayList<>();
        userlist = userTask.getCandidateUsers();
        if(rolelist.size() > 0) {
        	List<SysUser> list = new ArrayList<SysUser>();
			for(String roleId : rolelist ){
        	  List<SysUser> templist = iFlowThirdService.getUsersByRoleId(roleId);
        	  for(SysUser sysuser : templist) {
          		SysUser sysUserTemp = iFlowThirdService.getUserByUsername(sysuser.getUsername());
          		List<String> listdepname = iFlowThirdService.getDepartNamesByUsername(sysuser.getUsername());
          		if(listdepname.size()>0){
          			sysUserTemp.setOrgCodeTxt(listdepname.get(0).toString());
          		}
          		list.add(sysUserTemp);
          	  }
        	}
			sysuserlist = list.stream().filter(Objects::nonNull).map(obj-> (String) obj.getUsername()).collect(Collectors.toList());
           
        }
        else if(userlist.size() > 0) {
        	List<SysUser> list = new ArrayList<SysUser>();
        	for(String username : userlist) {
        		SysUser sysUser =  iFlowThirdService.getUserByUsername(username);
        		List<String> listdepname = iFlowThirdService.getDepartNamesByUsername(username);
        		if(listdepname.size()>0){
        			sysUser.setOrgCodeTxt(listdepname.get(0).toString());
        		}
        		list.add(sysUser);
        	}
        	sysuserlist = list.stream().filter(Objects::nonNull).map(obj-> (String) obj.getUsername()).collect(Collectors.toList());
        }    
    	return sysuserlist;
    }


    /**
     * 新增流程实例关联表单
     *
     * @param deployId,bpmnModel 流程实例关联表单
     * @return boolean
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveDeployForm(String deployId, BpmnModel bpmnModel) {
		List<SysDeployForm> deployFormList = new ArrayList<>();
        // 获取开始节点
        StartEvent startEvent = FlowableUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new RuntimeException("开始节点不存在，请检查流程设计是否有误！");
        }
        // 保存开始节点表单信息
        SysDeployForm startDeployForm = buildDeployForm(deployId, startEvent);
        if (ObjectUtil.isNotNull(startDeployForm)) {
            deployFormList.add(startDeployForm);
        }
        // 保存用户节点表单信息
        Collection<UserTask> userTasks = FlowableUtils.getAllUserTaskEvent(bpmnModel);
        if (CollUtil.isNotEmpty(userTasks)) {
            for (UserTask userTask : userTasks) {
            	SysDeployForm userTaskDeployForm = buildDeployForm(deployId, userTask);
                if (ObjectUtil.isNotNull(userTaskDeployForm)) {
                    deployFormList.add(userTaskDeployForm);
                }
            }
        }
        // 批量新增部署流程和表单关联信息
        return sysDeployFormService.insertBatch(deployFormList);
	}
	
	/**
     * 构建发布表单关联信息对象
     * @param deployId 部署ID
     * @param node 节点信息
     * @return 发布表单关联对象。若无表单信息（formKey），则返回null
     */
    private SysDeployForm buildDeployForm(String deployId, FlowNode node) {
        String formKey = null;
        SysDeployForm deployForm = new SysDeployForm();
        if (node instanceof StartEvent) {
            formKey = ((StartEvent) node).getFormKey();
            deployForm.setFormFlag("1"); //作为开始form表单标志    
        } else if (node instanceof UserTask) {
            formKey = ((UserTask) node).getFormKey();
        }
        if (StringUtils.isEmpty(formKey)) {
            return null;
        }
        SysForm sysForm = sysFormMapper.selectSysFormById(formKey);
        if (ObjectUtil.isNull(sysForm)) {
            throw new RuntimeException("表单信息查询错误");
        }
        deployForm.setDeployId(deployId);
        deployForm.setNodeKey(node.getId());
        deployForm.setNodeName(node.getName());
        deployForm.setFormId(formKey);
        return deployForm;
    }

}
