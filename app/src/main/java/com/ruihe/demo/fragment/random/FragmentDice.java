package com.ruihe.demo.fragment.random;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityRandom;
import com.ruihe.demo.common.utils.CaculateUtils;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.LogUtil;
import com.ruihe.demo.common.utils.ShakeUtils;
import com.ruihe.demo.common.utils.play.AudioPlayEntity;
import com.ruihe.demo.common.utils.play.AudioPlayUtils;
import com.ruihe.demo.common.view.FlowLayout;
import com.ruihe.demo.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 随机事件骰子页面
 * Created by RH on 2018/9/13 ruihedemo
 */
public class FragmentDice extends BaseFragment implements View.OnClickListener {

    private static final int[] DICE_CONTAINER_SHOW_ONE = {7};
    private static final int[] DICE_CONTAINER_SHOW_TWO = {4, 10};
    private static final int[] DICE_CONTAINER_SHOW_THREE = {4, 9, 11};
    private static final int[] DICE_CONTAINER_SHOW_FOUR = {3, 5, 9, 11};
    private static final int[] DICE_CONTAINER_SHOW_FIVE = {0, 2, 7, 12, 14};
    private static final int[] DICE_CONTAINER_SHOW_SIX = {0, 2, 6, 8, 12, 14};

    @BindView(R.id.ly_dice_total_num)
    LinearLayout lyDiceTotalNum;
    @BindView(R.id.tv_dice_total_num)
    TextView tvDiceTotalNum;

    @BindView(R.id.tv_dice_count)
    TextView tvDiceCount;

    @BindView(R.id.ly_dice_count_choose)
    LinearLayout lyDiceCountChoose;

    @BindView(R.id.fl_all_gif)
    FlowLayout flAllGif;

    private GifDrawable mGifDrawable;
    private ShakeUtils mShakeUtils;
    private int mCurrentDiceCount = 1;

    private DiceAnimationListener mDiceAnimationListener;

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        setUnBinder(ButterKnife.bind(this, view));
        lyDiceTotalNum.setVisibility(View.INVISIBLE);
        addDice();

