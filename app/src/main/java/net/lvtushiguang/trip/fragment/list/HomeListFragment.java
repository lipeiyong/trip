package net.lvtushiguang.trip.fragment.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.adapter.MessageAdapter;
import net.lvtushiguang.trip.base.BaseListFragment;
import net.lvtushiguang.trip.bean.ListEntity;
import net.lvtushiguang.trip.bean.Message;
import net.lvtushiguang.trip.bean.MessageList;
import net.lvtushiguang.trip.bean.SubTab;
import net.lvtushiguang.trip.interf.OnTabReselectListener;
import net.lvtushiguang.trip.util.AppOperator;
import net.lvtushiguang.trip.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhoenixTree on 2017/5/29.
 */

public class HomeListFragment extends BaseListFragment<Message>
        implements OnTabReselectListener {
    private static final String TAG = HomeListFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "messagelist_";

    @Override
    protected MessageAdapter getListAdapter() {
        return new MessageAdapter();
    }

    @Override
    protected void sendRequestData() {
//        LvTuShiGuangApi.getMessageList("1", mCurrentPage, mHandler);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(
                    AppContext.getInstance().getAssets().open("home_list_message.json")
                    , "UTF-8");
            List<Message> list = AppOperator.getGson().<ArrayList<Message>>fromJson(reader,
                    new TypeToken<ArrayList<Message>>() {
                    }.getType());
            //--
            JSONArray items = JSONArray.parseArray(JSON.toJSONString(list));
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("reContent", items);
            JSONObject object1 = new JSONObject(object.toJSONString());
            Log.i("TAG1", object1.toString());

            executeParserTask(object1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            StreamUtil.close(reader);
        }
    }

    @Override
    protected ListEntity<Message> parseList(JSONObject jo) throws Exception {
        MessageList list = null;
        try {
            list = JSON.parseObject(jo.toString(), MessageList.class);
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
    protected MessageList readList(Serializable seri) {
        return (MessageList) seri;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Message message = mAdapter.getItem(position);
        if (message != null) {
            UIHelper.showMessageDetail(getContext(), message);
        }
    }

}
