package com.miguo.live.model.getHostAuthTime;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelHostAuthTime {
    private String system_time;
    private String insert_time;
    private String is_host;

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public String getIs_host() {
        return is_host;
    }

    public void setIs_host(String is_host) {
        this.is_host = is_host;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }
}
