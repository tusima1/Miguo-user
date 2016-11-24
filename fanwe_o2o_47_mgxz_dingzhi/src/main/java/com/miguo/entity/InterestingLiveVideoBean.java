package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 狗蛋哥 on 2016/11/23.
 */

public class InterestingLiveVideoBean implements Serializable{

    String message;
    String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

            List<Live> live_list;
            int page_total;
            int page;
            int page_count;
            int page_size;

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getPage_count() {
                return page_count;
            }

            public void setPage_count(int page_count) {
                this.page_count = page_count;
            }

            public int getPage_size() {
                return page_size;
            }

            public void setPage_size(int page_size) {
                this.page_size = page_size;
            }

            public int getPage_total() {
                return page_total;
            }

            public void setPage_total(int page_total) {
                this.page_total = page_total;
            }

            public List<Live> getLive_list() {
                return live_list;
            }

            public void setLive_list(List<Live> live_list) {
                this.live_list = live_list;
            }

            public class Live implements Serializable{
                private String tencent_room_id;

                private String address;

                private String host_uid;

                private String play_url;

                private String create_time;

                private String playback_status;

                private String icon;

                private String playback_room_id;

                private String type;

                private String title;

                private String shop_name;

                private String start_status;

                private String cover;

                private String nick;

                private String geo_y;

                private String shop_id;

                private String start_time;

                private String geo_x;

                private String interest;

                private String file_id;

                private String id;

                private String channel_id;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getChannel_id() {
                    return channel_id;
                }

                public void setChannel_id(String channel_id) {
                    this.channel_id = channel_id;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public String getFile_id() {
                    return file_id;
                }

                public void setFile_id(String file_id) {
                    this.file_id = file_id;
                }

                public String getGeo_x() {
                    return geo_x;
                }

                public void setGeo_x(String geo_x) {
                    this.geo_x = geo_x;
                }

                public String getGeo_y() {
                    return geo_y;
                }

                public void setGeo_y(String geo_y) {
                    this.geo_y = geo_y;
                }

                public String getHost_uid() {
                    return host_uid;
                }

                public void setHost_uid(String host_uid) {
                    this.host_uid = host_uid;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getInterest() {
                    return interest;
                }

                public void setInterest(String interest) {
                    this.interest = interest;
                }

                public String getNick() {
                    return nick;
                }

                public void setNick(String nick) {
                    this.nick = nick;
                }

                public String getPlay_url() {
                    return play_url;
                }

                public void setPlay_url(String play_url) {
                    this.play_url = play_url;
                }

                public String getPlayback_room_id() {
                    return playback_room_id;
                }

                public void setPlayback_room_id(String playback_room_id) {
                    this.playback_room_id = playback_room_id;
                }

                public String getPlayback_status() {
                    return playback_status;
                }

                public void setPlayback_status(String playback_status) {
                    this.playback_status = playback_status;
                }

                public String getShop_id() {
                    return shop_id;
                }

                public void setShop_id(String shop_id) {
                    this.shop_id = shop_id;
                }

                public String getShop_name() {
                    return shop_name;
                }

                public void setShop_name(String shop_name) {
                    this.shop_name = shop_name;
                }

                public String getStart_status() {
                    return start_status;
                }

                public void setStart_status(String start_status) {
                    this.start_status = start_status;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getTencent_room_id() {
                    return tencent_room_id;
                }

                public void setTencent_room_id(String tencent_room_id) {
                    this.tencent_room_id = tencent_room_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
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

}
