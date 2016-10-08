package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.adapter.TuanDetailCommentAdapter;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.model.CommentModel;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.ModelComment;
import com.fanwe.seller.model.ModelDisplayComment;
import com.fanwe.seller.model.ModelImage;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getCommentTotal.ModelCommentTotal;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论列表
 *
 * @author js02
 */
public class CommentListActivity extends BaseActivity implements CallbackView {

    /**
     * id (int)
     */
    public static final String EXTRA_ID = "extra_id";

    /**
     * type 评论类型,传进来的值要引用Constant.CommentType的属性 (int)
     */
    public static final String EXTRA_TYPE = "extra_type";

    @ViewInject(R.id.act_tuan_comment_list_ptrlv_comments)
    private PullToRefreshListView mPtrlvComment;

    @ViewInject(R.id.act_tuan_comment_tv_buy_dp_avg)
    private TextView mTvByDpAvg;

    @ViewInject(R.id.act_tuan_comment_tv_buy_dp_count)
    private TextView mTvByDpCount;

    @ViewInject(R.id.act_tuan_comment_rb_star)
    private RatingBar mRbStar;

    @ViewInject(R.id.act_tuan_comment_pb_start5)
    private ProgressBar mPbStar5;

    @ViewInject(R.id.act_tuan_comment_tv_start5)
    private TextView mTvStar5;

    @ViewInject(R.id.act_tuan_comment_pb_start4)
    private ProgressBar mPbStar4;

    @ViewInject(R.id.act_tuan_comment_tv_start4)
    private TextView mTvStar4;

    @ViewInject(R.id.act_tuan_comment_pb_start3)
    private ProgressBar mPbStar3;

    @ViewInject(R.id.act_tuan_comment_tv_start3)
    private TextView mTvStar3;

    @ViewInject(R.id.act_tuan_comment_pb_start2)
    private ProgressBar mPbStar2;

    @ViewInject(R.id.act_tuan_comment_tv_start2)
    private TextView mTvStar2;

    @ViewInject(R.id.act_tuan_comment_pb_start1)
    private ProgressBar mPbStar1;

    @ViewInject(R.id.act_tuan_comment_tv_start1)
    private TextView mTvStar1;

    @ViewInject(R.id.act_tuan_comment_btn_publish)
    private Button mBtnPublish;

    private List<CommentModel> mListModel = new ArrayList<CommentModel>();
    private TuanDetailCommentAdapter mAdapter;

    private PageModel mPage = new PageModel();

