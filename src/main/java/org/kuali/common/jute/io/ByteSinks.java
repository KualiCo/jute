package org.kuali.common.jute.io;

import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.common.io.ByteSink;

public final class ByteSinks {

    /**
     * Return a ByteSink that wraps the provided ByteArrayOutputStream.
     */
    public static ByteSink wrap(ByteArrayOutputStream out) {
        return new WrappingByteSink(out, true);
    }

    /**
     * Return a ByteSink that writes to System.out when openStream() is called. Calling close() on the returned OutputStream has no affect.
     */
    public static ByteSink systemOut() {
        return new WrappingByteSink(System.out, false);
    }

    /**
     * Return a ByteSink that writes to System.err when openStream() is called. Calling close() on the returned OutputStream has no affect.
     */
    public static ByteSink systemErr() {
        return new WrappingByteSink(System.err, false);
    }

    private static class WrappingByteSink extends ByteSink {

        private WrappingByteSink(OutputStream out, boolean closeable) {
            this.out = checkNotNull(out, "out");
            this.closeable = closeable;
        }

        private final OutputStream out;
        private final boolean closeable;

        @Override
        public OutputStream openStream() throws IOException {
            return new DelegatingOutputStream(out, closeable);
        }

    }

    // delegate all calls to the output stream passed in
    private static class DelegatingOutputStream extends OutputStream {

        private DelegatingOutputStream(OutputStream delegate, boolean closeable) {
            this.delegate = checkNotNull(delegate, "delegate");
            this.closeable = closeable;
        }

        private final OutputStream delegate;
        private final boolean closeable;

        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            delegate.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
        }

        @Override
        public void close() throws IOException {
            if (closeable) {
                delegate.close();
            }
        }

    }

}
