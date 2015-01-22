package org.kuali.common.jute.logging;

import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;
import static com.google.common.collect.Lists.newArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.kuali.common.jute.runtime.ProcessIdProvider;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public class LogFormatter extends Formatter {

    private static final String FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";
    private static final String LS = LINE_SEPARATOR.value();
    private static final Optional<Integer> PID = ProcessIdProvider.INSTANCE.get();

    @Override
    public String format(LogRecord record) {

        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        String date = format.format(new Date(record.getMillis()));
        String thread = Thread.currentThread().getName();
        String name = shorten(record.getLoggerName());
        String method = record.getSourceMethodName();

        StringBuilder sb = new StringBuilder();
        sb.append(date);
        sb.append("  ");
        sb.append(record.getLevel());
        sb.append(" ");
        sb.append(PID.orNull());
        sb.append(" --- [");
        sb.append(thread);
        sb.append("] ");
        sb.append(name);
        sb.append(".");
        sb.append(method);
        sb.append(" : ");
        sb.append(record.getMessage());
        sb.append(" ");
        sb.append(LS);
        return sb.toString();
    }

    protected String shorten(String name) {
        if (name == null) {
            return "root";
        }
        List<String> tokens = Splitter.on('.').omitEmptyStrings().trimResults().splitToList(name);
        List<String> list = newArrayList();
        for (int i = 0; i < tokens.size() - 1; i++) {
            list.add(tokens.get(i).substring(0, 1));
        }
        list.add(tokens.get(tokens.size() - 1));
        return Joiner.on('.').join(list);
    }

}