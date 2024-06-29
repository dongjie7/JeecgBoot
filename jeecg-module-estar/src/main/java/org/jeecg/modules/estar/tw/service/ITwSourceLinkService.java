package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwSourceLink;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目资源关联表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwSourceLinkService extends IService<TwSourceLink> {
	public List<Map> getSourceLinkByLinkIdAndType(String linkId, String linkType);

	public Result<?> sourceDel(Map<String, Object> mmap);
}
