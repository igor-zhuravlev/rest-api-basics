package com.epam.esm.repository.util;

import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

public final class QueryUtil {
    public static final String LIKE_PERCENT_SIGN = "%";
    public static final String LIKE_UNDERSCORE_SIGN = "_";

    public static final String SPACE_DELIMITER = " ";

    public static final String GIFT_CERTIFICATE_TABLE_PREFIX = "gc.";
    public static final String TAG_TABLE_PREFIX = "t.";

    private static final String ORDER_BY_PREFIX = " ORDER BY ";
    private static final String ORDER_BY_PARAM_DELIMITER = ", ";

    public static String queryWithOrder(String query, Sort sort, String tablePrefix) {
        String orderPostfix = sort.stream()
                .map(order -> tablePrefix + order.getProperty() + SPACE_DELIMITER + order.getDirection())
                .collect(Collectors.joining(ORDER_BY_PARAM_DELIMITER, ORDER_BY_PREFIX, ""));
        return query + orderPostfix;
    }

    public static String anyMatchLikePattern(String value) {
        return LIKE_PERCENT_SIGN + value + LIKE_PERCENT_SIGN;
    }
}
