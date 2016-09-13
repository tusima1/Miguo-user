package com.miguo.live.model.getGiftInfo;

import java.util.List;

/**
 * Created by didik on 2016/9/13.
 */
public class Temp {

    public static class ResultBean {


        public static class BodyBean {
            private String userdiamond;
            /**
             * price : 30.00
             * icon : 1
             * name : 测试礼物1
             * id : 1
             * type : 2
             * is_delete : 0
             */

            private List<GiftlistBean> giftlist;



            public List<GiftlistBean> getGiftlist() {
                return giftlist;
            }

            public void setGiftlist(List<GiftlistBean> giftlist) {
                this.giftlist = giftlist;
            }

            public static class GiftlistBean {
                private String price;
                private String icon;
                private String name;
                private String id;
                private String type;
                private String is_delete;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getIs_delete() {
                    return is_delete;
                }

                public void setIs_delete(String is_delete) {
                    this.is_delete = is_delete;
                }
            }
        }
    }
}
