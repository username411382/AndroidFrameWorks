package com.ruihe.demo.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.net.VolleyUtils;
import com.ruihe.demo.common.utils.view.RoundedImageView;

/**
 * 获得初始化后的各种对话框的工具类
 */
public class DialogUtils {

    /**
     * 返回一个初始化好的不带标题的确认对话框（有确认按钮和取消按钮）
     */
    public static Dialog createConfirmDialog(Context context, String content, OnDialogButtonClickListener listener) {
        return createConfirmDialogWithTitle(context, null, content, listener);
    }

    /**
     * 返回一个初始化好的有标题的确认对话框（有确认按钮和取消按钮）
     */
    public static Dialog createConfirmDialogWithTitle(Context context, String title, String content,
                                                      OnDialogButtonClickListener listener) {
        return createConfirmDialogWithTitle(context, title, content, null, null, listener);

    }

    /**
     * 返回一个初始化好的有标题的确认对话框（有确认按钮和取消按钮）
     */
    public static Dialog createConfirmDialogWithTitle(Context context, String title, String content,
                                                      String cancelText, String confirmText, OnDialogButtonClickListener listener) {

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View view = View.inflate(context, R.layout.dialog_confirm, null);
        View dialogView = view.findViewById(R.id.lay_dialog);
        LayoutParams lp = (LayoutParams) dialogView.getLayoutParams();
        lp.width = (int) (DensityUtil.getScreenWidth(context) * 0.8);
        dialogView.setLayoutParams(lp);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        if (title == null || title.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
        tvContent.setText(content);
        if (!TextUtils.isEmpty(cancelText)) {
            ((TextView) view.findViewById(R.id.tv_dialog_cancel)).setText(cancelText);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            ((TextView) view.findViewById(R.id.tv_dialog_confirm)).setText(confirmText);
        }
        OnButtonClickListener buttonListener = new OnButtonClickListener(dialog, listener);
        view.findViewById(R.id.tv_dialog_confirm).setOnClickListener(buttonListener);
        view.findViewById(R.id.tv_dialog_cancel).setOnClickListener(buttonListener);
        dialog.setContentView(view);
        return dialog;

    }

    /**
     * 返回一个初始化好的不带标题的提示对话框(只有确认按钮)，listener中，只有onConfirm有效
     */
    public static Dialog createAlertDialog(Context context, String content, OnDialogButtonClickListener listener) {
        return createAlertDialogWithTitle(context, null, content, listener);
    }

    /**
     * 返回一个初始化好的带标题的提示对话框(只有确认按钮)，listener中，只有onConfirm有效
     */
    public static Dialog createAlertDialogWithTitle(Context context, String title, String content,
                                                    OnDialogButtonClickListener listener) {

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View view = View.inflate(context, R.layout.dialog_alert, null);
        View dialogView = view.findViewById(R.id.lay_dialog);
        LayoutParams lp = (LayoutParams) dialogView.getLayoutParams();
        lp.width = (int) (DensityUtil.getScreenWidth(context) * 0.8);
        dialogView.setLayoutParams(lp);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        if (title == null || title.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
        tvContent.setText(content);

        OnButtonClickListener buttonListener = new OnButtonClickListener(dialog, listener);
        view.findViewById(R.id.tv_dialog_confirm).setOnClickListener(buttonListener);
        dialog.setContentView(view);
        return dialog;

    }

    /**
     * 返回一个编辑单行的对话框
     */
    public static Dialog createEditDialogWithTitle(Context context, String title, String description, String hint,
                                                   String content, int etLength, final OnDialogButtonClickListener listener) {

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View view = View.inflate(context, R.layout.dialog_edit_one, null);
        View dialogView = view.findViewById(R.id.lay_dialog);
        LayoutParams lp = (LayoutParams) dialogView.getLayoutParams();
        lp.width = (int) (DensityUtil.getScreenWidth(context) * 0.85);
        dialogView.setLayoutParams(lp);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        TextView tvDescription = (TextView) view.findViewById(R.id.tv_dialog_description);
        if (TextUtils.isEmpty(description)) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        }

        EditText et = (EditText) view.findViewById(R.id.et_dialog);
        et.setHint(hint);
        et.setText(content);
        if (etLength > 0) {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etLength)});
        }

        OnButtonClickListener buttonListener = new OnButtonClickListener(dialog, listener);
        view.findViewById(R.id.tv_dialog_confirm).setOnClickListener(buttonListener);
        view.findViewById(R.id.tv_dialog_cancel).setOnClickListener(buttonListener);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        dialog.setContentView(view);
        return dialog;

    }

    /**
     * 返回一个初始化好的等待对话框
     */
    public static Dialog createWaitProgressDialog(Context context, OnDismissListener listener) {

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View view = View.inflate(context, R.layout.dialog_progress_wait, null);
        dialog.setOnDismissListener(listener);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }

    /**
     * 返回一个初始化好的进度对话框,修改进度请获取
     * R.id.cpv_loading得到进度条
     * R.id.tv_dialog_progress得到进度文字
     */
    public static Dialog createProgressDialog(Context context, String title, OnDismissListener listener) {

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View view = View.inflate(context, R.layout.dialog_progress, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        dialog.setOnDismissListener(listener);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }


    /*创建带图片、标题、内容的可确认或取消的对话框*/
    public static Dialog createConfirmDialogHasImageTitle(Context context, String imgUrl, int imgResource, String textTitle, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Dialog dialog = new Dialog(context, R.style.CustomDialog);

        View dialogView = View.inflate(context, R.layout.dialog_confirm_with_img_title, null);

        RoundedImageView ivRoundedTitle = (RoundedImageView) dialogView.findViewById(R.id.iv_round_title);//圆形标题
        LinearLayout lyTitle = (LinearLayout) dialogView.findViewById(R.id.ly_title);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_text_title);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.tv_content);

        Button btCancel = (Button) dialogView.findViewById(R.id.tv_dialog_cancel);
        final Button btConfirm = (Button) dialogView.findViewById(R.id.tv_dialog_confirm);

        View allView = dialogView.findViewById(R.id.lay_dialog);
        LayoutParams lp = (LayoutParams) allView.getLayoutParams();
        lp.width = (int) (DensityUtil.getScreenWidth(context) * 0.8);
        allView.setLayoutParams(lp);

        //无图片
        if (TextUtils.isEmpty(imgUrl) && imgResource == 0) {
            ivRoundedTitle.setVisibility(View.GONE);
        } else { //有图片
            if (imgResource != 0) {
                ivRoundedTitle.setImageResource(imgResource);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                VolleyUtils.loadImage(requestQueue, imgUrl, ivRoundedTitle, 0);
            }
        }

        //无标题
        if (TextUtils.isEmpty(textTitle)) {
            lyTitle.setVisibility(View.GONE);
        }

        tvTitle.setText(textTitle);
        tvContent.setText(textContent);
        btCancel.setText(cancelText);
        btConfirm.setText(confirmText);

        changeButtonBold(btCancel);
        changeButtonBold(btConfirm);

        OnButtonClickListener btListener = new OnButtonClickListener(dialog, dialogListener);
        btCancel.setOnClickListener(btListener);
        btConfirm.setOnClickListener(btListener);

        dialog.setContentView(dialogView);
        return dialog;
    }


    /**
     * 创建加载本地图片资源的对话框
     */
    public static Dialog createConfirmDialogHasLocalImage(Context context, int imgResource, String textTitle, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        return createConfirmDialogHasImageTitle(context, null, imgResource, textTitle, textContent, cancelText, confirmText, dialogListener);
    }

    /**
     * 创建加载Url图片的对话框
     */
    public static Dialog createConfirmDialogHasServersImage(Context context, String imgUrl, String textTitle, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        return createConfirmDialogHasImageTitle(context, imgUrl, 0, textTitle, textContent, cancelText, confirmText, dialogListener);
    }

    /*创建无图片的对话框*/
    public static Dialog createConfirmDialogNoImage(Context context, String textTitle, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        return createConfirmDialogHasImageTitle(context, null, 0, textTitle, textContent, cancelText, confirmText, dialogListener);
    }

    /* 创建有url图片无标题的对话框*/
    public static Dialog createConfirmDialogHasServersImageNoTitle(Context context, String imgUrl, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        return createConfirmDialogHasImageTitle(context, imgUrl, 0, null, textContent, cancelText, confirmText, dialogListener);
    }

    /* 创建有本地图片无标题的对话框*/
    public static Dialog createConfirmDialogHasLocalImageNoTitle(Context context, int imgResource, String textContent, String cancelText, String confirmText, OnDialogButtonClickListener dialogListener) {
        return createConfirmDialogHasImageTitle(context, null, imgResource, null, textContent, cancelText, confirmText, dialogListener);
    }


    /**
     * 对话框按钮点击事件接口
     *
     * @author Yang Huaichuan 2015年8月28日
     */
    public interface OnDialogButtonClickListener {
        void onConfirm();

        void onCancel();
    }

    public static void changeButtonBold(final Button btn) {
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    btn.getPaint().setFakeBoldText(true);

                } else {
                    btn.getPaint().setFakeBoldText(false);
                }
                return false;
            }
        });
    }

}

class OnButtonClickListener implements OnClickListener {

    private Dialog mDialog;
    private DialogUtils.OnDialogButtonClickListener mListener;

    public OnButtonClickListener(Dialog dialog, DialogUtils.OnDialogButtonClickListener listener) {
        mDialog = dialog;
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_confirm:
                if (mListener != null) {
                    mListener.onConfirm();
                }
                mDialog.dismiss();
                break;
            case R.id.tv_dialog_cancel:
                if (mListener != null) {
                    mListener.onCancel();
                }
                mDialog.dismiss();
                break;

            default:
                break;
        }

    }


}
