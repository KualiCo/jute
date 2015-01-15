package org.kuali.common.jute.enc.cipher;

import static org.kuali.common.jute.base.Exceptions.ioException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;

public final class Ciphers {

    private Ciphers() {
    }

    public static void cipheredCopy(ByteSource source, ByteSink sink, Cipher cipher) throws IOException {
        Closer closer = Closer.create();
        try {
            OutputStream out = closer.register(sink.openBufferedStream());
            cipheredCopy(source, out, cipher);
        } catch (Throwable e) {
            closer.rethrow(e);
        } finally {
            closer.close();
        }

    }

    public static void cipheredCopy(ByteSource source, OutputStream out, Cipher cipher) throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(source.openBufferedStream());
            cipheredCopy(in, out, cipher);
        } catch (Throwable e) {
            closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    public static void cipheredCopy(InputStream in, OutputStream out, Cipher cipher) throws IOException {
        try {
            byte[] buffer = new byte[4096];
            int length = in.read(buffer);
            while (length != -1) {
                out.write(cipher.update(buffer, 0, length));
                length = in.read(buffer);
            }
            out.write(cipher.doFinal());
        } catch (Exception e) {
            throw ioException(e);
        }
    }

}
