package org.jeecg.modules.erp.sale.mapper;

import java.util.List;
import org.jeecg.modules.erp.sale.entity.ErpSaleOutDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售出库单明细
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ErpSaleOutDetailMapper extends BaseMapper<ErpSaleOutDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ErpSaleOutDetail> selectByMainId(@Param("mainId") String mainId);
}
