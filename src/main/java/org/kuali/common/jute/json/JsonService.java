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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

public interface JsonService {

    <T> T readString(String json, Class<T> type);

    <T> T readString(String json, TypeReference<T> type);

    <T> T read(File file, Class<T> type) throws IOException;

    <T> T read(File file, TypeReference<T> type) throws IOException;

    <T> T read(ByteSource source, Class<T> type) throws IOException;

    <T> T read(ByteSource source, TypeReference<T> type) throws IOException;

    <T> T read(String url, Class<T> type) throws IOException;

    <T> T read(String url, TypeReference<T> type) throws IOException;

    <T> T read(URL url, Class<T> type) throws IOException;

    <T> T read(URL url, TypeReference<T> type) throws IOException;

    <T> String writeString(T reference);

    <T> void write(File file, T reference) throws IOException;

    <T> void write(ByteSink sink, T reference) throws IOException;

}
