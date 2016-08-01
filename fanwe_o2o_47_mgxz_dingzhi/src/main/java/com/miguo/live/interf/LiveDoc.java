package com.miguo.live.interf;

/**
 * Created by didik on 2016/8/1.
 * 直播说明文档
 */
public interface LiveDoc {

    /**
     * 直播类: LiveActivity (涵盖 直播,观众,被邀请的主播(以后简称主播2,在代码中一般命名为host2))
     */

    /**
     * 关于UI
     *
     * FrameLayout : controll_ui: 整个控制中心
     *
     * 1. HeadTopView : id(head_top)主播的顶部显示view
     * 2. MsgListview : id(im_msg_listview)聊天显示列表
     * 3. UserBottomToolView : id(normal_user_bottom_tool)普通用户(观众)的底部工具栏
     * 4. video_member_bottom_layout 被邀请直播(host2)的底部工具栏(在直播过程中3与4可以切换显示)
     * 5. HostBottomToolView : id(host_bottom_layout) 主播的底部工具栏(关于视频)
     * 6. HostMeiToolView : id(host_mei_layout) 直播的底部工具栏2(美颜功能)\
     * 7. HeartLayout : id(heart_layout) 直播的飘心动画view(只负责播放飘心动画)
     * 8. LinearLayout : id(ll_host_leave) 主播离开时的页面(Error页面)
     *
     */
}
