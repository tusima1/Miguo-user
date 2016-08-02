package com.miguo.live.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.RedTypeAdapter;
import com.miguo.live.model.RedPacketInfo;
import com.miguo.live.presenters.RedPacketHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包选择Diolog.
 */

public class SendRedPacketDialog extends Dialog {


    public SendRedPacketDialog(Context context) {
        super(context);
    }

    public SendRedPacketDialog(Context context, int theme) {
        super(context, theme);

    }

    public static class Builder {
        private Context context;
        private String message;
        private View contentView;
        private View.OnClickListener sendListener;
        private View.OnClickListener cancelListener;

        /**
         * 发送按钮。
         */
        private Button sendBtn;
        /**
         * gridView.
         */
        private RecyclerView gridView;
        /**
         * 手工输入红包数量
         */
        private EditText countEdit;
        /**
         * 全部红包。
         */
        private Button  allBtn;
        /**
         * 删除按钮。
         */
        private Button  cancelBtn;
        private   RedPacketHelper mRedHelper;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View v) {
            mRedHelper = new RedPacketHelper(context);
            this.contentView = v;
            return this;
        }



        public SendRedPacketDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SendRedPacketDialog dialog = new SendRedPacketDialog(context);
            View layout = inflater.inflate(R.layout.send_redpacket_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            sendBtn = (Button)layout.findViewById(R.id.send_btn);
            cancelBtn = (Button)layout.findViewById(R.id.cancel_btn);
            countEdit = (EditText)layout.findViewById(R.id.red_count);
            gridView = (RecyclerView)layout.findViewById(R.id.type_grid);
            //GridLayout 3列
            GridLayoutManager mgr=new GridLayoutManager(context,3);
            gridView.setLayoutManager(mgr);

          // List<RedPacketInfo> redPacketInfoList = mRedHelper.getTestData();
            List<RedPacketInfo>  redList= new ArrayList<RedPacketInfo>();

            for(int i = 1 ; i < 15; i ++) {
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.setCount(10+i);
                int type = i%9;
                redPacketInfo.setType(type);
                redList.add(redPacketInfo);
            }
            RedTypeAdapter redTypeAdapter = new RedTypeAdapter(redList,context);
            //设置适配器
           gridView.setAdapter(redTypeAdapter);

            allBtn = (Button)layout.findViewById(R.id.all_btn);
            sendBtn.setOnClickListener(sendListener);
            cancelBtn.setOnClickListener(cancelListener);


            dialog.setContentView(layout);
            return dialog;
        }

        public void setSendListener(View.OnClickListener sendListener) {
            this.sendListener = sendListener;
        }

        public void setCancelListener(View.OnClickListener cancelListener) {
            this.cancelListener = cancelListener;
        }
    }
}
