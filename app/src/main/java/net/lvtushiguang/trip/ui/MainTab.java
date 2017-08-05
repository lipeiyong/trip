package net.lvtushiguang.trip.ui;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.fragment.maintab.MainTabDemoFragment;
import net.lvtushiguang.trip.fragment.maintab.MainTabMeFragment;
import net.lvtushiguang.trip.fragment.maintab.MainTabVideoFragment;
import net.lvtushiguang.trip.fragment.maintab.MainTabHomeFragment;
import net.lvtushiguang.trip.fragment.maintab.MainTabTopFragment;

public enum MainTab {

    HOME(0, R.string.main_tab_name_home, R.drawable.tab_icon_home,
            MainTabHomeFragment.class),

    VIDEO(1, R.string.main_tab_name_video, R.drawable.tab_icon_video,
            MainTabVideoFragment.class),

    TOP(2, R.string.main_tab_name_top, R.drawable.tab_icon_top,
            MainTabTopFragment.class),

    ME(3, R.string.main_tab_name_nologin, R.drawable.tab_icon_nologin,
            MainTabMeFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
