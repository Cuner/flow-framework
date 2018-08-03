package org.cuner.flowframework.support;

import java.text.SimpleDateFormat;

public class ThreadLocalDateFormat extends SimpleDateFormat {
    private static final ThreadLocal<ThreadLocalDateFormat> localDateFormat = ThreadLocal.withInitial(() -> new ThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss,SSS"));

    ThreadLocalDateFormat(String pattern) {
        super(pattern);
    }

    public static ThreadLocalDateFormat current() {
        return localDateFormat.get();
    }
}
