package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */

public class RepresentBannerBean implements Serializable{

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

            List<Categories> categoryList;

            public List<Categories> getCategoryList() {
                return categoryList;
            }

            public void setCategoryList(List<Categories> categoryList) {
                this.categoryList = categoryList;
            }

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

        }

    }

}
