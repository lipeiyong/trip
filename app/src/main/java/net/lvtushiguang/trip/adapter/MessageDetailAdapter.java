package net.lvtushiguang.trip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.fragment.MessageDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息详情适配器
 */
public class MessageDetailAdapter<T> extends BaseAdapter {

    private List<T> mData;

    private LayoutInflater mInflater;

    public MessageDetailAdapter(Context context, List<T> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.list_message_detail, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_content)
        TextView content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
