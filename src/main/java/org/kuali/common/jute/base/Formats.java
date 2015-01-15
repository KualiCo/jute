/**
 * Copyright 2010-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.common.jute.base;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.endsWithIgnoreCase;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.kuali.common.jute.base.Exceptions.illegalArgument;
import static org.kuali.common.jute.base.Precondition.checkEquals;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;

/**
 * Format time, bytes, counts, dates, and transfer rates into human friendly form
 *
 * @author Jeff Caddel
 * @since May 27, 2010 6:46:17 PM
 */
public class Formats {

    public static final double SECOND = 1000;
    public static final double MINUTE = 60 * SECOND;
    public static final double HOUR = 60 * MINUTE;
    public static final double DAY = 24 * HOUR;
    public static final double YEAR = 365 * DAY;

    /**
     * This is the format produced by the {@code toString()} method of {@code java.util.Date} instance
     */
    public static final String JAVA_UTIL_DATE_TO_STRING_FORMAT = "EEE MMM d HH:mm:ss zzz y";
    private static final SimpleDateFormat JAVA_UTIL_DATE_TO_STRING_FORMATTER = new SimpleDateFormat(JAVA_UTIL_DATE_TO_STRING_FORMAT);

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    private static final List<String> TIME_TOKENS = Arrays.asList("ms", "s", "m", "h", "d", "y");
    private static final List<Long> TIME_MULTIPLIERS = getTimeMultipliers();

    private static final List<String> SIZE_TOKENS = Arrays.asList("b", "k", "m", "g", "t", "p", "e");
    private static final int BASE = 1024;

    private static NumberFormat largeSizeFormatter = NumberFormat.getInstance();
    private static NumberFormat sizeFormatter = NumberFormat.getInstance();
    private static NumberFormat timeFormatter = NumberFormat.getInstance();
    private static NumberFormat rateFormatter = NumberFormat.getInstance();
    private static NumberFormat countFormatter = NumberFormat.getInstance();
    private static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
    private static NumberFormat integerFormatter = NumberFormat.getInstance();

    static {
        integerFormatter.setGroupingUsed(false);
        integerFormatter.setMaximumFractionDigits(0);
        integerFormatter.setMinimumFractionDigits(0);
        sizeFormatter.setGroupingUsed(false);
        sizeFormatter.setMaximumFractionDigits(1);
        sizeFormatter.setMinimumFractionDigits(1);
        largeSizeFormatter.setGroupingUsed(false);
        largeSizeFormatter.setMaximumFractionDigits(3);
        largeSizeFormatter.setMinimumFractionDigits(3);
        timeFormatter.setGroupingUsed(false);
        timeFormatter.setMaximumFractionDigits(3);
        timeFormatter.setMinimumFractionDigits(3);
        rateFormatter.setGroupingUsed(false);
        rateFormatter.setMaximumFractionDigits(3);
        rateFormatter.setMinimumFractionDigits(3);
        countFormatter.setGroupingUsed(true);
        countFormatter.setMaximumFractionDigits(0);
        countFormatter.setMinimumFractionDigits(0);
    }

    public static String getCurrency(double number) {
        return currencyFormatter.format(number);
    }

    /**
     * Parse bytes from a size string that ends with a unit of measure. If no unit of measure is provided, bytes is assumed. Unit of measure is case insensitive.
     *
     * <pre>
     *   1  == 1 byte
     *   1b == 1 byte
     *   1k == 1 kilobyte == 1024   bytes ==                     1,024 bytes
     *   1m == 1 megabyte == 1024^2 bytes ==                 1,048,576 bytes
     *   1g == 1 gigabyte == 1024^3 bytes ==             1,073,741,824 bytes
     *   1t == 1 terabyte == 1024^4 bytes ==         1,099,511,627,776 bytes
     *   1p == 1 petabyte == 1024^5 bytes ==     1,125,899,906,842,624 bytes
     *   1e == 1 exabyte  == 1024^6 bytes == 1,152,921,504,606,846,976 bytes
     * </pre>
     */
    public static long getBytes(String size) {
        return getBytes(size, SIZE_TOKENS, BASE);
    }

