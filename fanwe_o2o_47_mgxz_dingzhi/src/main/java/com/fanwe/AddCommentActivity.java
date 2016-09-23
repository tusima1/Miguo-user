package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.AddCommentFragment;
import com.fanwe.fragment.AddCommentFragment.AddCommentFragmentListener;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.postShopComment.RootShopComment;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getBussDictionInfo.ModelBussDictionInfo;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.customviews.MGToast;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sunday.eventbus.SDEventManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加评论
 *
 * @author js02
 */
public class AddCommentActivity extends BaseActivity implements CallbackView {

    /**
     * id (int)
     */
    public static final String EXTRA_ID = "extra_id";

    /**
     * type 评论类型,传进来的值要引用Constant.CommentType的属性 (String)
     */
    public static final String EXTRA_TYPE = "extra_type";

    /**
     * name
     */
    public static final String EXTRA_NAME = "extra_name";

    private AddCommentFragment mFragAddComment;

    private String mId;
    private String mStrType;
    private String mStrName;
    private int mPoint;
    private List<File> mListFile;
    private LiveHttpHelper liveHttpHelper;
    private SellerHttpHelper sellerHttpHelper;
    private UploadManager uploadManager;

    SDDialogProgress mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_add_comment);
        init();
        mProgress = progressDialog("请稍等...");
    }

    private void init() {
        getIntentData();
        initTitle();
        addFragments();
        preParam();
    }

    private void preParam() {
        liveHttpHelper = new LiveHttpHelper(this, this);
        sellerHttpHelper = new SellerHttpHelper(this, this);
        uploadManager = new UploadManager();
    }

    private void addFragments() {
        mFragAddComment = new AddCommentFragment();
        mFragAddComment.setmListener(new AddCommentFragmentListener() {

            @Override
            public void onCompressFinish(List<File> listFile) {
                mListFile = listFile;
                requestComments();
            }

            @Override
            public void onCommendNumberListener(int number) {
                mPoint = number;
            }
        });
        getSDFragmentManager().replace(R.id.act_add_comment_fl_content, mFragAddComment);
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra(EXTRA_ID);
        mStrType = getIntent().getStringExtra(EXTRA_TYPE);
        mStrName = getIntent().getStringExtra(EXTRA_NAME);
        if (TextUtils.isEmpty(mId)) {
            MGToast.showToast("id为空");
            finish();
        }
        if (TextUtils.isEmpty(mStrType)) {
            MGToast.showToast("评论类型为空");
            finish();
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(mStrName);
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("发布");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        if (SDCollectionUtil.isEmpty(mFragAddComment.getSelectedImages())) {
            if (requestComments()) {
                if (mProgress != null) {
                    mProgress.show();
                }
            }
        } else {
            if (validateParam()) {
                mFragAddComment.compressSelectedImages();
                if (mProgress != null) {
                    mProgress.show();
                }
            }
        }
    }

    protected boolean validateParam() {
        if (!TextUtils.isEmpty(mFragAddComment.getCommentContent())) {
            if (mFragAddComment.getCommentContent().length() >= 15 && mFragAddComment.getCommentContent().length() <= 99) {
                return true;
            } else {
                return false;
            }
        } else {
            MGToast.showToast("评论内容不能为空");
            return false;
        }
    }

    protected boolean pointParam() {
        if (mPoint == 0) {
            MGToast.showToast("还没有对评价项评分哦");
            return false;
        }
        return true;
    }

    protected boolean requestComments() {
        if (!validateParam()) {
            return false;
        }
        if (!pointParam()) {
            return false;
        }
        if (!SDCollectionUtil.isEmpty(mListFile)) {
            //上传文件
            liveHttpHelper.getBussDictionInfo("Client");
        } else {
            if (Constant.CommentType.DEAL.equals(mStrType)) {
                //团购
                sellerHttpHelper.postGroupBuyComment(mId, mFragAddComment.getCommentContent(), String.valueOf(mPoint), "");
            } else if (Constant.CommentType.STORE.equals(mStrType)) {
                //门店
                sellerHttpHelper.postShopComment(mId, mFragAddComment.getCommentContent(), String.valueOf(mPoint), "");
            }
        }
        return true;
    }

    private SDDialogProgress progressDialog(final String msg) {
        mProgress = new SDDialogProgress();
        mProgress.setTextMsg(msg);
        return mProgress;
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    String uploadToken;
    String urlQiNiu;
    List<RootShopComment> rootShopCommentList;

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.BUSS_DICTION_INFO.equals(method)) {
            for (ModelBussDictionInfo modelBussDictionInfo : (ArrayList<ModelBussDictionInfo>) datas) {
                if ("DomainName".equals(modelBussDictionInfo.getDic_value())) {
                    urlQiNiu = modelBussDictionInfo.getDic_mean();
                }
            }
            liveHttpHelper.getUpToken();
        } else if (LiveConstants.UP_TOKEN.equals(method)) {
            ArrayList<ModelUpToken> tokens = (ArrayList<ModelUpToken>) datas;
            if (!SDCollectionUtil.isEmpty(tokens)) {
                uploadToken = tokens.get(0).getUptoken();
                if (!TextUtils.isEmpty(uploadToken)) {
                    uploadFile();
                }
            }
        } else if (SellerConstants.SHOP_COMMENT.equals(method)) {
            if (mProgress != null) {
                mProgress.dismiss();
            }
            rootShopCommentList = datas;
            if (!SDCollectionUtil.isEmpty(rootShopCommentList)) {
                RootShopComment root = rootShopCommentList.get(0);
                if (!"200".equals(root.getStatusCode())) {
                    {
                        MGToast.showToast(root.getMessage());
                        return;
                    }
                }
                MGToast.showToast("评论成功");
                SDEventManager.post(EnumEventTag.COMMENT_SUCCESS.ordinal());
                Intent intent = new Intent(mActivity, CommentListActivity.class);
                intent.putExtra(CommentListActivity.EXTRA_ID, mId);
                intent.putExtra(CommentListActivity.EXTRA_TYPE, mStrType);
                startActivity(intent);
                finish();
            }
        }
    }

    private StringBuffer sbFileKeys;

    private void uploadFile() {
        sbFileKeys = new StringBuffer();
        for (int i = 0; i < mListFile.size(); i++) {
            File uploadFile = mListFile.get(i);
            uploadManager.put(uploadFile, null, uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, com.qiniu.android.http.ResponseInfo respInfo,
                                             JSONObject jsonData) {
                            if (respInfo.isOK()) {
                                try {
                                    String fileKey = jsonData.getString("key");
                                    if (!TextUtils.isEmpty(fileKey)) {
                                        sbFileKeys.append(urlQiNiu + fileKey + ",");
                                    }
                                    if (sbFileKeys.toString().indexOf("http:") != -1) {
                                        String[] strs = sbFileKeys.toString().split("http://");
                                        if (mListFile.size() == (strs.length - 1)) {
                                            //文件上传成功，提交评论信息
                                            if (Constant.CommentType.DEAL.equals(mStrType)) {
                                                //团购
                                                sellerHttpHelper.postGroupBuyComment(mId, mFragAddComment.getCommentContent(), String.valueOf(mPoint), sbFileKeys.toString());
                                            } else if (Constant.CommentType.STORE.equals(mStrType)) {
                                                //门店
                                                sellerHttpHelper.postShopComment(mId, mFragAddComment.getCommentContent(), String.valueOf(mPoint), sbFileKeys.toString());
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                }
                            } else {
                            }
                        }
                    }, null);
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }
}