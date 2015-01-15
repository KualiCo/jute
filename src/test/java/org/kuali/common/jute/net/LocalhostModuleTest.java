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
package org.kuali.common.jute.net;

import static com.google.common.base.Stopwatch.createStarted;
import static com.google.inject.Guice.createInjector;
import static org.kuali.common.jute.net.LocalhostModule.networkInterfaceList;

import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.system.SystemModule;

import com.google.common.base.Stopwatch;
import com.google.inject.Injector;
import com.google.inject.Key;

public class LocalhostModuleTest extends BaseUnitTest {

    @Test
    public void test() {
        Stopwatch sw = createStarted();
        Injector injector = createInjector(new SystemModule(), new EnvModule(), new JacksonModule(), new LocalhostModule());
        JsonService json = injector.getInstance(JsonService.class);
        Key<List<NetworkInterface>> key = Key.get(networkInterfaceList(), NetworkInterfaces.class);
        List<NetworkInterface> nics = injector.getInstance(key);
        show(json, nics);
        elapsed(sw);
    }

}
