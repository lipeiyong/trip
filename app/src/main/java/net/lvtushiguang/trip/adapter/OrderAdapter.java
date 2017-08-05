package net.lvtushiguang.trip.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.ListBaseAdapter;
import net.lvtushiguang.trip.bean.Order;
import net.lvtushiguang.trip.util.StringUtils;
import net.lvtushiguang.trip.widget.AvatarView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends ListBaseAdapter<Order> {

    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_order, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Order item = (Order) mDatas.get(position);

        String state = "未付款";
        switch (item.getPayStatus()) {
            case "0":
                state = "未付款";
                break;
            case "1":
                state = "待确认";
                break;
            case "2":
                state = "已付款,地端未充值";
                break;
            case "3":
                state = "取消交易";
                break;
            case "4":
                state = "交易完成";
                break;
        }

        vh.avatar.setImageResource(R.drawable.money);
        vh.hours.setText(new SimpleDateFormat("HH:mm:ss").format(StringUtils.toDate(item.getCreateDate())));
        vh.yuars.setText(new SimpleDateFormat("yy-MM-dd").format(StringUtils.toDate(item.getCreateDate())));
        vh.money.setText("+" + item.getTotalFee());
        vh.state.setText(state);
        vh.orderNo.setText(item.getOrderNo());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_avatar)
        AvatarView avatar;
        @BindView(R.id.hours)
        TextView hours;
        @BindView(R.id.yuars)
        TextView yuars;
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.state)
        TextView state;
        @BindView(R.id.orderNo)
        TextView orderNo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
