package com.fanwe.seller.views;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.customview.ListViewForScrollView;
import com.fanwe.customview.SScrollView;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.GoodsDetailPagerAdapter;
import com.fanwe.seller.adapters.GoodsDetailShopListAdapter;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.model.getGroupDeatilNew.ImagesBean;
import com.fanwe.seller.model.getGroupDeatilNew.ModelGoodsDetailNew;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.seller.model.getGroupDeatilNew.ShopListBean;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.seller.presenters.SellerNewHttpHelper;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.umeng.UmengEventStatistics;
import com.fanwe.umeng.UmengShareManager;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends Activity implements CallbackView2, View.OnClickListener {
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
//    private String GoodsId="0ecd5927-1322-4d9e-b092-9cb213ada070";
//    private String GoodsId="004c0d20-43f8-4ef8-ae7f-a15d8359655f";
    private String GoodsId="";
    private TextView mTestHtmlTextView;
    private TextView mHtmlTipDesc;//温馨提示
    private TextView mHtmlTuiReason;//推荐理由
    private TextView mHtmlDetailDesc;//详情
    private LinearLayout mLLLauncher;//门店launcher
    private TextView mTvLauncherName;//门店launcher name
    private ImageView mIvShopArrow;//门店launcher img
    private FrameLayout mShopListLayout;//门店列表容器
    private boolean isOpen=false;//门店列表是否展开
    private ListViewForScrollView mShopListView;
    private GoodsDetailShopListAdapter mShopAdapter;

    private int offset;//偏移
    private int mDefaultHeight;
    private int mShopListCount=0;
    private ShareInfoBean mShare_info;
    private String isCollected="0";//是否已经收藏该商品
    private TextView mTvTopTitle;
    private TextView mTvTopYouHui;
    private TextView mTvTopQuan;
    private TextView mTvTopHot;
    private ImageButton mIbCollect;//第二个收藏按钮
    private TextView mTvOldMoney;
    private TextView mTvNewMoney;
    private TextView mTvBuy;
    private TextView mTvAdd2ShopCart;
    private SellerHttpHelper mSellerHelper;
    private ShoppingCartHelper mShoppingCartHelper;

    private int mNumber = 1;
    private String fx_id = "";
    private ShoppingCartInfo mShoppingCartInfo;
    private String mTimeStatus;
    private WebView mWebDetailDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_detail);
        init();
    }

    private void init() {
        getIntentData();
        initTitleLayout();
        initSScrollView();
        mHttpHelper = new SellerNewHttpHelper(this);
        mSellerHelper = new SellerHttpHelper(null,this,"");
        mShoppingCartHelper = new ShoppingCartHelper(this);
        mHttpHelper.getGroupBuyDetailNew(GoodsId);
    }
    private void getIntentData() {
        Intent intent = getIntent();
        GoodsId = intent.getStringExtra(EXTRA_GOODS_ID);
        mNumber = intent.getIntExtra(EXTRA_HOTEL_NUM, 1);
        fx_id = intent.getStringExtra(EXTRA_FX_ID);
        if (TextUtils.isEmpty(GoodsId)) {
            MGToast.showToast("id为空");
            finish();
            return;
        }
    }

    private void initTitleLayout() {
        mIvTitleLeft = ((ImageView) findViewById(R.id.iv_left));
        mIvTitleShare = ((ImageView) findViewById(R.id.iv_share));
        mIvTitleCollect = ((ImageView) findViewById(R.id.iv_collect));
        mTvTitleMiddle = ((TextView) findViewById(R.id.tv_middle));

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
                Log.e("test", "height:" + mTitleHeight + "----" + mFLViewpagerHeight);
                setTitleAction();
            }
        });

        mTestHtmlTextView = ((TextView) findViewById(R.id.text));
        mHtmlTipDesc = ((TextView) findViewById(R.id.tv_tips_desc));
        mHtmlTuiReason = ((TextView) findViewById(R.id.tv_description_reason));
        mHtmlDetailDesc = ((TextView) findViewById(R.id.tv_detail_desc));
        mWebDetailDesc = ((WebView) findViewById(R.id.test_webView));

        //门店列表layout
        mShopListLayout = ((FrameLayout) findViewById(R.id.fl_container));
        mShopListView = ((ListViewForScrollView) findViewById(R.id.list_shoplist));
        mLLLauncher= ((LinearLayout) findViewById(R.id.ll_launcher));
        mTvLauncherName= ((TextView) findViewById(R.id.tv_launcher_name));
        mIvShopArrow= ((ImageView) findViewById(R.id.iv_arrow));

        mDefaultHeight = DisplayUtil.dp2px(this, 163);
        offset = DisplayUtil.dp2px(this, 300);
        mLLLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen){
                    mShopListView.setInterceptTouchEventEnable(false);
                    animatorAll(mShopListLayout,offset,mDefaultHeight);
                    rotateView(mIvShopArrow,-180f,0f,false);
                    //在动画结束后刷新数据,让效果更自然
                }else {
                    //展开
                    if (mShopListCount<2){
                        return;
                    }
                    mShopListView.setInterceptTouchEventEnable(true);
                    mShopAdapter.setCount(mShopListCount);
                    mShopAdapter.notifyDataSetChanged();
                    animatorAll(mShopListLayout,mDefaultHeight,offset);
                    rotateView(mIvShopArrow,0f,180f,true);
                }
            }
        });
    }

    //底部的购买等
    private void initBottomLayout() {
        mTvOldMoney = ((TextView) findViewById(R.id.tv_old_money));
        mTvNewMoney = ((TextView) findViewById(R.id.tv_new_money));
        mTvBuy = ((TextView) findViewById(R.id.tv_buy));
        mTvAdd2ShopCart = ((TextView) findViewById(R.id.tv_add_shop_cart));
        mTvOldMoney.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线

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
                    mVGTitle.setBackgroundColor(Color.argb((int) 0, 255,255,255));
                } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    mTvTitleMiddle.setTextColor(Color.argb((int) alpha, 46,46,46));
                    mVGTitle.setBackgroundColor(Color.argb((int) alpha, 255,255,255));
                } else {    //滑动到banner下面设置普通颜色
                    mVGTitle.setBackgroundColor(Color.argb((int) 255, 255,255,255));
                }
