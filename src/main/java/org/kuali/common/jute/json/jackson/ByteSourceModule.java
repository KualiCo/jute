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
package org.kuali.common.jute.json.jackson;

import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.json.jackson.Jackson.DEFAULT_BASE_ENCODING;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;

public final class ByteSourceModule extends SimpleModule {

    public ByteSourceModule() {
        this(DEFAULT_BASE_ENCODING);
    }

    public ByteSourceModule(BaseEncoding encoder) {
        checkNotNull(encoder, "encoder");
        addDeserializer(ByteSource.class, new ByteSourceDeserializer(encoder));
        addSerializer(ByteSource.class, new ByteSourceSerializer(encoder));
    }

    private static final long serialVersionUID = 0;

}
