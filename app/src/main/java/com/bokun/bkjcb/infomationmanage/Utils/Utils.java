package com.bokun.bkjcb.infomationmanage.Utils;

import android.content.Context;
import android.os.Environment;

import com.bokun.bkjcb.infomationmanage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static String getDate() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return format.format(d);
    }

    public static void saveFile(InputStream is) {
        String path = Environment.getExternalStorageDirectory() + "/GasAddressBook";
        File file = new File(path, "GasAddressBooK.apk");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024 * 4];
            int length = 0;
            while ((length = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            is.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }
}
