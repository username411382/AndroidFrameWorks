package com.ruihe.demo.common.utils;

import java.util.Random;

/**
 * 计算工具类
 * Created by RH on 2018/9/14
 */
public class CaculateUtils {

    /**
     * 获取随机数
     *
     * @param maxRandom 最大随机值
     */
    public static int getRandom(int maxRandom) {
        return new Random().nextInt(maxRandom) + 1;
    }
}
