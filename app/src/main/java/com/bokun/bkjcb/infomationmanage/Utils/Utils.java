package com.bokun.bkjcb.infomationmanage.Utils;

import android.content.Context;

import com.bokun.bkjcb.infomationmanage.R;

import java.util.Random;

/**
 * Created by DengShuai on 2017/8/30.
 */

public class Utils {
    public static int GetRandomColor(Context context) {
        Random random = new Random();
        int[] colors = new int[]{R.color.random_1, R.color.random_2, R.color.random_3, R.color.random_4};
        return context.getResources().getColor(colors[random.nextInt(15) % 4]);
    }
}
