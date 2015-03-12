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
package org.kuali.common.jute.json;

import static org.kuali.common.jute.project.UnitTestInjection.createUnitTestInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Range;
import com.google.inject.Injector;

public class RangeTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createUnitTestInjector();
            JsonService json = injector.getInstance(JsonService.class);
            Range<Integer> range = Range.closed(0, 0);
            info(range.getClass().getCanonicalName());
            String text = json.writeString(range);
            info("\n" + text);
            Range<Integer> actual = json.readString(text, new TypeReference<Range<Integer>>() {});
            info(actual.getClass().getCanonicalName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
