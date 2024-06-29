package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectInfo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目自定义信息表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwProjectInfoService extends IService<TwProjectInfo> {

	List<Map> getProjectInfoByProjectId(String projectId);

}
