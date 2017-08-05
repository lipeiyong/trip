package net.lvtushiguang.trip.fragment.list;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.reflect.TypeToken;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseListFragment;
import net.lvtushiguang.trip.base.ListBaseAdapter;
import net.lvtushiguang.trip.bean.ListEntity;
import net.lvtushiguang.trip.bean.SubTab;
import net.lvtushiguang.trip.bean.Video;
import net.lvtushiguang.trip.bean.VideoList;
import net.lvtushiguang.trip.interf.OnTabReselectListener;
import net.lvtushiguang.trip.interf.OnViewPagerChangeListener;
import net.lvtushiguang.trip.util.AppOperator;
import net.lvtushiguang.trip.util.SizeUtils;
import net.lvtushiguang.trip.widget.AvatarView;
import net.lvtushiguang.trip.widget.VideoPlayView;
import net.oschina.common.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PhoenixTree on 2017/5/29.
 */

public class VideoListFragment extends BaseListFragment<Video> implements OnTabReselectListener,
        View.OnClickListener, VideoPlayView.MediaPlayerImpl, OnViewPagerChangeListener {
    protected static final String TAG = VideoListFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "videolist_";
    SubTab tab;

    private VideoAdapter adapter;
    private VideoPlayView playView;
    private View currentItemView;
    private int currentPosition = -1;
    private int scrollDistance;// 记录切换到横屏时滑动的距离
    private boolean isPlaying;
    private int firstVisiblePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = (SubTab) getArguments().getSerializable("tab");
    }

    @Override
    protected ListBaseAdapter<Video> getListAdapter() {
        adapter = new VideoAdapter(getContext());
        return adapter;
    }


    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDividerHeight(20);
    }

    @Override
    protected void sendRequestData() {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(
                    AppContext.getInstance().getAssets().open("video_list.json")
                    , "UTF-8");
            List<Video> list = AppOperator.getGson().<ArrayList<Video>>fromJson(reader,
                    new TypeToken<ArrayList<Video>>() {
                    }.getType());
            //--
            JSONArray items = JSONArray.parseArray(JSON.toJSONString(list));
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("reContent", items);
            JSONObject object1 = new JSONObject(object.toJSONString());

            executeParserTask(object1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(reader);
        }
    }

    @Override
    protected ListEntity<Video> parseList(JSONObject jo) throws Exception {
        VideoList list = null;
        try {
            list = JSON.parseObject(jo.toString(), VideoList.class);
        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }

        return list;
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    public String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected VideoList readList(Serializable seri) {
        return (VideoList) seri;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG, position + "");
    }

    //    //通过 set 方法在适配器中拿到这三个参数
    public void setPlayView(VideoPlayView playView) {
        this.playView = playView;
        playView.setMediaPlayerListenr(this);
        Log.i("XX", currentPosition + "");
        int[] curr = new int[2];
        currentItemView.getLocationOnScreen(curr);
        Log.i("TAG", curr[1] + "");
    }

    @Override
    public void onError() {
        closeVideo();
    }

    @Override
    public void onExpend() {
        firstVisiblePosition = mListView.getFirstVisiblePosition();
        //强制横屏
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onShrik() {
        //强制竖屏
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void closeVideo() {
        currentPosition = -1;
        isPlaying = false;
        playView.stop();
        adapter.notifyDataSetChanged();
        playView = null;
        currentItemView = null;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((currentPosition < mListView.getFirstVisiblePosition() || currentPosition > mListView
                .getLastVisiblePosition()) && isPlaying && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            closeVideo();
        }
    }

    /**
     * 在 manifest 中设置当前 activity, 当横竖屏切换时会执行该方法, 否则会 finish 重新执行一遍生命周期
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (playView != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            //设置横屏后要显示的当前的 itemView
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    //一定要对添加这句话,否则无效,因为界面初始化完成后 listView 失去了焦点
                    mListView.requestFocusFromTouch();
                    mListView.setSelection(currentPosition);
                }
            });

            setVideoViewScale(display.getWidth(), display.getHeight());
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playView.setScreenOrientation(VideoPlayView.SCREEN_ORIENTATION_LANDSCAPE);

            Log.i("XX", "横屏");
        } else if (playView != null && newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏时的设置会影响返回竖屏后的效果, 这里设置高度与 xml 文件中的高度相同
            Log.i("MM", currentPosition + "竖屏");

            //本来想切换到竖屏后恢复到初始位置,但是上部出现空白
//            videoList.scrollBy(0, -(scrollDistance));
            //通过该方法恢复位置,不过还是有点小问题
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.requestFocusFromTouch();
                    mListView.setSelection(firstVisiblePosition);
                }
            });

            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.Dp2Px(getContext(), 200));
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            playView.setScreenOrientation(VideoPlayView.SCREEN_ORIENTATION_PORTRAIT);
            Log.i("XX", "竖屏");
        }
    }

    /**
     * VideoView横竖屏切换
     *
     * @param width
     * @param height
     */
    private void setVideoViewScale(int width, int height) {
        //修改VideoView
        ViewGroup.LayoutParams lp = playView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        playView.setLayoutParams(lp);

        //修改VideoLayout
        RelativeLayout mVideoLayout = (RelativeLayout) currentItemView.findViewById(R.id.video_layout);
        ViewGroup.LayoutParams lp1 = mVideoLayout.getLayoutParams();
        lp1.width = width;
        lp1.height = height;
        mVideoLayout.setLayoutParams(lp1);

    }


    @Override
    public void onPause() {
        super.onPause();
        if (playView != null) {
            playView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playView != null) {
            playView.stop();
        }
    }

    /**
     * 自定义的
     */
    @Override
    public void onViewPagerChange() {
        if (playView != null) {
            closeVideo();
        }
    }

    public class VideoAdapter extends ListBaseAdapter<Video> implements View.OnClickListener {
        private Context mContext;

        public VideoAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected View getRealView(int position, View view, ViewGroup parent) {
            Video item = mDatas.get(position);
            ViewHolder vh = null;
            if (view == null || view.getTag() == null) {
                view = getLayoutInflater(mContext).inflate(
                        R.layout.list_item_video, null);
                vh = new VideoAdapter.ViewHolder(view);
                view.setTag(vh);
            } else {
                vh = (VideoAdapter.ViewHolder) view.getTag();
            }
            //--
            int cover = mContext.getResources().getIdentifier(item.getCover(),
                    "drawable", mContext.getPackageName());
            vh.mCover.setImageResource(cover);
            int portrait = mContext.getResources().getIdentifier(item.getAuthorIcon(),
                    "drawable", mContext.getPackageName());
            vh.mPortrait.setImageResource(portrait);
            vh.mAuthor.setText(item.getAuthorName());
            vh.mComment.setText(item.getCommentCount());

            vh.mPortrait.setOnClickListener(this);
            vh.mAuthor.setOnClickListener(this);
            vh.mAttention.setOnClickListener(this);
            vh.mComment.setOnClickListener(this);
            vh.mShare.setOnClickListener(this);

            vh.mPortrait.setTag(position);

            vh.mBtnPlay.setOnClickListener(new VideoAdapter.MyClick(position, vh.mBtnPlay,
                    vh.mVideoView, view));

            if (currentPosition == position) {
                vh.mVideoView.setVisibility(View.VISIBLE);
            } else {
                vh.mVideoView.setVisibility(View.GONE);
                vh.mVideoView.stop();
                vh.mBtnPlay.setVisibility(View.VISIBLE);
            }

            return view;
        }

        class MyClick implements View.OnClickListener {
            private int position;
            private VideoPlayView playView;
            private ImageView btnPlay;
            private View convertView;

            public MyClick(int position, ImageView btnPlay, VideoPlayView playView, View convertView) {
                this.position = position;
                this.btnPlay = btnPlay;
                this.playView = playView;
                this.convertView = convertView;
            }

            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                isPlaying = true;
                currentPosition = position;
                playView.setUrl(getData().get(position).getUrl());
                currentItemView = convertView;
                setPlayView(playView);
                playView.openVideo();
                notifyDataSetChanged();
            }
        }


        @Override
        public void onClick(View view) {
            //----------------------------------------
        }

        class ViewHolder {
            @BindView(R.id.cover)
            ImageView mCover;
            @BindView(R.id.video_play_view)
            VideoPlayView mVideoView;
            @BindView(R.id.play_btn)
            ImageView mBtnPlay;
            @BindView(R.id.attention)
            TextView mAttention;
            @BindView(R.id.comment)
            TextView mComment;
            @BindView(R.id.share)
            ImageView mShare;
            @BindView(R.id.portrait)
            AvatarView mPortrait;
            @BindView(R.id.author)
            TextView mAuthor;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
