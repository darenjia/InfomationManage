package com.bokun.bkjcb.infomationmanage.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

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
        int[] colors = new int[]{R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3};
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

    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "com.bokun.bkjcb.infomationmanage.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static Drawable compressBitmap(Resources resources, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, id, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight = 480;
        int reqWidth = 480;
        if (height < reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeResource(resources, id, options);
//        mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, w, h);
        return new BitmapDrawable( resources,mBitmap);
    }
}
