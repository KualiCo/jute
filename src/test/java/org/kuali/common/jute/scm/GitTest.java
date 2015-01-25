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
package org.kuali.common.jute.scm;

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.system.SystemModule;

import com.google.inject.Injector;
import com.google.inject.Key;

public class GitTest extends BaseUnitTest {

    @Test
    public void test() throws IOException {
        Injector injector = createInjector(new SystemModule(), new EnvModule(), new JacksonModule(), new GitModule());
        JsonService json = injector.getInstance(JsonService.class);
        File dir = injector.getInstance(Key.get(File.class, Directory.class));
        BuildScm scm = injector.getInstance(BuildScm.class);
        info("directory -> %s", dir.getCanonicalFile());
        show(json, scm);
    }
}
