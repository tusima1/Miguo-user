package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fanwe.app.AppConfig;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getNameCardQR.ModelNameCardQR;
import com.fanwe.user.presents.UserHttpHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.Arrays;
import java.util.List;

public class DistributionMyQRCodeActivity extends BaseActivity implements OnLongClickListener {

    @ViewInject(R.id.iv_image_qr)
    private ImageView iv_image_qr;
    private String img_user_avater;
    private String img_bg;
    @ViewInject(R.id.share_btn)
    private Button share_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_qrcode);
        init();
    }

    private void init() {
        initTitle();
        getData();
        initClick();
    }


    private void initClick() {
        iv_image_qr.setOnLongClickListener(this);
        share_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog();
            }
        });
    }

    private void clickImage() {
        startActivity(new Intent(this, DistributionStoreWapActivity.class));
    }

    private void bindData(ModelNameCardQR nameCardQR){
        //用户头像
        img_user_avater=nameCardQR.getIcon();
        //img_bg 后台给的图片
        img_bg=nameCardQR.getMyshop();
        SDViewBinder.setImageView(img_bg,iv_image_qr);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("我的二维码");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageRight(R.drawable.bg_share_img);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        showDialog();
    }

    private void showDialog() {
        SDDialogMenu dialog = new SDDialogMenu(this);
        String[] arrItem = new String[]{"分享", "保存到相册"};
        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(Arrays.asList
                (arrItem), this);
        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenuListener() {
            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0:
                        showShare();
                        break;
                    case 1:
                        clickSave();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onDismiss(SDDialogMenu dialog) {

            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog) {

            }
        });
        dialog.showBottom();
    }

    protected void clickSave() {

    }

    protected void showShare() {
        String title = AppConfig.getUserName() + "的小店";
        String content = AppConfig.getUserName();
        String imageUrl = img_user_avater;
        String clickUrl = img_bg;
        UmengShareManager.share(this, title, content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == iv_image_qr) {
            clickImage();
            return true;
        }
        return false;
    }

    public void getData() {
       new  UserHttpHelper(null, new CallbackView2() {
           @Override
           public void onSuccess(String responseBody) {

           }

           @Override
           public void onSuccess(String method, List datas) {
                if (UserConstants.QR_SHOP_CARD.equals(method)){
                    ModelNameCardQR nameCardQR = (ModelNameCardQR) datas.get(0);
                    if (nameCardQR!=null){
                        bindData(nameCardQR);
                    }else {
                        MGToast.showToast("请求失败!");
                        finish();
                    }
                }
           }

           @Override
           public void onFailue(String responseBody) {

           }

           @Override
           public void onFinish(String method) {

           }
       }).getMyShopNameCard();
    }
}
