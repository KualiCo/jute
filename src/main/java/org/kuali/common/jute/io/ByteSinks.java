package org.kuali.common.jute.io;

import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.io.ByteSink;

public final class ByteSinks {

    /**
     * Return a ByteSink that writes to System.out when openStream() is called. Calling close() on the returned OutputStream has no effect.
     */
    public static ByteSink systemOut() {
        return new SystemByteSink(System.out);
    }

    /**
     * Return a ByteSink that writes to System.err when openStream() is called. Calling close() on the returned OutputStream has no effect.
     */
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

    // delegate all method calls to the output stream passed in, with the exception of close()
    private static class UncloseableOutputStream extends OutputStream {

        public UncloseableOutputStream(OutputStream out) {
            this.out = checkNotNull(out, "out");
        }

        private final OutputStream out;

        @Override
        public void close() throws IOException {
            // never close it
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
