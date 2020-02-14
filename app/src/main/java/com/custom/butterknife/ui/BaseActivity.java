package com.custom.butterknife.ui;

import android.os.Bundle;

import com.custom.butterknife.core.Inject;
import com.custom.butterknife.ulit.ButterKnife;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by: Ysw on 2020/2/13.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);
        ButterKnife.bind(this);
    }
}
