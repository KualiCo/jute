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
package org.kuali.common.jute.project;

import static com.google.common.base.Stopwatch.createStarted;
import static org.kuali.common.jute.project.UnitTestInjection.createUnitTestInjector;

import java.util.Date;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.project.maven.ProjectMetadata;

import com.google.common.base.Stopwatch;
import com.google.inject.Injector;

public class KualiJuteModuleTest extends BaseUnitTest {

    @Test
    public void test() {
        Stopwatch sw = createStarted();
        Injector injector = createUnitTestInjector();
        ProjectMetadata metadata = injector.getInstance(ProjectMetadata.class);
        info("user    -> %s", metadata.getBuild().getUser());
        info("date    -> %s", new Date(metadata.getBuild().getTimestamp()));
        elapsed(sw);
    }

}
