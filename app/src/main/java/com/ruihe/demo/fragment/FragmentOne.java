package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;

/**
 * 描述：首页一
 * Created by ruihe on 2016/4/28.
 */
public class FragmentOne extends BaseFragment {

    private int[] mArray = {5, 3, 8, 6, 4, 18, 7, 2, 1, 10};
    private String[] mSortTypes = {"冒泡排序", "选择排序", "插入排序", "快速排序", "堆排序", "希尔排序", "归并排序", "计数排序", "桶排序"};
    private StringBuffer strBuffer = new StringBuffer();

    private TextView tvSortBefore;
    private TextView tvSortAfter;
    private TextView tvSortIntroduce;
    private LinearLayout lySortType;
    private OnSortItemListener mOnSortItemListener = new OnSortItemListener();


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {


        tvSortIntroduce = view.findViewById(R.id.tv_sort_introduce);
        tvSortBefore = view.findViewById(R.id.tv_sort_before);
        tvSortAfter = view.findViewById(R.id.tv_sort_after);
        lySortType = view.findViewById(R.id.ly_sort_type);

        holder.mTitleView.addRightText(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSortAfter.setText("排序后:");
            }
        }, "清空");
        bindData();

    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_first);
        StringBuffer buffer = new StringBuffer();
        buffer.append("排序前:");
        for (int j = 0; j < mArray.length; j++) {
            buffer.append(mArray[j]);
            buffer.append(",");
        }
        tvSortBefore.setText(buffer);
        lySortType.removeAllViewsInLayout();
        for (int i = 0; i < mSortTypes.length; i++) {
            View item = View.inflate(holder, R.layout.item_sort_view, null);
            TextView tvSortName = item.findViewById(R.id.tv_sort_name);
            tvSortName.setText(mSortTypes[i]);
            tvSortName.setTag(i);
            tvSortName.setOnClickListener(mOnSortItemListener);
            lySortType.addView(item);
        }
    }


    //冒泡排序
    private void bubbleSort(int[] arrays) {

        if (arrays == null || arrays.length == 0) {
            return;
        }
        for (int i = 0; i < arrays.length; i++) {
            for (int j = arrays.length - 1; j > i; j--) {
                if (arrays[j - 1] > arrays[j]) {
                    swap(arrays, j - 1, j);
                }
            }
        }
        if (strBuffer.length() > 0)
            strBuffer.delete(0, strBuffer.length());

        strBuffer.append("冒泡排序后:");
        for (int a : arrays) {
            strBuffer.append(a);
            strBuffer.append(",");
        }
        tvSortAfter.setText(strBuffer);
    }

    //选择排序
    private void selectSort(int[] arrays) {

        int minIndex;
        if (arrays == null || arrays.length == 0) {
            return;
        }
        for (int i = 0; i < arrays.length; i++) {
            minIndex = i;
            for (int j = i + 1; j < arrays.length; j++) {
                if (arrays[j] < arrays[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                swap(arrays, i, minIndex);
            }
        }

        if (strBuffer.length() > 0)
            strBuffer.delete(0, strBuffer.length());

        strBuffer.append("选择排序后:");
        for (int array : arrays) {
            strBuffer.append(array);
            strBuffer.append(",");
        }
        tvSortAfter.setText(strBuffer);
    }

    //插入排序
    private void insertSort(int[] arrays) {


        if (arrays == null || arrays.length == 0) {
            return;
        }
        for (int i = 1; i < arrays.length; i++) {

            int j = i;
            int target = arrays[i];

            while (j > 0 && target < arrays[j - 1]) {
                arrays[j] = arrays[j - 1];
                j--;
            }
            arrays[j] = target;
        }

        if (strBuffer.length() > 0)
            strBuffer.delete(0, strBuffer.length());
        strBuffer.append("插入排序后:");
        for (int array : arrays) {
            strBuffer.append(array);
            strBuffer.append(",");
        }
        tvSortAfter.setText(strBuffer);
    }

    //快速排序
    private void quickSort(int[] arrays) {

        if (arrays == null || arrays.length == 0) {
            return;
        }
        sort(arrays, 0, arrays.length - 1);

        if (strBuffer.length() > 0)
            strBuffer.delete(0, strBuffer.length());
        strBuffer.append("快速排序后:");
        for (int a : arrays) {
            strBuffer.append(a);
            strBuffer.append(",");
        }
        tvSortAfter.setText(strBuffer);
    }


    //快速排序划分
    private int divideArray(int[] array, int left, int right) {


        int keyPosition = left;
        int keyValue = array[left];

        while (left < right) {

            while (left < right && array[right] >= keyValue) {
                right--;
            }
            // array[left] = array[right];
            while (left < right && array[left] <= keyValue) {
                left++;
            }
            // array[right] = array[left];
            swap(array, left, right);
        }

        // array[left] = keyValue;

        swap(array, keyPosition, left);

        return left;

    }


    private void sort(int[] array, int left, int right) {

        if (left >= right) {
            return;
        }

        int meetingLocation = divideArray(array, left, right);

        sort(array, 0, meetingLocation - 1);
        sort(array, meetingLocation + 1, right);

    }


    private static void swap(int[] arrays, int i, int j) {
        int temp = arrays[i];
        arrays[i] = arrays[j];
        arrays[j] = temp;
    }


    private class OnSortItemListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int selectedPosition = (int) v.getTag();

            switch (selectedPosition) {
                case 0:
                    tvSortIntroduce.setText("冒泡排序是最简单的排序之一了，其大体思想就是通过与相邻元素的比较和交换来把小的数交换到最前面。这个过程类似于水泡向上升一样，因此而得名。举个栗子，对5,3,8,6,4这个无序序列进行冒泡排序。首先从后向前冒泡，4和6比较，把4交换到前面，序列变成5,3,8,4,6。同理4和8交换，变成5,3,4,8,6,3和4无需交换。5和3交换，变成3,5,4,8,6,3.这样一次冒泡就完了，把最小的数3排到最前面了。对剩下的序列依次冒泡就会得到一个有序序列。冒泡排序的时间复杂度为O(n^2)。");
                    bubbleSort(mArray);
                    break;
                case 1:
                    tvSortIntroduce.setText("选择排序的思想其实和冒泡排序有点类似，都是在一次排序后把最小的元素放到最前面。但是过程不同，冒泡排序是通过相邻的比较和交换。而选择排序是通过对整体的选择。举个栗子，对5,3,8,6,4这个无序序列进行简单选择排序，首先要选择5以外的最小数来和5交换，也就是选择3和5交换，一次排序后就变成了3,5,8,6,4.对剩下的序列一次进行选择和交换，最终就会得到一个有序序列。其实选择排序可以看成冒泡排序的优化，因为其目的相同，只是选择排序只有在确定了最小数的前提下才进行交换，大大减少了交换的次数。选择排序的时间复杂度为O(n^2)。");
                    selectSort(mArray);
                    break;
                case 2:
                    tvSortIntroduce.setText("插入排序不是通过交换位置而是通过比较找到合适的位置插入元素来达到排序的目的的。相信大家都有过打扑克牌的经历，特别是牌数较大的。在分牌时可能要整理自己的牌，牌多的时候怎么整理呢？就是拿到一张牌，找到一个合适的位置插入。这个原理其实和插入排序是一样的。举个栗子，对5,3,8,6,4这个无序序列进行简单插入排序，首先假设第一个数的位置时正确的，想一下在拿到第一张牌的时候，没必要整理。然后3要插到5前面，把5后移一位，变成3,5,8,6,4.想一下整理牌的时候应该也是这样吧。然后8不用动，6插在8前面，8后移一位，4插在5前面，从5开始都向后移一位。注意在插入一个数的时候要保证这个数前面的数已经有序。简单插入排序的时间复杂度也是O(n^2)。");
                    insertSort(mArray);
                    break;
                case 3:
                    tvSortIntroduce.setText("快速排序一听名字就觉得很高端，在实际应用当中快速排序确实也是表现最好的排序算法。快速排序虽然高端，但其实其思想是来自冒泡排序，冒泡排序是通过相邻元素的比较和交换把最小的冒泡到最顶端，而快速排序是比较和交换小数和大数，这样一来不仅把小数冒泡到上面同时也把大数沉到下面。");
                    quickSort(mArray);

                    break;
                default:
                    break;
            }

        }
    }


}
