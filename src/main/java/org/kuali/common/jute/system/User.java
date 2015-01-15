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

import static com.google.common.base.StandardSystemProperty.USER_DIR;
import static com.google.common.base.StandardSystemProperty.USER_HOME;
import static com.google.common.base.StandardSystemProperty.USER_NAME;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public final class User {

    private final String name;
    private final File home;
    private final File dir;

    private User(Builder builder) {
        this.name = builder.name;
        this.home = builder.home;
        this.dir = builder.dir;
    }

    public static User build() {
        Builder builder = builder();
        builder.withName(USER_NAME.value());
        builder.withHome(new File(USER_HOME.value()));
        builder.withDir(new File(USER_DIR.value()));
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<User> {

        private String name;
        private File home;
        private File dir;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withHome(File home) {
            this.home = home;
            return this;
        }

        public Builder withDir(File dir) {
            this.dir = dir;
            return this;
        }

        @Override
        public User build() {
            return checkNoNulls(new User(this));
        }
    }

    public String getName() {
        return name;
    }

    public File getHome() {
        return home;
    }

    public File getDir() {
        return dir;
    }

}
