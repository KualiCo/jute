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
package org.kuali.common.jute.runtime;

import static com.google.inject.Guice.createInjector;
import static org.kuali.common.jute.base.Formats.getTime;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.inject.Injector;

public class VirtualRuntimeTest extends BaseUnitTest {

    @Test
    public void test() {
        Injector injector = createInjector(new RuntimeModule());
        VirtualRuntime vr1 = injector.getInstance(VirtualRuntime.class);
        VirtualRuntime vr2 = injector.getInstance(VirtualRuntime.class);
        info("processors -> %s", vr1.getProcessors());
        info("uptime     -> %s", getTime(vr1.getUptime().getElapsed()));
        info("uptime     -> %s", getTime(vr2.getUptime().getElapsed()));
    }

}
