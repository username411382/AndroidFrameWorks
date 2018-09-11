package com.zbar.lib.decode;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DensityUtil;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;


final class DecodeHandler extends Handler {

    CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(CaptureActivity activity, Hashtable<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                Log.d("DecodeHandler", "handleMessage");
                break;
            case R.id.quit:
                Log.d("DecodeHandler", "handleMessage quit");
                Looper.myLooper().quit();
                break;
        }
    }

    private void decode(byte[] data, int width, int height) {
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width;// Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        int[] location = new int[2];
        activity.mCropLayout.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        Log.d("DecodeHandler", "left："+left);
        Log.d("DecodeHandler", "top："+top);
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height
                , left
                , top
                , DensityUtil.dip2px(activity, 250)
                , DensityUtil.dip2px(activity, 250));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result rawResult = null;
        Log.d("DecodeHandler", "handleMessage1");
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
            Log.d("DecodeHandler", "ReaderException扫码异常:" + re.getMessage());
        } finally {
            multiFormatReader.reset();
        }
	/*	ZbarManager manager = new ZbarManager();
		String result = manager.decode(rotatedData, width, height, true,
				activity.getX(), activity.getY(), activity.getCropWidth(),
				activity.getCropHeight());*/

        if (rawResult != null) {
            Log.d("DecodeHandler", "handleMessage2"+rawResult.getText());
            if (null != activity.getHandler()) {
                Message msg = new Message();
                msg.obj = rawResult.getText();
                msg.what = R.id.decode_succeeded;
                activity.getHandler().sendMessage(msg);
            }
        } else {
            if (null != activity.getHandler()) {
                activity.getHandler().sendEmptyMessage(R.id.decode_failed);
            }
        }
    }

}
