package com.ruihe.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.ruihe.demo.R;
import com.ruihe.demo.common.ActivitiesContainer;
import com.ruihe.demo.common.utils.ExcelUtils;
import com.ruihe.demo.common.utils.Log;
import com.ruihe.demo.common.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Function：创建电话簿vcf
 * Author：rui.he
 * Date：2020/8/17 15:41
 */
public class ActivityCreateVcf extends BaseActivity {

    private static final int REQUEST_CODE_PICK_TXT = 102;

    private TextView tvPickFileName;

    /**
     * excel文件名称
     */
    private String mExcelFilePath;

    /**
     * 是否能退出页面
     */
    private boolean isEnableExit;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_TXT) {
                assert data != null;
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                String txtPath = list.get(0);
                Log.logLongInfo("handler选择的路径：" + txtPath);
                String fileName = txtPath.substring(txtPath.lastIndexOf("/") + 1);
                tvPickFileName.setText(String.format("已选择Excel文件(.xls)名称为:\t\t%s", fileName));
                mExcelFilePath = txtPath;
            }
        }
    }


    @Override
    public int getViewId() {
        return R.layout.activity_vcf;
    }

    @Override
    public void onActivityViewCreated() {
        tvPickFileName = findViewById(R.id.tv_pick_file_name);
        tvPickFileName.setText("");
        requestStorePermission();
    }

    /**
     * 选择Excel文件
     */
    public void pickExcelFile(View view) {
        new LFilePicker().withActivity(this)
                .withRequestCode(REQUEST_CODE_PICK_TXT)
                .withStartPath("/storage/emulated/0/Download")
                .withMutilyMode(false)
                .withFileFilter(new String[]{".xls"}).start();
    }

    /**
     * 点击转换为vcf
     */
    public void buildVcf(View view) {
        if (TextUtils.isEmpty(mExcelFilePath)) {
            ToastUtil.show("请检查是否选择Excel文件(.xls)或格式是否正确");
            return;
        }
        try {
            ExcelUtils.readExcel(new File(mExcelFilePath), new ExcelUtils.FileResultListener() {
                @Override
                public void onCreateSuccess(String filePath) {
                    String vcfName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    ToastUtil.show(String.format("生成%s电话簿成功!", vcfName));
                    File file = new File(filePath);
                    File parentFlie = new File(file.getParent());

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        Uri contentUri = FileProvider.getUriForFile(holder, "com.ruihe.demo.fileProvider", file);
                       /* //拍照结果输出到这个uri对应的file中
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        //对这个uri进行授权
                        //mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //对这个uri进行授权
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/
                        intent.setDataAndType(contentUri, "*/*");
                    } else {
                        intent.setDataAndType(Uri.fromFile(file), "*/*");
                    }
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivity(intent);
                }

                @Override
                public void onCreateFail() {
                    ToastUtil.show("生成失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mExcelFilePath = null;
            ToastUtil.show("选择的文件格式有误");
            Log.logLongInfo("生成文件异常信息：" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (!isEnableExit) {
            ActivitiesContainer.getInstance().finishAllActivities();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 退出
     */
    public void exit(View view) {
        isEnableExit = true;
    }
}
