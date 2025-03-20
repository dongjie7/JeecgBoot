package org.jeecg.modules.flowable.mapper;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.entity.vo.LinkDownCateCode;
import org.jeecg.modules.flowable.entity.vo.SysCateDictCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-08-26 11:04
 * @description:
 **/

@Mapper
public interface HandongYZCommonMapper {

    //公共方法  数据字典
    List<SysDictItem> allSysDictItemInfo(@Param("dictCode") String dictCode);

    List<SysCategory>  customDictData(@Param("dictField") String dictField, @Param("dictText") String dictText, @Param("dictTable") String dictTable);

    List<SysCateDictCode> backfieldDataCateDictCode(@Param("parentId") String parentId);

    List<SysCateDictCode> backfieldDataCateDictCodeTwo(@Param("parentCode") String parentCode);

    List<SysCateDictCode> backfieldSelTreeDataCateDictCode(@Param("dataId") String dataId,@Param("dataParentId") String dataParentId,@Param("dictField") String dictField,@Param("dictText") String dictText,@Param("dictTable") String dictTable);

    List<LinkDownCateCode> backfieldSelTreeDataCateDictCodeLinkDown(@Param("dataId") String dataId,@Param("dataParentId") String dataParentId,@Param("dictField") String dictField,@Param("dictText") String dictText,@Param("dictTable") String dictTable);
}
