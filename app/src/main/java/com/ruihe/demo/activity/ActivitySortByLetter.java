package com.ruihe.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.adapter.AdapterSort;
import com.ruihe.demo.bean.SortModel;
import com.ruihe.demo.common.utils.CharacterParserUtil;
import com.ruihe.demo.common.utils.ContactUtil;
import com.ruihe.demo.common.utils.PinyinComparatorUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.view.ClearEditText;
import com.ruihe.demo.common.utils.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 描述：按字母排序的界面
 * Created by ruihe on 2016/6/17.
 */
public class ActivitySortByLetter extends BaseActivity implements AdapterView.OnItemClickListener, View.OnFocusChangeListener, TextWatcher {


    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;


    private Activity mActivity;
    private AdapterSort adapter;
    private ClearEditText mClearEditText;
    private Map<String, String> callRecords;

    private CharacterParserUtil characterParser;
    private List<SortModel> SourceDateList;

    private PinyinComparatorUtil pinyinComparator;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            callRecords = ContactUtil.getAllCallRecords(mActivity);
            List<String> contact = new ArrayList<>();
            for (Iterator<String> keys = callRecords.keySet().iterator(); keys
                    .hasNext(); ) {
                String key = keys.next();
                contact.add(key);
            }
            String[] names = new String[]{};
            names = contact.toArray(names);
            SourceDateList = filledData(names);

            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new AdapterSort(mActivity, SourceDateList);
            sortListView.setAdapter(adapter);
        }
    };


    @Override
    public int getViewId() {
        return R.layout.activity_sort_by_letter;
    }

    @Override
    public void onActivityViewCreated() {

        initVariable();
        initView();
        initListener();
        bindData();

    }

    private void initVariable() {
        mActivity = this;
        // 实例化汉字转拼音类
        characterParser = CharacterParserUtil.getInstance();
        pinyinComparator = new PinyinComparatorUtil();
    }

    private void initView() {
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sortListView = (ListView) findViewById(R.id.sortlist);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
    }

    private void initListener() {
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
        sortListView.setOnItemClickListener(this);

        mClearEditText.setOnFocusChangeListener(this);

        mClearEditText.addTextChangedListener(this);  // 根据输入框输入值的改变来过滤搜索

    }

    private void bindData() {
        mTitleView.setTitle("联系人");
        sideBar.setTextView(dialog);

        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String number = callRecords.get(((SortModel) adapter
                .getItem(position)).getName());
        ToastUtil.show(number);
    }
}
