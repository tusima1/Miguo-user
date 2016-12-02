package com.fanwe.user;

public class UserConstants {

    /**
     * 注册 极光别名。
     */
    public static final String JPUSH_ALIAS = "JPushAlias";
    /**
     * 登录接口。
     */
    public static final String USER_lOGIN = "UserLogin";
    /**
     * 注册接口。
     */
    public static final String USER_REGISTER = "UserRegister";
    /**
     * 验证手机号是否存在.
     */
    public static final String USER_CHECK_EXIST = "UserCheckExist";
    /**
     * 快捷登录接口。
     */
    public static final String USER_QUICK_LOGIN = "UserQuickLogin";
    /**
     * 第三方登录。
     */
    public static final String TRHID_LOGIN_URL = "ThirdpartyLoginA";
    /**
     * 第三方注册 。
     */

    public static final String THIRD_REGISTER_URL = "ThirdpartyLoginB";
    /**
     * 取用户信息。
     */

    public static final String USER_INFO_URL = "UserInfo";
    /**
     * 第三方OPENID.
     */
    public static final String THIRD_OPENID = "openid";
    /**
     * 第三方platform.
     */
    public static final String THIRD_PLATFORM = "platform";
    /**
     * 第三方头像地址.
     */
    public static final String THIRD_ICON = "icon";
    /**
     * 第三方昵称.
     */
    public static final String THIRD_NICK = "nick";
    /**
     * 修改用户信息
     */
    public static final String USER_INFO_METHOD = "UserInfo";


    public static final String BD_EXIT_APP = "bd_sxb_exit";

    public static final String USER_INFO = "user_info";

    public static final String USER_ID = "user_id";

    public static final String USER_SIG = "user_sig";

    public static final String USER_NICK = "user_nick";

    public static final String USER_SIGN = "user_sign";

    public static final String USER_AVATAR = "user_avatar";


    /**
     * 用户 密码。
     */
    public static final String USER_PASSWORD = "pwd";

    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "3061230415";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关说于授权回调页对移动客户端应用来对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "sns.whalecloud.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * qq APPID.
     */
    public static final String QQAPPID = "1101169715";
    /**
     * qq getuserinfo url
     */
    public static final String QQ_GET_USER_INFO = "https://graph.qq.com/user/get_user_info";


    //返回码
    /**
     * 注册成功。
     */
    public static final String REGISTER_URL = "211";
    /**
     * 注册 失败。
     */
    public static final String ALL_REGISTERED = "311";
    public static final String SUCCESS = "200";
    public static final String CODE_ERROR = "300";

    /**
     * 我的,界面数据展示
     */
    public static final String PERSONALHOME = "PersonalHome";
    /**
     * 修改手机号
     */
    public static final String USER_CHANGE_MOBILE = "UserChangeMobile";
    /**
     * 红包列表
     */
    public static final String USER_RED_PACKET_LIST = "GetUserRedPackets";

    /**
     * 订单列表
     */
    public static final String ORDER_INFO = "OrderInfo";
    public static final String ORDER_INFO_ALL = "OrderInfo_all";
    public static final String ORDER_INFO_PAY_WAIT = "OrderInfo_pay_wait";
    public static final String ORDER_INFO_USE_WAIT = "OrderInfo_use_wait";
    public static final String ORDER_INFO_COMMENT_WAIT = "OrderInfo_comment_wait";
    public static final String ORDER_INFO_REFUND = "OrderInfo_refund";
    /**
     * 取消订单
     */
    public static final String ORDER_INFO_CANCEL_ORDER = "OrderOperator";
    /**
     * 不要红包
     */
    public static final String ORDER_OPERATOR = "OrderOperator";
    public static final String ORDER_OPERATOR_GET = "OrderOperatorGet";
    /**
     * 退款页面数据
     */
    public static final String REFUND_APPLICATION_PAGE = "OrderItemTuangou";
    /**
     * 退款申请
     */
    public static final String REFUND_APPLICATION = "RefundApply";
    /**
     * 团购券
     */
    public static final String GROUP_BUY_COUPON_LIST = "GroupBuyCouponList";
    /**
     * 我的战队
     */
    public static final String MY_DISTRIBUTION_CROPS = "MyDistributionCorps";
    /**
     * 我的分销小店基本信息
     */
    public static final String DISTR_INFO = "DistrInfo";

