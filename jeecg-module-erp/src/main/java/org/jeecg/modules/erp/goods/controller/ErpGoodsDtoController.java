package org.jeecg.modules.erp.goods.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;

import org.jeecg.modules.erp.goods.dto.ErpGoodsDto;
import org.jeecg.modules.erp.goods.service.IErpGoodsDtoService;
import org.jeecg.modules.erp.goods.vo.QueryGoodsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: erp_goods_dto
 * @Author: nbacheng
 * @Date:   2023-02-09
 * @Version: V1.0
 */
@Api(tags="erp_goods_dto")
@RestController
@RequestMapping("/goods/erpGoodsDto")
@Slf4j
public class ErpGoodsDtoController extends JeecgController<ErpGoodsDto, IErpGoodsDtoService> {
	@Autowired
	private IErpGoodsDtoService erpGoodsDtoService;
	
	
	/**
	 * 分页列表查询
	 *
	 * @param erpGoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_goods-商品信息列表查询")
	@ApiOperation(value="erp_goods-商品信息列表查询", notes="erp_goods-商品信息列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryGoodsList(QueryGoodsVo queryGoodsvo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<ErpGoodsDto> page = new Page<ErpGoodsDto>(pageNo, pageSize);
		IPage<ErpGoodsDto> pageList = erpGoodsDtoService.queryGoodsList(page, queryGoodsvo);
		return Result.OK(pageList);
	}
	
	/**
	 * 通过ids查询
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品-通过ids查询")
	@ApiOperation(value="商品-通过ids查询", notes="商品-通过ids查询")
	@GetMapping(value = "/queryByIds")
	public Result<?> queryByIds(@RequestParam(name="ids",required=true) String ids) {
		List<ErpGoodsDto> listErpGoodsDto = erpGoodsDtoService.queryByIds(ids);
		if(listErpGoodsDto.size()==0) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(listErpGoodsDto);
	}
	

}
