package com.xwl41.common.basic.util;

import java.util.Collection;

class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