//                double fff = y * 1.0 / mFLViewpagerHeight;
//                if (fff >= 0 && fff <= 1) {
//                    int percent=(int) (fff * 100);
//                    String alphaWhite;
//                    if (fff *100<10){
//                        alphaWhite="00";
//                    }else {
//                        alphaWhite= getAlphaWhite(percent);
//                    }
//                    int alphaWhiteColor = Color.parseColor("#"+alphaWhite + "FFFFFF");
//                    mVGTitle.setBackgroundColor(alphaWhiteColor);
//                    Log.e("test","percent:"+percent);
//
//                    if (mTvTitleMiddle.getVisibility()==View.VISIBLE){
//                        //显示标题
//                        mTvTitleMiddle.setVisibility(View.GONE);
//                    }
//
//                } else {
//                    if (mTvTitleMiddle.getVisibility()==View.GONE){
//                        //显示标题
//                        mTvTitleMiddle.setVisibility(View.VISIBLE);
//                        mTvTitleMiddle.setTextColor(Color.DKGRAY);
//                    }
//                }

                //黑与白
                boolean ViewPagerVisible = mSScrollView.isChildVisible(mFlViewpager);
                if (ViewPagerVisible){
                    mIvTitleLeft.setImageResource(R.drawable.ic_arrow_left_white);
                    mIvTitleShare.setImageResource(R.drawable.ic_share_pure);
                }else{
                    mIvTitleLeft.setImageResource(R.drawable.ic_left_arrow_dark);
                    mIvTitleShare.setImageResource(R.drawable.ic_share_dark);
                }

                //------------------------------------------------------------------
                //收藏按钮
                boolean TopLayoutVisible = mSScrollView.isChildVisible(mLayoutTop);
                if (TopLayoutVisible){
                    mIvTitleCollect.setVisibility(View.GONE);
                }else {
                    mIvTitleCollect.setVisibility(View.VISIBLE);
                    if ("1".equals(isCollected)){
                        mIvTitleCollect.setImageResource(R.drawable.ic_collect_yes_dark);
                    }else {
                        mIvTitleCollect.setImageResource(R.drawable.ic_collect_no_dark);
                    }

                }

            }
        });
    }

    private String getAlphaWhite(int percent) {
        if (percent >= 100) {
            percent = 100;
        }
        if (percent <= 0) {
            percent = 0;
        }
        float temp = 255 * percent * 1.0f / 100f;
        int round = Math.round(temp);//四舍五入
        String hexString = Integer.toHexString(round);
        if (hexString.length() < 2) {
            hexString += "0";
        }
        return hexString;
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

    private List<ImageView> getTestData(List<ImagesBean> images) {
        List<ImageView> data = new ArrayList<>();
        for (ImagesBean image : images) {
            ImageView imageView = new ImageView(this);
            SDViewBinder.setImageView(image.getImage(),imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            data.add(imageView);
        }
        return data;
    }

    private void rotateView(View target, float start, float end, final boolean isOpen){
        // 第二个参数"rotation"表明要执行旋转
        // 0f -> 360f，从旋转360度，也可以是负值，负值即为逆时针旋转，正值是顺时针旋转。
        ObjectAnimator anim = ObjectAnimator.ofFloat(target, "rotation", start, end);
        // 动画的持续时间，执行多久？
        anim.setDuration(500);
        // 回调监听
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen){
                    mTvLauncherName.setText("收起门店");
//                    mIvShopArrow.clearAnimation();
//                    mIvShopArrow.setImageResource(R.drawable.ic_arrow_up);
                }else {
                    mTvLauncherName.setText("其他门店");
//                    mIvShopArrow.clearAnimation();
//                    mIvShopArrow.setImageResource(R.drawable.ic_arrow_down_dark);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        // 正式开始启动执行动画
        anim.start();
    }

    private void animatorAll(final View target, final int start, final int end){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer)animator.getAnimatedValue();
                Log.d("test", "current value: " + currentValue);

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //这里我偷懒了，不过有现成的干吗不用呢
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
                if (isOpen){
                    mShopAdapter.setCount(1);
                    mShopAdapter.notifyDataSetChanged();
                }
                isOpen=!isOpen;
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
//        if (TextUtils.isEmpty(GoodsId)){
//            return;
//        }
//        if (mHttpHelper==null){
//            mHttpHelper=new SellerNewHttpHelper(this);
//        }
//        mHttpHelper.getGroupBuyDetailNew(GoodsId);
    }

    private void bindData(ModelGoodsDetailNew modelGoodsDetailNew) {
        if (modelGoodsDetailNew==null){
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

        mTvTitleMiddle.setText(modelGoodsDetailNew.getShort_name());//短Title
        mTvTopTitle.setText(modelGoodsDetailNew.getName());//Title
        mTvTopHot.setText(modelGoodsDetailNew.getPopularity());//人气值

        String origin_price = modelGoodsDetailNew.getOrigin_price();//原价
        String tuan_price = modelGoodsDetailNew.getTuan_price();//团购价
        String tuan_price_with_unit = modelGoodsDetailNew.getTuan_price_with_unit();//99元/人/张
        mTvTopYouHui.setText(tuan_price_with_unit);

        //bind bottom(底部悬浮)
        mTvOldMoney.setText(origin_price);
        mTvNewMoney.setText(tuan_price);

        //收藏的状态
        isCollected=modelGoodsDetailNew.getIs_my_collect();
        setCollectResult();

        List<ShopListBean> shop_list = modelGoodsDetailNew.getShop_list();
        bindShopList(shop_list);

        //温馨提示
        String notes = modelGoodsDetailNew.getNotes();
        Log.e("test","notes:"+notes);
        mHtmlTipDesc.setTextSize(16);
        mHtmlTipDesc.setText(Html.fromHtml(notes));

        //推荐理由
        String recommend_desc = modelGoodsDetailNew.getRecommend_desc();
        if (TextUtils.isEmpty(recommend_desc)){
            //TODO 做啥呢?隐藏?
        }else {
            mHtmlTuiReason.setText(Html.fromHtml(recommend_desc));
        }

        //团购详情(详细说明)
        String tuan_detail = modelGoodsDetailNew.getTuan_detail();
//        mHtmlDetailDesc.setTextSize(16);
//        mHtmlDetailDesc.setText(Html.fromHtml(tuan_detail));
//        Spanned spanned = Html.fromHtml(tuan_detail);
//        String s = Html.toHtml(spanned);
//        mWebDetailDesc.loadData(tuan_detail,"text/html","utf-8");
        mWebDetailDesc.loadDataWithBaseURL(null,tuan_detail,"text/html","utf-8",null);

        //bind viewpager
        GoodsDetailPagerAdapter goodsDetailPagerAdapter = new GoodsDetailPagerAdapter(getTestData
                (modelGoodsDetailNew.getImages()));
        mViewpager.setAdapter(goodsDetailPagerAdapter);

        mCirIndictor.setViewPager(mViewpager);
        goodsDetailPagerAdapter.registerDataSetObserver(mCirIndictor.getDataSetObserver());
    }

    private void bindShopList(List<ShopListBean> shop_list) {
        if (shop_list!=null && shop_list.size()>0){
            mShopListCount=shop_list.size();
            mShopAdapter = new GoodsDetailShopListAdapter(shop_list,this);
            mShopListView.setScrollView(mSScrollView);
            mShopListView.setAdapter(mShopAdapter);
        }
    }

    private void setCollectResult(){
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("1".equals(isCollected)){
                    //收藏
                    mIvTitleCollect.setImageResource(R.drawable.ic_collect_yes_dark);
                    mIbCollect.setImageResource(R.drawable.ic_collect_yes);
                }else {
                    mIvTitleCollect.setImageResource(R.drawable.ic_collect_no_dark);
                    mIbCollect.setImageResource(R.drawable.ic_collect_no);
                }
            }
        });
    }

    private void parseGoodsDetailNew(List data) {
        if (data!=null){
            ModelGoodsDetailNew modelGoodsDetailNew = (ModelGoodsDetailNew) data.get(0);
            if (modelGoodsDetailNew!=null){
                bindData(modelGoodsDetailNew);

                //创建本地购物车实体
                mShoppingCartInfo = new ShoppingCartInfo();
                mShoppingCartInfo.setId(GoodsId);
                mShoppingCartInfo.setFx_user_id("");//TODO fx_id 没登陆都是空
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
        MGLog.e("test","数据失败");
        finish();
    }

    @Override
    public void onSuccess(String responseBody) {}
    @Override
    public void onSuccess(String method, List datas) {
        switch (method){
            case SellerConstants.GROUP_BUY_DETAIL_NEW:
                parseGoodsDetailNew(datas);
                break;
            case SellerConstants.CHECK_GROUP_BUY_COLLECT:
                if (datas!=null && datas.size()>0){
                    ModelCheckShopCollect modelCheckShopCollect = (ModelCheckShopCollect) datas.get(0);
                    isCollected=modelCheckShopCollect.getCollect();
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
                goToShopping();
                break;
            case ShoppingCartconstants.SHOPPING_CART_ADD:
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {}
    @Override
    public void onFinish(String method) {}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.tv_buy:
                clickBuyGoods(true);
                break;
        }
    }

    //收藏
    private void doCollect() {
        if ("1".equals(isCollected)){
            //已经收藏
            mSellerHelper.deleteGroupBuyCollect(GoodsId);
        }else if ("0".equals(isCollected)){
            mSellerHelper.postGroupBuyCollect(GoodsId);
        }else {
            mSellerHelper.checkGroupCollect(GoodsId);
        }

    }

    private void doShare() {
        if (mShare_info!=null){
            UmengShareManager.share(GoodsDetailActivity.this,mShare_info.getTitle(),mShare_info.getSummary(),mShare_info.getClickurl(),UmengShareManager.getUMImage(GoodsDetailActivity.this,mShare_info.getImageurl()),null);
        }
    }
      //立即购买
    private void clickBuyGoods(boolean isGoToShoppingCart) {
        if (TextUtils.isEmpty(GoodsId)) {
            return;
        }
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            //当前已经登录，
            addGoodsToShoppingPacket(isGoToShoppingCart);
        } else {
            //当前未登录.
            if ("0".equals(mTimeStatus)) {
                MGToast.showToast("商品活动未开始。");
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
        String add_goods_num = "1";
        if (mShoppingCartHelper != null) {
            if (isGoToShoppingCart){
                mShoppingCartHelper.addToShoppingCart("", "", lgn_user_id, GoodsId, cart_type, add_goods_num);
            }else {
                mShoppingCartHelper.addToShoppingCart2("", "", lgn_user_id, GoodsId, cart_type, add_goods_num);
            }
        }
    }

    /**
     * 加入本地购物车。
     */
    private void addToLocalShopping() {
        if (mShoppingCartInfo==null){
            MGToast.showToast("添加购物车失败!");
            return;
        }
        LocalShoppingcartDao.insertModel(mShoppingCartInfo);
    }

    public void goToShopping() {
        Intent intent = new Intent(this, ShopCartActivity.class);
        startActivity(intent);
    }
}
