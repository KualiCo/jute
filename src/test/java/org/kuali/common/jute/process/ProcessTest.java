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
package org.kuali.common.jute.process;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Stopwatch.createStarted;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.ByteStreams.copy;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.base.Threads.sleep;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.common.base.Stopwatch;
import com.google.common.io.ByteSource;

public class ProcessTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            info("hello world");
            ProcessBuilder pb = new ProcessBuilder("git", "rev-parse", "--verify", "HEAD");
            pb.directory(new File(".").getCanonicalFile());
            Stopwatch sw = createStarted();
            Process process = pb.start();
            while (isAlive(process)) {
                sleep(10);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copy(process.getInputStream(), baos);
            byte[] bytes = baos.toByteArray();
            ByteSource source = wrap(bytes);
            String output = new String(source.read(), UTF_8);
            info("exit value -> %s", process.exitValue());
            info("output -> '%s'", output);
            elapsed(sw);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean isAlive(Process process) {
        checkNotNull(process, "process");
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

}
