package com.ruihe.demo.common.utils.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * 描述：图片文件的工具类
 * Created by ruihe on 2016/5/17.
 */
public class ImageFileUtils {


    /**
     * bitmap转byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytesArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * bytes转bitmap
     *
     * @param bmpBytes
     * @return
     */
    public static Bitmap bytesArrayToBitmap(byte[] bmpBytes) {
        return BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
    }


}
