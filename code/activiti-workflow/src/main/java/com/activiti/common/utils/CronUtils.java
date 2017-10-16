package com.activiti.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转化为cron表达式
 * Created by 12490 on 2017/8/6.
 */
public class CronUtils {

    private static String formatDateByPattern(Date date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 调用方法
     * @param date
     * @return
     */
    public static String getCron(Date  date){
        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }
}
