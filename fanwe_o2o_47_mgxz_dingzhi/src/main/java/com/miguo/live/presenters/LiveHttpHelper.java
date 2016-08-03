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
import com.miguo.live.model.applyRoom.ModelApplyRoom;
import com.miguo.live.model.applyRoom.ResultApplyRoom;
import com.miguo.live.model.applyRoom.RootApplyRoom;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceCount.ResultAudienceCount;
import com.miguo.live.model.getAudienceCount.RootAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceList;
import com.miguo.live.model.getAudienceList.ResultAudienceList;
import com.miguo.live.model.getAudienceList.RootAudienceList;
import com.miguo.live.model.getBussDictionInfo.ModelBussDictionInfo;
import com.miguo.live.model.getBussDictionInfo.ResultBussDictionInfo;
import com.miguo.live.model.getBussDictionInfo.RootBussDictionInfo;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getHostInfo.ResultHostInfo;
import com.miguo.live.model.getHostInfo.RootHostInfo;
import com.miguo.live.model.getHostTags.ModelHostTags;
import com.miguo.live.model.getHostTags.ResultHostTags;
import com.miguo.live.model.getHostTags.RootHostTags;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.model.getUpToken.ResultUpToken;
import com.miguo.live.model.getUpToken.RootUpToken;
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
    private String token;

    public static final String RESULT_OK="no_body_but_is_ok";

    public LiveHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        if (!TextUtils.isEmpty(token)) {
            return token;
        } else {
            token = userCurrentInfo.getToken();
            return token;
        }
    }

    /**
     * 请求直播列表
     *
     * @param pageNum
     * @param pageSize
     */
    public void getLiveList(int pageNum, int pageSize, String tag, String keyword, String city) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
    public void applyRoom(String shop_id,MgCallback mgCallback) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
     * 获取观众列表
     *
     * @param room_id
     */
    public void getAudienceList(String room_id) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
                List<ModelAudienceList> modelAudienceList = resultAudienceList.getBody();
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
     * @param character
     * @param mobile
     * @param picture
     * @param city
     * @param interest
     */
    public void postHostInfo(String character, String mobile, String picture, String city, String
            interest) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("character", character);
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
    public void getHostTags(String host_id, String tag_type) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("host_id", host_id);
        params.put("tag_type", tag_type);
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
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
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
        getToken();
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
        getToken();
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


    @Override
    public void onDestroy() {
        mContext = null;
        mView = null;
        gson = null;
        userCurrentInfo = null;
    }
}
