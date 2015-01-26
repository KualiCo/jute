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
import static com.google.inject.Guice.createInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.system.SystemModule;

import com.google.common.base.Joiner;
import com.google.inject.Injector;

public class ProcessEnvironmentTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(new SystemModule(), new EnvModule(), new JacksonModule(), new ProcessModule());
            JsonService json = injector.getInstance(JsonService.class);
            ProcessService service = injector.getInstance(ProcessService.class);
            ProcessBuilder pb = new ProcessBuilder();
            show(json, pb.environment());
            ProcessContext context = ProcessContext.build("which", "svn");
            ProcessResult result = service.execute(context);
            info("command -> %s %s", context.getCommand(), Joiner.on(' ').join(context.getArgs()));
            info("stdin   -> '%s'", new String(result.getStdout().read(), UTF_8));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean isAlive(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

}
