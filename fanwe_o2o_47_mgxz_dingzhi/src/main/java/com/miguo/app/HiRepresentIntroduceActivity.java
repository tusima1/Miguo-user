package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiRepresentIntroduceCategory;

/**
 * Created by zlh on 2016/12/13.
 */

public class HiRepresentIntroduceActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiRepresentIntroduceCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hi_represent_introduce);
    }
}
