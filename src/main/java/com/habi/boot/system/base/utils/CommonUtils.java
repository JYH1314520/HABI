package com.habi.boot.system.base.utils;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class CommonUtils {
    public CommonUtils() {
    }

    public static <E> void foreach(Collection<E> collection, Consumer<E> closure) {
        if (collection != null && closure != null) {
            Iterator itr = collection.iterator();

            while(itr.hasNext()) {
                closure.accept((E) itr.next());
                itr.hasNext();
            }
        }

    }

    public static int indexOf(Object e, Object... arr) {
        int i;
        if (e == null) {
            for(i = 0; i < arr.length; ++i) {
                if (arr[i] == null) {
                    return i;
                }
            }
        } else {
            for(i = 0; i < arr.length; ++i) {
                if (e.equals(arr[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static boolean in(Object e, Object... arr) {
        return indexOf(e, arr) != -1;
    }

    public static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static <T> T nvl(T... args) {
        for(int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                return args[i];
            }
        }

        return null;
    }
}


