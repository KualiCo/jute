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
package org.kuali.common.jute.base;

import static com.google.common.base.Stopwatch.createStarted;
import static com.google.inject.Guice.createInjector;

import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.enc.openssl.OpenSSLModule;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.system.SystemModule;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TimedIntervalTest extends BaseUnitTest {

    @Test
    public void test() {
        try {

            List<AbstractModule> modules = Lists.newArrayList();
            modules.add(new SystemModule());
            modules.add(new EnvModule());
            modules.add(new OpenSSLModule());
            modules.add(new JacksonModule());
            Injector injectorr = Guice.createInjector(modules);

            Stopwatch sw = createStarted();
            Injector injector = createInjector(new SystemModule(), new EnvModule(), new JacksonModule());
            JsonService json = injector.getInstance(JsonService.class);
            TimedInterval timing = TimedInterval.build(sw);
            show(json, timing);
            checkPerfectReadWrite(json, timing, TimedInterval.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
