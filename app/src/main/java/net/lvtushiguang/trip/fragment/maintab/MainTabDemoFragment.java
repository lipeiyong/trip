package net.lvtushiguang.trip.fragment.maintab;

import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.reflect.TypeToken;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.adapter.TopInfoAdapter;
import net.lvtushiguang.trip.base.BaseFragment;
import net.lvtushiguang.trip.base.BaseRecyclerAdapter;
import net.lvtushiguang.trip.base.BaseRecyclerFragment;
import net.lvtushiguang.trip.bean.ListEntity;
import net.lvtushiguang.trip.bean.TopInfo;
import net.lvtushiguang.trip.bean.TopInfoList;
import net.lvtushiguang.trip.fragment.recycler.TopInfoRecyclerFragment;
import net.lvtushiguang.trip.util.AppOperator;
import net.oschina.common.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心界面
 * <p>
 * Created by 薰衣草 on 2016/7/19.
 */
public class MainTabDemoFragment extends BaseRecyclerFragment<TopInfo> {

    private static final String TAG = TopInfoRecyclerFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "topinforecycler_";

    @Override
    protected BaseRecyclerAdapter<TopInfo> getRecyclerAdapter() {
        return new TopInfoAdapter();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected void sendRequestData() {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(
                    AppContext.getInstance().getAssets().open("top_list_info.json")
                    , "UTF-8");
            List<TopInfo> list = AppOperator.getGson().<ArrayList<TopInfo>>fromJson(reader,
                    new TypeToken<ArrayList<TopInfo>>() {
                    }.getType());
            //--
            JSONArray items = JSONArray.parseArray(JSON.toJSONString(list));
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("reContent", items);
            JSONObject object1 = new JSONObject(object.toJSONString());
            Log.e("TAG", object1.toString());

            executeParserTask(object1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(reader);
        }
    }

    @Override
    protected ListEntity<TopInfo> parseList(JSONObject jo) throws Exception {
        TopInfoList list = null;
        try {
            list = JSON.parseObject(jo.toString(), TopInfoList.class);
        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }

        return list;
    }

}
