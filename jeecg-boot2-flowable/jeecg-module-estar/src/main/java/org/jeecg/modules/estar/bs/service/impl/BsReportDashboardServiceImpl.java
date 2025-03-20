package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.ChartDto;
import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.dto.OriginalDataDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardObjectDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardWidgetDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardWidgetValueDto;
import org.jeecg.modules.estar.bs.entity.BsFile;
import org.jeecg.modules.estar.bs.entity.BsReportDashboard;
import org.jeecg.modules.estar.bs.entity.BsReportDashboardWidget;
import org.jeecg.modules.estar.bs.mapper.BsReportDashboardMapper;
import org.jeecg.modules.estar.bs.service.ChartStrategy;
import org.jeecg.modules.estar.bs.service.IBsDataSetService;
import org.jeecg.modules.estar.bs.service.IBsFileService;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardService;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardWidgetService;
import org.jeecg.modules.estar.bs.service.IBsReportService;
import org.jeecg.modules.estar.bs.util.DateUtil;
import org.jeecg.modules.estar.bs.util.EstarAssert;
import org.jeecg.modules.estar.bs.util.EstarBeanUtils;
import org.jeecg.modules.estar.bs.util.FileUtil;
import org.jeecg.modules.estar.bs.util.FileUtils;
import org.jeecg.modules.estar.bs.util.RequestUtil;
import org.jeecg.modules.estar.bs.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大屏看板表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Service
@Slf4j
public class BsReportDashboardServiceImpl extends ServiceImpl<BsReportDashboardMapper, BsReportDashboard> implements IBsReportDashboardService, InitializingBean, ApplicationContextAware { 

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;

	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String uploadType;
	    
	@Autowired
    private BsReportDashboardMapper reportDashboardMapper;

    @Autowired
    private IBsReportDashboardWidgetService reportDashboardWidgetService;

    @Autowired
    private IBsDataSetService dataSetService;

    @Autowired
    private IBsFileService bsFileService;

    @Autowired
    private IBsReportService reportService;

    @Value("${bigscreen.downloadPath:}")
    private String fileDownloadPath;

    @Value("${bigscreen.file.tmp-path:.}")
    private String dictPath;

    private final static String ZIP_PATH = "/tmp_zip/";
    private final static String JSON_PATH = "dashboard.json";

    private Map<String, ChartStrategy> queryServiceImplMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public ReportDashboardObjectDto getDetail(String reportCode) {
        ReportDashboardObjectDto result = new ReportDashboardObjectDto();
        ReportDashboardDto reportDashboardDto = new ReportDashboardDto();
        QueryWrapper<BsReportDashboard> queryWrapper = new QueryWrapper<BsReportDashboard>();
    	queryWrapper.eq("report_code", reportCode);
        BsReportDashboard reportDashboard = this.getOne(queryWrapper);
        if (null == reportDashboard) {
            return new ReportDashboardObjectDto();
        }
        EstarBeanUtils.copyAndFormatter(reportDashboard, reportDashboardDto);

        List<BsReportDashboardWidget> list = reportDashboardWidgetService.list(
                new QueryWrapper<BsReportDashboardWidget>().lambda()
                        .eq(BsReportDashboardWidget::getReportCode, reportCode)
                        .orderByAsc(BsReportDashboardWidget::getSort)
        );
        List<ReportDashboardWidgetDto> reportDashboardWidgetDtoList = new ArrayList<>();
        list.forEach(reportDashboardWidget -> {
            ReportDashboardWidgetDto reportDashboardWidgetDto = new ReportDashboardWidgetDto();
            ReportDashboardWidgetValueDto value = new ReportDashboardWidgetValueDto();
            value.setSetup(StringUtils.isNotBlank(reportDashboardWidget.getSetup()) ? JSONObject.parseObject(reportDashboardWidget.getSetup()) : new JSONObject());
            value.setData(StringUtils.isNotBlank(reportDashboardWidget.getData()) ? JSONObject.parseObject(reportDashboardWidget.getData()) : new JSONObject());
            value.setPosition(StringUtils.isNotBlank(reportDashboardWidget.getPosition()) ? JSONObject.parseObject(reportDashboardWidget.getPosition()) : new JSONObject());
            value.setCollapse(StringUtils.isNotBlank(reportDashboardWidget.getCollapse()) ? JSONObject.parseObject(reportDashboardWidget.getCollapse()) : new JSONObject());

            //实时数据的替换
            analysisData(value);
            reportDashboardWidgetDto.setType(reportDashboardWidget.getType());
            reportDashboardWidgetDto.setValue(value);
            reportDashboardWidgetDto.setOptions(JSONObject.parseObject(reportDashboardWidget.getOptions()));
            reportDashboardWidgetDtoList.add(reportDashboardWidgetDto);
        });
        reportDashboardDto.setWidgets(reportDashboardWidgetDtoList);
        result.setDashboard(reportDashboardDto);
        result.setReportCode(reportCode);
        return result;
    }

