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

import static com.google.common.base.StandardSystemProperty.FILE_SEPARATOR;
import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;
import static com.google.common.base.StandardSystemProperty.PATH_SEPARATOR;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.collect.ImmutableProperties;
import org.kuali.common.jute.system.annotation.EnvironmentVariables;
import org.kuali.common.jute.system.annotation.FileSeparator;
import org.kuali.common.jute.system.annotation.LineSeparator;
import org.kuali.common.jute.system.annotation.PathSeparator;
import org.kuali.common.jute.system.annotation.SystemCharset;
import org.kuali.common.jute.system.annotation.SystemLocale;
import org.kuali.common.jute.system.annotation.SystemProperties;
import org.kuali.common.jute.system.annotation.SystemTimezone;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Strongly typed and immutable object containing basic information about the JVM we are running in.
 *
 * Annotated to support JSON serialization/deserialization via Jackson
 */
@JsonDeserialize(builder = VirtualSystem.Builder.class)
public final class VirtualSystem {

    private final User user;
    private final OperatingSystem os;
    private final Java java;
    private final String lineSeparator;
    private final String pathSeparator;
    private final String fileSeparator;
    private final ImmutableProperties properties;
    private final ImmutableProperties environment;
    private final Locale locale;
    private final Charset charset;
    // timezone is represented as a string since java's TimeZone object is not immutable
    private final String timezone;

    /**
     * Create a new immutable VirtualSystem instance representing the current state of the system we are running on.
     */
    public static VirtualSystem build() {
        Builder builder = builder();
        builder.withUser(User.build());
        builder.withOs(OperatingSystem.build());
        builder.withJava(Java.build());
        builder.withFileSeparator(FILE_SEPARATOR.value());
        builder.withLineSeparator(LINE_SEPARATOR.value());
        builder.withPathSeparator(PATH_SEPARATOR.value());
        builder.withProperties(ImmutableProperties.copyOf(System.getProperties()));
        builder.withEnvironment(ImmutableProperties.copyOf(System.getenv()));
        builder.withLocale(Locale.getDefault());
        builder.withCharset(Charset.defaultCharset());
        builder.withTimezone(TimeZone.getDefault().getID());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private VirtualSystem(Builder builder) {
        this.user = builder.user;
        this.os = builder.os;
        this.java = builder.java;
        this.lineSeparator = builder.lineSeparator;
        this.pathSeparator = builder.pathSeparator;
        this.fileSeparator = builder.fileSeparator;
        this.properties = ImmutableProperties.copyOf(builder.properties);
        this.environment = ImmutableProperties.copyOf(builder.environment);
        this.locale = builder.locale;
        this.charset = builder.charset;
        this.timezone = builder.timezone;
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<VirtualSystem>, Provider<VirtualSystem> {

        private User user;
        private OperatingSystem os;
        private Java java;
        private String lineSeparator;
        private String pathSeparator;
        private String fileSeparator;
        private Properties properties;
        private Properties environment;
        private Locale locale;
        private Charset charset;
        private String timezone;

        @Inject
        public Builder withCharset(@SystemCharset Charset charset) {
            this.charset = charset;
            return this;
        }

        @Inject
        public Builder withTimezone(@SystemTimezone String timezone) {
            this.timezone = timezone;
            return this;
        }

        @Inject
        public Builder withLocale(@SystemLocale Locale locale) {
            this.locale = locale;
            return this;
        }

        @Inject
        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        @Inject
        public Builder withOs(OperatingSystem os) {
            this.os = os;
            return this;
        }

        @Inject
        public Builder withJava(Java java) {
            this.java = java;
            return this;
        }

        @Inject
        public Builder withLineSeparator(@LineSeparator String lineSeparator) {
            this.lineSeparator = lineSeparator;
            return this;
        }

        @Inject
        public Builder withPathSeparator(@PathSeparator String pathSeparator) {
            this.pathSeparator = pathSeparator;
            return this;
        }

        @Inject
        public Builder withFileSeparator(@FileSeparator String fileSeparator) {
            this.fileSeparator = fileSeparator;
            return this;
        }

        @Inject
        public Builder withProperties(@SystemProperties Properties properties) {
            this.properties = properties;
            return this;
        }

        @Inject
        public Builder withEnvironment(@EnvironmentVariables Properties environment) {
            this.environment = environment;
            return this;
        }

        @Override
        public VirtualSystem get() {
            return build();
        }

        @Override
        public VirtualSystem build() {
            return checkNoNulls(new VirtualSystem(this));
        }

    }

    public User getUser() {
        return user;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public Java getJava() {
        return java;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public String getPathSeparator() {
        return pathSeparator;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public ImmutableProperties getProperties() {
        return properties;
    }

    public ImmutableProperties getEnvironment() {
        return environment;
    }

    public Locale getLocale() {
        return locale;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getTimezone() {
        return timezone;
    }

}
