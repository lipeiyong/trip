package net.lvtushiguang.trip.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.TextView;

import net.lvtushiguang.trip.R;

/**
 * 搜索界面
 * Created by thanatos on 16/9/7.
 */

@SuppressWarnings("RestrictedApi")
public class SearchActivity extends AppCompatActivity {
    TextView mFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        mFinish = (TextView) findViewById(R.id.tv_cancel);
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
