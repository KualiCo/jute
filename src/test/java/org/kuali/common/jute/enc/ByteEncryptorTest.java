/**
 * Copyright 2014-2014 The Kuali Foundation
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
package org.kuali.common.jute.enc;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.io.BaseEncoding.base64;
import static com.google.common.io.ByteSource.concat;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.Files.asCharSource;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.enc.cipher.CipherMode;
import org.kuali.common.jute.system.User;

import com.google.common.io.ByteSource;

public class ByteEncryptorTest extends BaseUnitTest {

    @Test
    public void test() throws Exception {
        // 1
        int saltBytes = 8;
        ByteSource salt = wrap(getSalt(saltBytes));

        // 2,3,4,5,6,7
        String digest = "MD5";
        String encryption = "AES";
        int keyBits = 128;
        String transformation = "AES/CBC/PKCS5Padding";
        int iterations = 1;
        ByteSource password = wrap(getPassword());
        CipherMode mode = CipherMode.ENCRYPT;

        Cipher cipher = buildCipher(digest, encryption, transformation, keyBits, iterations, password, salt, mode);

        ByteSource data = wrap("foobar".getBytes(UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = data.openBufferedStream();
        encryptedCopy(in, out, cipher);

        ByteSource prefix = wrap("Salted__".getBytes(UTF_8));
        ByteSource encrypted = wrap(out.toByteArray());

        ByteSource result = concat(prefix, salt, encrypted);
        String text = base64().encode(result.read());
        info(text);
    }

    private static Cipher buildCipher(String digest, String encryption, String transformation, int keyBits, int iterations, ByteSource data, ByteSource salt, CipherMode mode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            int initVectorLength = cipher.getBlockSize();
            MessageDigest md = MessageDigest.getInstance(digest);
            int keyLength = keyBits / Byte.SIZE;
            int keyIndex = 0;
            int initVectorIndex = 0;
            byte[] key = new byte[keyLength];
            byte[] initVector = new byte[initVectorLength];
            byte[] digestBuffer = null;
            int i = 0;
            int addDigest = 0;
            for (;;) {
                md.reset();
                if (addDigest++ > 0) {
                    md.update(digestBuffer);
                }
                md.update(data.read());
                if (salt != null) {
                    md.update(salt.read());
                }
                digestBuffer = md.digest();
                for (i = 1; i < iterations; i++) {
                    md.reset();
                    md.update(digestBuffer);
                    digestBuffer = md.digest();
                }
                i = 0;
                if (keyLength > 0) {
                    for (;;) {
                        if (keyLength == 0) {
                            break;
                        }
                        if (i == digestBuffer.length) {
                            break;
                        }
                        key[keyIndex++] = digestBuffer[i];
                        keyLength--;
                        i++;
                    }
                }
                if (initVectorLength > 0 && i != digestBuffer.length) {
                    for (;;) {
                        if (initVectorLength == 0) {
                            break;
                        }
                        if (i == digestBuffer.length) {
                            break;
                        }
                        initVector[initVectorIndex++] = digestBuffer[i];
                        initVectorLength--;
                        i++;
                    }
                }
                if (keyLength == 0 && initVectorLength == 0) {
                    break;
                }
            }
            for (i = 0; i < digestBuffer.length; i++) {
                digestBuffer[i] = 0;
            }

            Key initKey = new SecretKeySpec(key, encryption);
            AlgorithmParameterSpec initParams = new IvParameterSpec(initVector);
            cipher.init(mode.getValue(), initKey, initParams);
            return cipher;
        } catch (Exception e) {
            throw illegalState(e);
        }
    }

    private static byte[] getPassword() throws IOException {
        User user = User.build();
        File file = new File(user.getHome(), "/.enc/password");
        checkArgument(file.isFile(), "'%s' is not a normal file", file);
        String password = trimToNull(asCharSource(file, UTF_8).readFirstLine());
        checkNotBlank(password, "password");
        return password.getBytes(UTF_8);
    }

    private static byte[] getSalt(int bytes) {
        Random random = new SecureRandom();
        byte[] buffer = new byte[bytes];
        random.nextBytes(buffer);
        return buffer;
    }

    private static void encryptedCopy(InputStream in, OutputStream out, Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] buffer = new byte[4096];
        int length = in.read(buffer);
        while (length != -1) {
            out.write(cipher.update(buffer, 0, length));
            length = in.read(buffer);
        }
        out.write(cipher.doFinal());
    }

}
