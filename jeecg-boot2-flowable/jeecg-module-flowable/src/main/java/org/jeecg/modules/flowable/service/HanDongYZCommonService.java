package org.jeecg.modules.flowable.service;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.entity.vo.LinkDownCateCode;
import org.jeecg.modules.flowable.entity.vo.SysCateDictCode;

import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-08-26 11:01
 * @description:
 **/
public interface HanDongYZCommonService {


    //公共方法  数据字典
    List<SysDictItem> allSysDictItemInfo(String dictCode);

    List<SysCategory>  customDictData(String dictField, String dictText, String dictTable);

    List<SysCateDictCode> backfieldDataCateDictCode(String parentId);

    List<SysCateDictCode> backfieldDataCateDictCodeTwo(String parentCode);


    List<SysCateDictCode> backfieldSelTreeDataCateDictCode(String dataId,String dataParentId,String dictField, String dictText, String dictTable);


    List<LinkDownCateCode> backfieldSelTreeDataCateDictCodeLinkDown(String dataId,String dataParentId,String dictField, String dictText, String dictTable);
}
