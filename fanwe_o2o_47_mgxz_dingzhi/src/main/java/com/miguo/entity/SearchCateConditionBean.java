package com.miguo.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/10
 * Description: 
 */

public class SearchCateConditionBean implements Serializable{

    private String message;
    private String token;
    private String statusCode;
    private long timestamp;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        private List<BodyBean> body;

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public static class BodyBean implements Serializable{
            /**
             * tradeArea : [{"name":"测试商圈12","id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a2"},
             * {"name":"测试商圈13","id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a3"},{"name":"测试商圈14",
             * "id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a4"}]
             * name : 行政区1
             * id : ebc23b65-e007-44a4-b461-42d47efe2640
             */

            private List<AdminAreaListBean> adminAreaList;
            /**
             * hotAreaList2 : [{"name":"测试商圈31","id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a9"},
             * {"name":"测试商圈32","id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a10"},{"name":"测试商圈33",
             * "id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a11"},{"name":"测试商圈34",
             * "id":"f90f4b6b-d375-4126-b4c0-de887bfbd3a12"},{"name":"石门",
             * "id":"36531bd0-51c0-4f88-bb41-f2534b986118"},{"name":"凤鸣街道",
             * "id":"0fff4efc-b2de-4353-bef4-30be28a5f4cc"},{"name":"振华路",
             * "id":"4d2e9f7d-ba9f-409a-addb-46b749b2b912"},{"name":"庆丰南路",
             * "id":"3332d044-da2e-4e80-99b4-4c26e3caa759"},{"name":"东兴",
             * "id":"24d03d85-3fa2-4956-9618-8ad6a20be8ba"},{"name":"市政府",
             * "id":"248b6400-4666-4790-9643-e7e1d896ade0"},{"name":"屠甸路",
             * "id":"4843229b-a4a8-4ad9-965b-c41893e76712"},{"name":"南关厢",
             * "id":"2eec21c5-281d-461e-b886-9fa56f46ec10"}]
             * name : 热门商圈
             */

            private List<HotAreaList1Bean> hotAreaList1;
            /**
             * img : http://cdn.mgxz.com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI
             * name : 全部分类
             * uncheck_img : http://cdn.mgxz.com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI
             * category_type : [{"img":"http://cdn.mgxz
             * .com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI","name":"全部","uncheck_img":"http://cdn
             * .mgxz.com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI","id":"wholecategory"}]
             * id : wholecategory
             */

            private List<CategoryListBean> categoryList;
            /**
             * name : 智能排序
             * intelList2 : [{"value":"智能排序","key":"intelligent_ordering"},{"value":"离我最近",
             * "key":"nearest"},{"value":"人气最高","key":"popularity"},{"value":"好评优先",
             * "key":"evaluate"},{"value":"新店开张","key":"latest"}]
             */

            private List<IntelList1Bean> intelList1;
            /**
             * nearList : [{"value":"全城","key":"wholecity"},{"value":"500米","key":"500m"},
             * {"value":"1000米","key":"1000m"},{"value":"2000米","key":"2000m"},{"value":"5000米",
             * "key":"5000m"}]
             * name : 附近
             */

            private List<NearByListBean> nearByList;
            /**
             * name : 筛选
             * filterList2 : [{"filterList":[{"value":"我喜欢","key":""},{"value":"收藏",
             * "key":"collect"},{"value":"求代言","key":"popularity"}],"value":"我喜欢",
             * "key":"my_special"},{"filterList":[{"value":"优惠","key":"discount"},{"value":"首单",
             * "key":"first_order"},{"value":"限时优惠","key":"limited_discount"},{"value":"活动",
             * "key":"campaign"}],"value":"优惠","key":"discount"},{"filterList":[{"value":"价格",
             * "key":"price"},{"value":"0-50","key":"zero_to_fifty"},{"value":"50-100",
             * "key":"fifty_to_hundred"},{"value":"100-300","key":"one_to_three_hundred"},
             * {"value":"300以上","key":"over_three_hundred"}],"value":"价格（元）","key":"price"}]
             */

            private List<FilterList1Bean> filterList1;

            public List<AdminAreaListBean> getAdminAreaList() {
                return adminAreaList;
            }

            public void setAdminAreaList(List<AdminAreaListBean> adminAreaList) {
                this.adminAreaList = adminAreaList;
            }

            public List<HotAreaList1Bean> getHotAreaList1() {
                return hotAreaList1;
            }

            public void setHotAreaList1(List<HotAreaList1Bean> hotAreaList1) {
                this.hotAreaList1 = hotAreaList1;
            }

            public List<CategoryListBean> getCategoryList() {
                return categoryList;
            }

            public void setCategoryList(List<CategoryListBean> categoryList) {
                this.categoryList = categoryList;
            }

            public List<IntelList1Bean> getIntelList1() {
                return intelList1;
            }

            public void setIntelList1(List<IntelList1Bean> intelList1) {
                this.intelList1 = intelList1;
            }

            public List<NearByListBean> getNearByList() {
                return nearByList;
            }

            public void setNearByList(List<NearByListBean> nearByList) {
                this.nearByList = nearByList;
            }

            public List<FilterList1Bean> getFilterList1() {
                return filterList1;
            }

            public void setFilterList1(List<FilterList1Bean> filterList1) {
                this.filterList1 = filterList1;
            }

            public static class AdminAreaListBean extends TwoMode<AdminAreaListBean.TradeAreaBean> implements Serializable{
                private String id;
                /**
                 * name : 测试商圈12
                 * id : f90f4b6b-d375-4126-b4c0-de887bfbd3a2
                 */

                private List<TradeAreaBean> tradeArea;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                    setSingleId(id);
                }

                public void setTradeArea(List<TradeAreaBean> tradeArea) {
                    giveList2Parent(tradeArea);
                    this.tradeArea = tradeArea;
                }

                public List<TradeAreaBean> getTradeArea() {
                    return tradeArea;
                }

                public class TradeAreaBean extends SingleMode implements Serializable{
                    private String id;
                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                        setSingleId(id);
                    }
                }
            }

            public static class HotAreaList1Bean extends TwoMode<HotAreaList1Bean.HotAreaList2Bean> implements Serializable{
                /**
                 * name : 测试商圈31
                 * id : f90f4b6b-d375-4126-b4c0-de887bfbd3a9
                 */
                private String key;

                private List<HotAreaList2Bean> hotAreaList2;

                public List<HotAreaList2Bean> getHotAreaList2() {
                    return hotAreaList2;
                }

                public void setHotAreaList2(List<HotAreaList2Bean> hotAreaList2) {
                    giveList2Parent(hotAreaList2);
                    this.hotAreaList2=hotAreaList2;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                    setSingleId(key);
                }

                public static class HotAreaList2Bean extends SingleMode implements Serializable{
                    private String id;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                        setSingleId(id);
                    }

                }
            }

            public static class CategoryListBean extends TwoMode<CategoryListBean.CategoryTypeBean> implements Serializable{
                private String img;
                private String uncheck_img;
                private String id;
                /**
                 * img : http://cdn.mgxz.com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI
                 * name : 全部
                 * uncheck_img : http://cdn.mgxz.com/2016/11/FqeEFC85SO6HcFf66SWzokVgCMoI
                 * id : wholecategory
                 */

                private List<CategoryTypeBean> category_type;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getUncheck_img() {
                    return uncheck_img;
                }

                public void setUncheck_img(String uncheck_img) {
                    this.uncheck_img = uncheck_img;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                    setSingleId(id);
                }

                public List<CategoryTypeBean> getCategory_type() {
                    return category_type;
                }

                public void setCategory_type(List<CategoryTypeBean> category_type) {
                    giveList2Parent(category_type);
                    this.category_type = category_type;
                }

                public static class CategoryTypeBean extends SingleMode implements Serializable{
                    private String img;
                    private String uncheck_img;
                    private String id;

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getUncheck_img() {
                        return uncheck_img;
                    }

                    public void setUncheck_img(String uncheck_img) {
                        this.uncheck_img = uncheck_img;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                        setSingleId(id);
                    }
                }
            }

            public static class IntelList1Bean extends TwoMode<IntelList1Bean.IntelList2Bean> implements Serializable{
                /**
                 * value : 智能排序
                 * key : intelligent_ordering
                 */

                private List<IntelList2Bean> intelList2;

                public List<IntelList2Bean> getIntelList2() {
                    return intelList2;
                }

                public void setIntelList2(List<IntelList2Bean> intelList2) {
                    giveList2Parent(intelList2);
                    this.intelList2 = intelList2;
                }

                public static class IntelList2Bean extends SingleMode implements Serializable{
                    private String value;
                    private String key;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        setName(value);
                        this.value = value;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                        setSingleId(key);
                    }
                }
            }

            public static class NearByListBean extends TwoMode<NearByListBean.NearListBean> implements Serializable{
                /**
                 * value : 全城
                 * key : wholecity
                 */
                private String name;
                private String key;
                private List<NearListBean> nearList;


                public List<NearListBean> getNearList() {
                    return nearList;
                }

                public void setNearList(List<NearListBean> nearList) {
                    giveList2Parent(nearList);
                    this.nearList = nearList;
                }


                public String getValue() {
                    return name;
                }

                public void setValue(String name) {
                    setName(name);
                    this.name = name;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                    setSingleId(key);
                }

                public static class NearListBean extends SingleMode implements Serializable{
                    private String value;
                    private String key;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        setName(value);
                        this.value = value;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                        setSingleId(key);
                    }
                }
            }

            public static class FilterList1Bean extends SingleMode implements Serializable{
                /**
                 * filterList : [{"value":"我喜欢","key":""},{"value":"收藏","key":"collect"},{"value":"求代言","key":"popularity"}]
                 * value : 我喜欢
                 * key : my_special
                 */

                private List<FilterList2Bean> filterList2;

                public List<FilterList2Bean> getFilterList2() {
                    return filterList2;
                }

                public void setFilterList2(List<FilterList2Bean> filterList2) {
                    this.filterList2 = filterList2;
                }

                public static class FilterList2Bean extends TwoMode<FilterList2Bean.FilterListBean> implements Serializable{
                    private String value;
                    private String key;
                    /**
                     * value : 我喜欢
                     * key :
                     */

                    private List<FilterListBean> filterList;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        setName(value);
                        this.value = value;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        setSingleId(key);
                        this.key = key;
                    }

                    public List<FilterListBean> getFilterList() {
                        return filterList;
                    }

                    public void setFilterList(List<FilterListBean> filterList) {
                        giveList2Parent(filterList);
                        this.filterList = filterList;
                    }

                    public static class FilterListBean extends SingleMode implements Serializable{
                        private String value;
                        private String key;

                        public String getValue() {
                            return value;
                        }

                        public void setValue(String value) {
                            setName(value);
                            this.value = value;
                        }

                        public String getKey() {
                            return key;
                        }

                        public void setKey(String key) {
                            setSingleId(key);
                            this.key = key;
                        }
                    }
                }
            }

            /**
             * created by zlh
             * @return
             */
            public BodyBean clone(){
                BodyBean condition = null;
                try{
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(BodyBean.this);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    condition = (BodyBean) objectInputStream.readObject();
                }catch (IOException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                return condition;
            }

        }
    }
}
