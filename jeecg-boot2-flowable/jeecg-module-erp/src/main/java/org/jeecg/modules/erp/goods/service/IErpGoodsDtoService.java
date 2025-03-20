package org.jeecg.modules.erp.goods.service;

import org.jeecg.modules.erp.goods.dto.ErpGoodsDto;
import org.jeecg.modules.erp.goods.vo.QueryGoodsVo;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: erp_goods_dto
 * @Author: nbacheng
 * @Date:   2023-02-09
 * @Version: V1.0
 */
public interface IErpGoodsDtoService extends IService<ErpGoodsDto> {
	IPage<ErpGoodsDto> queryGoodsList(Page<ErpGoodsDto> page, QueryGoodsVo queryGoodsVo);
	List<ErpGoodsDto> queryByIds(String ids);
}