    /***
     * 保存大屏设计
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDashboard(ReportDashboardObjectDto dto) {
        String reportCode = dto.getReportCode();
        EstarAssert.notEmpty(reportCode, ResponseCode.PARAM_IS_NULL, "reportCode");
        //查询ReportDashboard
        QueryWrapper<BsReportDashboard> queryWrapper = new QueryWrapper<BsReportDashboard>();
        queryWrapper.eq("report_code", reportCode);
        BsReportDashboard reportDashboard = this.getOne(queryWrapper);
        BsReportDashboard dashboard = new BsReportDashboard();
        EstarBeanUtils.copyAndFormatter(dto.getDashboard(), dashboard);
        BeanUtils.copyProperties(dto.getDashboard(), dashboard);
        dashboard.setReportCode(reportCode);
        if (null == reportDashboard) {
            //新增
            this.save(dashboard);
        } else {
            //更新
            dashboard.setId(reportDashboard.getId());
            this.updateById(dashboard);
        }

        //删除reportDashboardWidget
        reportDashboardWidgetService.remove(new QueryWrapper<BsReportDashboardWidget>()
                .lambda().eq(BsReportDashboardWidget::getReportCode, reportCode));
        List<ReportDashboardWidgetDto> widgets = dto.getWidgets();

//        List<ReportDashboardWidget> reportDashboardWidgetList = new ArrayList<>();
        for (int i = 0; i < widgets.size(); i++) {
        	BsReportDashboardWidget reportDashboardWidget = new BsReportDashboardWidget();
            ReportDashboardWidgetDto reportDashboardWidgetDto = widgets.get(i);
            String type = reportDashboardWidgetDto.getType();
            ReportDashboardWidgetValueDto value = reportDashboardWidgetDto.getValue();
            reportDashboardWidget.setReportCode(reportCode);
            reportDashboardWidget.setType(type);
            reportDashboardWidget.setSetup(value.getSetup() != null ? JSONObject.toJSONString(value.getSetup()) : "");
            reportDashboardWidget.setData(value.getData() != null ? JSONObject.toJSONString(value.getData()) : "");
            reportDashboardWidget.setPosition(value.getPosition() != null ? JSONObject.toJSONString(value.getPosition()) : "");
            reportDashboardWidget.setCollapse(value.getCollapse() != null ? JSONObject.toJSONString(value.getCollapse()) : "");
            reportDashboardWidget.setOptions(reportDashboardWidgetDto.getOptions() != null ? JSONObject.toJSONString(reportDashboardWidgetDto.getOptions()) : "");
            reportDashboardWidget.setStatus("1");
            reportDashboardWidget.setSort((int)(i + 1));

            //兼容底层，不采用批量插入
            reportDashboardWidgetService.save(reportDashboardWidget);

//            reportDashboardWidgetList.add(reportDashboardWidget);
        }
//        reportDashboardWidgetService.insertBatch(reportDashboardWidgetList);

    }

    @Override
    public Object getChartData(ChartDto dto) {
//        String chartType = dto.getChartType();
        DataSetDto setDto = new DataSetDto();
        setDto.setSetCode(dto.getSetCode());
        setDto.setContextData(dto.getContextData());
        OriginalDataDto result = dataSetService.getData(setDto);
        List<JSONObject> data = result.getData();
        //处理时间轴
        List<JSONObject> resultData = buildTimeLine(data, dto);
        return resultData;
//        return getTarget(chartType).transform(dto, result.getData());
    }

    /**
     * 导出大屏，zip文件
     *
     * @param request
     * @param response
     * @param reportCode
     * @return
     * @throws Exception 
     */
    @Override
    public ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response, String reportCode, Integer showDataSet) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        boolean isIeBrowser = userAgent.indexOf("MSIE") > 0;

        ReportDashboardObjectDto detail = getDetail(reportCode);
        List<ReportDashboardWidgetDto> widgets = detail.getDashboard().getWidgets();
        detail.setWidgets(widgets);
        detail.getDashboard().setWidgets(null);


        //1.组装临时目录,/app/disk/upload/zip/临时文件夹
        String path = dictPath + ZIP_PATH + UuidUtil.generateShortUuid();

        //将涉及到的图片保存下来（1.背景图，2.组件为图片的）
        String backgroundImage = detail.getDashboard().getBackgroundImage();
        zipLoadImage(backgroundImage, path);
        detail.getWidgets().stream()
                .filter(reportDashboardWidgetDto -> "widget-image".equals(reportDashboardWidgetDto.getType()))
                .forEach(reportDashboardWidgetDto -> {
                    String imageAddress = reportDashboardWidgetDto.getValue().getSetup().getString("imageAdress");
                    try {
						zipLoadImage(imageAddress, path);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                });

        //showDataSet == 0 代表不包含数据集
        if (0 == showDataSet) {
            detail.getWidgets().forEach(reportDashboardWidgetDto -> {
                ReportDashboardWidgetValueDto value = reportDashboardWidgetDto.getValue();
                JSONObject data = value.getData();
                if (null != data && data.containsKey("dataType")) {
                    reportDashboardWidgetDto.getValue().getData().put("dataType", "staticData");
                }
            });
        }


        //2.将大屏设计到的json文件保存
        String jsonPath = path + "/" + JSON_PATH;
        FileUtil.WriteStringToFile(jsonPath, JSONObject.toJSONString(detail));


        //将path文件夹打包zip
        String zipPath = path + ".zip";
        FileUtil.compress(path, zipPath);


        File file = new File(zipPath);
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentLength(file.length());
        //application/octet-stream 二进制数据流（最常见的文件下载）
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        if (isIeBrowser) {
            builder.header("Content-Disposition", "attachment; filename=" + reportCode + ".zip");
        } else {
            builder.header("Content-Disposition", "attacher; filename*=UTF-8''" + reportCode + ".zip");
        }

        ResponseEntity<byte[]> body = builder.body(FileUtils.readFileToByteArray(file));

        //删除zip文件
        file.delete();
        //删除path临时文件夹
        FileUtil.delete(path);
        log.info("删除临时文件：{}，{}", zipPath, path);

        //异步统计下载次数
        CompletableFuture.runAsync(() -> {
            log.info("=======>ip:{} 下载模板：{}", RequestUtil.getIpAddr(request), reportCode);
            reportService.downloadStatistics(reportCode);
        });


        return body;
    }

    /**
     * 导入大屏zip
     *
     * @param file
     * @param reportCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDashboard(MultipartFile file, String reportCode) {
        log.info("导入开始,{}", reportCode);
        //1.组装临时目录,/app/disk/upload/zip/临时文件夹
        String path = dictPath + ZIP_PATH + UuidUtil.generateShortUuid();
        //2.解压
        FileUtil.decompress(file, path);
        // path/uuid/
        File parentPath = new File(path);
        //获取打包的第一层目录
        File firstFile = parentPath.listFiles()[0];

        File[] files = firstFile.listFiles();

        //定义map
        Map<String, String> fileMap = new HashMap<>();
        String content = "";

        for (int i = 0; i < files.length; i++) {
            File childFile = files[i];
            if (JSON_PATH.equals(childFile.getName())) {
                //json文件
                content = FileUtil.readFile(childFile);
            } else if ("image".equals(childFile.getName())) {
                File[] imageFiles = childFile.listFiles();
                //所有需要上传的图片
                for (File imageFile : imageFiles) {
                    //查看是否存在此image
                    String fileName = imageFile.getName().split("\\.")[0];
                    //根据fileId，从bs_file中读出filePath
                    LambdaQueryWrapper<BsFile> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(BsFile::getFileId, fileName);
                    BsFile bsFile = bsFileService.getOne(queryWrapper);
                    String uploadPath;
                    if (null == bsFile) {
                    	if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
                    		uploadPath = CommonUtils.uploadLocal(FileUtil.getMultipartFile(imageFile),"/bs",uploadpath);
                            
                        }else{
                        	uploadPath = CommonUtils.upload(FileUtil.getMultipartFile(imageFile), "/bs", uploadType);
                        }
                    	if(StringUtils.isNotBlank(uploadPath)) {
                    		String filename = FileUtil.getFileName(uploadPath);
                    		// 保存到文件管理中
                            BsFile bsfile = new BsFile();
                            
                            bsfile.setFileId(FileUtil.getFilePrefixName(filename));
                            bsfile.setFilePath(filename);
                            bsfile.setUrlPath(FileUtil.getFilePath(uploadPath));
                            bsfile.setFileType(FileUtil.getFileSuffixName(filename).replace(".", ""));
                            bsfile.setFileInstruction(filename);
                            bsFileService.save(bsfile);
                    	}
                    }else {
                        uploadPath = bsFile.getUrlPath();
                    }
                    fileMap.put(fileName, uploadPath);
                }
            }

        }


        //解析cotent
        ReportDashboardObjectDto detail = JSONObject.parseObject(content, ReportDashboardObjectDto.class);
        //将涉及到的图片路径替换（1.背景图，2.组件为图片的）
        String backgroundImage = detail.getDashboard().getBackgroundImage();
        detail.getDashboard().setBackgroundImage(replaceUrl(backgroundImage, fileMap));
        detail.getWidgets().stream()
                .filter(reportDashboardWidgetDto -> "widget-image".equals(reportDashboardWidgetDto.getType()))
                .forEach(reportDashboardWidgetDto -> {
                    String imageAddress = reportDashboardWidgetDto.getValue().getSetup().getString("imageAdress");
                    String address = replaceUrl(imageAddress, fileMap);
                    reportDashboardWidgetDto.getValue().getSetup().put("imageAdress", address);
                    reportDashboardWidgetDto.getOptions().getJSONArray("setup").getJSONObject(4).put("value", address);
                });
        //将新的大屏编码赋值
        detail.setReportCode(reportCode);

        //解析结束，删除临时文件夹
        FileUtil.delete(path);

        log.info("解析成功，开始存入数据库...");
        insertDashboard(detail);
    }


    private String replaceUrl(String imageAddress, Map<String, String> fileMap) {
        if (StringUtils.isBlank(imageAddress)) {
            return "";
        }
        String fileId = imageAddress.substring(imageAddress.trim().length() - 36);
        String orDefault = fileMap.getOrDefault(fileId, null);
        if (StringUtils.isBlank(orDefault)) {
            return imageAddress;
        }
        return orDefault;
    }

    /**
     * 将大屏涉及到的图片存入指定文件夹
     * @param imageAddress
     * @param path
     * @throws Exception 
     */
    private void zipLoadImage(String imageAddress, String path) throws Exception {
        if (imageAddress.trim().startsWith(fileDownloadPath)) {
            //以fileDownloadPath为前缀的代表为上传的图片
            String fileName = imageAddress.substring(fileDownloadPath.length() + 1);
            String fileId = FileUtil.getFilePrefixName(fileName);
            //根据fileId，从bs_file中读出filePath
            LambdaQueryWrapper<BsFile> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(BsFile::getFileId, fileId);
            BsFile bsFile = bsFileService.getOne(queryWrapper);
            if (null != bsFile) {
                byte[] file = bsFileService.getFile(bsFile.getFileId());
                path = path + "/image/";
                FileUtil.byte2File(file, path, bsFile.getFileId().concat(".").concat(bsFile.getFileType()));
            }
        }

    }

    public ChartStrategy getTarget(String type) {
        for (String s : queryServiceImplMap.keySet()) {
            if (s.contains(type)) {
                return queryServiceImplMap.get(s);
            }
        }
         Result.error(ResponseCode.RULE_CONTENT_NOT_EXIST);
         return null;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, ChartStrategy> beanMap = applicationContext.getBeansOfType(ChartStrategy.class);
        //遍历该接口的所有实现，将其放入map中
        for (ChartStrategy serviceImpl : beanMap.values()) {
            queryServiceImplMap.put(serviceImpl.type(), serviceImpl);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 解析图层数据
     *
     * @param dto
     */
    public void analysisData(ReportDashboardWidgetValueDto dto) {
//        if (StringUtils.isBlank(reportDashboardWidgetDto.getSetCode())) {
//            return;
//        }
//        DataSetDto dto = new DataSetDto();
//        dto.setSetCode(reportDashboardWidgetDto.getSetCode());
//        if (reportDashboardWidgetDto.getContextData() != null && reportDashboardWidgetDto.getContextData().size() > 0) {
//            dto.setContextData(reportDashboardWidgetDto.getContextData());
//        }
//        OriginalDataDto data = dataSetService.getData(dto);
//        reportDashboardWidgetDto.setData(JSONObject.toJSONString(data.getData()));
    }


    public List<JSONObject> buildTimeLine(List<JSONObject> data, ChartDto dto) {
        Map<String, String> chartProperties = dto.getChartProperties();
        if (null == chartProperties || chartProperties.size() < 1) {
            return data;
        }
        Map<String, Object> contextData = dto.getContextData();
        if (null == contextData || contextData.size() < 1) {
            return data;
        }
        if (contextData.containsKey("startTime") && contextData.containsKey("endTime")) {
            dto.setStartTime(contextData.get("startTime").toString());
            dto.setEndTime(contextData.get("endTime").toString());
        }
        if (StringUtils.isBlank(dto.getStartTime()) || StringUtils.isBlank(dto.getEndTime())) {
            return data;
        }
        //获取时间轴字段和解析时间颗粒度

        for (Map.Entry<String, String> entry : chartProperties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            dto.setParticles(value);
            setTimeLineFormat(dto);
            if (StringUtils.isNotBlank(dto.getDataTimeFormat())) {
                dto.setTimeLineFiled(key);
                break;
            }

        }


        if (StringUtils.isBlank(dto.getDataTimeFormat())) {
            return data;
        }

        Date beginTime = DateUtil.parseHmsTime(dto.getStartTime());
        Date endTime = DateUtil.parseHmsTime(dto.getEndTime());
        SimpleDateFormat showFormat = new SimpleDateFormat(dto.getTimeLineFormat());
        SimpleDateFormat dataFormat = new SimpleDateFormat(dto.getDataTimeFormat());


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endTime);

        List<String> timeLine = new ArrayList<>();
        List<String> dataTimeline = new ArrayList<>();
        timeLine.add(showFormat.format(calendar.getTime()));
        dataTimeline.add(dataFormat.format(calendar.getTime()));

        //添加时间轴数据
        while (true) {
            calendar.add(dto.getTimeUnit(), 1);
            timeLine.add(showFormat.format(calendar.getTime()));
            dataTimeline.add(dataFormat.format(calendar.getTime()));
            if (showFormat.format(calendar.getTime()).equals(showFormat.format(calendarEnd.getTime()))) {
                break;
            }
        }

        //根据时间轴生成对应的时间线，数据不存在，补数据
        List<JSONObject> result = new ArrayList<>();
        JSONObject jsonDemo = data.get(0);
        String timeLineFiled = dto.getTimeLineFiled();
        for (String dateFormat : dataTimeline) {
            boolean flag = true;
            for (JSONObject datum : data) {
                if (datum.containsKey(timeLineFiled) && datum.getString(timeLineFiled).equals(dateFormat)) {
                    result.add(datum);
                    flag = false;
                }
            }
            if (flag) {
                //补数据
                JSONObject json = new JSONObject();
                jsonDemo.forEach((s, o) -> {
                    if (s.equals(timeLineFiled)) {
                        json.put(timeLineFiled, dateFormat);
                    } else {
                        json.put(s, 0);
                    }
                });
                result.add(json);
            }

        }
        return result;
    }

    //设置时间格式
    private void setTimeLineFormat(ChartDto dto) {
        String particles = dto.getParticles();
        if ("xAxis-hour".equals(particles)) {
            dto.setDataTimeFormat("yyyy-MM-dd HH");
            dto.setTimeLineFormat("MM-dd HH");
            dto.setTimeUnit(Calendar.HOUR);
        } else if ("xAxis-day".equals(particles)) {
            dto.setDataTimeFormat("yyyy-MM-dd");
            dto.setTimeLineFormat("yyyy-MM-dd");
            dto.setTimeUnit(Calendar.DATE);
        } else if ("xAxis-month".equals(particles)) {
            dto.setDataTimeFormat("yyyy-MM");
            dto.setTimeLineFormat("yyyy-MM");
            dto.setTimeUnit(Calendar.MONTH);
        } else if ("xAxis-year".equals(particles)) {
            dto.setDataTimeFormat("yyyy");
            dto.setTimeLineFormat("yyyy");
            dto.setTimeUnit(Calendar.YEAR);
        }
    }
}
