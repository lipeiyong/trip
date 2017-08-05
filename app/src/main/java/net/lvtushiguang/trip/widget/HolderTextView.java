package net.lvtushiguang.trip.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import net.lvtushiguang.trip.R;

public class HolderTextView extends TextView {

    public HolderTextView(Context context) {
        super(context);
        setTextSize(getResources().getDimension(R.dimen.text_size_10));
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding((int) getResources().getDimension(R.dimen.text_size_30), 10,
                0, 10);
    }
}
