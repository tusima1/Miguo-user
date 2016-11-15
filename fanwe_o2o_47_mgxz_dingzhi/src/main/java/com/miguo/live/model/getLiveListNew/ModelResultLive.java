package com.miguo.live.model.getLiveListNew;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ModelResultLive {

    private String page_total;
    private String page;
    private String title;
    private String page_count;
    private String page_size;
    /**
     * create_time : 1477968395005
     * play_url : 20
     * playback_status : 1
     * live_type : 1
     * playback_room_id :
     * title : 米果小站
     * fileset : [{"duration":"5396","file_id":"14651978969406208719","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_76cb4e51d43d4d0ba5d908089f35e115.f0.mp4\"}]"},{"duration":"1231","file_id":"14651978969406242769","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_b114ee44296149b1bd1d1fdc07967929.f0.mp4\"}]"}]
     * start_status : 2
     * cover : http://pic1.mofang.com.tw/2014/0516/20140516051344912.jpg
     * start_time : 1477968395942
     * chat_room_id : 779683950
     * host : {"uid":"200a3e14-3046-4b76-bc44-2208f40473db","host_user_id":"200a3e14-3046-4b76-bc44-2208f40473db","nickname":"18888888888","avatar":"","tags":["女神","呆萌","小富婆","霸王餐挑战"]}
     * id : 779683950
     * av_room_id : 779683950
     * lbs : {"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"}
     * channel_id : 2012312312
     */

    private List<ModelRoom> body;

    public String getPage_total() {
        return page_total;
    }

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<ModelRoom> getBody() {
        return body;
    }

    public void setBody(List<ModelRoom> body) {
        this.body = body;
    }

}
