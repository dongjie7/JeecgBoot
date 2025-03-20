package org.jeecg.modules.erp.goods.mapper;

import java.util.List;
import org.jeecg.modules.erp.goods.entity.ErpGoodsPrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 商品价格信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
public interface ErpGoodsPriceMapper extends BaseMapper<ErpGoodsPrice> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ErpGoodsPrice> selectByMainId(@Param("mainId") String mainId);
}
