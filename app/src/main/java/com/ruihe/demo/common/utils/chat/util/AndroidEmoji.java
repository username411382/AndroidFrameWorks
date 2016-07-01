package com.ruihe.demo.common.utils.chat.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ReplacementSpan;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.chat.mode.Emoji;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * Created by ruihe on 2016/7/1.
 */
public class AndroidEmoji {

    private static Resources sResources;
    private static float density;
    private static Map<Integer, Emoji> sEmojiMap;
    private static List<Emoji> sEmojiList;

    public AndroidEmoji() {
    }

    public static void init(Context context) {
        sEmojiMap = new HashMap();
        sEmojiList = new ArrayList();
        sResources = context.getResources();
        int[] codes = sResources.getIntArray(R.array.rc_emoji_code);
        TypedArray array = sResources.obtainTypedArray(R.array.rc_emoji_res);
        if (codes.length != array.length()) {
            throw new RuntimeException("Emoji resource init fail.");
        } else {
            int i = -1;

            while (true) {
                ++i;
                if (i >= codes.length) {
                    DisplayMetrics var5 = context.getResources().getDisplayMetrics();
                    density = var5.density;
                    Log.d("SystemUtils", "density:" + density);
                    return;
                }

                Emoji dm = new Emoji(codes[i], array.getResourceId(i, -1));
                sEmojiMap.put(Integer.valueOf(codes[i]), dm);
                sEmojiList.add(dm);
            }
        }
    }

    public static List<Emoji> getEmojiList() {
        return sEmojiList;
    }

    public static int getEmojiCount(String input) {
        if (input == null) {
            return 0;
        } else {
            int count = 0;
            char[] chars = input.toCharArray();
            new SpannableStringBuilder(input);

            for (int i = 0; i < chars.length; ++i) {
                if (!Character.isHighSurrogate(chars[i])) {
                    int codePoint;
                    boolean isSurrogatePair;
                    if (Character.isLowSurrogate(chars[i])) {
                        if (i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                            continue;
                        }

                        codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                        isSurrogatePair = true;
                    } else {
                        codePoint = chars[i];
                        isSurrogatePair = false;
                    }

                    if (sEmojiMap.containsKey(Integer.valueOf(codePoint))) {
                        ++count;
                    }
                }
            }

            return count;
        }
    }

    public static CharSequence ensure(String input) {
        if (input == null) {
            return input;
        } else {
            char[] chars = input.toCharArray();
            SpannableStringBuilder ssb = new SpannableStringBuilder(input);

            for (int i = 0; i < chars.length; ++i) {
                if (!Character.isHighSurrogate(chars[i])) {
                    int codePoint;
                    boolean isSurrogatePair;
                    if (Character.isLowSurrogate(chars[i])) {
                        if (i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                            continue;
                        }

                        codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                        isSurrogatePair = true;
                    } else {
                        codePoint = chars[i];
                        isSurrogatePair = false;
                    }

                    if (sEmojiMap.containsKey(Integer.valueOf(codePoint))) {
                        ssb.setSpan(new AndroidEmoji.EmojiImageSpan(sResources, codePoint), isSurrogatePair ? i - 1 : i, i + 1, 34);
                    }
                }
            }

            return ssb;
        }
    }

    public static boolean isEmoji(String input) {
        if (input == null) {
            return false;
        } else {
            char[] chars = input.toCharArray();
            boolean codePoint = false;
            int length = chars.length;

            for (int i = 0; i < length; ++i) {
                if (!Character.isHighSurrogate(chars[i])) {
                    int var5;
                    if (Character.isLowSurrogate(chars[i])) {
                        if (i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                            continue;
                        }

                        var5 = Character.toCodePoint(chars[i - 1], chars[i]);
                    } else {
                        var5 = chars[i];
                    }

                    if (sEmojiMap.containsKey(Integer.valueOf(var5))) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static void ensure(Spannable spannable) {
        char[] chars = spannable.toString().toCharArray();

        for (int i = 0; i < chars.length; ++i) {
            if (!Character.isHighSurrogate(chars[i])) {
                int codePoint;
                boolean isSurrogatePair;
                if (Character.isLowSurrogate(chars[i])) {
                    if (i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                        continue;
                    }

                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                } else {
                    codePoint = chars[i];
                    isSurrogatePair = false;
                }

                if (sEmojiMap.containsKey(Integer.valueOf(codePoint))) {
                    spannable.setSpan(new AndroidEmoji.EmojiImageSpan(sResources, codePoint), isSurrogatePair ? i - 1 : i, i + 1, 34);
                }
            }
        }

    }

    public static class EmojiImageSpan extends ReplacementSpan {
        Drawable mDrawable;
        private static final String TAG = "DynamicDrawableSpan";
        public static final int ALIGN_BOTTOM = 0;
        private WeakReference<Drawable> mDrawableRef;

        private EmojiImageSpan(Resources resources, int codePoint) {
            if (AndroidEmoji.sEmojiMap.containsKey(Integer.valueOf(codePoint))) {
                this.mDrawable = resources.getDrawable(((Emoji) AndroidEmoji.sEmojiMap.get(Integer.valueOf(codePoint))).getRes());
                int width = this.mDrawable.getIntrinsicWidth() - (int) (4.0F * AndroidEmoji.density);
                int height = this.mDrawable.getIntrinsicHeight() - (int) (4.0F * AndroidEmoji.density);
                this.mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
            }

        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }

        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = this.getCachedDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;
                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        }

        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = this.getCachedDrawable();
            canvas.save();
            int transY = bottom - b.getBounds().bottom;
            transY = (int) ((float) transY - AndroidEmoji.density);
            canvas.translate(x, (float) transY);
            b.draw(canvas);
            canvas.restore();
        }

        private Drawable getCachedDrawable() {
            WeakReference wr = this.mDrawableRef;
            Drawable d = null;
            if (wr != null) {
                d = (Drawable) wr.get();
            }

            if (d == null) {
                d = this.getDrawable();
                this.mDrawableRef = new WeakReference(d);
            }

            return d;
        }
    }
}
