package com.rainwood.medicalalliance.utils;

import java.util.List;

/**
 * @Author: sxs797
 * @time : 2019/12/9 16:40
 * @Desc: List工具类
 */
public final class ListUtils {

    public static int getSize(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
        return (sourceList != null && !sourceList.contains(entry)) && sourceList.add(entry);
    }

}
