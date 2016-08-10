package com.miguo.live.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.home.model.ResultLive;
import com.fanwe.home.model.Room;
import com.fanwe.home.model.RootLive;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.checkFocus.ModelCheckFocus;
import com.miguo.live.model.checkFocus.ResultCheckFocus;
import com.miguo.live.model.checkFocus.RootCheckFocus;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceCount.ResultAudienceCount;
import com.miguo.live.model.getAudienceCount.RootAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.model.getAudienceList.ResultAudienceList;
import com.miguo.live.model.getAudienceList.RootAudienceList;
import com.miguo.live.model.getBussDictionInfo.ModelBussDictionInfo;
import com.miguo.live.model.getBussDictionInfo.ResultBussDictionInfo;
import com.miguo.live.model.getBussDictionInfo.RootBussDictionInfo;
import com.miguo.live.model.getHandOutRedPacket.ModelHandOutRedPacket;
import com.miguo.live.model.getHandOutRedPacket.ResultHandOutRedPacket;
import com.miguo.live.model.getHandOutRedPacket.RootHandOutRedPacket;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getHostInfo.ResultHostInfo;
import com.miguo.live.model.getHostInfo.RootHostInfo;
import com.miguo.live.model.getHostTags.ModelHostTags;
import com.miguo.live.model.getHostTags.ResultHostTags;
import com.miguo.live.model.getHostTags.RootHostTags;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.model.getUpToken.ResultUpToken;
import com.miguo.live.model.getUpToken.RootUpToken;
import com.miguo.live.model.postHandOutRedPacket.ModelHandOutRedPacketPost;
import com.miguo.live.model.postHandOutRedPacket.ResultHandOutRedPacketPost;
import com.miguo.live.model.postHandOutRedPacket.RootHandOutRedPacketPost;
import com.miguo.live.model.stopLive.ModelStopLive;
import com.miguo.live.model.stopLive.ResultStopLive;
import com.miguo.live.model.stopLive.RootStopLive;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/7/28.
 */
public class LiveHttpHelper implements IHelper {

