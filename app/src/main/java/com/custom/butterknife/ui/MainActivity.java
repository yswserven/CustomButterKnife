package com.custom.butterknife.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.annotation.BindView;
import com.custom.butterknife.R;
import com.custom.butterknife.annotation.ContentView;
import com.custom.butterknife.annotation.OnClick;
import com.custom.butterknife.annotation.OnLongClick;
import com.custom.butterknife.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    /* 这个是通过注解反射进行控件绑定 @author Ysw created 2020/2/14 */
    @ViewInject(R.id.tv_text)
    private TextView tvText;

    /* 这个是通过编译是注入代码实现控件绑定 @author Ysw created 2020/2/14 */
    @BindView(R.id.bt_onClickOne)
    Button btOnClickOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvText.setText("控件注入");
        Log.d("Ysw", "onCreate: " + btOnClickOne);
    }

    @OnClick({R.id.bt_onClickOne, R.id.bt_onClickTwo})
    public boolean click(View view) {
        switch (view.getId()) {
            case R.id.bt_onClickOne:
                tvText.setText("click: id = R.id.bt_onClickOne");
                Log.d("Ysw", "click: id = R.id.bt_onClickOne");
                Toast.makeText(this, "click: id = R.id.bt_onClickOne", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_onClickTwo:
                tvText.setText("click: id = R.id.bt_onClickTwo");
                Log.d("Ysw", "click: id = R.id.bt_onClickTwo");
                Toast.makeText(this, "click: id = R.id.bt_onClickTwo", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }


    @OnLongClick({R.id.bt_onLongClickOne, R.id.bt_onLongClickTwo})
    public boolean longClick(View view) {
        switch (view.getId()) {
            case R.id.bt_onLongClickOne:
                tvText.setText("longClick: id = R.id.bt_onLongClickOne");
                Log.d("Ysw", "longClick: id = R.id.bt_onLongClickOne");
                Toast.makeText(this, "longClick: id = R.id.bt_onLongClickOne", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_onLongClickTwo:
                tvText.setText("longClick: id = R.id.bt_onLongClickTwo");
                Log.d("Ysw", "longClick: id = R.id.bt_onLongClickTwo");
                Toast.makeText(this, "longClick: id = R.id.bt_onLongClickTwo", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
