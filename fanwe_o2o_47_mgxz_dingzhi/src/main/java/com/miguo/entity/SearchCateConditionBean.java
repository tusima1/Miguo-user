package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */

public class SearchCateConditionBean implements Serializable{

    String message;
    int statusCode;
    List<Result> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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

            /**
             * 一级/二级类目
             */
            List<Categories> categoryList;
            /**
             * 智能排序
             */
            List<IntelList> intelList;
            /**
             * 热门商圈
             */
            List<HotAreaList> hotAreaList;

            /**
             * 行政区及商圈
             */
            List<AdminAreaList> adminAreaList;
            /**
             * 筛选数据
             */
            List<FilterList> filterList;
            /**
             * 附近
             */
            List<NearByList> nearByList;

            public List<FilterList> getFilterList() {
                return filterList;
            }

            public void setFilterList(List<FilterList> filterList) {
                this.filterList = filterList;
            }

            public List<NearByList> getNearByList() {
                return nearByList;
            }

            public void setNearByList(List<NearByList> nearByList) {
                this.nearByList = nearByList;
            }

            public List<AdminAreaList> getAdminAreaList() {
                return adminAreaList;
            }

            public void setAdminAreaList(List<AdminAreaList> adminAreaList) {
                this.adminAreaList = adminAreaList;
            }

            public List<HotAreaList> getHotAreaList() {
                return hotAreaList;
            }

            public void setHotAreaList(List<HotAreaList> hotAreaList) {
                this.hotAreaList = hotAreaList;
            }

            public List<IntelList> getIntelList() {
                return intelList;
            }

            public void setIntelList(List<IntelList> intelList) {
                this.intelList = intelList;
            }

            public List<Categories> getCategoryList() {
                return categoryList;
            }

            public void setCategoryList(List<Categories> categoryList) {
                this.categoryList = categoryList;
            }

            /**
             * 一级/二级类目
             */
            public class Categories implements Serializable{
                String id;
                String city_id;
                String name;
                String is_effect;
                String sort;
                String img;
                String uncheck_img;
                String is_recommend;
                List<CategoryType> category_type;

                public List<CategoryType> getCategory_type() {
                    return category_type;
                }

                public void setCategory_type(List<CategoryType> category_type) {
                    this.category_type = category_type;
                }

                public String getCity_id() {
                    return city_id;
                }