        mShakeUtils = new ShakeUtils(holder);
        mShakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                startRandom();
            }
        });

        //选择骰子数量监听
        for (int i = 0; i < lyDiceCountChoose.getChildCount(); i++) {
            TextView textView = (TextView) lyDiceCountChoose.getChildAt(i);
            textView.setTag(i);
            textView.setOnClickListener(this);
            textView.setSelected(i == 0);
        }

        AudioPlayUtils.getInstance().setAudioPlayListener(new AudioPlayUtils.OnAudioPlayListener() {
            @Override
            public void audioPlayComplete(AudioPlayEntity audioPlayEntity) {

            }

            @Override
            public void audioPlayStart(AudioPlayEntity audioPlayEntity) {

            }

            @Override
            public void audioPlayError(AudioPlayEntity audioPlayEntity) {

            }

            @Override
            public void audioPlayReset(AudioPlayEntity audioPlayEntity) {

            }
        });
        AudioPlayUtils.getInstance().startPlay(AudioPlayEntity.createLocalResAudio(getResources().openRawResourceFd(R.raw.dice_sound)));
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_dice;
    }


    @Override
    public void onResume() {
        super.onResume();
        mShakeUtils.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGifDrawable.stop();
        if (!mGifDrawable.isRecycled()) {
            mGifDrawable.recycle();
        }
        AudioPlayUtils.getInstance().stopPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShakeUtils.onPause();
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    /**
     * 选择骰子数量
     */
    @OnClick(R.id.ly_dice_count)
    void onChooseDiceCount(View view) {
        showDiceAnimation();
    }


    /**
     * 添加骰子
     */
    private void addDice() {
        RelativeLayout.LayoutParams rpDiceContainer = (RelativeLayout.LayoutParams) flAllGif.getLayoutParams();
        rpDiceContainer.width = DensityUtil.dip(holder, 70) * 3;
        flAllGif.setLayoutParams(rpDiceContainer);
        flAllGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("开始随机数");
                startRandom();
            }
        });
        for (int i = 0; i < 15; i++) {
            View.inflate(holder, R.layout.item_dice, flAllGif);
        }
        showDice();
    }

    /**
     * 显示骰子
     */
    private void showDice() {
        int[] dices;
        switch (mCurrentDiceCount) {
            case 1:
                dices = DICE_CONTAINER_SHOW_ONE;
                break;
            case 2:
                dices = DICE_CONTAINER_SHOW_TWO;
                break;
            case 3:
                dices = DICE_CONTAINER_SHOW_THREE;
                break;
            case 4:
                dices = DICE_CONTAINER_SHOW_FOUR;
                break;
            case 5:
                dices = DICE_CONTAINER_SHOW_FIVE;
                break;
            case 6:
                dices = DICE_CONTAINER_SHOW_SIX;
                break;
            default:
                dices = DICE_CONTAINER_SHOW_ONE;
                break;
        }

        for (int i = 0; i < flAllGif.getChildCount(); i++) {
            RelativeLayout itemDice = (RelativeLayout) flAllGif.getChildAt(i);
            GifImageView gvDice = (GifImageView) itemDice.getChildAt(0);
            ImageView ivDiceValue = (ImageView) itemDice.getChildAt(1);
            gvDice.setVisibility(View.INVISIBLE);
            ivDiceValue.setVisibility(View.INVISIBLE);
            if (i == dices[0]) {
                if (mGifDrawable != null) {
                    mGifDrawable.removeAnimationListener(mDiceAnimationListener);
                } else {
                    mDiceAnimationListener = new DiceAnimationListener();
                }
                mGifDrawable = (GifDrawable) gvDice.getDrawable();
                mGifDrawable.reset();
                mGifDrawable.addAnimationListener(mDiceAnimationListener);
            }

            for (int dice : dices) {
                if (i == dice) {
                    ivDiceValue.setVisibility(View.VISIBLE);
                    ivDiceValue.setImageResource(R.drawable.dice_six);
                    break;
                }
            }
        }
    }

    /**
     * 开始随机
     */
    private void startRandom() {

        lyDiceTotalNum.setVisibility(View.INVISIBLE);

        int[] dices;
        switch (mCurrentDiceCount) {
            case 1:
                dices = DICE_CONTAINER_SHOW_ONE;
                break;
            case 2:
                dices = DICE_CONTAINER_SHOW_TWO;
                break;
            case 3:
                dices = DICE_CONTAINER_SHOW_THREE;
                break;
            case 4:
                dices = DICE_CONTAINER_SHOW_FOUR;
                break;
            case 5:
                dices = DICE_CONTAINER_SHOW_FIVE;
                break;
            case 6:
                dices = DICE_CONTAINER_SHOW_SIX;
                break;
            default:
                dices = DICE_CONTAINER_SHOW_ONE;
                break;
        }
        for (int i = 0; i < dices.length; i++) {
            RelativeLayout itemDice = (RelativeLayout) flAllGif.getChildAt(dices[i]);
            GifImageView gvDice = (GifImageView) itemDice.getChildAt(0);
            ImageView ivDiceValue = (ImageView) itemDice.getChildAt(1);

            ivDiceValue.setVisibility(View.INVISIBLE);
            gvDice.setVisibility(View.VISIBLE);
            ((GifDrawable) gvDice.getDrawable()).reset();
            ((GifDrawable) gvDice.getDrawable()).start();
        }

        AudioPlayUtils.getInstance().startPlay(AudioPlayEntity.createLocalResAudio(getResources().openRawResourceFd(R.raw.dice_sound)));
    }

    /**
     * 点击骰子空白处
     */
    @OnClick(R.id.rl_dice_container)
    void OnClickDiceBlankSpace(View view) {
        if (lyDiceCountChoose.getVisibility() == View.VISIBLE) {
            showDiceAnimation();
        }
    }

    @OnClick(R.id.tv_exit_page)
    void onExitPage(View view) {
        ((ActivityRandom) holder).setExit(true);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        //选择骰子数量监听
        int tag = (int) v.getTag();
        for (int i = 0; i < lyDiceCountChoose.getChildCount(); i++) {
            TextView textView = (TextView) lyDiceCountChoose.getChildAt(i);
            textView.setSelected(i == tag);
        }
        tvDiceCount.setText(String.format("×%d", tag + 1));
        mCurrentDiceCount = tag + 1;
        showDiceAnimation();
        showDice();

    }

    /**
     * 显示选择骰子的动画
     */
    private void showDiceAnimation() {
        int maxLeftMargin = DensityUtil.getScreenWidth(holder) - DensityUtil.dip2px(holder, 30);
        int minLeftMargin = DensityUtil.dip2px(holder, 30);

        final RelativeLayout.LayoutParams rpChooseDice = (RelativeLayout.LayoutParams) lyDiceCountChoose.getLayoutParams();
        final boolean isExpand = rpChooseDice.leftMargin > minLeftMargin || lyDiceCountChoose.getVisibility() == View.GONE;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isExpand ? maxLeftMargin : minLeftMargin
                , isExpand ? minLeftMargin : maxLeftMargin);
        valueAnimator.setDuration(250);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                rpChooseDice.leftMargin = (int) value;
                lyDiceCountChoose.setLayoutParams(rpChooseDice);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if (isExpand) {
                    lyDiceCountChoose.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isExpand) {
                    lyDiceCountChoose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private class DiceAnimationListener implements AnimationListener {

        @Override
        public void onAnimationCompleted(int loopNumber) {

            int[] dices;
            switch (mCurrentDiceCount) {
                case 1:
                    dices = DICE_CONTAINER_SHOW_ONE;
                    break;
                case 2:
                    dices = DICE_CONTAINER_SHOW_TWO;
                    break;
                case 3:
                    dices = DICE_CONTAINER_SHOW_THREE;
                    break;
                case 4:
                    dices = DICE_CONTAINER_SHOW_FOUR;
                    break;
                case 5:
                    dices = DICE_CONTAINER_SHOW_FIVE;
                    break;
                case 6:
                    dices = DICE_CONTAINER_SHOW_SIX;
                    break;
                default:
                    dices = DICE_CONTAINER_SHOW_ONE;
                    break;
            }

            //各个显示骰子的值
            int totalDiceValue = 0;
            for (int i = 0; i < dices.length; i++) {
                View itemDice = flAllGif.getChildAt(dices[i]);
                ImageView ivDiceValue = itemDice.findViewById(R.id.iv_dice_value);
                int random = CaculateUtils.getRandom(6);
                showDiceValue(ivDiceValue, random);
                totalDiceValue += random;
            }

            lyDiceTotalNum.setVisibility(View.VISIBLE);
            tvDiceTotalNum.setText(String.valueOf(totalDiceValue));
        }
    }

    /**
     * 显示骰子的值
     *
     * @param ivDiceValue 骰子值的View
     * @param diceValue   骰子值
     */
    private void showDiceValue(ImageView ivDiceValue, int diceValue) {
        ivDiceValue.setVisibility(View.VISIBLE);
        switch (diceValue) {
            case 1:
                ivDiceValue.setImageResource(R.drawable.dice_one);
                break;
            case 2:
                ivDiceValue.setImageResource(R.drawable.dice_two);
                break;
            case 3:
                ivDiceValue.setImageResource(R.drawable.dice_three);
                break;
            case 4:
                ivDiceValue.setImageResource(R.drawable.dice_four);
                break;
            case 5:
                ivDiceValue.setImageResource(R.drawable.dice_five);
                break;
            case 6:
                ivDiceValue.setImageResource(R.drawable.dice_six);
                break;
            default:
                break;
        }
    }

}
