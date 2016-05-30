package com.ruihe.demo.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.ruihe.demo.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final long DOUBLEMB = 200 * 1024;

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String getSmallFile(String filePath) {
        try {
            if (getFileSize(filePath) < DOUBLEMB) {
                return filePath;
            }
        } catch (Exception e1) {
            return filePath;
        }
        Bitmap bitmap = getSmallBitmap(filePath);
        File cacheDir = getDiskCacheDir(MyApplication.getInstance(), "yunlehui");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        File imgFile = new File(cacheDir, "temp.jpg");
        try {
            imgFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imgFile);
            bitmap.compress(CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            return imgFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static long getFileSize(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        long result = fis.available();
        if (fis != null) {
            fis.close();
        }
        return result;
    }

    public static void delete(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
