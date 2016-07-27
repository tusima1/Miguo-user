package com.miguo.live.views.customviews;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.fanwe.app.App;

/**
 * Created by didik on 2016/7/25.
 */
public class MGToast {
    private static Toast toast;

    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void showToast(String text)
    {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showToast(final String text, final int duration)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            show(text, duration);
        } else
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    show(text, duration);
                }
            });
        }
    }

    private static void show(String text, int duration)
    {
        if (TextUtils.isEmpty(text))
        {
            return;
        }
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(App.getInstance().getApplicationContext(), text, duration);
        toast.show();
    }
}
