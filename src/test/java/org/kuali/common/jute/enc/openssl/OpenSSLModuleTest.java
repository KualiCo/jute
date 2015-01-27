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
package org.kuali.common.jute.enc.openssl;

import static org.kuali.common.jute.base.Precondition.checkEquals;
import static org.kuali.common.jute.project.UnitTestInjection.createBasicInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.enc.Encryptor;

import com.google.inject.Injector;

public class OpenSSLModuleTest extends BaseUnitTest {

    @Test
    public void test() {
        Injector injector = createBasicInjector();
        Encryptor enc = injector.getInstance(Encryptor.class);
        String plaintext = "foobar";
        String encrypted = enc.encrypt(plaintext);
        String decrypted = enc.decrypt(encrypted);
        info("plaintext -> %s", plaintext);
        info("encrypted -> %s", encrypted);
        info("decrypted -> %s", decrypted);
        checkEquals(decrypted, plaintext, "decrypted");
    }

}
