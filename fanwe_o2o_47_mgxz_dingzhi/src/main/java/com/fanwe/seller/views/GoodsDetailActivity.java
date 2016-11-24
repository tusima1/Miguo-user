package com.fanwe.seller.views;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.didikee.uilibs.views.WaitFinishTextView;
import com.fanwe.LoginActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.ListViewForScrollView;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.customview.SScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.GoodsDetailPagerAdapter;
import com.fanwe.seller.adapters.GoodsDetailShopListAdapter;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.model.getGroupDeatilNew.ImagesBean;
import com.fanwe.seller.model.getGroupDeatilNew.ModelGoodsDetailNew;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.seller.model.getGroupDeatilNew.ShopListBean;
import com.fanwe.seller.model.getSpecialTopic.TagBean;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.seller.presenters.SellerNewHttpHelper;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.umeng.UmengEventStatistics;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.MGDictUtil;
import com.fanwe.utils.MGStringFormatter;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.NetWorkStateUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.fanwe.o2o.miguo.R.id.tv_buy;

/**
 * created by didikee
 * 2016/10/20
 */
public class GoodsDetailActivity extends AppCompatActivity implements CallbackView2, View
        .OnClickListener {
    /**
     * 商品id (int)
     */
    public static final String EXTRA_GOODS_ID = "extra_goods_id";
    public static final String EXTRA_HOTEL_NUM = "extra_number";
    public static final String EXTRA_DETAIL_ID = "detail_id";
    public static final String EXTRA_FX_ID = "fx_id";

    private ViewPager mViewpager;
    private CircleIndicator mCirIndictor;
    private ImageView mIvTitleLeft;
    private ImageView mIvTitleShare;//标题分享
    private ImageView mIvTitleCollect;//标题收藏
    private TextView mTvTitleMiddle;//标题文字
    private SScrollView mSScrollView;
    private View mVGTitle;
    private View mFlViewpager;

    private int mTitleHeight;
    private int mFLViewpagerHeight;
    private View mLayoutTop;
    private SellerNewHttpHelper mHttpHelper;
    private String GoodsId = "";
    private TextView mTestHtmlTextView;
    private TextView mHtmlTipDesc;//温馨提示
    private TextView mHtmlTuiReason;//推荐理由
    private TextView mHtmlDetailDesc;//详情
    private LinearLayout mLLLauncher;//门店launcher
    private TextView mTvLauncherName;//门店launcher name
    private ImageView mIvShopArrow;//门店launcher img
    private FrameLayout mShopListLayout;//门店列表容器
    private boolean isOpen = false;//门店列表是否展开
    private ListViewForScrollView mShopListView;
    private GoodsDetailShopListAdapter mShopAdapter;

    private int offset;//偏移
    private int mDefaultHeight;
    private int mShopListCount = 0;
    private ShareInfoBean mShare_info;
    private String isCollected = "0";//是否已经收藏该商品
    private TextView mTvTopTitle;
    private TextView mTvTopYouHui;
    private TextView mTvTopQuan;
    private TextView mTvTopHot;
    private ImageButton mIbCollect;//第二个收藏按钮
    private TextView mTvOldMoney;
    private TextView mTvNewMoney;
    private WaitFinishTextView mTvBuy;
    private TextView mTvAdd2ShopCart;
    private SellerHttpHelper mSellerHelper;
    private ShoppingCartHelper mShoppingCartHelper;

    private int mNumber = 1;
    private String fx_id = "";
    private ShoppingCartInfo mShoppingCartInfo;
    private String mTimeStatus;
    private WebView mWebDetailDesc;//商品详细说明
    private WebView mWebDetailReason;//推荐理由
    private WebView mWebDetailTip;//温馨提示
    private View mStatusBar;//虚假状态栏
    private LinearLayout mLLTags;
    private View mLL3_Reason;
    private View mLL4_Detail;
    private View mLL5_Tips;
    private int mCollectTopHeight;

    private int mListViewHeight ;

    private boolean isTop;
    private boolean isBottom;
    private MGProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_detail);
        setBarStyle();
        init();

    }

    public void setBarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void init() {
        getIntentData(getIntent());
        initTitleLayout();
        initSScrollView();
        mHttpHelper = new SellerNewHttpHelper(this);
        mSellerHelper = new SellerHttpHelper(null, this, "");
        mShoppingCartHelper = new ShoppingCartHelper(this);
        requestData();
    }
    private void requestData(){
        mHttpHelper.getGroupBuyDetailNew(GoodsId);
        mStatusBar.post(new Runnable() {
            @Override
            public void run() {
                dialog = new MGProgressDialog(GoodsDetailActivity.this,R.style.MGProgressDialog);
                dialog.needFinishActivity(GoodsDetailActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntentData(intent)){
            requestData();
        }
    }

    private boolean getIntentData(Intent intent) {
        if (intent==null){
            finish();
            return false;
        }
        GoodsId = intent.getStringExtra(EXTRA_GOODS_ID);
        mNumber = intent.getIntExtra(EXTRA_HOTEL_NUM, 1);
        fx_id = intent.getStringExtra(EXTRA_FX_ID);
        if (TextUtils.isEmpty(GoodsId)) {
            MGToast.showToast("id为空");
            finish();
            return false;
        }
        return true;
    }

    private void initTitleLayout() {
        mIvTitleLeft = ((ImageView) findViewById(R.id.iv_left));
        mIvTitleShare = ((ImageView) findViewById(R.id.iv_share));
        mIvTitleCollect = ((ImageView) findViewById(R.id.iv_collect));
        mTvTitleMiddle = ((TextView) findViewById(R.id.tv_middle));
        mStatusBar = findViewById(R.id.status_bar);

        mVGTitle = findViewById(R.id.fr_top_title);
        mIvTitleLeft.setOnClickListener(this);
        mIvTitleShare.setOnClickListener(this);
        mIvTitleCollect.setOnClickListener(this);
    }

    private void initSScrollView() {
        initTopLayout();
        initBottomLayout();

        mSScrollView = ((SScrollView) findViewById(R.id.scrollView));
        mFlViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mFlViewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mTitleHeight = mVGTitle.getHeight(); //height is ready
                mFLViewpagerHeight = mFlViewpager.getHeight();
                int[] location=new int[2];
                mIbCollect.getLocationInWindow(location);
                mCollectTopHeight = location[1] - mTitleHeight - getSystemStatusBarHeight(GoodsDetailActivity.this) + mIbCollect.getHeight() ;

                mListViewHeight = mShopListView.getHeight();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    mFlViewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mFlViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                setTitleAction();
            }
        });

        mLL3_Reason = findViewById(R.id.location_3);
        mLL4_Detail = findViewById(R.id.location_4);
        mLL5_Tips = findViewById(R.id.location_5);

        mTestHtmlTextView = ((TextView) findViewById(R.id.text));
        mHtmlTipDesc = ((TextView) findViewById(R.id.tv_tips_desc));
        mHtmlTuiReason = ((TextView) findViewById(R.id.tv_description_reason));
        mHtmlDetailDesc = ((TextView) findViewById(R.id.tv_detail_desc));
        mWebDetailDesc = ((WebView) findViewById(R.id.test_webView));
        mWebDetailReason = ((WebView) findViewById(R.id.webView_reason));
        mWebDetailTip = ((WebView) findViewById(R.id.webView_tip));
        mLLTags = ((LinearLayout) findViewById(R.id.ll_tip_tags));

        //门店列表layout
        mShopListLayout = ((FrameLayout) findViewById(R.id.fl_container));
        mShopListView = ((ListViewForScrollView) findViewById(R.id.list_shoplist));
        mLLLauncher = ((LinearLayout) findViewById(R.id.ll_launcher));
        mTvLauncherName = ((TextView) findViewById(R.id.tv_launcher_name));
        mIvShopArrow = ((ImageView) findViewById(R.id.iv_arrow));

        mDefaultHeight = DisplayUtil.dp2px(this, 163);
        offset = DisplayUtil.dp2px(this, 300);
        mLLLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    mShopListView.setInterceptTouchEventEnable(false);
                    animatorAll(mShopListLayout, offset, mDefaultHeight);
                    rotateView(mIvShopArrow, -180f, 0f, false);
                    //在动画结束后刷新数据,让效果更自然
                } else {
                    //展开
                    if (mShopListCount < 2) {
                        return;
                    }
                    mShopListView.setInterceptTouchEventEnable(true);
                    mShopAdapter.setCount(mShopListCount);
                    mShopAdapter.notifyDataSetChanged();
                    animatorAll(mShopListLayout, mDefaultHeight, offset);
                    rotateView(mIvShopArrow, 0f, 180f, true);
                }
            }
        });
    }

    //底部的购买等
    private void initBottomLayout() {
        mTvOldMoney = ((TextView) findViewById(R.id.tv_old_money));
        mTvNewMoney = ((TextView) findViewById(R.id.tv_new_money));
        mTvBuy = ((WaitFinishTextView) findViewById(tv_buy));
        mTvAdd2ShopCart = ((TextView) findViewById(R.id.tv_add_shop_cart));
        mTvOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

        mTvBuy.setOnClickListener(this);
        mTvAdd2ShopCart.setOnClickListener(this);
    }


    private void setTitleAction() {
        if (mFLViewpagerHeight <= 0) {
            return;
        }
        mSScrollView.setOnScrollListener(new SScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                //状态栏透明度回调
                final int height = mFLViewpagerHeight - mTitleHeight;
                if (y <= 0) {   //设置标题的背景颜色
                    mVGTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    mTvTitleMiddle.setTextColor(Color.argb((int) 0, 46, 46, 46));
                    mStatusBar.setBackgroundColor(Color.argb((int) 0, 204, 204, 204));

                    mIvTitleLeft.setImageResource(R.drawable.ic_arrow_left_white);
                    mIvTitleShare.setImageResource(R.drawable.ic_share_pure);
                } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    mTvTitleMiddle.setTextColor(Color.argb((int) alpha, 46, 46, 46));
                    mVGTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    mStatusBar.setBackgroundColor(Color.argb((int) alpha, 204, 204, 204));

                    mIvTitleLeft.setImageResource(R.drawable.ic_arrow_left_white);
                    mIvTitleShare.setImageResource(R.drawable.ic_share_pure);
                } else {    //滑动到banner下面设置普通颜色
                    mVGTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    mTvTitleMiddle.setTextColor(Color.argb((int) 255, 46, 46, 46));
                    mStatusBar.setBackgroundColor(Color.argb((int) 255, 204, 204, 204));
                    mIvTitleLeft.setImageResource(R.drawable.ic_left_arrow_dark);
                    mIvTitleShare.setImageResource(R.drawable.ic_share_dark);

                }

                //------------------------------------------------------------------
                //收藏按钮
                if (mCollectTopHeight > 0) {
                    if (y > mCollectTopHeight) {
                        mIvTitleCollect.setVisibility(View.VISIBLE);
                        if ("1".equals(isCollected)) {
                            mIvTitleCollect.setImageResource(R.drawable.ic_collect_yes_dark);
                        } else {
                            mIvTitleCollect.setImageResource(R.drawable.ic_collect_no_dark);
                        }
                    }else {
                        mIvTitleCollect.setVisibility(View.GONE);
                    }
                }

                //-------------- listView 滑动冲突 ---------------
                //展开
                if (isOpen) {
                    if (isTop && oldY< y){
                        isTop=false;
                        mShopListView.setInterceptTouchEventEnable(true);
                    }
                    if (isBottom && oldY > y){
                        isBottom=false;
                        mShopListView.setInterceptTouchEventEnable(true);
                    }
                }


            }
        });

        mShopListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mShopListView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        mShopListView.setInterceptTouchEventEnable(false);
                        isTop=true;
                    }else {
                        isTop=false;
                        mShopListView.setInterceptTouchEventEnable(true);
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    mListViewHeight = mShopListView.getHeight();
                    View lastVisibleItemView = mShopListView.getChildAt(mShopListView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
                        mShopListView.setInterceptTouchEventEnable(false);
                        isBottom=true;
                    }else {
                        isBottom=false;
                        mShopListView.setInterceptTouchEventEnable(true);
                    }
                }
            }
        });
    }

    private void initTopLayout() {
        mLayoutTop = findViewById(R.id.layout_top);//顶部layout
        mViewpager = ((ViewPager) findViewById(R.id.viewpager));

        mCirIndictor = ((CircleIndicator) findViewById(R.id.indicator_circle));

        mFlViewpager = findViewById(R.id.fl_viewpager);

        mTvTopTitle = ((TextView) findViewById(R.id.tv_top_title));
        mTvTopYouHui = ((TextView) findViewById(R.id.tv_top_youhui));
        mTvTopQuan = ((TextView) findViewById(R.id.tv_top_quan));
        mTvTopHot = ((TextView) findViewById(R.id.tv_hot));
        mIbCollect = ((ImageButton) findViewById(R.id.ib_collect));
        mIbCollect.setOnClickListener(this);
    }

    private int getSystemStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void rotateView(View target, float start, float end, final boolean isOpen) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(target, "rotation", start, end);
        anim.setDuration(500);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    mTvLauncherName.setText("收起门店");
                } else {
                    mTvLauncherName.setText("其他门店");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private void animatorAll(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //直接调用整型估值器通过比例计算出宽度，然后再设给Button
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    mShopAdapter.setCount(1);
                    mShopAdapter.notifyDataSetChanged();
                }
                isOpen = !isOpen;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        valueAnimator.setDuration(350).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void bindData(ModelGoodsDetailNew modelGoodsDetailNew) {
        if (modelGoodsDetailNew == null) {
            finish();
            return;
        }
        mShare_info = modelGoodsDetailNew.getShare_info();

        //bind top layout
        mTvTopTitle = ((TextView) findViewById(R.id.tv_top_title));
        mTvTopYouHui = ((TextView) findViewById(R.id.tv_top_youhui));
        mTvTopQuan = ((TextView) findViewById(R.id.tv_top_quan));
        mTvTopHot = ((TextView) findViewById(R.id.tv_hot));
        mIbCollect = ((ImageButton) findViewById(R.id.ib_collect));

        String short_name = modelGoodsDetailNew.getShort_name();
        String limitedString = MGStringFormatter.getLimitedString(short_name, 10, "...");
        mTvTitleMiddle.setText(limitedString);//短Title
        mTvTitleMiddle.setTextColor(Color.argb(0, 46, 46, 46));
        mTvTopTitle.setText(modelGoodsDetailNew.getName());//Title

        String popularity = modelGoodsDetailNew.getPopularity();
        if (TextUtils.isEmpty(popularity)) {
            popularity = "0";
        }
        mTvTopHot.setText(popularity);//人气值

        String origin_price = modelGoodsDetailNew.getOrigin_price();//原价
        String tuan_price = modelGoodsDetailNew.getTuan_price();//团购价
        String tuan_price_with_unit = modelGoodsDetailNew.getTuan_price_with_unit();//99元/人/张
        mTvTopYouHui.setText(tuan_price_with_unit);

        //bind bottom(底部悬浮)
        mTvOldMoney.setText(MGStringFormatter.getFloat2(origin_price));
        mTvNewMoney.setText(MGStringFormatter.getFloat2(tuan_price));

        //收藏的状态
        isCollected = modelGoodsDetailNew.getIs_my_collect();
        setCollectResult();

        List<ShopListBean> shop_list = modelGoodsDetailNew.getShop_list();
        bindShopList(shop_list);
        if (shop_list!=null && shop_list.size()<=1){
            mLLLauncher.setVisibility(View.GONE);
        }

        //温馨提示
        String notes = modelGoodsDetailNew.getNotes();
        if (TextUtils.isEmpty(notes)) {
            mLL5_Tips.setVisibility(View.GONE);
        } else {
            List<TagBean> tag_list = modelGoodsDetailNew.getTag_list();
            final int margin = DisplayUtil.dp2px(this, 5);
            final int padding = DisplayUtil.dp2px(this, 1);
            if (tag_list != null && tag_list.size() > 0) {
                for (int i = 0; i < tag_list.size(); i++) {
                    TagBean tagBean = tag_list.get(i);
                    TextView item = new TextView(this);
                    item.setBackgroundResource(R.drawable.shape_solid_f5b830_cricle_small);
                    item.setTextColor(Color.WHITE);
                    item.setTextSize(12);
                    item.setGravity(Gravity.CENTER);
                    item.setText(tagBean.getTitle());
                    item.setPadding(margin, padding, margin, padding);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                            .LayoutParams.WRAP_CONTENT, ViewGroup
                            .LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    layoutParams.setMargins(0, 0, margin, 0);
                    item.setLayoutParams(layoutParams);
                    mLLTags.addView(item);
                }
            }
            loadWebData(mWebDetailTip, notes);
        }


        //推荐理由
        String recommend_desc = modelGoodsDetailNew.getRecommend_desc();
        if (TextUtils.isEmpty(recommend_desc)) {
            mLL3_Reason.setVisibility(View.GONE);
        } else {
            loadWebData(mWebDetailReason, recommend_desc);
        }

        //团购详情(详细说明)
        String tuan_detail = modelGoodsDetailNew.getTuan_detail();
        if (TextUtils.isEmpty(tuan_detail)) {
            mLL4_Detail.setVisibility(View.GONE);
        } else {
            loadWebData(mWebDetailDesc, tuan_detail);
        }

        //bind viewpager
        List<String> imageUrls = new ArrayList<>();
        for (ImagesBean imagesBean : modelGoodsDetailNew.getImages()) {
            imageUrls.add(imagesBean.getImage());
        }
        if (imageUrls.size()==0){
            imageUrls.add("");
        }
        GoodsDetailPagerAdapter goodsDetailPagerAdapter = new GoodsDetailPagerAdapter(imageUrls);
        mViewpager.setAdapter(goodsDetailPagerAdapter);

        mCirIndictor.setViewPager(mViewpager);
        goodsDetailPagerAdapter.registerDataSetObserver(mCirIndictor.getDataSetObserver());

        //--end bind data
    }

    private void loadWebData(WebView webView, String data) {
        if (TextUtils.isEmpty(data)) {
            webView.setVisibility(View.GONE);
            return;
        }
        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    private void bindShopList(List<ShopListBean> shop_list) {
        if (shop_list != null && shop_list.size() > 0) {
            mShopListCount = shop_list.size();
            mShopAdapter = new GoodsDetailShopListAdapter(shop_list, this);
            mShopListView.setScrollView(mSScrollView);
            mShopListView.setAdapter(mShopAdapter);
        }
    }

    private void setCollectResult() {
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("1".equals(isCollected)) {
                    //收藏
                    mIvTitleCollect.setImageResource(R.drawable.ic_collect_yes_dark);
                    mIbCollect.setImageResource(R.drawable.ic_collect_yes);
                } else {
                    mIvTitleCollect.setImageResource(R.drawable.ic_collect_no_dark);
                    mIbCollect.setImageResource(R.drawable.ic_collect_no);
                }
            }
        });
    }

    private void parseGoodsDetailNew(List data) {
        if (data != null&&data.size()>0) {
            ModelGoodsDetailNew modelGoodsDetailNew = (ModelGoodsDetailNew) data.get(0);
            if (modelGoodsDetailNew != null) {
                bindData(modelGoodsDetailNew);

                //创建本地购物车实体
                mShoppingCartInfo = new ShoppingCartInfo();
                mShoppingCartInfo.setId(GoodsId);
                mShoppingCartInfo.setFx_user_id(fx_id);//TODO fx_id 没登陆都是空
                mShoppingCartInfo.setNumber("1");
                mShoppingCartInfo.setImg(modelGoodsDetailNew.getIcon());
                mShoppingCartInfo.setLimit_num(modelGoodsDetailNew.getMax_num());
                mShoppingCartInfo.setIs_first(modelGoodsDetailNew.getIs_first());
                mShoppingCartInfo.setIs_first_price(modelGoodsDetailNew.getIs_first_price());
                mShoppingCartInfo.setOrigin_price(modelGoodsDetailNew.getOrigin_price());
                mShoppingCartInfo.setTuan_price(modelGoodsDetailNew.getTuan_price());
                mShoppingCartInfo.setTitle(modelGoodsDetailNew.getShort_name());
                mTimeStatus = modelGoodsDetailNew.getTime_status();
                mShoppingCartInfo.setBuyFlg(mTimeStatus);
                return;
            }
        }
        MGLog.e("test", "数据失败");
        finish();
    }

    @Override
    public void onBackPressed() {
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            GoodsDetailActivity.this.finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onSuccess(String responseBody) {
    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case SellerConstants.GROUP_BUY_DETAIL_NEW:
                if (dialog!=null){
                    dialog.dismiss();
                }
                parseGoodsDetailNew(datas);
                break;
            case SellerConstants.CHECK_GROUP_BUY_COLLECT:
                if (datas != null && datas.size() > 0) {
                    ModelCheckShopCollect modelCheckShopCollect = (ModelCheckShopCollect) datas
                            .get(0);
                    isCollected = modelCheckShopCollect.getCollect();
                    setCollectResult();
                }
                break;
            case SellerConstants.GROUP_BUY_COLLECT_DELETE:
                //取消收藏
                isCollected = "0";
                setCollectResult();
                break;
            case SellerConstants.GROUP_BUY_COLLECT_POST:
                //收藏
                isCollected = "1";
                setCollectResult();
                break;
            case ShoppingCartconstants.SHOPPING_CART:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvBuy.onFinish();
                    }
                });
                goToShopping();
                break;
            case ShoppingCartconstants.SHOPPING_CART_ADD:
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {
        if (dialog!=null){
            dialog.dismiss();
        }
        if (LiveConstants.SHOPPING_CART.equals(responseBody)){
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvBuy.onFinish();
                }
            });
        }
    }

    @Override
    public void onFinish(String method) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                //分享
                doShare();
                break;
            case R.id.iv_collect:
            case R.id.ib_collect:
                //收藏
                doCollect();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_add_shop_cart:
                clickBuyGoods(false);
                break;
            case tv_buy:
                clickBuyGoods(true);
                break;
        }
    }

    //收藏
    private void doCollect() {
        if (TextUtils.isEmpty(App.getInstance().getToken())){
            startActivity(new Intent(App.getApplication(), LoginActivity.class));
            return;
        }
        if ("1".equals(isCollected)) {
            //已经收藏
            mSellerHelper.deleteGroupBuyCollect(GoodsId);
        } else if ("0".equals(isCollected)) {
            mSellerHelper.postGroupBuyCollect(GoodsId);
        } else {
            mSellerHelper.checkGroupCollect(GoodsId);
        }

    }

    private void doShare() {
        if (mShare_info != null) {
            String content = mShare_info.getSummary();
            if (TextUtils.isEmpty(content)) {
                content = "欢迎来到米果小站";
            }
            String imageUrl = mShare_info.getImageurl();
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
                if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                    imageUrl = MGDictUtil.getShareIcon();
                }
            } else if (!imageUrl.startsWith("http")) {
                imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
                if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                    imageUrl = MGDictUtil.getShareIcon();
                }
            }
            String clickUrl = mShare_info.getClickurl();
            if (TextUtils.isEmpty(clickUrl)) {
                clickUrl = ServerUrl.SERVER_H5;
            } else {
                if (!TextUtils.isEmpty(fx_id)) {
                    clickUrl = clickUrl + "/ref_id/" + fx_id;
                } else {
                    clickUrl = clickUrl + "/ref_id/" + App.getApplication().getmUserCurrentInfo()
                            .getUserInfoNew().getUser_id();
                }
            }
            String title = mShare_info.getTitle();
            if (TextUtils.isEmpty(title)) {
                title = "米果小站";
            }
            UmengShareManager.share(this, title, content.trim(), clickUrl, UmengShareManager.getUMImage
                    (this, imageUrl), null);
        } else {
            MGToast.showToast("无分享内容");
        }
    }

    //立即购买
    private void clickBuyGoods(boolean isGoToShoppingCart) {
        if (TextUtils.isEmpty(GoodsId) && !NetWorkStateUtil.isConnected(GoodsDetailActivity.this)) {
            return;
        }
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            //当前已经登录，
            addGoodsToShoppingPacket(isGoToShoppingCart);
        } else {
            //当前未登录.
            if ("0".equals(mTimeStatus)) {
                MGToast.showToast("商品活动未开始");
                return;
            } else if ("1".equals(mTimeStatus)) {
                addToLocalShopping();
                goToShopping();
            } else if ("2".equals(mTimeStatus)) {
                MGToast.showToast("商品已经过期。");
                return;
            }
        }
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.BUY);
    }

    /**
     * 添加到购物车。
     */
    public void addGoodsToShoppingPacket(boolean isGoToShoppingCart) {
        String lgn_user_id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
        String cart_type = "1";
        mNumber = mNumber < 1 ? 1 : mNumber;
        if (mShoppingCartHelper != null) {
            if (isGoToShoppingCart) {
                mShoppingCartHelper.addToShoppingCart("", fx_id, lgn_user_id, GoodsId, cart_type,
                        mNumber + "");
            } else {
                mShoppingCartHelper.addToShoppingCart2("", fx_id, lgn_user_id, GoodsId,
                        cart_type, mNumber + "");
            }
        }
    }

    /**
     * 加入本地购物车。
     */
    private void addToLocalShopping() {
        if (mShoppingCartInfo == null) {
            MGToast.showToast("添加购物车失败!");
            return;
        }
        LocalShoppingcartDao.insertSingleNum(mShoppingCartInfo);
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvBuy.onFinish();
            }
        });
    }

    public void goToShopping() {
        Intent intent = new Intent(this, ShopCartActivity.class);
        startActivity(intent);
    }
}
