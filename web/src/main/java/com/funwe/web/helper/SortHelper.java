package com.funwe.web.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/28 15:16
 */
public class SortHelper {

    public static Sort getSort(String sorter, String defaultSort){
        Sort sort = Sort.by(Sort.Direction.ASC, defaultSort);
        String defaultValue = "{}";
        String splitValue = "_";
        String asc = "ascend";
        String dsc = "descend";
        if (StringUtils.isNotEmpty(sorter) && !defaultValue.equals(sorter)){
            String[] splitSorter = sorter.split(splitValue);
            if (asc.equals(splitSorter[1])){
                sort = Sort.by(Sort.Direction.ASC, splitSorter[0]);
            }else if (dsc.equals(splitSorter[1])){
                sort = Sort.by(Sort.Direction.DESC, splitSorter[0]);
            }
        }
        return sort;
    }
}
