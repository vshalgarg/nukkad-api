package com.code.monks.nukkad.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Kolkata");

    // Convert UTC to target timezone
    public static LocalDateTime utcToLocal(LocalDateTime utcTime) {
        if (utcTime == null) return null;
        ZonedDateTime utcZoned = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(DEFAULT_ZONE);
        return localZoned.toLocalDateTime();
    }
}