    public static long getBytes(String size, List<String> tokens, int base) {
        checkNotBlank(size, "size");
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            long multiplier = (long) Math.pow(base, i);
            if (endsWithIgnoreCase(size, token)) {
                return getByteValue(size, token, multiplier);
            }
        }
        // Assume bytes
        return getByteValue(size, "", 1);
    }

    protected static long getByteValue(String time, String suffix, long multiplier) {
        int len = length(time);
        String substring = substring(time, 0, len - suffix.length());
        Double value = new Double(substring);
        value = value * multiplier;
        return value.longValue();
    }

    /**
     * Parse milliseconds from a time string that ends with a unit of measure. If no unit of measure is provided, milliseconds is assumed. Unit of measure is case insensitive.
     *
     * <pre>
     *   1   == 1 millisecond
     *   1ms == 1 millisecond
     *   1s  == 1 second ==           1000 milliseconds
     *   1m  == 1 minute ==         60,000 milliseconds
     *   1h  == 1 hour   ==      3,600,000 milliseconds
     *   1d  == 1 day    ==     86,400,000 milliseconds
     *   1y  == 1 year   == 31,536,000,000 milliseconds
     * </pre>
     */
    public static long getMillis(String time) {
        return getMillis(time, TIME_TOKENS, TIME_MULTIPLIERS);
    }

    /**
     * Parse milliseconds from a time string that ends with a unit of measure. If no unit of measure is provided, milliseconds is assumed. Unit of measure is case insensitive.
     *
     * <pre>
     *   1   == 1 millisecond
     *   1ms == 1 millisecond
     *   1s  == 1 second ==           1000 milliseconds
     *   1m  == 1 minute ==         60,000 milliseconds
     *   1h  == 1 hour   ==      3,600,000 milliseconds
     *   1d  == 1 day    ==     86,400,000 milliseconds
     *   1y  == 1 year   == 31,536,000,000 milliseconds
     * </pre>
     */
    public static int getMillisAsInt(String time) {
        Long millis = getMillis(time);
        if (millis <= Integer.MAX_VALUE) {
            return millis.intValue();
        } else {
            throw illegalArgument("[%s] converts to [%s]. maximum allowable integer value is [%s]", time, millis, Integer.MAX_VALUE);
        }
    }

    public static long getMillis(String time, List<String> tokens, List<Long> multipliers) {
        checkNotBlank(time, "time");
        checkEquals(tokens.size(), multipliers.size(), "tokens");
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            long multiplier = multipliers.get(i);
            if (endsWithIgnoreCase(time, token)) {
                return getTimeValue(time, token, multiplier);
            }
        }
        // Assume milliseconds
        return getTimeValue(time, "", 1);
    }

    protected static long getTimeValue(String time, String suffix, long multiplier) {
        int len = StringUtils.length(time);
        String substring = StringUtils.substring(time, 0, len - suffix.length());
        Double value = new Double(substring);
        value = value * multiplier;
        return value.longValue();
    }

    /**
     * Parse a date from the string. The string must be in the same format returned by the getDate() methods
     */
    public static Date parseDate(String date) {
        try {
            synchronized (DATE_FORMATTER) {
                return DATE_FORMATTER.parse(date);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse [" + date + "]", e);
        }
    }

    /**
     * Return a formatted date
     */
    public static String dateAsString(long millis) {
        return dateAsString(new Date(millis));
    }

    /**
     * Return a formatted date
     */
    public static String dateAsString(Date date) {
        synchronized (JAVA_UTIL_DATE_TO_STRING_FORMATTER) {
            return JAVA_UTIL_DATE_TO_STRING_FORMATTER.format(date);
        }
    }

    /**
     * Return a formatted date
     */
    public static String dateAsString(long millis, String timezone) {
        return dateAsString(new Date(millis), timezone);
    }

    /**
     * Return a formatted date
     */
    public static String dateAsString(Date date, String timezone) {
        SimpleDateFormat sdf = new SimpleDateFormat(JAVA_UTIL_DATE_TO_STRING_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(date);
    }

    /**
     * Return a formatted date
     */
    public static String getDate(long millis) {
        return getDate(new Date(millis));
    }

    /**
     * Return a formatted date
     */
    public static String getDate(Date date) {
        synchronized (DATE_FORMATTER) {
            return DATE_FORMATTER.format(date);
        }
    }

    /**
     *
     */
    public static String getThroughputInSeconds(long millis, long count, String label) {
        double seconds = millis / SECOND;
        double countPerSecond = count / seconds;
        synchronized (countFormatter) {
            return countFormatter.format(countPerSecond) + " " + label;
        }
    }

    /**
     * Given a number of bytes and the number of milliseconds it took to transfer that number of bytes, return bytes/s, KB/s, MB/s, GB/s, TB/s, PB/s, or EB/s as appropriate
     */
    public static String getRate(long millis, long bytes) {
        return getRate(millis, bytes, rateFormatter);
    }

    /**
     * Given a number of bytes and the number of milliseconds it took to transfer that number of bytes, return bytes/s, KB/s, MB/s, GB/s, TB/s, PB/s, or EB/s as appropriate
     */
    public static String getRate(long millis, long bytes, NumberFormat rateFormatter) {
        double seconds = millis / SECOND;
        double bytesPerSecond = bytes / seconds;
        Size bandwidthLevel = getSizeEnum(bytesPerSecond);
        double transferRate = bytesPerSecond / bandwidthLevel.getValue();
        synchronized (rateFormatter) {
            return rateFormatter.format(transferRate) + " " + bandwidthLevel.getRateLabel();
        }
    }

    /**
     * Return a formatted <code>count</code> representing the number of keys in the map
     */
    public static String getCount(Map<?, ?> map) {
        return getCount(map.keySet());
    }

    /**
     * Return a formatted <code>count</code> representing the number of elements in the iterable
     */
    public static String getCount(Iterable<?> iterable) {
        return getCount(Iterables.size(iterable));
    }

    /**
     * Return a formatted <code>count</code>
     */
    public static String getCount(long count) {
        synchronized (countFormatter) {
            return countFormatter.format(count);
        }
    }

    /**
     * Given milliseconds, return milliseconds, seconds, minutes, hours, days, or years as appropriate. Note that years is approximate since the logic always assumes there are
     * exactly 365 days per year.
     */
    public static String getTime(long millis) {
        return getTime(millis, timeFormatter);
    }

    /**
     * Given a stopwatch, return milliseconds, seconds, minutes, hours, days, or years as appropriate. Note that years is approximate since the logic always assumes there are
     * exactly 365 days per year.
     */
    public static String getTime(Stopwatch stopwatch) {
        return getTime(stopwatch.elapsed(MILLISECONDS));
    }

    /**
     * Given milliseconds, return milliseconds, seconds, minutes, hours, days, or years as appropriate. Note that years is approximate since the logic always assumes there are
     * exactly 365 days per year.
     */
    public static String getTime(long millis, NumberFormat formatter) {
        long abs = Math.abs(millis);
        synchronized (formatter) {
            if (abs < SECOND) {
                return millis + "ms";
            } else if (abs < MINUTE) {
                return formatter.format(millis / SECOND) + "s";
            } else if (abs < HOUR) {
                return formatter.format(millis / MINUTE) + "m";
            } else if (abs < DAY) {
                return formatter.format(millis / HOUR) + "h";
            } else if (abs < YEAR) {
                return formatter.format(millis / DAY) + "d";
            } else {
                return formatter.format(millis / YEAR) + "y";
            }
        }
    }

    /**
     * Given a number of bytes return bytes, kilobytes, megabytes, gigabytes, terabytes, petabytes, or exabytes as appropriate.
     */
    public static String getSize(long bytes) {
        return getSize(bytes, (Size) null);
    }

    /**
     * Given a number of bytes return bytes, kilobytes, megabytes, gigabytes, terabytes, petabytes, or exabytes as appropriate.
     */
    public static String getIntegerSize(long bytes) {
        return getIntegerSize(bytes, null);
    }

    /**
     * Given a number of bytes return a string formatted into the unit of measure indicated
     */
    public static String getIntegerSize(long bytes, final Size unitOfMeasure) {
        Size uom = (unitOfMeasure == null) ? getSizeEnum(bytes) : unitOfMeasure;
        StringBuilder sb = new StringBuilder();
        synchronized (integerFormatter) {
            sb.append(integerFormatter.format(bytes / (double) uom.getValue()));
        }
        sb.append(uom.getSizeLabel());
        return sb.toString();
    }

    /**
     * Given a number of bytes return a string formatted into the unit of measure indicated
     */
    public static String getSize(long bytes, NumberFormat formatter) {
        return getSize(bytes, null, formatter);
    }

    /**
     * Given a number of bytes return a string formatted into the unit of measure indicated
     */
    public static String getSize(long bytes, final Size unitOfMeasure, NumberFormat formatter) {
        Size uom = (unitOfMeasure == null) ? getSizeEnum(bytes) : unitOfMeasure;
        StringBuilder sb = new StringBuilder();
        synchronized (formatter) {
            sb.append(formatter.format(bytes / (double) uom.getValue()));
        }
        sb.append(uom.getSizeLabel());
        return sb.toString();
    }

    /**
     * Given a number of bytes return a string formatted into the unit of measure indicated
     */
    public static String getSize(long bytes, Size unitOfMeasure) {
        Size uom = (unitOfMeasure == null) ? getSizeEnum(bytes) : unitOfMeasure;
        StringBuilder sb = new StringBuilder();
        sb.append(getFormattedSize(bytes, uom));
        sb.append(uom.getSizeLabel());
        return sb.toString();
    }

    public static String getFormattedSize(long bytes, Size size) {
        switch (size) {
            case BYTE:
                return bytes + "";
            case KB:
            case MB:
            case GB:
                synchronized (sizeFormatter) {
                    return sizeFormatter.format(bytes / (double) size.getValue());
                }
            default:
                synchronized (largeSizeFormatter) {
                    return largeSizeFormatter.format(bytes / (double) size.getValue());
                }
        }
    }

    public static Size getSizeEnum(double bytes) {
        bytes = Math.abs(bytes);
        if (bytes < Size.KB.getValue()) {
            return Size.BYTE;
        } else if (bytes < Size.MB.getValue()) {
            return Size.KB;
        } else if (bytes < Size.GB.getValue()) {
            return Size.MB;
        } else if (bytes < Size.TB.getValue()) {
            return Size.GB;
        } else if (bytes < Size.PB.getValue()) {
            return Size.TB;
        } else if (bytes < Size.EB.getValue()) {
            return Size.PB;
        } else {
            return Size.EB;
        }
    }

    protected static final List<Long> getTimeMultipliers() {
        List<Long> m = new ArrayList<Long>();
        m.add(1L);
        m.add(new Double(SECOND).longValue());
        m.add(new Double(MINUTE).longValue());
        m.add(new Double(HOUR).longValue());
        m.add(new Double(DAY).longValue());
        m.add(new Double(YEAR).longValue());
        return m;
    }
}
