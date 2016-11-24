package com.miguo.fake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class HomeBannerFakeData implements Serializable{


    public static HomeBannerFakeData instance = null;

    public static HomeBannerFakeData getInstance(){
        return new HomeBannerFakeData();
    }

    public List<Banner> getBanner(){
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("http://img.xiaoneiit.com/mgxz/food1.jpg"));
        banners.add(new Banner("http://img.xiaoneiit.com/mgxz/food2.jpg"));
        banners.add(new Banner("http://img.xiaoneiit.com/mgxz/food3.jpg"));
        banners.add(new Banner("http://img.xiaoneiit.com/mgxz/food4.jpg"));
        banners.add(new Banner("http://img.xiaoneiit.com/mgxz/food5.jpg"));
        return banners;
    }

    String[] urls = {
            "http://img.xiaoneiit.com/mgxz/ad2_1.jpg",
            "http://img.xiaoneiit.com/mgxz/ad2_2.jpg",
            "http://img.xiaoneiit.com/mgxz/ad2_3.jpg",
            "http://img.xiaoneiit.com/mgxz/ad2_4.jpg",
            "http://img.xiaoneiit.com/mgxz/ad2_5.jpg",
            "http://img.xiaoneiit.com/mgxz/ad2_6.jpg"
    };


    public class Banner implements Serializable{
        String url;


        public Banner(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
