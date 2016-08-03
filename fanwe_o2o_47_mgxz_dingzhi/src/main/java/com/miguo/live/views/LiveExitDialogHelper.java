package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.customviews.MaxHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/7/26.
 * 直播退出界面
 */
public class LiveExitDialogHelper implements IHelper, View.OnClickListener {

    private  Activity mActivity;
    private CircleImageView civ_user_image;
    private TextView tv_username;
    private TextView tv_user_location;
    private TextView tv_count;
    private MaxHeightGridView gridview;
    private Dialog dialog;

    public LiveExitDialogHelper(Activity activity){
        this.mActivity=activity;
        createDialog();
    }
    private void createDialog(){
        dialog = new Dialog(mActivity, R.style.floag_dialog);
        //init view
        dialog.setContentView(R.layout.pop_live_exit);
        dialog.setCancelable(false);
        View iv_close = dialog.findViewById(R.id.iv_close);//右上角关闭
        //主播头像
        civ_user_image = (CircleImageView) dialog.findViewById(R.id.civ_user_image);
        //主播名称
        tv_username = (TextView) dialog.findViewById(R.id.tv_username);
        //位置
        tv_user_location = (TextView) dialog.findViewById(R.id.tv_user_location);
        //观看人数
        tv_count = (TextView) dialog.findViewById(R.id.tv_count);
        View tv_youhuiquan = dialog.findViewById(R.id.tv_youhuiquan);//优惠
        TextView tv_follow = (TextView) dialog.findViewById(R.id.tv_follow);//关注
        //关注
        gridview = (MaxHeightGridView) dialog.findViewById(R.id.mhgridview_show);


        //init click
        iv_close.setOnClickListener(this);
        tv_youhuiquan.setOnClickListener(this);
        tv_follow.setOnClickListener(this);

        //init gridview
        int[] imageIds=new int[]{
                R.drawable.app_icon,
                R.drawable.app_icon,
                R.drawable.app_icon,
                R.drawable.app_icon,
        };
        //创建一个List对象，List对象的元素是Map
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        for(int i=0;i<imageIds.length;i++)
        {
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("image", imageIds[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(mActivity,
                listItems,
                R.layout.item_pop_live_exit,
                new String[]{"image"},
                new int[]{R.id.iv_ren});
        gridview.setAdapter(simpleAdapter);
    }

    public void bindData(){
//        civ_user_image
//        tv_username
//        tv_user_location
//        tv_count
    }

    /**
     * 是否在显示
     * @return
     */
    public boolean isShowing(){
        return dialog.isShowing();
    };

    public void show(){
        if (dialog.isShowing()){
            //
        }else {
            dialog.show();
        }
    }
    public void dismiss(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }else {
            return;
        }
    }

    @Override
    public void onDestroy() {
        //防止内存泄漏,在这里释放资源
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.iv_close){
            dismiss();
            mActivity.finish();
        }else if (id==R.id.tv_youhuiquan){
            clickYouHui();
        }else if (id==R.id.tv_follow){
            clickFollow();
        }
    }

    /**
     * 关注
     */
    private void clickFollow() {
        MGToast.showToast("关注");
    }

    /**
     * 优惠
     */
    private void clickYouHui() {
        MGToast.showToast("优惠");
    }
}
