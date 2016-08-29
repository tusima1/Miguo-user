package com.miguo.live.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.RedNumGridAdapter;
import com.miguo.live.adapters.RedTypeAdapter;
import com.miguo.live.interf.MyItemClickListenerRedNum;
import com.miguo.live.interf.MyItemClickListenerRedType;
import com.miguo.live.model.getHandOutRedPacket.ModelHandOutRedPacket;
import com.miguo.live.model.getHandOutRedPacket.ModelRedNum;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.RedPacketHelper;
import com.miguo.utils.DisplayUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包选择Diolog.
 */

public class SendRedPacketDialog extends Dialog {
    private static RedTypeAdapter redTypeAdapter;
    private static List<ModelHandOutRedPacket> redList;

    private static RedNumGridAdapter redNumGridAdapter;
    private static List<ModelRedNum> redNumDatas;

    public SendRedPacketDialog(Context context) {
        super(context);
    }

    public SendRedPacketDialog(Context context, int theme) {
        super(context, theme);

    }

    public void updateDatasType(ArrayList<ModelHandOutRedPacket> datas) {
        redList.clear();
        if (!SDCollectionUtil.isEmpty(datas)) {
            for (ModelHandOutRedPacket temp : datas) {
                if (temp.getRed_packet_type().equals("1") || temp.getRed_packet_type().equals("2")) {
                    redList.add(temp);
                }
            }
        }
        redTypeAdapter.notifyDataSetChanged();
        checkBtnSure();
    }

    public void updateDatasNum(ArrayList<ModelRedNum> datas) {
        redNumDatas.clear();
        if (!SDCollectionUtil.isEmpty(datas)) {
            redNumDatas.addAll(datas);
        }
        redNumGridAdapter.notifyDataSetChanged();
        checkBtnSure();
    }

    /**
     * 设置按钮状态
     */
    private void checkBtnSure() {
        boolean flag = false;
        for (ModelHandOutRedPacket bean : redList) {
            if (bean.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            flag = false;
            for (ModelRedNum bean : redNumDatas) {
                if (bean.isChecked()) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            btnSure.setClickable(true);
            btnSure.setBackgroundResource(R.drawable.bg_orange);
        } else {
            btnSure.setClickable(false);
            btnSure.setBackgroundResource(R.drawable.bg_grey_big);
        }
    }

    private static Button btnSure;

    public static class Builder {
        private Context context;
        private String message;
        private View contentView;
        private View.OnClickListener sendListener;
        private View.OnClickListener cancelListener;
        private MyItemClickListenerRedType myItemClickListenerType;
        private MyItemClickListenerRedNum myItemClickListenerNum;
        private Handler mHandler;

        /**
         * 发送按钮。
         */
        private Button sendBtn;
        /**
         * gridView.
         */
        private RecyclerView mTypeGridView;
        private RecyclerView mNumGridView;
        /**
         * 手工输入红包数量
         */
        private EditText countEdit;
        /**
         * 全部红包。
         */
        private Button allBtn;
        /**
         * 删除按钮。
         */
        private Button cancelBtn;
        /**
         * 商铺名称
         */
        private TextView tvShopName;
        private RedPacketHelper mRedHelper;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View v) {
            mRedHelper = new RedPacketHelper(context);
            this.contentView = v;
            return this;
        }

        public SendRedPacketDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SendRedPacketDialog dialog = new SendRedPacketDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.dialog_live_red_packet, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            btnSure = (Button) layout.findViewById(R.id.btn_submit_dialog_red);
            tvShopName = (TextView) layout.findViewById(R.id.shop_name_dialog_live_red_packet);
            cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
            mTypeGridView = (RecyclerView) layout.findViewById(R.id.type_grid);
            mNumGridView = (RecyclerView) layout.findViewById(R.id.num_grid);

            //GridLayout 3列
            GridLayoutManager mgr = new GridLayoutManager(context, 2);
            mTypeGridView.setLayoutManager(mgr);
            final int dp12 = DisplayUtil.dp2px(context, 12);
            final int dp5 = DisplayUtil.dp2px(context, 5);
            final int dp7 = DisplayUtil.dp2px(context, 7);
            mTypeGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                           RecyclerView.State state) {
                    int childLayoutPosition = parent.getChildLayoutPosition(view);
                    if (childLayoutPosition % 2 == 0) {
                        outRect.right = dp5;
                    } else {
                        outRect.left = dp5;
                    }
                    outRect.top = dp12;
                }
            });



            tvShopName.setText(CurLiveInfo.modelShop.getShop_name());
            redList = new ArrayList<ModelHandOutRedPacket>();


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandler != null) {
                        Message message = new Message();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                }
            };




            redTypeAdapter = new RedTypeAdapter(redList, context);

            if (myItemClickListenerType != null) {
                redTypeAdapter.setOnItemClickListener(myItemClickListenerType);
            }


            //设置适配器
            mTypeGridView.setAdapter(redTypeAdapter);

            //---设置数量gridview--
            mNumGridView.setLayoutManager(new GridLayoutManager(context, 3));
            mNumGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                           RecyclerView.State state) {
                    int childLayoutPosition = parent.getChildLayoutPosition(view);
                    if (childLayoutPosition % 3 == 0) {
                        outRect.right = dp7;
                    } else if (childLayoutPosition % 3 == 1) {
                        outRect.left = dp7;
                        outRect.right = dp7;
                    } else {
                        //%3==2
                        outRect.left = dp7;
                    }
                    outRect.top = dp12;
                }
            });
            redNumDatas = new ArrayList<ModelRedNum>();

            redNumGridAdapter = new RedNumGridAdapter(context, redNumDatas);
            if (myItemClickListenerNum != null) {
                redNumGridAdapter.setOnItemClickListener(myItemClickListenerNum);
            }
            mNumGridView.setAdapter(redNumGridAdapter);
            cancelBtn.setOnClickListener(cancelListener);
            btnSure.setOnClickListener(sendListener);

            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return dialog;
        }

        /**
         * 设置发送红包监听
         * @param sendListener
         */
        public void setSendListener(View.OnClickListener sendListener) {
            this.sendListener = sendListener;
        }

        public void setCancelListener(View.OnClickListener cancelListener) {
            this.cancelListener = cancelListener;
        }

        public void setHandler(Handler mHandler) {
            this.mHandler = mHandler;
        }

        public void setItemClickType(MyItemClickListenerRedType myItemClickListenerType) {
            this.myItemClickListenerType = myItemClickListenerType;
        }

        public void setItemClickNum(MyItemClickListenerRedNum myItemClickListenerNum) {
            this.myItemClickListenerNum = myItemClickListenerNum;
        }

    }
}
