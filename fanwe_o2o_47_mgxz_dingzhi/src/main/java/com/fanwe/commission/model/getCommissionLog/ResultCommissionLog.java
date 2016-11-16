package com.fanwe.commission.model.getCommissionLog;

import com.fanwe.base.PageBean;

import java.util.List;

/**
 * Created by didik on 2016/8/28.
 */
public class ResultCommissionLog {
//    "salary": "205.0",           总佣金
//    "blocksalary": "205.0"       不可提现的佣金 需升级白银后才能体现
    private PageBean page;
    private String salary;
    private String blocksalary;
    /**\
     * 累计佣金
     */
    private String salary_total;


    private List<ModelCommissionLog> list;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBlocksalary() {
        return blocksalary;
    }

    public void setBlocksalary(String blocksalary) {
        this.blocksalary = blocksalary;
    }

    public List<ModelCommissionLog> getList() {
        return list;
    }

    public void setList(List<ModelCommissionLog> list) {
        this.list = list;
    }

    public String getSalary_total() {
        return salary_total;
    }

    public void setSalary_total(String salary_total) {
        this.salary_total = salary_total;
    }
}
