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

import static com.google.common.base.Stopwatch.createStarted;
import static org.kuali.common.jute.base.Formats.getCount;

import java.security.SecureRandom;
import java.util.Random;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.common.base.Stopwatch;

public class SecureRandomTest extends BaseUnitTest {

    @Test
    public void test() {
        Random random = new SecureRandom();
        byte[] salt = new byte[8];
        Stopwatch sw = createStarted();
        int iterations = 1000000;
        info("iterations -> %s", getCount(iterations));
        for (int i = 0; i < iterations; i++) {
            random.nextBytes(salt);
        }
        elapsed(sw);
        sw.reset().start();
        for (int i = 0; i < iterations; i++) {
            new SecureRandom().nextBytes(salt);
        }
        elapsed(sw);
    }

}