    /**
     * 二维码
     */
    public static final String QR_SHOP_CARD = "MyShopNameCard";
    /**
     * 用户升级
     */
    public static final String USER_UPGRADE_ORDER = "UserUpgradeOrder";
    public static final String USER_UPGRADE_ORDER_GET = "UserUpgradeOrderGet";
    public static final String USER_UPGRADE_ORDER_POST = "UserUpgradeOrderPost";
    /**
     * 第三方登录成功。
     */
    public static final String THIRD_LOGIN_SUCCESS = "third_login_success";
    /**
     * 第三方登录未注册 手机号，
     */
    public static final String THIRD_LOGIN_UNREGISTER = "third_login_unregister";
    /**
     * 修改密码
     */
    public static final String USER_CHANGE_PWD = "UserChangePwd";
    /**
     * 忘记密码
     */
    public static final String USER_FORGOT = "UserForgot";
    /**
     * 提交建议
     */
    public static final String ADVICE = "Advice";
    /**
     * 粉丝
     */
    public static final String ATTENTION_Fans = "AttentionFans";
    /**
     * 粉丝关注
     */
    public static final String ATTENTION = "Attention";
    /**
     * 钱包
     */
    public static final String MY_WALLET = "MyWallet";
    /**
     * 2.0.1新钱包接口。
     */
    public static final String WALLET = "Wallet";
    /**
     *2.0.1钱包收益接口。
     */

    public static final String  WALLET_INCOME= "WalletIncome";
    public static final String  WALLET_INCOME_GET= "WalletIncomeGet";
    public static final String  WALLET_INCOME_POST= "WalletIncomePost";
    public static final String  WALLET_INCOME_PUT= "WalletIncomePut";
    /**
     * 2.0.1版我的钱包，收益兑换记录 doGet
     */
    public static final String   WALLET_INCOME_CONVERTHISTORY= "WalletIncomeConvertHistory";

    /**
     * 钱包金额。
     */
    public static final String WALLET_BALANCE = "WalletBalance";


    public static final String GET_WALLET_BALANCE = "GetWalletBalance";
    public static final String POST_WALLET_BALANCE = "PostWalletBalance";
    /**
     * 获取用户关注的人的一览接口
     */
    public static final String ATTENTION_FOCUS = "AttentionFocus";
    /**
     * 我的收藏
     */
    public static final String SHOP_AND_USER_COLLECT = "ShopAndUserCollect";
    /**
     * 获取个人主页信息（头像，昵称，签名，关注，赞，粉丝）
     */
    public static final String PERSON_HOME_PAGE = "PersonHomePage";
    /**
     * 获得TA的最爱
     */
    public static final String GET_PRODUCT_LIST = "GetProductList";
    /**
     * 获得网红的直播场次
     */
    public static final String GET_SPOKE_PLAY = "GetSpokePlayNew";
    /**
     * 获得关注状态
     */
    public static final String USER_ATTENTION = "UserAttention";
    /**
     * 用户分销等级。
     */

    public static final String  USER_DISTRIBUTION_LEVEL  ="UserDistributionLevel";
    /**
     * 人气值 。
     */
    public static final String   POPULARITY= "popularity";
    /**
     * 商家分类 大类
     */
    public static final String   CATE_ID= "cate_id";
    /**
     *
     * 商家分类 小类
     */

    public static final String   TID= "tid";

    /**
     米果仁
     */
    public static final String  BEAN= "bean";
    /**
     米果豆转换人名币
     */
    public static final String  BEAN_CONVERT_RMB= "bean_convert_rmb";
    /**
     * 用户分销等级。
     */

    public static final String  USER_DISTRIBUTION_LEVEL  ="UserDistributionLevel";
    /**
     * 兑换结果：0失败，1成功.
     */
    public static final String  CONVERT_STATUS= "convert_status";
}
