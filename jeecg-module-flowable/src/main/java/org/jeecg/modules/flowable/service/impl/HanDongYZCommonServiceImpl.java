package org.jeecg.modules.flowable.service.impl;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.entity.vo.LinkDownCateCode;
import org.jeecg.modules.flowable.entity.vo.SysCateDictCode;
import org.jeecg.modules.flowable.mapper.HandongYZCommonMapper;
import org.jeecg.modules.flowable.service.HanDongYZCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-08-26 11:07
 * @description:
 **/

@Service
public class HanDongYZCommonServiceImpl implements HanDongYZCommonService {

    @Autowired
    HandongYZCommonMapper handongYZCommonMapper;

    @Override
    public List<SysDictItem> allSysDictItemInfo(String dictCode) {
        return handongYZCommonMapper.allSysDictItemInfo(dictCode);
    }

    @Override
    public List<SysCategory> customDictData(String dictField, String dictText, String dictTable) {
        return handongYZCommonMapper.customDictData(dictField,dictText,dictTable);
    }

    @Override
    public List<SysCateDictCode> backfieldDataCateDictCode(String parentId) {
        return handongYZCommonMapper.backfieldDataCateDictCode(parentId);
    }

    @Override
    public List<SysCateDictCode> backfieldDataCateDictCodeTwo(String parentCode) {
        return handongYZCommonMapper.backfieldDataCateDictCodeTwo(parentCode);
    }

    @Override
    public List<SysCateDictCode> backfieldSelTreeDataCateDictCode(String dataId,String dataParentId,String dictField, String dictText, String dictTable) {

        return handongYZCommonMapper.backfieldSelTreeDataCateDictCode(dataId,dataParentId,dictField,dictText,dictTable);
    }

    @Override
    public List<LinkDownCateCode> backfieldSelTreeDataCateDictCodeLinkDown(String dataId, String dataParentId, String dictField, String dictText, String dictTable) {
      return handongYZCommonMapper.backfieldSelTreeDataCateDictCodeLinkDown(dataId,dataParentId,dictField,dictText,dictTable);
    }


}
