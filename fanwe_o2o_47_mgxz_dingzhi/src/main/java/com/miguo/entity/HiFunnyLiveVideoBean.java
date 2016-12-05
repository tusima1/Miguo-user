package com.miguo.entity;

import com.miguo.live.model.getLiveListNew.ModelResultLive;
import com.miguo.live.model.getLiveListNew.ModelRootLive;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class HiFunnyLiveVideoBean implements Serializable{

    /**
     * result : [{"page_total":"1","page":"1","body":[{"create_time":"1477968395005","play_url":"20","playback_status":"1","live_type":"1","playback_room_id":"","title":"米果小站","fileset":[{"duration":"5396","file_id":"14651978969406208719","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_76cb4e51d43d4d0ba5d908089f35e115.f0.mp4\"}]"},{"duration":"1231","file_id":"14651978969406242769","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_b114ee44296149b1bd1d1fdc07967929.f0.mp4\"}]"}],"start_status":"2","cover":"http://pic1.mofang.com.tw/2014/0516/20140516051344912.jpg","start_time":"1477968395942","chat_room_id":"779683950","host":{"uid":"200a3e14-3046-4b76-bc44-2208f40473db","host_user_id":"200a3e14-3046-4b76-bc44-2208f40473db","nickname":"18888888888","avatar":"","tags":["女神","呆萌","小富婆","霸王餐挑战"]},"id":"779683950","av_room_id":"779683950","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":"2012312312"},{"create_time":"1474616661000","play_url":"","playback_status":"0","live_type":"2","playback_room_id":"","title":"10","fileset":[{"duration":"3","file_id":"14651978969262532999","playset":"[{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f220.av.m3u8\",\"definition\":220,\"vbitrate\":575104,\"vheight\":640,\"vwidth\":368},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f20.mp4\",\"definition\":20,\"vbitrate\":519110,\"vheight\":640,\"vwidth\":368},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f10.mp4\",\"definition\":10,\"vbitrate\":247360,\"vheight\":320,\"vwidth\":184},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f30.mp4\",\"definition\":30,\"vbitrate\":1072317,\"vheight\":1280,\"vwidth\":736},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f210.av.m3u8\",\"definition\":210,\"vbitrate\":282216,\"vheight\":320,\"vwidth\":184},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f230.av.m3u8\",\"definition\":230,\"vbitrate\":1169844,\"vheight\":1280,\"vwidth\":736},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f0.mp4\",\"definition\":0,\"vbitrate\":0,\"vheight\":0,\"vwidth\":0}]"}],"start_status":"1","cover":"","start_time":"1474560000000","chat_room_id":"746166612","host":{"uid":"058a9ddf-b1df-4dd5-ba93-38c78fb3e254","host_user_id":"058a9ddf-b1df-4dd5-ba93-38c78fb3e254","nickname":"13258856699","avatar":"null","tags":["霸王餐挑战","试睡","高冷","意识流","女神"]},"id":"746166612","av_room_id":"746166612","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":""},{"create_time":"1474560000000","play_url":"","playback_status":"0","live_type":"2","playback_room_id":"","title":"哈哈哈","fileset":[{"duration":"32","file_id":"14651978969268791113","playset":"[{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f20.mp4\",\"definition\":20,\"vbitrate\":530043,\"vheight\":360,\"vwidth\":640},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f30.mp4\",\"definition\":30,\"vbitrate\":1064901,\"vheight\":720,\"vwidth\":1280},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f230.av.m3u8\",\"definition\":230,\"vbitrate\":1180719,\"vheight\":720,\"vwidth\":1280},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f210.av.m3u8\",\"definition\":210,\"vbitrate\":318078,\"vheight\":180,\"vwidth\":320},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f0.mp4\",\"definition\":0,\"vbitrate\":0,\"vheight\":0,\"vwidth\":0},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f10.mp4\",\"definition\":10,\"vbitrate\":265620,\"vheight\":180,\"vwidth\":320},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f220.av.m3u8\",\"definition\":220,\"vbitrate\":602649,\"vheight\":360,\"vwidth\":640}]"}],"start_status":"1","cover":"","start_time":"1474560000000","chat_room_id":"87654321","host":{"uid":"a44b84b1-0b35-4564-af60-e1ff9f775593","host_user_id":"a44b84b1-0b35-4564-af60-e1ff9f775593","nickname":"刚回家天天","avatar":"http://obc58vgro.bkt.clouddn.com//2016/10/FiEOIKgQv4rcMLpDbo7yrUOFLI1A?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200","tags":[]},"id":"87654321","av_room_id":"87654321","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":""}],"title":"room_list","page_count":"3","page_size":"10"}]
     * message : 操作成功
     * token : 82b0982765d40df7db7a89ffb3a0b2bd
     * statusCode : 200
     */

    private String message;
    private String token;
    private int statusCode;
    /**
     * page_total : 1
     * page : 1
     * body : [{"create_time":"1477968395005","play_url":"20","playback_status":"1","live_type":"1","playback_room_id":"","title":"米果小站","fileset":[{"duration":"5396","file_id":"14651978969406208719","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_76cb4e51d43d4d0ba5d908089f35e115.f0.mp4\"}]"},{"duration":"1231","file_id":"14651978969406242769","playset":"[{\"vbitrate\":0,\"definition\":0,\"vheight\":0,\"vwidth\":0,\"url\":\"http://200023194.vod.myqcloud.com/200023194_b114ee44296149b1bd1d1fdc07967929.f0.mp4\"}]"}],"start_status":"2","cover":"http://pic1.mofang.com.tw/2014/0516/20140516051344912.jpg","start_time":"1477968395942","chat_room_id":"779683950","host":{"uid":"200a3e14-3046-4b76-bc44-2208f40473db","host_user_id":"200a3e14-3046-4b76-bc44-2208f40473db","nickname":"18888888888","avatar":"","tags":["女神","呆萌","小富婆","霸王餐挑战"]},"id":"779683950","av_room_id":"779683950","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":"2012312312"},{"create_time":"1474616661000","play_url":"","playback_status":"0","live_type":"2","playback_room_id":"","title":"10","fileset":[{"duration":"3","file_id":"14651978969262532999","playset":"[{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f220.av.m3u8\",\"definition\":220,\"vbitrate\":575104,\"vheight\":640,\"vwidth\":368},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f20.mp4\",\"definition\":20,\"vbitrate\":519110,\"vheight\":640,\"vwidth\":368},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f10.mp4\",\"definition\":10,\"vbitrate\":247360,\"vheight\":320,\"vwidth\":184},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f30.mp4\",\"definition\":30,\"vbitrate\":1072317,\"vheight\":1280,\"vwidth\":736},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f210.av.m3u8\",\"definition\":210,\"vbitrate\":282216,\"vheight\":320,\"vwidth\":184},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f230.av.m3u8\",\"definition\":230,\"vbitrate\":1169844,\"vheight\":1280,\"vwidth\":736},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f0.mp4\",\"definition\":0,\"vbitrate\":0,\"vheight\":0,\"vwidth\":0}]"}],"start_status":"1","cover":"","start_time":"1474560000000","chat_room_id":"746166612","host":{"uid":"058a9ddf-b1df-4dd5-ba93-38c78fb3e254","host_user_id":"058a9ddf-b1df-4dd5-ba93-38c78fb3e254","nickname":"13258856699","avatar":"null","tags":["霸王餐挑战","试睡","高冷","意识流","女神"]},"id":"746166612","av_room_id":"746166612","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":""},{"create_time":"1474560000000","play_url":"","playback_status":"0","live_type":"2","playback_room_id":"","title":"哈哈哈","fileset":[{"duration":"32","file_id":"14651978969268791113","playset":"[{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f20.mp4\",\"definition\":20,\"vbitrate\":530043,\"vheight\":360,\"vwidth\":640},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f30.mp4\",\"definition\":30,\"vbitrate\":1064901,\"vheight\":720,\"vwidth\":1280},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f230.av.m3u8\",\"definition\":230,\"vbitrate\":1180719,\"vheight\":720,\"vwidth\":1280},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f210.av.m3u8\",\"definition\":210,\"vbitrate\":318078,\"vheight\":180,\"vwidth\":320},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f0.mp4\",\"definition\":0,\"vbitrate\":0,\"vheight\":0,\"vwidth\":0},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f10.mp4\",\"definition\":10,\"vbitrate\":265620,\"vheight\":180,\"vwidth\":320},{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_fb071fea772a11e6af418d235c872386.f220.av.m3u8\",\"definition\":220,\"vbitrate\":602649,\"vheight\":360,\"vwidth\":640}]"}],"start_status":"1","cover":"","start_time":"1474560000000","chat_room_id":"87654321","host":{"uid":"a44b84b1-0b35-4564-af60-e1ff9f775593","host_user_id":"a44b84b1-0b35-4564-af60-e1ff9f775593","nickname":"刚回家天天","avatar":"http://obc58vgro.bkt.clouddn.com//2016/10/FiEOIKgQv4rcMLpDbo7yrUOFLI1A?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200?imageView2/0/w/200/h/200","tags":[]},"id":"87654321","av_room_id":"87654321","lbs":{"geo_y":"70","geo_x":"130","shop_id":"4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2","address":"浦沿","shop_name":"爆米花"},"channel_id":""}]
     * title : room_list
     * page_count : 3
     * page_size : 10
     */

    List<Result> result;

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

    public class Result extends ModelResultLive implements Serializable{



    }

}
