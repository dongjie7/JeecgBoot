package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.dto.OriginalDataDto;
import org.jeecg.modules.estar.bs.entity.BsDataSet;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bs_data_set
 * @Author: nbacheng
 * @Date:   2023-03-20
 * @Version: V1.0
 */
public interface IBsDataSetService extends IService<BsDataSet> {
	/**
    *
    * @param dto
    * @return
    */
   OriginalDataDto testTransform(DataSetDto dto);
   
   /**
    * 获取数据
    * @param dto
    * @return
    */
   OriginalDataDto getData(DataSetDto dto);
   
   /**
    * 单条详情
    * @param setCode
    * @return
    */
   DataSetDto detailSet(String setCode);

   /**
    * 获取所有数据集
    * @return
    */
   List<BsDataSet> queryAllDataSet();

   /**
    * 单条详情
    *
    * @param id
    * @return
    */
   DataSetDto detailSet(Long id);
}
