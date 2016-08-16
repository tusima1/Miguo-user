package com.fanwe.seller.model.getBusinessCircleList;

import android.text.TextUtils;

import com.fanwe.library.utils.SDCollectionUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelBusinessCircleList {
    private String name;
    private String id;
    private String pid;

    private List<ModelBusinessCircleList> quan_sub;

    // /////////////////////////add
    private boolean isSelect;
    private boolean isHasChild;

    public boolean isHasChild() {
        return isHasChild;
    }

    public void setHasChild(boolean isHasChild) {
        this.isHasChild = isHasChild;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelBusinessCircleList> getQuan_sub() {
        return quan_sub;
    }

    public void setQuan_sub(List<ModelBusinessCircleList> quan_sub) {
        this.quan_sub = quan_sub;
        if (quan_sub != null && quan_sub.size() > 1) {
            setHasChild(true);
        } else {
            setHasChild(false);
        }
    }

    public static int[] findIndex(String qid, List<ModelBusinessCircleList> listModel) {
        int leftIndex = 0;
        int rightIndex = 0;
        if (!TextUtils.isEmpty(qid) && !SDCollectionUtil.isEmpty(listModel)) {
            ModelBusinessCircleList model = null;
            for (int i = 0; i < listModel.size(); i++) {
                model = listModel.get(i);
                if (model.getId().equals(qid)) {
                    leftIndex = i;
                    break;
                } else {
                    List<ModelBusinessCircleList> listChildModel = model.getQuan_sub();
                    if (!SDCollectionUtil.isEmpty(listChildModel)) {
                        for (int j = 0; j < listChildModel.size(); j++) {
                            model = listChildModel.get(j);
                            if (model.getId().equals(qid)) {
                                leftIndex = i;
                                rightIndex = j;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return new int[]{leftIndex, rightIndex};
    }

}
