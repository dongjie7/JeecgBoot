package org.jeecg.modules.erp.goods.mapper;

import org.jeecg.modules.erp.goods.dto.ErpGoodsDto;
import org.jeecg.modules.erp.goods.vo.QueryGoodsVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: erp_goods_dto
 * @Author: nbacheng
 * @Date:   2023-02-09
 * @Version: V1.0
 */
public interface ErpGoodsDtoMapper extends BaseMapper<ErpGoodsDto> {
	 List<ErpGoodsDto> queryGoodsList(Page<ErpGoodsDto> page, @Param("vo") QueryGoodsVo vo);
	 public List<ErpGoodsDto> getByIds(@Param("idArray") String[] idArray);
}
