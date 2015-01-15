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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;

public class ByteSourceSerializer extends JsonSerializer<ByteSource> {

    public ByteSourceSerializer() {
        this(DEFAULT_BASE_ENCODING);
    }

    public ByteSourceSerializer(BaseEncoding encoding) {
        this.encoder = checkNotNull(encoding, "encoding");
    }

    private final BaseEncoding encoder;

    @Override
    public void serialize(ByteSource source, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(encoder.encode(source.read()));
    }

}
