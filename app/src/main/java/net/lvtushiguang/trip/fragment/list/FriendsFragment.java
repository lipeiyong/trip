package net.lvtushiguang.trip.fragment.list;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.adapter.FriendAdapter;
import net.lvtushiguang.trip.base.BaseListFragment;
import net.lvtushiguang.trip.bean.Entity;
import net.lvtushiguang.trip.bean.Friend;
import net.lvtushiguang.trip.bean.FriendsList;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 关注、粉丝
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:15:37
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FriendsFragment extends BaseListFragment<Friend> {

    public final static String BUNDLE_KEY_UID = "UID";

    protected static final String TAG = FriendsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "friend_list";

    private String mUid;

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mUid = args.getString(BUNDLE_KEY_UID);
        }
    }

    @Override
    public void onResume() {
        if (mCatalog == FriendsList.TYPE_FANS
                && mUid == AppContext.getInstance().getLoginUid()) {
            refreshNotice();
        }
        super.onResume();
    }

    private void refreshNotice() {
//        Notice notice = MainActivity.mNotice;
//        if (notice != null && notice.getNewFansCount() > 0) {
//            onRefresh();
//        }
    }

    @Override
    protected FriendAdapter getListAdapter() {
        return new FriendAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + mCatalog + "_" + mUid;
    }

    @Override
    protected FriendsList parseList(JSONObject jo) throws Exception {
        return null;
    }

    @Override
    protected FriendsList readList(Serializable seri) {
        return ((FriendsList) seri);
    }

    @Override
    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (((Friend) enity).getUserid() == ((Friend) data.get(i))
                        .getUserid()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void sendRequestData() {
//        LvTuShiGuangApi.getFriendList(mUid, mCatalog, mCurrentPage, mHandler);
    }

    @Override
    protected void onRefreshNetworkSuccess() {
//        if ((NoticeViewPagerFragment.sCurrentPage == 3 || NoticeViewPagerFragment.sShowCount[3] > 0)
//                && mCatalog == FriendsList.TYPE_FANS
//                && mUid == AppContext.getInstance().getLoginUid()) {
//            NoticeUtils.clearNotice(Notice.TYPE_NEWFAN);
//            UIHelper.sendBroadcastForNotice(getActivity());
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Friend item = (Friend) mAdapter.getItem(position);
//        if (item != null) {
//            if (mUid == AppContext.getInstance().getLoginUid()) {
//                UIHelper.showMessageDetail(getActivity(), item.getUserid(),
//                        item.getName());
//                return;
//            }
//            UIHelper.showUserCenter(getActivity(), item.getUserid(),
//                    item.getName());
//        }
    }
}
