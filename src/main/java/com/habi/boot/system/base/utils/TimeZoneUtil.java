package com.habi.boot.system.base.utils;
import java.util.TimeZone;

public final class TimeZoneUtil {
    private static ThreadLocal<TimeZone> local = new ThreadLocal();

    private TimeZoneUtil() {
    }

    public static TimeZone getTimeZone() {
        TimeZone timeZone = (TimeZone)local.get();
        return timeZone != null ? timeZone : TimeZone.getDefault();
    }

    public static void setTimeZone(TimeZone timeZone) {
        local.set(timeZone);
    }

    public static String toGMTFormat(TimeZone timeZone) {
        long of = (long)timeZone.getRawOffset();
        if (of == 0L) {
            return "GMT";
        } else if (of > 0L) {
            return String.format("GMT+%02d%02d", of / 3600000L, of / 60000L % 60L);
        } else {
            of = Math.abs(of);
            return String.format("GMT-%02d%02d", of / 3600000L, of / 60000L % 60L);
        }
    }
}
