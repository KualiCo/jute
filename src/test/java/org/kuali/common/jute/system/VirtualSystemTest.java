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
package org.kuali.common.jute.system;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.json.jackson.JacksonJsonService;
import org.kuali.common.jute.json.jackson.ObjectMapperProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VirtualSystemTest extends BaseUnitTest {

    @Test
    public void test() {
        info("hello world");
        VirtualSystem vs = VirtualSystem.build();
        ObjectMapper mapper = ObjectMapperProvider.build().get();
        JsonService json = new JacksonJsonService(mapper);

        show(json, vs);

        // Make sure we can write a VirtualSystem object to json and create a new VirtualSystem object from json
        checkPerfectReadWrite(json, vs, VirtualSystem.class);
    }

    @Test
    public void testValidationFail() {
        try {
            VirtualSystem.builder().build();
            fail("This should have failed");
        } catch (Exception e) {
            ; // ignore
        }
    }

}
