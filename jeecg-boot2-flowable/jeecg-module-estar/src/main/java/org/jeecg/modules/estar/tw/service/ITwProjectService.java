package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.dto.ProjectDto;
import org.jeecg.modules.estar.tw.entity.TwProject;

import java.util.Map;

import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwProjectService extends IService<TwProject> {

	void saveProject(TwProject twProject);

	void recycle(String id);

	void recovery(String id);

	Result<?> collect(String id,String type);
	
	IPage<TwProject> queryCollectList(Page<TwProject> page,String userId);

	TwProject projectSet(String id);

	boolean quitProject(String id);

	void archive(String id);

	void recoveryArchive(String id);

	Result<?> querySelfList(ProjectDto projectDto);
	
	public IPage<Map> getMemberProjects(IPage<Map> page,Map params);
	public TwProject getProjectById(String id);
	
	public TwProject getProjectByIdNotDel(String id);

	Result<?> getLogBySelfProject(@RequestParam Map<String,Object> mmap);

	Result<?> projectStats(String projectId);

	Result<?> getProjectReport(String projectId);
	

}
