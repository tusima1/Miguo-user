package com.miguo.app;

import android.content.Intent;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiMessageCategory;
import com.miguo.definition.RequestCode;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/6.
 */
public class HiMessageActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiMessageCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_himessage);
    }

    @Override
    protected void doOnResume() {
        getCategory().initMessageCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RequestCode.AMOUNT_MESSAGE_LOGIN:
                    handleLoginSuccessForAmountMessage();
                    break;
            }
        }
    }

    private void handleLoginSuccessForAmountMessage(){
        try{
            getCategory().getListener().clickAmountMessage();
        }catch (NullPointerException e){

        }
    }

    @Override
    public HiMessageCategory getCategory() {
        return (HiMessageCategory)super.getCategory();
    }
}
