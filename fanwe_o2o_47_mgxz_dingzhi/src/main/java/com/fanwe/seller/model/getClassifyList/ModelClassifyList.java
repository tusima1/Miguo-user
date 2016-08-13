package com.fanwe.seller.model.getClassifyList;

import android.text.TextUtils;

import com.fanwe.library.utils.SDCollectionUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelClassifyList {
    private String icon_img;

    private String iconfont;

    private String name;

    private String id;

    private List<ModelClassifyList> bcate_type;

    private String iconcolor;

    // /////////////////////////add
    private boolean isSelect;
    private boolean isHasChild;

    public String getIcon_img() {
        return icon_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelClassifyList> getBcate_type() {
        return bcate_type;
    }

    public void setBcate_type(List<ModelClassifyList> bcate_type) {
        this.bcate_type = bcate_type;
        if (bcate_type != null && bcate_type.size() > 1) {
            setHasChild(true);
        } else {
            setHasChild(false);
        }
    }

    public static int[] findIndex(String cate_id, String tid, List<ModelClassifyList> listModel) {
        int leftIndex = 0;
        int rightIndex = 0;
        if (!SDCollectionUtil.isEmpty(listModel)) {
            if (!TextUtils.isEmpty(cate_id) || !TextUtils.isEmpty(tid)) {
                ModelClassifyList model = null;
                for (int i = 0; i < listModel.size(); i++) {
                    model = listModel.get(i);
                    if (model.getId().equals(cate_id)) // 找到大分类
                    {
                        leftIndex = i;
                        List<ModelClassifyList> listChildModel = model.getBcate_type();
                        if (!SDCollectionUtil.isEmpty(listChildModel)) {
                            for (int j = 0; j < listChildModel.size(); j++) {
                                model = listChildModel.get(j);
                                if (model.getId().equals(tid)) // 找到小分类
                                {
                                    rightIndex = j;
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return new int[]{leftIndex, rightIndex};
    }

    public static int[] findIndex(String cate_id, List<ModelClassifyList> listModel) {
        int leftIndex = 0;
        int rightIndex = 0;
        if (!SDCollectionUtil.isEmpty(listModel)) {
            if (!TextUtils.isEmpty(cate_id)) {
                ModelClassifyList model = null;
                for (int i = 0; i < listModel.size(); i++) {
                    model = listModel.get(i);
                    if (model.getId().equals(cate_id)) {
                        leftIndex = i;
                        break;
                    } else {
                        List<ModelClassifyList> listChildModel = model.getBcate_type();
                        if (!SDCollectionUtil.isEmpty(listChildModel)) {
                            for (int j = 0; j < listChildModel.size(); j++) {
                                model = listChildModel.get(j);
                                if (model.getId().equals(cate_id)) {
                                    leftIndex = i;
                                    rightIndex = j;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return new int[]{leftIndex, rightIndex};
    }

}
