package net.lvtushiguang.trip.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.lvtushiguang.trip.AppContext;
import net.lvtushiguang.trip.R;

/**
 * Created by 薰衣草 on 2016/8/8.
 */
public class ActionBarPopupDialog extends PopupWindow implements OnClickListener {
    private View conentView;
    private OnActionBarPopItemClickListener onActionBarPopItemClickListener;

    public interface OnActionBarPopItemClickListener {
        void onItemClick(View v);
    }

    public void setOnActionBarPopItemClickListener(OnActionBarPopItemClickListener onActionBarPopItemClickListener) {
        this.onActionBarPopItemClickListener = onActionBarPopItemClickListener;
    }


    public ActionBarPopupDialog(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.actionbar_popup_dialog, null);
        TextView tvScan = (TextView) conentView.findViewById(R.id.scan);
        TextView tvChitchat = (TextView) conentView.findViewById(R.id.chitchat);
        TextView tvFriend = (TextView) conentView.findViewById(R.id.friend);
        TextView tvQrcode = (TextView) conentView.findViewById(R.id.qrcode);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 100);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
        ColorDrawable dw;
        if (AppContext.getNightModeSwitch()) {
            dw = new ColorDrawable(ContextCompat.getColor(context, R.color.night_layout_bg_normal));
            tvScan.setTextColor(ContextCompat.getColor(context, R.color.night_textColor));
            tvChitchat.setTextColor(ContextCompat.getColor(context, R.color.night_textColor));
            tvFriend.setTextColor(ContextCompat.getColor(context, R.color.night_textColor));
            tvQrcode.setTextColor(ContextCompat.getColor(context, R.color.night_textColor));
        } else {
            dw = new ColorDrawable(ContextCompat.getColor(context, R.color.day_layout_bg_normal));
            tvScan.setTextColor(ContextCompat.getColor(context, R.color.day_textColor));
            tvChitchat.setTextColor(ContextCompat.getColor(context, R.color.day_textColor));
            tvFriend.setTextColor(ContextCompat.getColor(context, R.color.day_textColor));
            tvQrcode.setTextColor(ContextCompat.getColor(context, R.color.day_textColor));
        }
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.ActionBarPopupDialogAnimation);

        LinearLayout llScan = (LinearLayout) conentView.findViewById(R.id.popup_scan_layout);
        LinearLayout llChitchat = (LinearLayout) conentView.findViewById(R.id.popup_chitchat_layout);
        LinearLayout llFriend = (LinearLayout) conentView.findViewById(R.id.popup_friend_layout);
        LinearLayout llQrCode = (LinearLayout) conentView.findViewById(R.id.popup_qrcode_layout);

        llScan.setOnClickListener(this);
        llChitchat.setOnClickListener(this);
        llFriend.setOnClickListener(this);
        llQrCode.setOnClickListener(this);

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(Context context, View parent, int x, int y) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);

            showAtLocation(parent, Gravity.RIGHT | Gravity.TOP, x, y);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_scan_layout:
                onActionBarPopItemClickListener.onItemClick(v);
                this.dismiss();
                break;
            case R.id.popup_chitchat_layout:
                onActionBarPopItemClickListener.onItemClick(v);
                this.dismiss();
                break;
            case R.id.popup_friend_layout:
                onActionBarPopItemClickListener.onItemClick(v);
                this.dismiss();
                break;
            case R.id.popup_qrcode_layout:
                onActionBarPopItemClickListener.onItemClick(v);
                this.dismiss();
                break;
        }
    }

}
