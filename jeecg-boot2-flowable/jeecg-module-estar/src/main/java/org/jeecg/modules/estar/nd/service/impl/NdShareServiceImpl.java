package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.dto.ShareFileDTO;
import org.jeecg.modules.estar.nd.dto.ShareListDTO;
import org.jeecg.modules.estar.nd.entity.NdShare;
import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.mapper.NdShareMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdShareService;
import org.jeecg.modules.estar.nd.service.INdSharefileService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.vo.ShareFileVO;
import org.jeecg.modules.estar.nd.vo.ShareListVO;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 网盘分享表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class NdShareServiceImpl extends ServiceImpl<NdShareMapper, NdShare> implements INdShareService {

	@Resource
	NdShareMapper shareMapper;
	@Resource
	INdSharefileService shareFileService;
	//@Resource
	//INdShareService shareService;
	//@Resource
	//INdUserfileService userFileService;
	@Resource
	NdUserfileMapper userFileMapper;
	@Resource
	FileDealComp fileDealComp;
	
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public List<ShareListVO> selectShareList(ShareListDTO shareListDTO, String username) {
		Long beginCount = (shareListDTO.getCurrentPage() - 1) * shareListDTO.getPageCount();
        return shareMapper.selectShareList(shareListDTO.getShareFilePath(),
                shareListDTO.getShareBatchNum(),
                beginCount, shareListDTO.getPageCount(), username);
	}

	@Override
	public ShareFileVO shareFile(ShareFileDTO shareSecretDTO) {
		ShareFileVO shareSecretVO = new ShareFileVO();
		SysUser loginUser = iEstarThirdService.getLoginUser();

        String uuid = UUID.randomUUID().toString().replace("-", "");
        NdShare share = new NdShare();
        BeanUtil.copyProperties(shareSecretDTO, share);
        share.setCreateTime(new Date());
        share.setCreateBy(loginUser.getUsername());
        share.setSharestatus(0);
        if (shareSecretDTO.getSharetype() == 1) {
            String extractionCode = RandomUtil.randomNumbers(6);
            share.setExtractioncode(extractionCode);
            shareSecretVO.setExtractionCode(share.getExtractioncode());
        }

        share.setSharebatchnum(uuid);
        //shareService.save(share);
        shareMapper.insert(share);

        List<NdSharefile> fileList = JSON.parseArray(shareSecretDTO.getFiles(), NdSharefile.class);
        List<NdSharefile> saveFileList = new ArrayList<>();
        for (NdSharefile shareFile : fileList) {
        	//NdUserfile userFile = userFileService.getById(shareFile.getUserfileid());
        	NdUserfile userFile = userFileMapper.selectById(shareFile.getUserfileid());
            if (userFile.getCreateBy().compareTo(loginUser.getUsername()) != 0) {
            	Result.error("您只能分享自己的文件");
                return null;
            }
            if (userFile.getIsdir() == 1) {
            	EstarFile estarFile = new EstarFile(userFile.getFilepath(), userFile.getFilename(), true);
                //List<NdUserfile> userfileList = userFileService.selectUserFileByLikeRightFilePath(estarFile.getPath(), loginUser.getUsername());
                List<NdUserfile> userfileList = userFileMapper.selectUserFileByLikeRightFilePath(estarFile.getPath(), loginUser.getUsername());
                for (NdUserfile userFile1 : userfileList) {
                	NdSharefile shareFile1 = new NdSharefile();
                    shareFile1.setUserfileid(userFile1.getId());
                    shareFile1.setSharebatchnum(uuid);
                    shareFile1.setSharefilepath(userFile1.getFilepath().replaceFirst(userFile.getFilepath().equals("/") ? "" : userFile.getFilepath(), ""));
                    saveFileList.add(shareFile1);
                }
            }
            shareFile.setSharefilepath("/");
            shareFile.setSharebatchnum(uuid);
            saveFileList.add(shareFile);


        }
        for (NdSharefile saveFile : saveFileList) {
        	shareFileService.save(saveFile);
        }
        shareSecretVO.setShareBatchNum(uuid);
        return shareSecretVO;
	}

}
