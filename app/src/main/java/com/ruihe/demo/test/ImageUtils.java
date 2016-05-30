package com.ruihe.demo.test;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static String saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {
        File newFile = new File("/sdcard/" + bitName + ".png");
        newFile.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(newFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile.getAbsolutePath();
    }

}