    private static final String TAG = LiveHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public LiveHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }



    /**
     * 请求直播列表
     *
     * @param pageNum
     * @param pageSize
     */
    public void getLiveList(int pageNum, int pageSize, String tag, String keyword, String city) {

        TreeMap<String, String> params = new TreeMap<String, String>();

        params.put("token", App.getInstance().getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("tag", tag);
        params.put("keyword", keyword);
        params.put("city", city);
        params.put("method", LiveConstants.LIVE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootLive rootLive = gson.fromJson(responseBody, RootLive.class);
                List<ResultLive> resultLives = rootLive.getResult();
                if (SDCollectionUtil.isEmpty(resultLives) || resultLives.size() < 1) {
                    mView.onSuccess(LiveConstants.LIVE_LIST, null);
                    return;
                }
                ResultLive resultLive = resultLives.get(0);
                List<Room> rooms = resultLive.getBody();
                mView.onSuccess(LiveConstants.LIVE_LIST, rooms);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 申请直播房间ID
     *
     * @param shop_id
     */
    public void applyRoom(String shop_id, MgCallback mgCallback) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("shop_id", shop_id);
        params.put("method", LiveConstants.APPLY_ROOM);

        OkHttpUtils.getInstance().get(null, params, mgCallback);

    }

    /**
     * 请求观众数
     *
     * @param room_id
     */
    public void getAudienceCount(String room_id) {
        if (TextUtils.isEmpty(App.getInstance().getToken()) || TextUtils.isEmpty(room_id)) {
            return;
        }
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.AUDIENCE_COUNT);


        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootAudienceCount rootAudienceCount = gson.fromJson(responseBody,
                        RootAudienceCount.class);
                List<ResultAudienceCount> resultAudienceCounts = rootAudienceCount.getResult();
                if (SDCollectionUtil.isEmpty(resultAudienceCounts)) {
                    mView.onSuccess(LiveConstants.AUDIENCE_COUNT, null);
                    return;
                }
                ResultAudienceCount resultAudienceCount = resultAudienceCounts.get(0);
                List<ModelAudienceCount> modelAudienceCounts = resultAudienceCount.getBody();
                mView.onSuccess(LiveConstants.AUDIENCE_COUNT, modelAudienceCounts);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 获取当前房间的观众列表
     *
     * @param room_id
     */
    public void getAudienceList(String room_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.AUDIENCE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootAudienceList rootAudienceList = gson.fromJson(responseBody, RootAudienceList
                        .class);
                List<ResultAudienceList> resultAudienceLists = rootAudienceList.getResult();
                if (SDCollectionUtil.isEmpty(resultAudienceLists)) {
                    mView.onSuccess(LiveConstants.AUDIENCE_LIST, null);
                    return;
                }
                ResultAudienceList resultAudienceList = resultAudienceLists.get(0);
                List<ModelAudienceInfo> modelAudienceList = resultAudienceList.getBody();
                mView.onSuccess(LiveConstants.AUDIENCE_LIST, modelAudienceList);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 结束直播
     *
     * @param room_id
     */
    public void endInfo(String room_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.END_INFO);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(LiveConstants.END_INFO, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 观众进入房间
     *
     * @param room_id
     */
    public void enterRoom(String room_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.ENTER_ROOM);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(LiveConstants.ENTER_ROOM, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 观众退出房间
     *
     * @param room_id
     */
    public void exitRoom(String room_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.EXIT_ROOM);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(LiveConstants.EXIT_ROOM, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 直播登录，返回用户直播签名  GenerateSign
     */
    public void generateSign() {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.GENERATE_SIGN);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGenerateSign rootGenerateSign = gson.fromJson(responseBody, RootGenerateSign
                        .class);
                List<ResultGenerateSign> resultGenerateSigns = rootGenerateSign.getResult();
                if (SDCollectionUtil.isEmpty(resultGenerateSigns)) {
                    mView.onSuccess(LiveConstants.GENERATE_SIGN, null);
                    return;
                }
                ResultGenerateSign resultGenerateSign = resultGenerateSigns.get(0);
                List<ModelGenerateSign> modelGenerateSign = resultGenerateSign.getBody();
                mView.onSuccess(LiveConstants.GENERATE_SIGN, modelGenerateSign);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * get获取主播信息
     *
     * @param host_id
     */
    public void getHostInfo(String host_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("host_id", host_id);
        params.put("method", LiveConstants.HOST_INFO);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHostInfo rootHostInfo = gson.fromJson(responseBody, RootHostInfo.class);
                List<ResultHostInfo> resultHostInfos = rootHostInfo.getResult();
                if (SDCollectionUtil.isEmpty(resultHostInfos)) {
                    mView.onSuccess(LiveConstants.HOST_INFO, null);
                    return;
                }
                ResultHostInfo resultHostInfo = resultHostInfos.get(0);
                List<ModelHostInfo> modelHostInfo = resultHostInfo.getBody();
                mView.onSuccess(LiveConstants.HOST_INFO, modelHostInfo);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * post申请成为主播
     *
     * @param sex
     * @param mobile
     * @param picture
     * @param city
     * @param interest
     */
    public void postHostInfo(String sex, String mobile, String picture, String city, String
            interest) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("sex", sex);
        params.put("mobile", mobile);
        params.put("picture", picture);
        params.put("city", city);
        params.put("interest", interest);
        params.put("method", LiveConstants.HOST_INFO);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHostInfo rootHostInfo = gson.fromJson(responseBody, RootHostInfo.class);
                List<ResultHostInfo> resultHostInfos = rootHostInfo.getResult();
                if (SDCollectionUtil.isEmpty(resultHostInfos)) {
                    mView.onSuccess(LiveConstants.HOST_INFO, null);
                    return;
                }
                ResultHostInfo resultHostInfo = resultHostInfos.get(0);
                List<ModelHostInfo> modelHostInfo = resultHostInfo.getBody();
                mView.onSuccess(LiveConstants.HOST_INFO, modelHostInfo);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }


    /**
     * 获取主播标签
     */
    public void getHostTags(String tag_count) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("tag_count", tag_count);
        params.put("method", LiveConstants.HOST_TAGS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHostTags rootHostTags = gson.fromJson(responseBody, RootHostTags.class);
                List<ResultHostTags> resultHostTagss = rootHostTags.getResult();
                if (SDCollectionUtil.isEmpty(resultHostTagss)) {
                    mView.onSuccess(LiveConstants.HOST_TAGS, null);
                    return;
                }
                ResultHostTags resultHostTags = resultHostTagss.get(0);
                List<ModelHostTags> modelHostTags = resultHostTags.getBody();
                mView.onSuccess(LiveConstants.HOST_TAGS, modelHostTags);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 主播退出，结束直播
     */
    public void stopLive(String room_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("room_id", room_id);
        params.put("method", LiveConstants.STOP_LIVE);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootStopLive rootStopLive = gson.fromJson(responseBody, RootStopLive.class);
                List<ResultStopLive> resultStopLives = rootStopLive.getResult();
                if (SDCollectionUtil.isEmpty(resultStopLives)) {
                    mView.onSuccess(LiveConstants.STOP_LIVE, null);
                    return;
                }
                ResultStopLive resultStopLive = resultStopLives.get(0);
                List<ModelStopLive> modelStopLive = resultStopLive.getBody();
                mView.onSuccess(LiveConstants.STOP_LIVE, modelStopLive);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 业务服务器的数据字典接口
     */
    public void getBussDictionInfo(String dic_type) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("dic_type", dic_type);
        params.put("method", LiveConstants.BUSS_DICTION_INFO);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootBussDictionInfo rootBussDictionInfo = gson.fromJson(responseBody,
                        RootBussDictionInfo.class);
                List<ResultBussDictionInfo> resultBussDictionInfos = rootBussDictionInfo
                        .getResult();
                if (SDCollectionUtil.isEmpty(resultBussDictionInfos)) {
                    mView.onSuccess(LiveConstants.BUSS_DICTION_INFO, null);
                    return;
                }
                ResultBussDictionInfo resultBussDictionInfo = resultBussDictionInfos.get(0);
                List<ModelBussDictionInfo> modelBussDictionInfo = resultBussDictionInfo.getBody();
                mView.onSuccess(LiveConstants.BUSS_DICTION_INFO, modelBussDictionInfo);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 获取七牛UpToken
     */
    public void getUpToken() {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", LiveConstants.UP_TOKEN);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootUpToken rootUpToken = gson.fromJson(responseBody, RootUpToken.class);
                List<ResultUpToken> resultUpTokens = rootUpToken.getResult();
                if (SDCollectionUtil.isEmpty(resultUpTokens)) {
                    mView.onSuccess(LiveConstants.UP_TOKEN, null);
                    return;
                }
                ResultUpToken resultUpToken = resultUpTokens.get(0);
                List<ModelUpToken> modelUpToken = resultUpToken.getBody();
                mView.onSuccess(LiveConstants.UP_TOKEN, modelUpToken);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 校验用户是否关注该用户(主播)
     */
    public void checkFocus(String host_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("host_id", host_id);
        params.put("method", LiveConstants.CHECK_FOCUS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootCheckFocus rootCheckFocus = gson.fromJson(responseBody, RootCheckFocus.class);
                List<ResultCheckFocus> resultCheckFocuss = rootCheckFocus.getResult();
                if (SDCollectionUtil.isEmpty(resultCheckFocuss)) {
                    mView.onSuccess(LiveConstants.CHECK_FOCUS, null);
                    return;
                }
                ResultCheckFocus resultCheckFocus = resultCheckFocuss.get(0);
                List<ModelCheckFocus> modelCheckFocus = resultCheckFocus.getBody();
                mView.onSuccess(LiveConstants.CHECK_FOCUS, modelCheckFocus);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 关注主播
     */
    public void userFocus(String host_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("host_id", host_id);
        params.put("method", LiveConstants.USER_FOCUS);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(LiveConstants.USER_FOCUS, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 获取主播红包列表
     *
     * @param shop_id
     * @param host_id
     */
    public void getHandOutRedPacket(String shop_id, String host_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("id", shop_id);
        params.put("method", LiveConstants.HAND_OUT_RED_PACKET);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHandOutRedPacket rootHandOutRedPacket = gson.fromJson(responseBody, RootHandOutRedPacket.class);
                List<ResultHandOutRedPacket> resultHandOutRedPackets = rootHandOutRedPacket.getResult();
                if (SDCollectionUtil.isEmpty(resultHandOutRedPackets)) {
                    mView.onSuccess(LiveConstants.HAND_OUT_RED_PACKET_GET, null);
                    return;
                }
                ResultHandOutRedPacket resultHandOutRedPacket = resultHandOutRedPackets.get(0);
                List<ModelHandOutRedPacket> modelHandOutRedPacket = resultHandOutRedPacket.getBody();
                mView.onSuccess(LiveConstants.HAND_OUT_RED_PACKET_GET, modelHandOutRedPacket);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 主播发红包
     *
     * @param room_id
     * @param shop_id
     * @param host_id
     * @param red_packet_type
     * @param red_packet_count
     * @param red_packet_amount
     */
    public void postHandOutRedPacket(String room_id, String shop_id, String host_id, String red_packet_type, String red_packet_count, String red_packet_amount) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("tencent_room_id", room_id);
        params.put("id", shop_id);
        params.put("spokesman_id", host_id);
        params.put("red_packet_type", red_packet_type);
        params.put("red_packets", red_packet_count);
        params.put("red_packet_amount", red_packet_amount);

        params.put("method", LiveConstants.HAND_OUT_RED_PACKET);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHandOutRedPacketPost rootHandOutRedPacketPost = gson.fromJson(responseBody, RootHandOutRedPacketPost.class);
                List<ResultHandOutRedPacketPost> resultHandOutRedPacketPosts = rootHandOutRedPacketPost.getResult();
                if (SDCollectionUtil.isEmpty(resultHandOutRedPacketPosts)) {
                    mView.onSuccess(LiveConstants.HAND_OUT_RED_PACKET_POST, null);
                    return;
                }
                ResultHandOutRedPacketPost resultHandOutRedPacketPost = resultHandOutRedPacketPosts.get(0);
                List<ModelHandOutRedPacketPost> modelHandOutRedPacketPost = resultHandOutRedPacketPost.getBody();
                mView.onSuccess(LiveConstants.HAND_OUT_RED_PACKET_POST, modelHandOutRedPacketPost);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 抢红包接口。
     * @param user_id
     * @param red_packets_key
     */
    public void getRedPackets(String user_id, String red_packets_key) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("user_id", user_id);
        params.put("red_packets_key", red_packets_key);

        params.put("method", LiveConstants.GET_RED_PACKETS);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(LiveConstants.GET_RED_PACKETS, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }


    @Override
    public void onDestroy() {
        mContext = null;
        mView = null;
        gson = null;
        userCurrentInfo = null;
    }
}
