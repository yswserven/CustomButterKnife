package com.custom.butterknife.ulit;

import android.app.Activity;

import com.custom.butterknife.core.IBinder;

/**
 * Created by: Ysw on 2020/2/14.
 */
public class ButterKnife {
    public static void bind(Activity activity) {
        String name = activity.getClass().getName() + "_ViewBinding";
        try {
            Class<?> clazz = Class.forName(name);
            IBinder iBinder = (IBinder) clazz.newInstance();
            iBinder.bind(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
