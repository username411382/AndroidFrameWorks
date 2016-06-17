package com.ruihe.demo.common.utils;

import com.ruihe.demo.bean.SortModel;

import java.util.Comparator;


/**
 * 拼音的比较器
 */
public class PinyinComparatorUtil implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
