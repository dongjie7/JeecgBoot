package org.jeecg.modules.estar.nd.controller;

import cn.hutool.core.util.IdUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.dto.EditOfficeFileDTO;
import org.jeecg.modules.estar.nd.dto.PreviewOfficeFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.office.documentserver.managers.history.HistoryManager;
import org.jeecg.modules.estar.nd.office.documentserver.models.enums.Action;
import org.jeecg.modules.estar.nd.office.documentserver.models.enums.Type;
import org.jeecg.modules.estar.nd.office.documentserver.models.filemodel.FileModel;
import org.jeecg.modules.estar.nd.office.services.configurers.FileConfigurer;
import org.jeecg.modules.estar.nd.office.services.configurers.wrappers.DefaultFileWrapper;
import org.jeecg.modules.estar.nd.service.INdFileService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.estar.nd.file.DownloadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.ArrayList;

@Api(tags="office", description = "该接口为Onlyoffice文件操作接口，主要用来做一些文档的编辑，浏览等。")
@RestController
@Slf4j
@RequestMapping({"/office"})
public class OfficeController {
    public static final String CURRENT_MODULE = "Onlyoffice文件操作接口";
    @Resource
    private IEstarThirdService iEstarThirdService;
    @Resource
    NDFactory ndFactory;
    @Resource
    FileDealComp fileDealComp;
    @Value("${nbcio.deployment.host}")
    private String deploymentHost;
    @Value("${server.port}")
    private String port;
    @Value(value="${jeecg.uploadType}")
    private String storageType;

    @Value("${nbcio.files.docservice.url.site}")
    private String docserviceSite;

    @Value("${nbcio.files.docservice.url.api}")
    private String docserviceApiUrl;
    @Autowired
    private FileConfigurer<DefaultFileWrapper> fileConfigurer;

    @Resource
    INdFileService fileService;
    @Resource
    INdUserfileService userFileService;
    @Autowired
    private HistoryManager historyManager;

    @ApiOperation(value = "预览office文件", notes = "预览office文件", tags = {"office"})
    @RequestMapping(value = "/previewofficefile", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> previewOfficeFile(HttpServletRequest request, @RequestBody PreviewOfficeFileDTO previewOfficeFileDTO) {
    	Result<JSONObject> result = new Result<>();
        try {
            String token = request.getHeader("token");
            String previewUrl = request.getScheme() + "://" + deploymentHost + ":" + port + "/filetransfer/preview?userFileId=" + previewOfficeFileDTO.getUserFileId() + "&isMin=false&shareBatchNum=undefined&extractionCode=undefined&token=" + token;
            SysUser loginUser = iEstarThirdService.getLoginUser();

            NdUserfile userFile = userFileService.getById(previewOfficeFileDTO.getUserFileId());
           

            Action action = Action.view;
            Type type = Type.desktop;
            Locale locale = new Locale("zh");
            FileModel fileModel = fileConfigurer.getFileModel(
                    DefaultFileWrapper
                            .builder()
                            .userFile(userFile)
                            .type(type)
                            .lang(locale.toLanguageTag())
                            .action(action)
                            .user(loginUser)
                            .actionData(previewUrl)
                            .build()
            );

            JSONObject jsonObject = new JSONObject();
       
            jsonObject.put("file",fileModel);
//            jsonObject.put("fileHistory", historyManager.getHistory(fileModel.getDocument()));  // get file history and add it to the model
            jsonObject.put("docserviceApiUrl", docserviceSite + docserviceApiUrl);
            jsonObject.put("reportName",userFile.getFilename());
            result.setResult(jsonObject);
            result.setCode(200);
            result.setMessage("获取报告成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setCode(500);
            result.setMessage("服务器错误！");
        }
        return result;
    }
    @ApiOperation(value = "编辑office文件", notes = "编辑office文件", tags = {"office"})
    @ResponseBody
    @RequestMapping(value = "/editofficefile", method = RequestMethod.POST)
    public Result<?> editOfficeFile(HttpServletRequest request, @RequestBody EditOfficeFileDTO editOfficeFileDTO) {
    	Result<JSONObject> result = new Result<>();
        String token = request.getHeader("token");
        String previewUrl = request.getScheme() + "://" + deploymentHost + ":" + port + "/filetransfer/preview?userFileId=" + editOfficeFileDTO.getUserFileId() + "&isMin=false&shareBatchNum=undefined&extractionCode=undefined&token=" + token;
        log.info("editOfficeFile");
        try {
        	SysUser loginUser = iEstarThirdService.getLoginUser();

            NdUserfile userFile = userFileService.getById(editOfficeFileDTO.getUserFileId());

            Action action = Action.edit;
            Type type = Type.desktop;
            Locale locale = new Locale("zh");
            FileModel fileModel = fileConfigurer.getFileModel(
                    DefaultFileWrapper
                            .builder()
                            .userFile(userFile)
                            .type(type)
                            .lang(locale.toLanguageTag())
                            .action(action)
                            .user(loginUser)
                            .actionData(previewUrl)
                            .build()
            );
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("file",fileModel);
            jsonObject.put("docserviceApiUrl", docserviceSite + docserviceApiUrl);
            jsonObject.put("reportName",userFile.getFilename());
            result.setResult(jsonObject);
            result.setCode(200);
            result.setMessage("编辑报告成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setCode(500);
            result.setMessage("服务器错误！");
        }
        return result;
    }


    @RequestMapping(value = "/IndexServlet", method = RequestMethod.POST)
    @ResponseBody
    public void IndexServlet(HttpServletResponse response, HttpServletRequest request) throws IOException {
       // String token = request.getParameter("token");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException();
        }

        PrintWriter writer = response.getWriter();
        Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String body = scanner.hasNext() ? scanner.next() : "";

        JSONObject jsonObj = JSON.parseObject(body);
        log.info("===saveeditedfile:" + jsonObj.get("status")); ;
        String status = jsonObj != null ? jsonObj.get("status").toString() : "";
        if ("2".equals(status) || "6".equals(status)) {
            String type = request.getParameter("type");
            String downloadUri = (String) jsonObj.get("url");

            if("edit".equals(type)){ //修改报告
                String userFileId = request.getParameter("userFileId");
                NdUserfile userFile = userFileService.getById(userFileId);
                NdFile fileBean = fileService.getById(userFile.getFileid());
                Long pointCount = fileService.getFilePointCount(userFile.getFileid());
                String fileUrl = fileBean.getFileurl();
                if (pointCount > 1) {
                    fileUrl = fileDealComp.copyFile(fileBean, userFile);
                }

                URL url = new URL(downloadUri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream stream = connection.getInputStream();
                    fileDealComp.saveFileInputStream(fileBean.getStoragetype(), fileUrl, stream);

                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {

                    int fileLength = connection.getContentLength();
                    log.info("当前修改文件大小为：" + (long) fileLength);

                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.setFileUrl(fileBean.getFileurl());
                    InputStream inputStream = ndFactory.getDownloader(fileBean.getStoragetype()).getInputStream(downloadFile);
                    String md5Str = DigestUtils.md5Hex(inputStream);

                    fileService.updateFileDetail(userFile.getId(), md5Str, fileLength);
                    connection.disconnect();
                }
            }
        }

        if("3".equals(status)||"7".equals(status)) {//不强制手动保存时为6,"6".equals(status)
            log.debug("====保存失败:");
            writer.write("{\"error\":1}");
        }else {
            log.debug("状态为：0") ;
            writer.write("{\"error\":" + "0" + "}");

        }
    }

}