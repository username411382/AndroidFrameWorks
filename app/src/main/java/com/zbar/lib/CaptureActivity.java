package com.zbar.lib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.ruihe.demo.R;
import com.ruihe.demo.activity.BaseActivity;
import com.ruihe.demo.common.utils.DialogUtils;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import java.io.IOException;
import java.util.Vector;

import io.reactivex.functions.Consumer;

public abstract class CaptureActivity extends BaseActivity implements Callback {

    public RelativeLayout mContainer = null;
    public RelativeLayout mCropLayout = null;

    private Dialog dialogCameraClosed;

    protected Activity mActivity;
    protected CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int extraPaddingY = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;

    private boolean flag = false;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setExtraPaddingTop(int extraPaddingTop) {
        extraPaddingY = extraPaddingTop;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }


    @Override
    public void onActivityViewCreated() {
        // 初始化 CameraManager
        CameraManager.init(getApplication());
        mActivity = this;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(mActivity);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(String result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
        onScanSuccess(result);

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.y * 2 / 2;
            int height = point.x * 2 / 2;

            int x = mCropLayout.getLeft() * width / mContainer.getWidth();
            int y = (mCropLayout.getTop() + extraPaddingY) * height / mContainer.getHeight();

            int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);

        } catch (IOException | RuntimeException ioe) {
            openCameraFailed();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @SuppressLint("CheckResult")
    public void openCameraFailed() {
        if (dialogCameraClosed != null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showCameraClosedDialog();
            return;
        }
        getRxPermissions().request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isGranted) throws Exception {
                if (!isGranted){
                    showCameraClosedDialog();
                }
            }
        });

    }

    private void showCameraClosedDialog() {
        if (dialogCameraClosed == null) {
            dialogCameraClosed = DialogUtils.createAlertDialog(mActivity, getString(R.string.txt_run_home_scan_code_open_camera_fail)
                    , new DialogUtils.OnDialogButtonClickListener() {
                @Override
                public void onConfirm() {
                }

                @Override
                public void onCancel() {

                }
            });
            dialogCameraClosed.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            dialogCameraClosed.show();
        }
    }

    protected void light(ImageView ivLight) {
        if (!flag) {
            flag = true;
            // 开闪光灯
            if (ivLight != null) {
                ivLight.setImageResource(R.drawable.ic_scan_flash_light_on);
            }
            CameraManager.get().openLight();
        } else {
            flag = false;
            // 关闪光灯
            if (ivLight != null) {
                ivLight.setImageResource(R.drawable.ic_scan_flash_light_off);
            }
            CameraManager.get().offLight();
        }
    }

    public abstract void onScanSuccess(String result);
}