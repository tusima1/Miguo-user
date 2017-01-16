package com.miguo.entity;

import com.miguo.definition.PageSize;

import java.io.Serializable;

/**
 * Created by zlh on 2017/1/13.
 */

public class RepresentFilterBean implements Serializable {
    /**
     * 区域一级
     */
    String areaOne;
    /**
     * 区域二级
     */
    String areaTwo;
    /**
     * 类目一级
     */
    String categoryOne;
    /**
     * 类目二级
     */
    String categoryTwo;
    /**
     * 筛选
     */
    String filter;
    /**
     * 关键字
     */
    String keyword;
    /**
     * 排序类型
     */
    String sortType;
    /**
     * 页码
     */
    int pageNum;
    /**
     * 页码数量
     */
    int pageSize;
    /**
     * 商家类型：1，优惠商家  0，全部商家
     */
    String merchantType;

    public RepresentFilterBean() {
        setAreaOne("");
        setAreaTwo("");
        setCategoryOne("");
        setCategoryTwo("");
        setFilter("");
        setKeyword("");
        setPageNum(PageSize.BASE_NUMBER_ONE);
        setPageSize(PageSize.BASE_PAGE_SIZE);
        setMerchantType("1");
        setSortType("");
    }

    public String getAreaOne() {
        return areaOne;
    }

    public void setAreaOne(String areaOne) {
        this.areaOne = areaOne;
    }

    public String getAreaTwo() {
        return areaTwo;
    }

    public void setAreaTwo(String areaTwo) {
        this.areaTwo = areaTwo;
    }

    public String getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(String categoryOne) {
        this.categoryOne = categoryOne;
    }

    public String getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(String categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
