package com.miguo.category.fragment;

import android.view.View;

import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.miguo.dao.InterestingLiveVideoDao;
import com.miguo.dao.impl.InterestingLiveVideoDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.entity.InterestingLiveVideoBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiSingleLiveListFragment;
import com.miguo.view.InterestingLiveVideoView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class HiSingleLiveListFragmentCategory extends FragmentCategory implements InterestingLiveVideoView{

    InterestingLiveVideoDao interestingLiveVideoDao;

    public HiSingleLiveListFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        interestingLiveVideoDao = new InterestingLiveVideoDaoImpl(this);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {

    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {
        onReresh();
    }

    public void onReresh(){
        interestingLiveVideoDao.getInterestingLiveVideo(0, PageSize.BASE_PAGE_SIZE, getFragment().getTab_id(), AppRuntimeWorker.getCity_id());
    }

    public HiSingleLiveListFragment getFragment(){
        return (HiSingleLiveListFragment)fragment;
    }

    @Override
    public void getInterestingLiveVideoError() {

    }

    @Override
    public void getInterestingLiveVideoSuccess(List<InterestingLiveVideoBean.Result.Body.Live> liveList) {

    }

    @Override
    public void getInterestingLiveVideoLoadmoreSuccess(List<InterestingLiveVideoBean.Result.Body.Live> liveList) {

    }
}
