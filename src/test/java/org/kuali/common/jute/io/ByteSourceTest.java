package org.kuali.common.jute.io;

import static com.google.common.base.Charsets.UTF_8;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.google.common.io.ByteSink;

public class ByteSourceTest {

    @Test
    public void test() {
        try {
            ByteSink sink = systemOut();
            OutputStream out = sink.openStream();
            out.write("hello world".getBytes(UTF_8));
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static ByteSink systemOut() {
        return new SystemByteSink(System.out);
    }

    public static ByteSink systemErr() {
        return new SystemByteSink(System.err);
    }

    private static class SystemByteSink extends ByteSink {

        public SystemByteSink(OutputStream out) {
            this.out = checkNotNull(out, "out");
        }

        private final OutputStream out;

        @Override
        public OutputStream openStream() throws IOException {
            return new UncloseableOutputStream(out);
        }

    }

    private static class UncloseableOutputStream extends OutputStream {

        public UncloseableOutputStream(OutputStream out) {
            this.out = checkNotNull(out, "out");
        }

        private final OutputStream out;

        @Override
        public void close() throws IOException {
            // do not close it
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

    }

}
