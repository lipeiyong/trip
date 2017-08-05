package net.lvtushiguang.trip.bean;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.fragment.ActivityFindPsdFragment;
import net.lvtushiguang.trip.fragment.ActivityFindPsdCodeFragment;
import net.lvtushiguang.trip.fragment.MessageDetailFragment;
import net.lvtushiguang.trip.fragment.MyInformationFragmentAvatarRedEnvelopes;
import net.lvtushiguang.trip.fragment.MyInformationFragmentCommunity;
import net.lvtushiguang.trip.fragment.MyInformationFragmentFavorite;
import net.lvtushiguang.trip.fragment.MyInformationFragmentFollower;
import net.lvtushiguang.trip.fragment.MyInformationFragmentFollowing;
import net.lvtushiguang.trip.fragment.MyInformationFragmentMaterial;
import net.lvtushiguang.trip.fragment.MyInformationFragmentWallet;
import net.lvtushiguang.trip.fragment.ActionBarPopupChatFragment;
import net.lvtushiguang.trip.fragment.ActionBarPopupFriendFragment;
import net.lvtushiguang.trip.fragment.ActionBarPopupQrCodeFragment;
import net.lvtushiguang.trip.fragment.ActionBarPopupScanFragment;
import net.lvtushiguang.trip.fragment.menu.MenuAboutFragment;
import net.lvtushiguang.trip.fragment.menu.MenuFeedbackFragment;
import net.lvtushiguang.trip.fragment.menu.MenuHelpFragment;
import net.lvtushiguang.trip.fragment.menu.MenuSettingsFragment;
import net.lvtushiguang.trip.fragment.menu.MenuTeamFragment;

public enum SimpleBackPage {

    SCAN(1, R.string.actionbar_popup_scan, ActionBarPopupScanFragment.class),

    Chat(2, R.string.actionbar_popup_chat, ActionBarPopupChatFragment.class),

    FRIEND(3, R.string.actionbar_popup_friend, ActionBarPopupFriendFragment.class),

    QRCODEQ(4, R.string.actionbar_popup_friend, ActionBarPopupQrCodeFragment.class),

    TEAM(5, R.string.menu_team, MenuTeamFragment.class),

    Feedback(6, R.string.menu_feedback, MenuFeedbackFragment.class),

    ABOUT(7, R.string.menu_about, MenuAboutFragment.class),

    HELP(8, R.string.menu_help,
            MenuHelpFragment.class),

    SETTING(9, R.string.menu_setting,
            MenuSettingsFragment.class),

    MY_INFORMATION_MATERIAL(10, R.string.user_info,
            MyInformationFragmentMaterial.class),

    MY_INFORMATION_FAVORITE(11, R.string.favorite,
            MyInformationFragmentFavorite.class),

    MY_INFORMATION_FOLLOWING(12, R.string.following,
            MyInformationFragmentFollowing.class),

    MY_INFORMATION_FOLLOWER(13, R.string.follower,
            MyInformationFragmentFollower.class),

    MY_INFORMATION_RED_ENVELOPES(14, R.string.red_envelopes,
            MyInformationFragmentAvatarRedEnvelopes.class),

    MY_INFORMATION_WALLET(15, R.string.wallet,
            MyInformationFragmentWallet.class),

    MY_INFORMATION_COMMUNITY(16, R.string.community,
            MyInformationFragmentCommunity.class),

    ACTIONBAR_POPUP_QRCODE(17, R.string.actionbar_popup_qrcode, ActionBarPopupQrCodeFragment.class),

    FIND_PASSWORD(18, R.string.find_password, ActivityFindPsdFragment.class),

    FIND_PASSWORD_CODE(19, R.string.input_verification_code, ActivityFindPsdCodeFragment.class),

    MESSAGE_DETAIL(20,R.string.main_tab_item_message_detail,MessageDetailFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
