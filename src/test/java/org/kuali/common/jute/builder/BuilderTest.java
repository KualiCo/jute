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
package org.kuali.common.jute.builder;

import static com.google.common.base.Stopwatch.createStarted;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.kuali.common.jute.base.Formats.getCount;
import static org.kuali.common.jute.base.Formats.getTime;

import org.apache.commons.lang3.builder.Builder;
import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.common.base.Stopwatch;

public class BuilderTest extends BaseUnitTest {

    @Test
    public void test() {
        int count = 5000000;
        info("     iterations -> %s", getCount(count));
        build(FooUnchecked.builder(), count);
        build(FooGuava1.builder(), count);
        build(FooGuava2.builder(), count);
        build(FooJute1.builder(), count);
        build(FooJute2.builder(), count);
        build(FooReflection.builder(), count);
    }

    protected <T extends Builder<?>> void build(T builder, int count) {
        Stopwatch sw = createStarted();
        for (int i = 0; i < count; i++) {
            builder.build();
        }
        info("%s -> %s", leftPad(builder.getClass().getDeclaringClass().getSimpleName(), 15), getTime(sw));
    }

}
