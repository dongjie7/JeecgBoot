package org.jeecg.modules.erp.goods.service.impl;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.erp.goods.entity.ErpGoodsCategory;
import org.jeecg.modules.erp.goods.mapper.ErpGoodsCategoryMapper;
import org.jeecg.modules.erp.goods.service.IErpGoodsCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: erp_goods_category
 * @Author: nbacheng
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Service
public class ErpGoodsCategoryServiceImpl extends ServiceImpl<ErpGoodsCategoryMapper, ErpGoodsCategory> implements IErpGoodsCategoryService {

	@Override
	public void addErpGoodsCategory(ErpGoodsCategory erpGoodsCategory) {
	   //新增时设置hasChild为0
	    erpGoodsCategory.setHasChild(IErpGoodsCategoryService.NOCHILD);
		if(oConvertUtils.isEmpty(erpGoodsCategory.getParentId())){
			erpGoodsCategory.setParentId(IErpGoodsCategoryService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			ErpGoodsCategory parent = baseMapper.selectById(erpGoodsCategory.getParentId());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(erpGoodsCategory);
	}
	
	@Override
	public void updateErpGoodsCategory(ErpGoodsCategory erpGoodsCategory) {
		ErpGoodsCategory entity = this.getById(erpGoodsCategory.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getParentId();
		String new_pid = erpGoodsCategory.getParentId();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				erpGoodsCategory.setParentId(IErpGoodsCategoryService.ROOT_PID_VALUE);
			}
			if(!IErpGoodsCategoryService.ROOT_PID_VALUE.equals(erpGoodsCategory.getParentId())) {
				baseMapper.updateTreeNodeStatus(erpGoodsCategory.getParentId(), IErpGoodsCategoryService.HASCHILD);
			}
		}
		baseMapper.updateById(erpGoodsCategory);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteErpGoodsCategory(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    ErpGoodsCategory erpGoodsCategory = this.getById(idVal);
                    String pidVal = erpGoodsCategory.getParentId();
                    //查询此节点上一级是否还有其他子节点
                    List<ErpGoodsCategory> dataList = baseMapper.selectList(new QueryWrapper<ErpGoodsCategory>().eq("parent_id", pidVal).notIn("id",Arrays.asList(idArr)));
                    if((dataList == null || dataList.size()==0) && !Arrays.asList(idArr).contains(pidVal)
                            && !sb.toString().contains(pidVal)){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            ErpGoodsCategory erpGoodsCategory = this.getById(id);
            if(erpGoodsCategory==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(erpGoodsCategory.getParentId());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<ErpGoodsCategory> queryTreeListNoPage(QueryWrapper<ErpGoodsCategory> queryWrapper) {
        List<ErpGoodsCategory> dataList = baseMapper.selectList(queryWrapper);
        List<ErpGoodsCategory> mapList = new ArrayList<>();
        for(ErpGoodsCategory data : dataList){
            String pidVal = data.getParentId();
            //递归查询子节点的根节点
            if(pidVal != null && !"0".equals(pidVal)){
                ErpGoodsCategory rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IErpGoodsCategoryService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<ErpGoodsCategory>().eq("parent_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IErpGoodsCategoryService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private ErpGoodsCategory getTreeRoot(String pidVal){
        ErpGoodsCategory data =  baseMapper.selectById(pidVal);
        if(data != null && !"0".equals(data.getParentId())){
            return this.getTreeRoot(data.getParentId());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<ErpGoodsCategory> dataList = baseMapper.selectList(new QueryWrapper<ErpGoodsCategory>().eq("parent_id", pidVal));
        if(dataList != null && dataList.size()>0){
            for(ErpGoodsCategory tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
