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

import static com.google.common.base.StandardSystemProperty.JAVA_VM_NAME;
import static com.google.common.base.StandardSystemProperty.JAVA_VM_VENDOR;
import static com.google.common.base.StandardSystemProperty.JAVA_VM_VERSION;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;
import static org.kuali.common.jute.system.Specification.buildVirtualMachineSpecification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = VirtualMachine.Builder.class)
public final class VirtualMachine {

    private final String name;
    private final String version;
    private final String vendor;
    private final Specification specification;

    private VirtualMachine(Builder builder) {
        this.name = builder.name;
        this.vendor = builder.vendor;
        this.version = builder.version;
        this.specification = builder.specification;
    }

    public static VirtualMachine build() {
        Builder builder = builder();
        builder.withName(JAVA_VM_NAME.value());
        builder.withVendor(JAVA_VM_VENDOR.value());
        builder.withVersion(JAVA_VM_VERSION.value());
        builder.withSpecification(buildVirtualMachineSpecification());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<VirtualMachine> {

        private String name;
        private String version;
        private String vendor;
        private Specification specification;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withVendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withSpecification(Specification specification) {
            this.specification = specification;
            return this;
        }

        @Override
        public VirtualMachine build() {
            return checkNoNulls(new VirtualMachine(this));
        }
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public String getVersion() {
        return version;
    }

    public Specification getSpecification() {
        return specification;
    }

}
