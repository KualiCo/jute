package org.kuali.common.jute.io;

import static com.google.common.base.Charsets.UTF_8;
import static org.kuali.common.jute.io.ByteSinks.systemOut;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.google.common.io.ByteSink;

public class ByteSinksTest {

    @Test
    public void test() throws IOException {
        byte[] bytes = "hello world\n".getBytes(UTF_8);
        ByteSink sink = systemOut();
        OutputStream out = sink.openStream();
        out.write(bytes);
        // this must be a noop
        out.close();
        // must be able to write to system out still
        out.write(bytes);
    }
}
