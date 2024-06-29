package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwSourceLink;
import org.jeecg.modules.estar.tw.mapper.TwSourceLinkMapper;
import org.jeecg.modules.estar.tw.service.ITwFileService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwSourceLinkService;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目资源关联表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwSourceLinkServiceImpl extends ServiceImpl<TwSourceLinkMapper, TwSourceLink> implements ITwSourceLinkService {

	@Autowired
	ITwTaskStagesService taskStagesService;
	@Autowired
	ITwProjectService projectService;
	@Autowired
	ITwFileService fileService;
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	@Override
	public List<Map> getSourceLinkByLinkIdAndType(String linkId, String linkType) {
		return baseMapper.selectSourceLinkByLinkIdAndType(linkId,linkType);
	}

	@Override
	@Transactional
	public Result<?> sourceDel(Map<String, Object> mmap) {
		String sourceId = MapUtils.getString(mmap,"sourceId");
        SysUser loginUser = iEstarThirdService.getLoginUser();
        String memberId = loginUser.getUsername();
        if(StringUtils.isEmpty(sourceId)){
            return Result.error("资源不存在！");
        }
        TwSourceLink sourceLinkDetail = getSourceDetail(sourceId);
        int i = delSourceLinkById(sourceId);
        if("task".equals(sourceLinkDetail.getLinkType())){
        	taskStagesService.taskHook(memberId,sourceLinkDetail.getLinkId(),"unlinkFile","",0,
                    "","","",new HashMap(){{
                        put("title",sourceLinkDetail.getTitle());
                        put("url",MapUtils.getString(sourceLinkDetail.getSourceDetail(),"file_url"));
                    }},null);
        }
		return Result.OK(i);
	}

	private int delSourceLinkById(String sourceId) {
		LambdaQueryWrapper<TwSourceLink> ldQueryWrappe = new LambdaQueryWrapper<>();
		ldQueryWrappe.eq(TwSourceLink::getId,sourceId);
        return baseMapper.delete(ldQueryWrappe);
	}

	private TwSourceLink getSourceDetail(String sourceId) {
		TwSourceLink sourceLink = getSourceLinkById(sourceId);
        String source_type = sourceLink.getSourceType();
        Map sourceDetail = new HashMap();
        if("file".equals(source_type)){
            sourceLink.setTitle("");
            sourceDetail = fileService.getFileById(sourceLink.getSourceId());
            if(MapUtils.isNotEmpty(sourceDetail)){
                sourceLink.setTitle(MapUtils.getString(sourceDetail,"title"));
                TwProject project=projectService.getProjectById(MapUtils.getString(sourceDetail,"project_id"));
                sourceDetail.put("projectName",project.getName());
            }
        }
        sourceLink.setSourceDetail(sourceDetail);
        return sourceLink;
	}

	private TwSourceLink getSourceLinkById(String sourceId) {
		LambdaQueryWrapper<TwSourceLink> ldQueryWrappe = new LambdaQueryWrapper<>();
		ldQueryWrappe.eq(TwSourceLink::getId,sourceId);
        return baseMapper.selectOne(ldQueryWrappe);
	}

}
