package net.lvtushiguang.trip.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import net.lvtushiguang.trip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhoenixTree on 2017/7/2.
 */

public class TestActivity extends AppCompatActivity {
    ListView mListView;
    MyAdapter mAdapter;
    List<String> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mListView = (ListView) findViewById(R.id.listview);
        data = new ArrayList<>();
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        data.add("0");
        mAdapter = new MyAdapter(this, data);
        mListView.setAdapter(mAdapter);
    }


    private class MyAdapter extends BaseAdapter {
        Context context;
        List<String> data;

        public MyAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.test_item, null);
            return view;
        }
    }
}
