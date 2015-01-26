package org.kuali.common.jute.base;

import static java.lang.String.format;
import static org.kuali.common.jute.base.Formats.getTime;
import static org.kuali.common.jute.base.Precondition.checkEquals;
import static org.kuali.common.jute.logging.Loggers.newLogger;

import java.util.logging.Logger;

import org.kuali.common.jute.json.JsonService;

import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;

public abstract class BaseUnitTest {

    protected final Logger logger = newLogger(this);

    /**
     * Convert the object reference to json.
     *
     * Create a new object instance from the json and then create a new json string from the new object instance.
     *
     * Verify that the original json string and the new json string are exactly the same.
     */
    public static <T> T checkPerfectReadWrite(JsonService json, T reference, Class<T> type) {
        String expected = json.writeString(reference);
        String actual = json.writeString(json.readString(expected, type));
        checkEquals(actual, expected, "actual");
        return reference;
    }

    public <T> void show(JsonService json, T reference) {
        String string = json.writeString(reference);
        Iterable<String> lines = Splitter.on('\n').split(string);
        for (String line : lines) {
            info("%s", line);
        }
    }

    public void debug(String msg, Object... args) {
        logger.info((args == null || args.length == 0) ? msg : format(msg, args));
    }

    public void info(String msg, Object... args) {
        if (args == null || args.length == 0) {
            logger.info(msg);
        } else {
            logger.info(format(msg, args));
        }
    }

    public void elapsed(Stopwatch sw) {
        info("elapsed -> %s", getTime(sw));
    }

}
