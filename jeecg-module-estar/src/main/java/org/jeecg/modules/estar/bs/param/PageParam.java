package org.jeecg.modules.estar.bs.param;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 分页参数
 * @author nbacheng
 * @since 2023-03-16
 */
public class PageParam {

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
    /**
     * 页数
     */
    private Integer pageNumber = 1;

    /**
     * 每页的记录数
     */
    private Integer pageSize = 10;

    /**
     * 升序还是降序
     */
    private String order;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 偏移量
     */
    private Integer offset;

    /**
     * 批量操作idList
     */
    private List<Long> ids;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
