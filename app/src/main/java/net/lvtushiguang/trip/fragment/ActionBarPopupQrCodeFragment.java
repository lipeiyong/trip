package net.lvtushiguang.trip.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的二维码
 * Created by 薰衣草 on 2016/8/11.
 */
public class ActionBarPopupQrCodeFragment extends BaseFragment {

    @BindView(R.id.qrcode)
    ImageView ivQrCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actionbar_popup_qrcode, container, false);
        ButterKnife.bind(this, view);

        String content = "我的二维码";
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_receivables);
        //生成二维码
//        Bitmap bitmap = EncodingUtils.createQRCode(content, 500, 500, logo);
//        ivQrCode.setImageBitmap(bitmap);

        return view;
    }
}
