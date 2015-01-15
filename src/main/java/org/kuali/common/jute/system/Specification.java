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

import static com.google.common.base.StandardSystemProperty.JAVA_SPECIFICATION_NAME;
import static com.google.common.base.StandardSystemProperty.JAVA_SPECIFICATION_VENDOR;
import static com.google.common.base.StandardSystemProperty.JAVA_SPECIFICATION_VERSION;
import static com.google.common.base.StandardSystemProperty.JAVA_VM_SPECIFICATION_NAME;
import static com.google.common.base.StandardSystemProperty.JAVA_VM_SPECIFICATION_VENDOR;
import static com.google.common.base.StandardSystemProperty.JAVA_VM_SPECIFICATION_VERSION;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Specification.Builder.class)
public final class Specification {

    private final String name;
    private final String version;
    private final String vendor;

    private Specification(Builder builder) {
        this.name = builder.name;
        this.version = builder.version;
        this.vendor = builder.vendor;
    }

    public static Specification buildRuntimeEnvironmentSpecification() {
        Builder builder = builder();
        builder.withVendor(JAVA_SPECIFICATION_VENDOR.value());
        builder.withVersion(JAVA_SPECIFICATION_VERSION.value());
        builder.withName(JAVA_SPECIFICATION_NAME.value());
        return builder.build();
    }

    public static Specification buildVirtualMachineSpecification() {
        Builder builder = builder();
        builder.withVendor(JAVA_VM_SPECIFICATION_VENDOR.value());
        builder.withVersion(JAVA_VM_SPECIFICATION_VERSION.value());
        builder.withName(JAVA_VM_SPECIFICATION_NAME.value());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Specification> {

        private String name;
        private String version;
        private String vendor;

        public Builder withVendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Specification build() {
            return checkNoNulls(new Specification(this));
        }
    }

    public String getVendor() {
        return vendor;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

}
