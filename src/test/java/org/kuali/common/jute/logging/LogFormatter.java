package org.kuali.common.jute.logging;

import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;
import static java.lang.Thread.currentThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.kuali.common.jute.runtime.ProcessIdProvider;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

/**
 * Produce a richly formatted log message from a LogRecord.
 *
 * The log message format is:<br>
 * 1 - timestamp<br>
 * 2 - level (INFO,WARN,DEBUG,ERROR)<br>
 * 3 - pid<br>
 * 4 - thread name<br>
 * 5 - class with method name<br>
 * 6 - log message<br>
 *
 * <pre>
 * 2015-01-21 20:26:57.159  INFO 47461 [main] o.k.c.j.b.BuilderTest.info : iterations -> 5,000,000
 * </pre>
 */
public class LogFormatter extends Formatter {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LS = LINE_SEPARATOR.value();
    private static final Optional<Integer> PID = ProcessIdProvider.INSTANCE.get();
    private static final Splitter SPLITTER = Splitter.on('.').omitEmptyStrings().trimResults();

    @Override
    public String format(LogRecord record) {

        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        String date = format.format(new Date(record.getMillis()));
        String thread = currentThread().getName();
        String name = shorten(record.getLoggerName());
        String method = record.getSourceMethodName();

        StringBuilder sb = new StringBuilder();
        sb.append(date);
        sb.append(" ");
        sb.append(record.getLevel());
        sb.append(" ");
        sb.append(PID.orNull());
        sb.append(" [");
        sb.append(thread);
        sb.append("] ");
        sb.append(name);
        if (method != null) {
            sb.append(".");
            sb.append(method);
        }
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
        List<String> tokens = SPLITTER.splitToList(name);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size() - 1; i++) {
            sb.append(tokens.get(i).substring(0, 1));
            sb.append('.');
        }
        sb.append(tokens.get(tokens.size() - 1));
        return sb.toString();
    }

}