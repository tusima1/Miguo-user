package com.miguo.dao;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/13.
 */

public interface CreateShareRecordDao extends BaseDao {

    /**
     *
     * @param content_type      1：商品详情，
     *                           2：门店详情，
     *                           3：专题详情，
     *                           4：直播详情,
     *                           6:大转盘活动分享，
     *                           10：网站首页，
     *                           11：小店首页（名片），
     *                           12：网红主页，
     *                           50：URL，
     *                           51：商品列表，
     *                           52：门店列表，
     *                           53：专题列表，
     *                           54：直播列表，
     *                           61：小店商品列表，
     *                           62：小店门店列表，
     *                           63小段子分享
     * @param share_target      分享（1：微信好友 2：微信朋友圈 3：QQ好友 4：QQ朋友圈）
     * @param lgn_user_id       用户id
     * @param generate_episode  是否生成小段子1生成 0不生成（默认都传1：生成）
     * @param content_id        根据content_type的值而变，如果是到店买单，则传门店id， 如果是在线买单，如果是多件不同商品，则传空，如果是同一间商品，则传商品id
     */
    void createShareRecord(String content_type, String share_target, String lgn_user_id, String generate_episode, String content_id);

    /**
     * 到店买单获取分享码
     * @param lgn_user_id   用户id
     * @param content_id    门店id
     */
    void createShareRecordFromOfflinePay(String lgn_user_id, String content_id);

    /**
     * 单间商品分享商品购买获取商品码
     * @param lgn_user_id   用户id
     * @param content_id    商品id
     */
    void createShareRecordFromSingleSalePay(String lgn_user_id, String content_id);

    /**
     * 多件商品分享首页获取分享吗
     * @param lgn_user_id
     */
    void createShareRecordFromMultiSalePay(String lgn_user_id);

}
