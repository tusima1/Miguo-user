package com.fanwe;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fanwe.adapter.barry.TimeLimitAdapter;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.impl.GetSpecialListDaoImpl;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getShopInfo.Share;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.MGDictUtil;
import com.fanwe.view.LoadMoreRecyclerView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Barry on 2016/10/11.
 */
public class TimeLimitActivity extends BaseActivity implements GetSpecialListView, PtrHandler, LoadMoreRecyclerView.OnRefreshEndListener{

    private Share share;

    String tag = "TimeLimitActivity";

    @ViewInject(R.id.ptr_layout)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;
    TimeLimitAdapter adapter;

    GetSpecialListDao getSpecialListDao;
    String page = "1";

    boolean canRefresh = true;
    boolean needLoadmore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.activity_time_limit);
        init();
    }

    private void setListener(){

    }

    private void init(){
        initTitle();
        setListener();
        initRecyclerView();
        initPtrLayout();
        initDao();
        onRefresh();
    }

    protected void initPtrLayout(){
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        MaterialHeader ptrHead = new MaterialHeader(this);
        ptrHead.setPadding(0, 24, 0, 24);
        ptrFrameLayout.setHeaderView(ptrHead);
        ptrFrameLayout.addPtrUIHandler(ptrHead);
        /**
         * 设置下拉刷新回调
         */
        ptrFrameLayout.setPtrHandler(this);
        recyclerView.setOnRefreshEndListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                setCanRefresh( newState == 0 ? true : false);
                Log.d(tag, "new state: " + newState);
                if(newState == 0){
                    if(isNeedLoadmore()){
                        setNeedLoadmore(false);
                        onLoadmore();
                    }
                }
            }
        });
    }


    private void onRefresh(){
        getSpecialListDao.getSpecialList(
                AppRuntimeWorker.getCity_id(),
                BaiduMapManager.getInstance().getBDLocation().getLongitude() + "",
                BaiduMapManager.getInstance().getBDLocation().getLatitude() + "",
                "1");
    }

    private void initDao(){
        getSpecialListDao = new GetSpecialListDaoImpl(this);
    }

    private void initTitle(){
        mTitle.setMiddleTextTop("即时特惠");
//        mTitle.initRightItem(1);
//        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_tuan_detail_share);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList list = new ArrayList();
        adapter = new TimeLimitAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        switch (index) {
            case 0:
                clickShare();
                break;
            default:
                break;
        }
    }

    /**
     * 分享
     */
    private void clickShare() {
        if (share != null) {
            String content = share.getSummary();
            if (TextUtils.isEmpty(content)) {
                content = "欢迎来到米果小站";
            }
            String imageUrl = share.getImageurl();
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
            String clickUrl = share.getClickurl();
            if (TextUtils.isEmpty(clickUrl)) {
                clickUrl = ServerUrl.SERVER_H5;
            } else {
                clickUrl = clickUrl + "/ref_id/" + App.getApplication().getmUserCurrentInfo().getUserInfoNew().getUser_id();
            }
            String title = share.getTitle();
            if (TextUtils.isEmpty(title)) {
                title = "米果小站";
            }

            UmengShareManager.share(this, title, content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
        } else {
            MGToast.showToast("无分享内容");
        }
    }

    @Override
    public void getSpecialListSuccess(final SpecialListModel.Result result) {
        if(result != null && result.getBody() != null && result.getBody().size() > 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(result.getBody());
                    setPage(result.getPage());
                    loadComplete();
                }
            });
        }
    }

    @Override
    public void getSpecialListLoadmoreSuccess(final SpecialListModel.Result result) {
        if(result != null && result.getBody() != null && result.getBody().size() > 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChangedLoadmore(result.getBody());
                    setPage(result.getPage());
                    loadComplete();
                }
            });
        }
    }

    @Override
    public void getSpecialListError(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadComplete();
            }
        });
    }

    public void loadComplete(){
        ptrFrameLayout.refreshComplete();
        recyclerView.loadComplete();
    }

    @Override
    public void onLoadmore() {
        if(isCanRefresh()){
            setNeedLoadmore(false);
            getSpecialListDao.getSpecialList(
                    AppRuntimeWorker.getCity_id(),
                    BaiduMapManager.getInstance().getBDLocation().getLongitude() + "",
                    BaiduMapManager.getInstance().getBDLocation().getLatitude() + "",
                    Integer.parseInt(getPage()) + 1 + "");
        }else {
            setNeedLoadmore(true);
        }

    }

    @Override
    public void onMoveTop() {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return recyclerView.isRefreshAble() && isCanRefresh();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public boolean isCanRefresh() {
        return canRefresh;
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    public boolean isNeedLoadmore() {
        return needLoadmore;
    }

    public void setNeedLoadmore(boolean needLoadmore) {
        this.needLoadmore = needLoadmore;
    }
}
