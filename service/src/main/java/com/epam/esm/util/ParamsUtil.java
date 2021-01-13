package com.epam.esm.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ParamsUtil {
    public static final String TAG_PARAM = "tag";
    public static final String PART_PARAM = "part";
    public static final String SORT_PARAM = "sort";

    private static final String SORT_ITEM_PARAM_DELIMITER = ",";

    public static boolean hasParams(String param, Map<String, String[]> params) {
        return params.get(param) != null;
    }

    public static String getTagParam(Map<String, String[]> params) {
        return params.get(TAG_PARAM)[0];
    }

    public static String getPartParam(Map<String, String[]> params) {
        return params.get(PART_PARAM)[0];
    }

    public static String[] getSortParams(Map<String, String[]> params) {
        return params.get(SORT_PARAM);
    }

    public static Sort buildSortByParams(String[] sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sort : sorts) {
            String[] sortItem = sort.strip().split(SORT_ITEM_PARAM_DELIMITER);
            if (sortItem[1].strip().equalsIgnoreCase(Sort.Direction.ASC.name())) {
                orders.add(Sort.Order.asc(sortItem[0]));
            } else {
                orders.add(Sort.Order.desc(sortItem[0]));
            }
        }
        return Sort.by(orders);
    }
}
