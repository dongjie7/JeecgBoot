package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportShare;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏分享表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
public interface IBsReportShareService extends IService<BsReportShare> {

	/***
     * 查询详情
     *
     * @param id
     * @return
     */
	BsReportShare getDetail(Long id);

    ReportShareDto insertShare(ReportShareDto dto);

    BsReportShare detailByCode(String shareCode);

    /**
     * 延期过期时间
     * @param dto
     */
    void shareDelay(ReportShareDto dto);
}
