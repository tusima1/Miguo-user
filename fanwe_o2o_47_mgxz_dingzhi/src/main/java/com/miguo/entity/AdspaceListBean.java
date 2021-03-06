package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry/ on 2016/10/27.
 */
public class AdspaceListBean implements Serializable{

    String message;
    int statusCode;
    List<Result> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result implements Serializable{

        List<Body> body;

        public List<Body> getBody() {
            return body;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }

        public class Body implements Serializable{
            String type_id;// 参数
            String icon;// 图标
            String title;// 标题
            String descript;// 说明
            String type;//1:团购 2:门店 3:商家 4:主题 5:限时特买
            String adspace_id;
            /**
             * 优先加载
             */
            int resId;

            public int getResId() {
                return resId;
            }

            public void setResId(int resId) {
                this.resId = resId;
            }

            public String getAdspace_id() {
                return adspace_id;
            }

            public void setAdspace_id(String adspace_id) {
                this.adspace_id = adspace_id;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescript() {
                return descript;
            }

            public void setDescript(String descript) {
                this.descript = descript;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

    }

}
