package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.mapper.NdSharefileMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdSharefileService;
import org.jeecg.modules.estar.nd.vo.ShareFileListVO;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 网盘分享文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class NdSharefileServiceImpl extends ServiceImpl<NdSharefileMapper, NdSharefile> implements INdSharefileService {

	@Resource
	NdSharefileMapper shareFileMapper;
    @Resource
    NdUserfileMapper userFileMapper;
	
	@Override
	public List<ShareFileListVO> selectShareFileList(String shareBatchNum, String filePath) {
		return shareFileMapper.selectShareFileList(shareBatchNum, filePath);
	}

}
