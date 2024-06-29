package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportShare;
import org.jeecg.modules.estar.bs.enums.EnableFlagEnum;
import org.jeecg.modules.estar.bs.mapper.BsReportShareMapper;
import org.jeecg.modules.estar.bs.service.IBsReportShareService;
import org.jeecg.modules.estar.bs.util.DateUtil;
import org.jeecg.modules.estar.bs.util.JwtUtil;
import org.jeecg.modules.estar.bs.util.MD5Util;
import org.jeecg.modules.estar.bs.util.UuidUtil;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大屏分享表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Service
public class BsReportShareServiceImpl extends ServiceImpl<BsReportShareMapper, BsReportShare> implements IBsReportShareService {

	private static final String SHARE_BSFLAG = "/bs/";
    private static final String SHARE_ELFLAG = "/el/";

    private static final String REPORT = "1";
    private static final String EXCEL = "2";
    /**
     * 默认跳转路由为bs的页面
     */
    private static final String SHARE_FLAG = "/bs/";

    private static final String SHARE_URL = "/estar";

    @Autowired
    private BsReportShareMapper reportShareMapper;


    @Override
    public BsReportShare getDetail(Long id) {
    	QueryWrapper<BsReportShare> queryWrapper = new QueryWrapper<BsReportShare>();
    	queryWrapper.eq("id", id);
    	BsReportShare reportShare = this.getOne(queryWrapper);
        return reportShare;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportShareDto insertShare(ReportShareDto dto) {
        //设置分享码
        if (dto.isSharePasswordFlag()) {
            dto.setSharePassword(UuidUtil.getRandomPwd(4));
        }

        ReportShareDto reportShareDto = new ReportShareDto();
        BsReportShare entity = new BsReportShare();
        BeanUtils.copyProperties(dto, entity);
        init(entity);
        save(entity);
        //将分享链接返回
        reportShareDto.setShareUrl(entity.getShareUrl());
        reportShareDto.setSharePassword(dto.getSharePassword());
        return reportShareDto;
    }

    @Override
    public BsReportShare detailByCode(String shareCode) {
        LambdaQueryWrapper<BsReportShare> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BsReportShare::getShareCode, shareCode);
        wrapper.eq(BsReportShare::getStatus, EnableFlagEnum.ENABLE.getCodeValue());
        BsReportShare reportShare = this.getOne(wrapper);
        if (null == reportShare) {
        	Result.error(ResponseCode.REPORT_SHARE_LINK_INVALID);
        	return null;
        }
        //解析jwt token，获取密码
        String password = JwtUtil.getPassword(reportShare.getShareToken());
        if (StringUtils.isNotBlank(password)) {
            //md5加密返回
            reportShare.setSharePassword(MD5Util.encrypt(password));
        }
        return reportShare;
    }

    /**
     * 延期过期时间
     *
     * @param dto
     */
    @Override
    public void shareDelay(ReportShareDto dto) {
    	Integer shareValidType = dto.getShareValidType();
        if (null == dto.getId() || null == shareValidType) {
        	Result.error("入参不完整");
        	return;
        }
        QueryWrapper<BsReportShare> queryWrapper = new QueryWrapper<BsReportShare>();
    	queryWrapper.eq("id", dto.getId());
    	BsReportShare entity = this.getOne(queryWrapper);
        entity.setShareValidTime(DateUtil.getFutureDateTmdHmsByTime(entity.getShareValidTime(), shareValidType));
        entity.setShareToken(JwtUtil.createToken(entity.getReportCode(), entity.getShareCode(), entity.getSharePassword(), entity.getShareValidTime()));
        updateById(entity);
    }
    /**
     * 新增初始化
     * @param entity
     */
    private void init(BsReportShare entity) {
        String shareCode = UuidUtil.generateShortUuid();
        entity.setShareCode(shareCode);


        if (REPORT.equals(entity.getReportType())) {
            if (entity.getShareUrl().contains(SHARE_URL)) {
                String prefix = entity.getShareUrl().substring(0, entity.getShareUrl().indexOf(SHARE_URL));
                entity.setShareUrl(prefix + SHARE_BSFLAG + shareCode);
            }else {
                entity.setShareUrl(entity.getShareUrl() + SHARE_BSFLAG + shareCode);
            }
        }else if (EXCEL.equals(entity.getReportType())) {
            if (entity.getShareUrl().contains(SHARE_URL)) {
                String prefix = entity.getShareUrl().substring(0, entity.getShareUrl().indexOf(SHARE_URL));
                entity.setShareUrl(prefix + SHARE_ELFLAG + shareCode);
            }else {
                entity.setShareUrl(entity.getShareUrl() + SHARE_ELFLAG + shareCode);
            }
        }else {
            return;
        }

        entity.setShareValidTime(DateUtil.getFutureDateTmdHms(entity.getShareValidType()));
        entity.setShareToken(JwtUtil.createToken(entity.getReportCode(), shareCode, entity.getSharePassword(), entity.getShareValidTime()));
    }
}
