package com.fanwe.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.MyXiaomiDetailActivity;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Member;
import com.fanwe.model.RemineModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.RemineContance;
import com.fanwe.utils.RemineHelper;
import com.fanwe.utils.SDDateUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyXiaoMiAdapter extends SDBaseAdapter<Member> {
    private int mType;
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private List<Member> listModels;

    public MyXiaoMiAdapter(List<Member> listModels, Activity activity, int type) {
        super(listModels, activity);
        this.listModels = listModels;
        this.mType = type;
        simpleDateFormat = new SimpleDateFormat("dd");
        date = new Date();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final Member model) {
        final Member bean = listModels.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dist_lv, null);
        }
        ImageView iv_user = ViewHolder.get(convertView, R.id.iv_item_user_avatar);
        LinearLayout ll_number = ViewHolder.get(convertView, R.id.ll_number);
        TextView tv_username = ViewHolder.get(convertView, R.id.tv_item_username);
        TextView tv_date = ViewHolder.get(convertView, R.id.tv_item_date);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_item_phone);
        TextView tv_momey = ViewHolder.get(convertView, R.id.tv_item_momey);
        TextView tv_number = ViewHolder.get(convertView, R.id.tv_item_mumber);
        if (bean != null) {
            SDViewBinder.setImageView(iv_user, bean.getAvatar());
            SDViewBinder.setTextView(tv_username, bean.getUser_name());
            SDViewBinder.setTextView(tv_date, SDDateUtil.milToStringlongPoint(DataFormat.toLong(bean.getCreate_time())));
            if (!TextUtils.isEmpty(bean.getMobile())) {
//                if (bean.getMobile().length() > 7) {
//                    String zh = bean.getMobile().replace(bean.getMobile().substring(3, 7), "****");
//                    SDViewBinder.setTextView(tv_phone,zh);
//                }
                SDViewBinder.setTextView(tv_phone, bean.getMobile());
            } else {
                SDViewBinder.setTextView(tv_phone, "");
            }
            if (DataFormat.toInt(bean.getSalary()) == 0) {
                SDViewBinder.setTextView(tv_momey, "+0.00");
            } else {
                SDViewBinder.setTextView(tv_momey, "+" + TextMoney.textFarmat(bean.getSalary()), "+0.00");
            }
            SDViewBinder.setTextView(tv_number, bean.getUser_num() + "个成员");
            if (bean.getUser_num() == 0 || mType == 2) {
                ll_number.setBackgroundResource(R.drawable.my_xiaomi_second);
            }
            iv_user.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getId());
                    mActivity.startActivity(intent);
                }
            });

            tv_username.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
                    intent.putExtra("id", bean.getId());
                    mActivity.startActivity(intent);
                }
            });

            if (bean.getUser_num() > 0 && mType == 1) {
                ll_number.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mActivity, MyXiaomiDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getId());
                        bundle.putString("user", bean.getUser_name());
                        bundle.putInt("number", bean.getUser_num());
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }
                });
            }
            if (bean.getRank() == 1 && !"".equals(bean)) {
                tv_phone.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        if (insertHistory(String.valueOf(bean.getId()), simpleDateFormat.format(date))) {
//                            clickAlert(bean.getId());
//                        } else {
//                            MGToast.showToast("您已经提醒过该成员，明天再试试吧!");
//                        }
                    }
                });
            }
        }
        return convertView;
    }

    protected void clickAlert(final String id) {
        RequestModel model = new RequestModel();
        model.putCtl("uc_fxinvite");
        model.putAct("remind");
        model.put("id", id);
        SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {
            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    showDialog();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    protected void showDialog() {
        View view = SDViewUtil.inflate(R.layout.item_dialog_sucess, null);
        final AlertDialog builder = new AlertDialog.Builder(mActivity).create();
        builder.setCanceledOnTouchOutside(false);
        builder.setView(view);
        builder.show();
        Window window = builder.getWindow();
        Button bt_confirm = (Button) window.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });
    }

    @Override
    public void updateData(List<Member> listModel) {
        super.updateData(listModel);
    }

    private boolean insertHistory(String search, String time) {
        RemineHelper helper = new RemineHelper(mActivity.getApplicationContext());
        // 插入数据库
        SQLiteDatabase db = helper.getWritableDatabase();

        int count = 0;
        // 查询数据库，判断edittext的内容是否已经存在，如果存在了，就不写了，如果不存在，就插入数据库
        // 取回查询存放history表的h_name列的list集合
        List<RemineModel> list = queryHistorySql();
        for (int i = 0; i < list.size(); i++) {
            // 获取搜索框的输入内容，和数据已经存在的记录比对，如果有一样的，就count增加；
            if (list.get(i).getRemine_id().equals(search)
                    && (DataFormat.toInt(time) - DataFormat.toInt(list.get(i).getC_time())) < 1) {
                count++;
            }
        }
        // 如果count == 0，说明没有重复的数据，就可以插入数据库history表中
        if (count == 0) {
            db.execSQL("insert into " + RemineContance.HISTORY_TABLENAME
                    + " values(?,?,?)", new Object[]{null, search, time});
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    private List<RemineModel> queryHistorySql() {
        RemineHelper helper = new RemineHelper(mActivity.getApplicationContext());
        List<RemineModel> list = new ArrayList<RemineModel>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + RemineContance.HISTORY_TABLENAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RemineModel remineModel = new RemineModel();
            //查询数据库，取出h_name这一列，然后全部放到list集合中，在前面调用此方法的时候，用来判断
            remineModel.setRemine_id(cursor.getString(cursor.getColumnIndex("remine_id")));
            remineModel.setC_time(cursor.getString(cursor.getColumnIndex("c_time")));
            list.add(remineModel);
            cursor.moveToNext();
        }
        db.close();
        // 返回一个list集合
        return list;
    }
}
