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

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Files.createParentDirs;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.inject.Inject;

import org.kuali.common.jute.json.JsonService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public final class JacksonJsonService implements JsonService {

    @Inject
    public JacksonJsonService(ObjectMapper mapper) {
        // make a defensive copy of the mapper so its impossible for someone
        // to mess with service behavior by altering the original mapper
        this.mapper = mapper.copy();
    }

    // ObjectMapper is mutable, don't expose it via a getter
    private final ObjectMapper mapper;

    @Override
    public <T> T readString(String json, Class<T> valueType) {
        try {
            ByteSource source = wrap(json.getBytes(UTF_8));
            return read(source, valueType);
        } catch (IOException e) {
            throw illegalState(e, "unexpected io error");
        }
    }

    @Override
    public <T> T readString(String json, TypeReference<T> type) {
        try {
            ByteSource source = wrap(json.getBytes(UTF_8));
            return read(source, type);
        } catch (IOException e) {
            throw illegalState(e, "unexpected io error");
        }
    }

    @Override
    public <T> T read(String url, TypeReference<T> type) throws IOException {
        checkNotBlank(url, "url");
        return read(new URL(url), type);
    }

    @Override
    public <T> T read(String url, Class<T> type) throws IOException {
        checkNotBlank(url, "url");
        return read(new URL(url), type);
    }

    @Override
    public <T> T read(URL url, TypeReference<T> type) throws IOException {
        return read(Resources.asByteSource(url), type);
    }

    @Override
    public <T> T read(URL url, Class<T> type) throws IOException {
        return read(Resources.asByteSource(url), type);
    }

    @Override
    public <T> T read(File file, Class<T> type) throws IOException {
        return read(Files.asByteSource(file), type);
    }

    @Override
    public <T> T read(File file, TypeReference<T> type) throws IOException {
        return read(Files.asByteSource(file), type);
    }

    @Override
    public <T> T read(ByteSource source, TypeReference<T> type) throws IOException {
        checkNotNull(source, "source");
        checkNotNull(type, "type");
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(source.openBufferedStream());
            return mapper.readValue(in, type);
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Override
    public <T> T read(ByteSource source, Class<T> valueType) throws IOException {
        checkNotNull(source, "source");
        checkNotNull(valueType, "valueType");
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(source.openBufferedStream());
            return mapper.readValue(in, valueType);
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Override
    public <T> String writeString(T reference) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            write(out, reference);
            return out.toString(UTF_8.name());
        } catch (IOException e) {
            throw illegalState(e, "unexpected io error");
        }
    }

    private <T> void write(OutputStream out, T reference) throws IOException {
        mapper.writeValue(out, reference);
    }

    @Override
    public <T> void write(ByteSink sink, T reference) throws IOException {
        Closer closer = Closer.create();
        try {
            OutputStream out = closer.register(sink.openBufferedStream());
            write(out, reference);
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Override
    public <T> void write(File file, T reference) throws IOException {
        ByteSink sink = asByteSink(file);
        createParentDirs(file);
        write(sink, reference);
    }

}
