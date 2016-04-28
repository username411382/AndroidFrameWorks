package com.ruihe.demo.common.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.widget.ImageView;


/**
 * 渐变显示图片的Drawable
 */
public final class TransitionFadeInDrawable extends BitmapDrawable {

    // Only accessed from main thread.
    private static final float FADE_DURATION = 400f; //ms

    /**
     * 有渐变得显示图片
     */
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap, Drawable defaultDrawable) {
        setPlaceholder(imageView, defaultDrawable);
        setBitmap(imageView, imageView.getContext(), bitmap, true);
    }

    /**
     * Create or update the drawable on the target {@link ImageView} to display the supplied bitmap
     * image.
     */
    static void setBitmap(ImageView target, Context context, Bitmap bitmap, boolean fade) {
        Drawable placeholder = target.getDrawable();
        if (placeholder instanceof AnimationDrawable) {
            ((AnimationDrawable) placeholder).stop();
        }
        TransitionFadeInDrawable drawable = new TransitionFadeInDrawable(context, bitmap, placeholder, fade);
        target.setImageDrawable(drawable);
    }

    /**
     * Create or update the drawable on the target {@link ImageView} to display the supplied
     * placeholder image.
     */
    static void setPlaceholder(ImageView target, Drawable placeholderDrawable) {
        target.setImageDrawable(placeholderDrawable);
        if (target.getDrawable() instanceof AnimationDrawable) {
            ((AnimationDrawable) target.getDrawable()).start();
        }
    }

    Drawable placeholder;

    long startTimeMillis;
    boolean animating;
    int alpha = 0xFF;

    TransitionFadeInDrawable(Context context, Bitmap bitmap, Drawable placeholder, boolean fade) {
        super(context.getResources(), bitmap);

        if (fade) {
            this.placeholder = placeholder;
            animating = true;
            startTimeMillis = SystemClock.uptimeMillis();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!animating) {
            super.draw(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
            if (normalized >= 1f) {
                animating = false;
                placeholder = null;
                super.draw(canvas);
            } else {
                if (placeholder != null) {
                    placeholder.draw(canvas);
                }

                int partialAlpha = (int) (alpha * normalized);
                super.setAlpha(partialAlpha);

                super.draw(canvas);
                super.setAlpha(alpha);

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    invalidateSelf();
                }
            }
        }

    }

}