    private String mId;
    private String mStrType;
    int pageNum = 1;
    int pageSize = 10;
    boolean isRefresh;
    private ModelDisplayComment modelDisplayComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_comment_list);
        init();
    }

    private SellerHttpHelper sellerHttpHelper;

    private void getData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
        if (Constant.CommentType.DEAL.equals(mStrType)) {
            //团购
            sellerHttpHelper.getCommentList(pageNum, pageSize, mId, "");
        } else if (Constant.CommentType.STORE.equals(mStrType)) {
            //门店
            sellerHttpHelper.getCommentList(pageNum, pageSize, "", mId);
        }
    }

    private void init() {
        getIntentData();
        initTitle();
        bindDefaultData();
        initPullToRefreshListView();
        bindData(modelDisplayComment);

        if (sellerHttpHelper==null){
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
        if (Constant.CommentType.DEAL.equals(mStrType)) {
            //团购
            sellerHttpHelper.getCommentTotal(mId, "");
        } else if (Constant.CommentType.STORE.equals(mStrType)) {
            //门店
            sellerHttpHelper.getCommentTotal("", mId);
        }
    }

    private void bindDefaultData() {
        mAdapter = new TuanDetailCommentAdapter(mListModel, this);
        mPtrlvComment.setAdapter(mAdapter);
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra(EXTRA_ID);
        mStrType = getIntent().getStringExtra(EXTRA_TYPE);
        modelDisplayComment = (ModelDisplayComment) getIntent().getSerializableExtra("modelDisplayComment");

        if (TextUtils.isEmpty(mId)) {
            MGToast.showToast("id为空");
            finish();
        }
        if (TextUtils.isEmpty(mStrType)) {
            MGToast.showToast("评论类型为空");
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        init();
        super.onNewIntent(intent);
    }

    private void initPullToRefreshListView() {
        mPtrlvComment.setMode(Mode.BOTH);
        mPtrlvComment.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(items)) {
                    pageNum++;
                }
                getData();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPtrlvComment.setRefreshing();
    }

    /**
     * 评分栏
     */
    private void bindData(ModelDisplayComment bean) {
        if (bean == null) {
            return;
        }
        final String strName = bean.getName();

        if (!TextUtils.isEmpty(App.getInstance().getmUserCurrentInfo().getToken())) {
            if (bean.getAllow_dp() == 1) {
                mBtnPublish.setVisibility(View.VISIBLE);
            } else {
                mBtnPublish.setVisibility(View.GONE);
            }
        } else {
            mBtnPublish.setVisibility(View.VISIBLE);
        }

        mBtnPublish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(CommentListActivity.this, AddCommentActivity.class);
                i.putExtra(AddCommentActivity.EXTRA_ID, mId);
                i.putExtra(AddCommentActivity.EXTRA_TYPE, mStrType);
                if (!TextUtils.isEmpty(strName)) {
                    i.putExtra(AddCommentActivity.EXTRA_NAME, strName);
                }
                startActivity(i);
            }
        });

        mTvByDpAvg.setText(bean.getBuy_dp_avg());
        mTvByDpCount.setText(bean.getMessage_count());

        float ratingStar = SDTypeParseUtil.getFloat(bean.getBuy_dp_avg());
        mRbStar.setRating(ratingStar);

        mTvStar5.setText(bean.getStar_5());
        mTvStar4.setText(bean.getStar_4());
        mTvStar3.setText(bean.getStar_3());
        mTvStar2.setText(bean.getStar_2());
        mTvStar1.setText(bean.getStar_1());

        mPbStar5.setProgress(bean.getStar_dp_width_5());
        mPbStar4.setProgress(bean.getStar_dp_width_4());
        mPbStar3.setProgress(bean.getStar_dp_width_3());
        mPbStar2.setProgress(bean.getStar_dp_width_2());
        mPbStar1.setProgress(bean.getStar_dp_width_1());

    }

    private void initTitle() {
        mTitle.setMiddleTextTop("评论列表");
    }

    @Override
    protected void onNeedRefreshOnResume() {
        mPtrlvComment.setRefreshing();
        if (Constant.CommentType.DEAL.equals(mStrType)) {
            //团购
            sellerHttpHelper.getCommentTotal(mId, "");
        } else if (Constant.CommentType.STORE.equals(mStrType)) {
            //门店
            sellerHttpHelper.getCommentTotal("", mId);
        }
        super.onNeedRefreshOnResume();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        // TODO Auto-generated method stub
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case LOGIN_SUCCESS:
                setmIsNeedRefreshOnResume(true);
                break;
            case COMMENT_SUCCESS:
                setmIsNeedRefreshOnResume(true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelComment> items;
    List<ModelCommentTotal> itemsTotal;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (SellerConstants.COMMENT_LIST.equals(method)) {
            items = datas;
            msg.what = 0;
        } else if (SellerConstants.COMMENT_TOTAL.equals(method)) {
            itemsTotal = datas;
            msg.what = 1;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        mListModel.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        for (ModelComment modelComment : items) {
                            CommentModel beanCommentModel = new CommentModel();
                            beanCommentModel.setId(modelComment.getId());
                            beanCommentModel.setCreate_time(modelComment.getCreate_time());
                            beanCommentModel.setContent(modelComment.getContent());
                            beanCommentModel.setPoint(modelComment.getPoint());
                            beanCommentModel.setUser_name(modelComment.getNick());

                            //缩略图
                            List<String> images = new ArrayList<>();
                            if (!SDCollectionUtil.isEmpty(modelComment.getImages())) {
                                for (ModelImage imageShopInfo : modelComment.getImages()) {
                                    images.add(imageShopInfo.getImage());
                                }
                            }
                            beanCommentModel.setImages(images);
                            //原图
                            List<String> oimages = new ArrayList<>();
                            if (!SDCollectionUtil.isEmpty(modelComment.getOimages())) {
                                for (ModelImage imageShopInfo : modelComment.getOimages()) {
                                    oimages.add(imageShopInfo.getImage());
                                }
                            }
                            beanCommentModel.setOimages(oimages);

                            mListModel.add(beanCommentModel);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mPtrlvComment.onRefreshComplete();
                    break;
                case 1:
                    if (!SDCollectionUtil.isEmpty(itemsTotal)) {
                        ModelCommentTotal bean = itemsTotal.get(0);
                        modelDisplayComment = new ModelDisplayComment();

                        modelDisplayComment.setStar_1(bean.getYi());
                        modelDisplayComment.setStar_2(bean.getEr());
                        modelDisplayComment.setStar_3(bean.getSan());
                        modelDisplayComment.setStar_4(bean.getSi());
                        modelDisplayComment.setStar_5(bean.getWu());
                        modelDisplayComment.setBuy_dp_avg(bean.getAvgcmt());
                        modelDisplayComment.setBuy_dp_sum(bean.getTotal());
                        bindData(modelDisplayComment);
                    }
                    break;
            }
        }
    };
}