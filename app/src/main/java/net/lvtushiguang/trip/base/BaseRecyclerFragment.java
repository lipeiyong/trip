package net.lvtushiguang.trip.base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.container.RotationFooter;
import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.bean.Entity;
import net.lvtushiguang.trip.bean.ListEntity;
import net.lvtushiguang.trip.bean.Result;
import net.lvtushiguang.trip.cache.CacheManager;
import net.lvtushiguang.trip.ui.empty.EmptyLayout;
import net.lvtushiguang.trip.util.TDevice;
import net.lvtushiguang.trip.widget.SpacesItemDecoration;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * RecyclerView基类
 * Created by PhoenixTree on 2017/7/19.
 */

public abstract class BaseRecyclerFragment<T extends Entity> extends BaseFragment {

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    protected int mCatalog = 1;

    private Unbinder unbinder;

    @BindView(R.id.springview)
    public SpringView mSpringView;

    @BindView(R.id.recycler)
    public RecyclerView mRecyclerView;

    @BindView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 0;

    protected BaseRecyclerAdapter<T> mAdapter;

    private AsyncTask<String, Void, ListEntity<T>> mCacheTask;
    private ParserTask mParserTask;

    // 错误信息
    protected Result mResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_recycler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
    }

    @Override
    public void initView(View view) {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));

        mSpringView.setHeader(new RotationHeader(getContext()));
        mSpringView.setFooter(new RotationFooter(getContext()));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestData(true);
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringView.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });

        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getRecyclerAdapter();
            mRecyclerView.setAdapter(mAdapter);

            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
//            mState = STATE_NONE;
            //调用刷新
            mSpringView.setHeaderRefreshing();

        }

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
//                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    protected abstract String getCacheKeyPrefix();

    /***
     * 获取列表数据
     *
     * @param refresh
     * @return void
     * @author 火蚁 2015-2-9 下午3:16:12
     */
    protected void requestData(boolean refresh) {
        String key = getCacheKey();
        if (isReadCacheData(refresh)) {
            readCacheData(key);
        } else {
            // 取新的数据
            sendRequestData();
        }
    }

    /***
     * 判断是否需要读取缓存的数据
     *
     * @param refresh
     * @return
     * @author 火蚁 2015-2-10 下午2:41:02
     */
    protected boolean isReadCacheData(boolean refresh) {
        String key = getCacheKey();
        if (!TDevice.hasInternet()) {
            return true;
        }
        // 第一页若不是主动刷新，缓存存在，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key) && !refresh
                && mCurrentPage == 0) {
            return true;
        }
        // 其他页数的，缓存存在以及还没有失效，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key)
                && !CacheManager.isCacheDataFailure(getActivity(), key)
                && mCurrentPage != 0) {
            return true;
        }

        return false;
    }

    protected abstract void sendRequestData();

    private void readCacheData(String cacheKey) {
//        cancelReadCacheTask();
//        mCacheTask = new BaseListFragment.CacheTask(getActivity()).execute(cacheKey, "1");
    }


    protected void executeParserTask(JSONObject data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    protected abstract BaseRecyclerAdapter<T> getRecyclerAdapter();

    protected ListEntity<T> parseList(JSONObject jo) throws Exception {
        return null;
    }

    private void executeSaveCacheTask(ListEntity<T> data) {
        new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
    }

    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }

        if (mResult != null && !mResult.OK()) {
            AppContext.showToast(mResult.getReMsg());
//            注销登陆，密码已经修改，cookie，失效了
//            AppContext.getInstance().Logout();
        }

        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(mAdapter.getData(), data.get(i))) {
                data.remove(i);
                i--;
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if ((mAdapter.getItemCount() + data.size()) == 0) {
            adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || (data.size() < getPageSize() && mCurrentPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
//            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        mAdapter.addData(data);
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getPageSize() {
        return AppContext.PAGE_SIZE;
    }


    // 完成刷新
    protected void executeOnLoadFinish() {
        mSpringView.onFinishFreshAndLoad();
//        mState = STATE_NONE;
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final JSONObject reponseData;
        private boolean parserError;
        private List<T> list;

        public ParserTask(JSONObject data) {
            this.reponseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                ListEntity<T> data = parseList(reponseData);
                executeSaveCacheTask(data);

                list = data.getList();

                if (list == null) {
//                    mResult=
                }
            } catch (Exception e) {
                parserError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (parserError) {
                readCacheData(getCacheKey());
            } else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

    class SaveCacheTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        public SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

}
