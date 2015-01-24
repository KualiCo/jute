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
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.scm.annotation.Revision;
import org.kuali.common.jute.system.SystemModule;

import com.google.common.base.Optional;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class SvnTest extends BaseUnitTest {

    @Test
    public void test() throws IOException {
        try {
            Injector injector = createInjector(new SystemModule(), new EnvModule(), new SvnModule());
            File dir = injector.getInstance(Key.get(File.class, Directory.class));
            Optional<String> revision = injector.getInstance(Key.get(new TypeLiteral<Optional<String>>() {}, Revision.class));
            info("directory -> %s", dir.getCanonicalFile());
            info("revision  -> %s", revision);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