                public void setCity_id(String city_id) {
                    this.city_id = city_id;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getIs_effect() {
                    return is_effect;
                }

                public void setIs_effect(String is_effect) {
                    this.is_effect = is_effect;
                }

                public String getIs_recommend() {
                    return is_recommend;
                }

                public void setIs_recommend(String is_recommend) {
                    this.is_recommend = is_recommend;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSort() {
                    return sort;
                }

                public void setSort(String sort) {
                    this.sort = sort;
                }

                public String getUncheck_img() {
                    return uncheck_img;
                }

                public void setUncheck_img(String uncheck_img) {
                    this.uncheck_img = uncheck_img;
                }

                public class CategoryType implements Serializable{
                    String id;
                    String name;
                    String is_effect;
                    String sort;
                    String img;
                    String uncheck_img;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getIs_effect() {
                        return is_effect;
                    }

                    public void setIs_effect(String is_effect) {
                        this.is_effect = is_effect;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getSort() {
                        return sort;
                    }

                    public void setSort(String sort) {
                        this.sort = sort;
                    }

                    public String getUncheck_img() {
                        return uncheck_img;
                    }

                    public void setUncheck_img(String uncheck_img) {
                        this.uncheck_img = uncheck_img;
                    }
                }

            }

            /**
             * 智能排序
             */
            public class IntelList implements Serializable{
                String name;
                List<Intel> intelList;

                public List<Intel> getIntelList() {
                    return intelList;
                }

                public void setIntelList(List<Intel> intelList) {
                    this.intelList = intelList;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public class  Intel implements Serializable{
                    /**
                     * intelligent_ordering
                     */
                    String key;
                    /**
                     * 智能排序
                     */
                    String value;
                }

            }

            /**
             * 热门商圈
             */
            public class HotAreaList implements Serializable{
                /**
                 * 热门商圈数据
                 */
                List<NearList> nearList;
                /**
                 * 热门商圈
                 */
                String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<NearList> getNearList() {
                    return nearList;
                }

                public void setNearList(List<NearList> nearList) {
                    this.nearList = nearList;
                }

                public class NearList implements Serializable{
                    /**
                     * 1
                     */
                    String is_hot;
                    /**
                     * "测试商圈31
                     */
                    String name;

                    public String getIs_hot() {
                        return is_hot;
                    }

                    public void setIs_hot(String is_hot) {
                        this.is_hot = is_hot;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

            }

            /**
             * 行政区及商圈
             */
            public class AdminAreaList implements Serializable{
                /**
                 * 0
                 */
                String is_hot;
                /**
                 * 行政区及商圈数据
                 */
                List<TradeArea> tradeArea;
                /**
                 * 行政区1
                 */
                String name;
                /**
                 * pid
                 */
                String pid;

                /**
                 * id
                 */
                String id;

                /**
                 * 1
                 */
                String sort;

                /**
                 * 城市id
                 */
                String city_id;


                public String getCity_id() {
                    return city_id;
                }

                public void setCity_id(String city_id) {
                    this.city_id = city_id;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIs_hot() {
                    return is_hot;
                }

                public void setIs_hot(String is_hot) {
                    this.is_hot = is_hot;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getSort() {
                    return sort;
                }

                public void setSort(String sort) {
                    this.sort = sort;
                }

                public List<TradeArea> getTradeArea() {
                    return tradeArea;
                }

                public void setTradeArea(List<TradeArea> tradeArea) {
                    this.tradeArea = tradeArea;
                }

                public class TradeArea implements Serializable{
                    /**
                     * 0
                     */
                    String is_hot;
                    /**
                     * 测试商圈12
                     */
                    String name;
                    /**
                     * ebc23b65-e007-44a4-b461-42d47efe2640
                     */
                    String pid;
                    /**
                     * f90f4b6b-d375-4126-b4c0-de887bfbd3a2
                     */
                    String id;
                    /**
                     * 2
                     */
                    String sort;
                    /**
                     * 69e0405b-de8c-4247-8a0a-91ca45c4b30c
                     */
                    String city_id;

                    public String getCity_id() {
                        return city_id;
                    }

                    public void setCity_id(String city_id) {
                        this.city_id = city_id;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getIs_hot() {
                        return is_hot;
                    }

                    public void setIs_hot(String is_hot) {
                        this.is_hot = is_hot;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getPid() {
                        return pid;
                    }

                    public void setPid(String pid) {
                        this.pid = pid;
                    }

                    public String getSort() {
                        return sort;
                    }

                    public void setSort(String sort) {
                        this.sort = sort;
                    }
                }
            }

            /**
             * 筛选
             */
            public class FilterList implements Serializable{
                String name;
                List<Filter> iLikeList;
                List<Filter> disCountList;
                List<Filter> priceList;

                public List<Filter> getDisCountList() {
                    return disCountList;
                }

                public void setDisCountList(List<Filter> disCountList) {
                    this.disCountList = disCountList;
                }

                public List<Filter> getiLikeList() {
                    return iLikeList;
                }

                public void setiLikeList(List<Filter> iLikeList) {
                    this.iLikeList = iLikeList;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<Filter> getPriceList() {
                    return priceList;
                }

                public void setPriceList(List<Filter> priceList) {
                    this.priceList = priceList;
                }

                public class Filter implements Serializable{
                    /**
                     * 收藏
                     */
                    String value;
                    /**
                     * collect
                     */
                    String key;

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }
                }


            }

            /**
             * 附近
             */
            public class NearByList implements Serializable{
                List<NearList> nearList;
                String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<NearList> getNearList() {
                    return nearList;
                }

                public void setNearList(List<NearList> nearList) {
                    this.nearList = nearList;
                }

                public class NearList implements Serializable{
                    /**
                     * 全城
                     */
                    String value;
                    /**
                     * wholecity
                     */
                    String key;

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }
                }

            }

        }

    }

}
