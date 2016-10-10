package com.fanwe.common.model.getUpgradeVersion;

/**
 * Created by didik on 2016/10/10.
 */

public class ModelVersion{

//    "upgrade": "",                升级内容
//    "has_upgrade": "1",           1:可升级;0:不可升级 (用于判断需不需要升级)
//    "down_url": "1",              升级url
//    "server_version": "6.0.24",   系统版本号
//    "forced_upgrade": "1"         0:非强制升级;1:强制升级
    private String upgrade;
    private String has_upgrade;
    private String down_url;
    private String server_version;
    private String forced_upgrade;

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getHas_upgrade() {
        return has_upgrade;
    }

    public void setHas_upgrade(String has_upgrade) {
        this.has_upgrade = has_upgrade;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getServer_version() {
        return server_version;
    }

    public void setServer_version(String server_version) {
        this.server_version = server_version;
    }

    public String getForced_upgrade() {
        return forced_upgrade;
    }

    public void setForced_upgrade(String forced_upgrade) {
        this.forced_upgrade = forced_upgrade;
    }
}
