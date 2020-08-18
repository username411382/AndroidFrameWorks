package com.ruihe.demo.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.ExcelUtils;
import com.ruihe.demo.common.utils.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Function：创建电话簿vcf
 * Author：rui.he
 * Date：2020/8/17 15:41
 */
public class ActivityCreateVcf extends BaseActivity {

    private static final int REQUEST_CODE_PICK_TXT = 102;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_TXT) {
                assert data != null;
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                final String txtPath = list.get(0);
                Log.logLongInfo("handler选择的路径：" + txtPath);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ExcelUtils.readExcel(new File(txtPath));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.logLongInfo("异常信息：" + e.getMessage());
                        }
                    }
                }).start();
                // etLocation.setText(txtContent);
            }
        }
    }


    @Override
    public int getViewId() {
        return R.layout.activity_vcf;
    }

    @Override
    public void onActivityViewCreated() {
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
}
