package com.miguo.live.views.gift;

import com.fanwe.constant.GiftId;
import com.fanwe.o2o.miguo.R;

/**
 * Created by zlh on 2016/9/21.
 */
public class GiftPictures {
    /**
     * 挚爱
     */
    public static final int[] GIFT_BEST_LOVE = {
            R.drawable.best_love_001,
            R.drawable.best_love_002,
            R.drawable.best_love_003,
            R.drawable.best_love_004,
            R.drawable.best_love_005,
            R.drawable.best_love_006,
            R.drawable.best_love_007,
            R.drawable.best_love_008,
            R.drawable.best_love_009,
            R.drawable.best_love_010,
            R.drawable.best_love_011,
            R.drawable.best_love_012,
            R.drawable.best_love_013,
            R.drawable.best_love_014,
            R.drawable.best_love_015,
            R.drawable.best_love_016,
            R.drawable.best_love_017,
            R.drawable.best_love_018,
            R.drawable.best_love_019,
            R.drawable.best_love_020,
            R.drawable.best_love_021
    };
    /**
     * 烟花
     */
    public static final int[] GIFT_FIREWORKS = {
            R.drawable.fireworks_001,
            R.drawable.fireworks_002,
            R.drawable.fireworks_003,
            R.drawable.fireworks_004,
            R.drawable.fireworks_005,
            R.drawable.fireworks_006,
            R.drawable.fireworks_007,
            R.drawable.fireworks_008,
            R.drawable.fireworks_009,
            R.drawable.fireworks_010,
            R.drawable.fireworks_011,
            R.drawable.fireworks_012,
            R.drawable.fireworks_013,
            R.drawable.fireworks_014,
            R.drawable.fireworks_015,
            R.drawable.fireworks_016,
            R.drawable.fireworks_017,
            R.drawable.fireworks_018
    };
    /**
     * 花篮
     */
    public static final int[] GIFT_BASKETS = {
            R.drawable.baskets_001,
            R.drawable.baskets_002,
            R.drawable.baskets_003,
            R.drawable.baskets_004,
            R.drawable.baskets_005,
            R.drawable.baskets_006,
            R.drawable.baskets_007,
            R.drawable.baskets_008,
            R.drawable.baskets_009,
            R.drawable.baskets_010,
            R.drawable.baskets_011,
            R.drawable.baskets_012,
            R.drawable.baskets_013,
            R.drawable.baskets_014,
            R.drawable.baskets_015,
            R.drawable.baskets_016
    };
    /**
     * 庞巴迪
     */
    public static final int[] GIFT_BOMBARDIER = {
            R.drawable.bombardier_001,
            R.drawable.bombardier_002,
            R.drawable.bombardier_003,
            R.drawable.bombardier_004,
            R.drawable.bombardier_005,
            R.drawable.bombardier_006,
            R.drawable.bombardier_007,
            R.drawable.bombardier_008,
            R.drawable.bombardier_009,
            R.drawable.bombardier_010,
            R.drawable.bombardier_011,
            R.drawable.bombardier_012,
            R.drawable.bombardier_013,
            R.drawable.bombardier_014,
            R.drawable.bombardier_015,
            R.drawable.bombardier_016,
            R.drawable.bombardier_017,
            R.drawable.bombardier_018,
            R.drawable.bombardier_019,
            R.drawable.bombardier_020,
            R.drawable.bombardier_021,
            R.drawable.bombardier_022
    };
    /**
     * 法拉利
     */
    public static final int[] GIFT_FERRARI = {
            R.drawable.ferrari_001,
            R.drawable.ferrari_002,
            R.drawable.ferrari_003,
            R.drawable.ferrari_004,
            R.drawable.ferrari_005,
            R.drawable.ferrari_006,
            R.drawable.ferrari_007,
            R.drawable.ferrari_008,
            R.drawable.ferrari_009,
            R.drawable.ferrari_010,
            R.drawable.ferrari_011,
            R.drawable.ferrari_012,
            R.drawable.ferrari_013,
            R.drawable.ferrari_014
    };
    /**
     * 一起冲天
     */
    public static final int[] GIFT_SOAR = {
            R.drawable.soar_001,
            R.drawable.soar_002,
            R.drawable.soar_003,
            R.drawable.soar_004,
            R.drawable.soar_005,
            R.drawable.soar_006,
            R.drawable.soar_007,
            R.drawable.soar_008,
            R.drawable.soar_009,
            R.drawable.soar_010,
            R.drawable.soar_011,
            R.drawable.soar_012,
            R.drawable.soar_013,
            R.drawable.soar_014,
            R.drawable.soar_015,
            R.drawable.soar_016,
            R.drawable.soar_017,
            R.drawable.soar_018
    };

    public static int[] getGiftPictures(String giftId){
        switch (giftId){
            case GiftId.BEST_LOVE:
                return GIFT_BEST_LOVE;
            case GiftId.FIREWORKS:
                return GIFT_FIREWORKS;
            case GiftId.BASKETS:
                return GIFT_BASKETS;
            case GiftId.BOMBARDIER:
                return GIFT_BOMBARDIER;
            case GiftId.FERRARI:
                return GIFT_FERRARI;
            case GiftId.SOAR:
                return GIFT_SOAR;
        }
        return GIFT_BEST_LOVE;
    }

    public static int getCount(String giftId){
        return getGiftPictures(giftId).length;
    }

    public static final String getImageUri(String giftId, int index){
        return "drawable://" + getGiftPictures(giftId)[index];
    }

    public static int getItem(String giftId, int index){
        return getGiftPictures(giftId)[index];
    }

}
