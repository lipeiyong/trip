package net.lvtushiguang.trip.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.ListBaseAdapter;
import net.lvtushiguang.trip.bean.Message;
import net.lvtushiguang.trip.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends ListBaseAdapter<Message> {

    @Override
    public int getItemViewType(int position) {
        if (position >= mDatas.size())
            return 0;

        Message item = mDatas.get(position);
        if (item.getMessagetype().equals("0"))
            return 0;
        else
            return 1;
    }

    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        final Message item = (Message) mDatas.get(position);
        Context context = parent.getContext();
        ViewHolder vh = null;
        ViewHolder1 vh1 = null;
        if (convertView == null || convertView.getTag() == null) {
            switch (item.getMessagetype()) {
                case "0"://普通资讯
                    convertView = getLayoutInflater(context).inflate(
                            R.layout.list_cell_home_message, null);
                    vh = new ViewHolder(convertView);
                    convertView.setTag(vh);
                    break;
                case "1":
                    convertView = getLayoutInflater(parent.getContext()).inflate(
                            R.layout.list_cell_home_video, null);
                    vh1 = new ViewHolder1();
                    vh1.mTitle = (TextView) convertView.findViewById(R.id.title);
                    vh1.mImage = (ImageView) convertView.findViewById(R.id.image);
                    vh1.mSort = (ImageView) convertView.findViewById(R.id.sort);
                    vh1.mAuthorName = (TextView) convertView.findViewById(R.id.authorname);
                    vh1.mCommentCount = (TextView) convertView.findViewById(R.id.commentCount);
                    vh1.mSenderTime = (TextView) convertView.findViewById(R.id.sendertime);
                    convertView.setTag(vh1);
                    break;
            }
        } else {
            switch (item.getMessagetype()) {
                case "0":
                    if (convertView.getTag() instanceof ViewHolder) {
                        vh = (ViewHolder) convertView.getTag();
                    } else {
                        convertView = getLayoutInflater(context).inflate(
                                R.layout.list_cell_home_message, null);
                        vh = new ViewHolder(convertView);
                        convertView.setTag(vh);
                    }
                    break;
                case "1":
                    if (convertView.getTag() instanceof ViewHolder1) {
                        vh1 = (ViewHolder1) convertView.getTag();
                    } else {
                        convertView = getLayoutInflater(context).inflate(
                                R.layout.list_cell_home_video, null);
                        vh1 = new ViewHolder1();
                        vh1.mTitle = (TextView) convertView.findViewById(R.id.title);
                        vh1.mImage = (ImageView) convertView.findViewById(R.id.image);
                        vh1.mSort = (ImageView) convertView.findViewById(R.id.sort);
                        vh1.mAuthorName = (TextView) convertView.findViewById(R.id.authorname);
                        vh1.mCommentCount = (TextView) convertView.findViewById(R.id.commentCount);
                        vh1.mSenderTime = (TextView) convertView.findViewById(R.id.sendertime);
                        convertView.setTag(vh1);
                    }
                    break;
            }

        }

        switch (item.getMessagetype()) {
            case "0":

                vh.mTitle.setText(item.getTitle());

                int icon1 = context.getResources().getIdentifier(item.getImage1(),
                        "drawable", context.getPackageName());
                vh.mImage1.setImageResource(icon1);
                int icon2 = context.getResources().getIdentifier(item.getImage2(),
                        "drawable", context.getPackageName());
                vh.mImage2.setImageResource(icon2);
                int icon3 = context.getResources().getIdentifier(item.getImage3(),
                        "drawable", context.getPackageName());
                vh.mImage3.setImageResource(icon3);

                switch (item.getSort()) {//排序：0-置顶 1-热 2-广告 3-普通
                    case "0":
                        vh.mSort.setImageResource(R.drawable.zhiding);
                        break;
                    case "1":
                        vh.mSort.setImageResource(R.drawable.zuozhe);
                        break;
                    case "2":
                        vh.mSort.setImageResource(R.drawable.yuanchuang);
                        break;
                    case "3":
                        vh.mSort.setVisibility(View.GONE);
                        break;
                }

                vh.mAuthorName.setText(item.getAuthorname());
                vh.mCommentCount.setText(item.getCommentCount());
                vh.mSenderTime.setText(StringUtils.friendly_time(item.getSendertime()));

                break;
            case "1":
                vh1.mTitle.setText(item.getTitle());

                int icon = context.getResources().getIdentifier(item.getImage1(),
                        "drawable", context.getPackageName());
                vh1.mImage.setImageResource(icon);

                switch (item.getSort()) {//排序：0-置顶 1-热 2-广告 3-普通
                    case "0":
                        vh1.mSort.setImageResource(R.drawable.zhiding);
                        break;
                    case "1":
                        vh1.mSort.setImageResource(R.drawable.zuozhe);
                        break;
                    case "2":
                        vh1.mSort.setImageResource(R.drawable.yuanchuang);
                        break;
                    case "3":
                        vh1.mSort.setVisibility(View.GONE);
                        break;
                }

                vh1.mAuthorName.setText(item.getAuthorname());
                vh1.mCommentCount.setText(item.getCommentCount());
                vh1.mSenderTime.setText(StringUtils.friendly_time(item.getSendertime()));
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.image1)
        ImageView mImage1;
        @BindView(R.id.image2)
        ImageView mImage2;
        @BindView(R.id.image3)
        ImageView mImage3;
        @BindView(R.id.sort)
        ImageView mSort;
        @BindView(R.id.authorname)
        TextView mAuthorName;
        @BindView(R.id.commentCount)
        TextView mCommentCount;
        @BindView(R.id.sendertime)
        TextView mSenderTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder1 {
        TextView mTitle;
        ImageView mImage;
        ImageView mSort;
        TextView mAuthorName;
        TextView mCommentCount;
        TextView mSenderTime;
    }
}
