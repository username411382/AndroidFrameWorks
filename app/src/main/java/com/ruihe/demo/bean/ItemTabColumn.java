package com.ruihe.demo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：表格的单条
 * Created by ruihe on 2016/5/4.
 */
public class ItemTabColumn {

    public String packageName;
    public int packageNameCount;
    public String foodName;
    public String foodPrice;
    public String foodCount;
    public String foodTotal;

    public static String[] packageNames = {"主食4选1（不可重复选）", "干锅类5选1（不可重复选）", "特色", "其他"};
    public static int[] packageNamesCount = {4, 5, 2, 6};

    public static String[] foodNames = {"老酸菜一把骨（大锅）", "萝卜一把骨", "海带一把骨（大锅）", "番茄一把骨（大锅）", "炭火糯香鸭掌", "炭火糯香猪蹄", "干锅美蛙", "炭火糯香鸡爪", "炭火贡鱼", "杏鲍菇炒肉", "干捞粉丝", "萝卜一把骨", "海带一把骨（大锅）", "番茄一把骨（大锅）", "炭火糯香鸭掌", "炭火糯香猪蹄", "干锅美蛙"};
    public static String[] foodPrices = {"¥108", "¥108", "¥108", "¥108", "¥68", "¥68", "¥68", "¥68", "¥68", "¥38", "¥22", "¥108", "¥108", "¥108", "¥68", "¥68", "¥68"};

    public static List<ItemTabColumn> getTabFood() {

        List<ItemTabColumn> listData = new ArrayList<>();

        for (int i = 0; i < foodNames.length; i++) {
            ItemTabColumn item = new ItemTabColumn();
            item.foodName = foodNames[i];
            item.foodPrice = foodPrices[i];
            item.foodCount = "1份";
            item.foodTotal = foodPrices[i];
            listData.add(item);
        }
        return listData;
    }

    public static List<ItemTabColumn> getTabPackage() {
        List<ItemTabColumn> listData = new ArrayList<>();
        for (int i = 0; i < packageNames.length; i++) {
            ItemTabColumn itemTabColumn = new ItemTabColumn();
            itemTabColumn.packageName = packageNames[i];
            itemTabColumn.packageNameCount = packageNamesCount[i];
            listData.add(itemTabColumn);
        }
        return listData;
    }


}
