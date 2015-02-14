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
package org.kuali.common.jute.runtime;

import static org.kuali.common.jute.base.Precondition.checkMin;

import javax.inject.Provider;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Memory.Builder.class)
public final class Memory {

    private final long used;
    private final long free;
    private final long allocated;
    private final long max;

    private Memory(Builder builder) {
        this.used = builder.used;
        this.free = builder.free;
        this.allocated = builder.allocated;
        this.max = builder.max;
    }

    public static Memory build() {
        // Total amount of memory the JVM is allowed to use
        long max = Runtime.getRuntime().maxMemory();

        // Total amount of memory currently allocated
        long allocated = Runtime.getRuntime().totalMemory();

        // The JDK method "freeMemory()" reports what is free in the currently allocated heap
        // The amount of memory currently being used by the JVM is the difference between what has been allocated and what is still free
        long used = allocated - Runtime.getRuntime().freeMemory();

        // The true amount of free memory is the difference between max and what is currently being used
        long free = max - used;

        return builder().withAllocated(allocated).withFree(free).withMax(max).withUsed(used).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Memory>, Provider<Memory> {

        // Total amount of memory the JVM is allowed to use
        private long max;

        // Total amount of memory currently allocated
        private long allocated;

        // The amount of memory currently being used by the JVM
        private long used;

        // The total amount of memory still available to the JVM
        private long free;

        public Builder withUsed(long used) {
            this.used = used;
            return this;
        }

        public Builder withFree(long free) {
            this.free = free;
            return this;
        }

        public Builder withAllocated(long allocated) {
            this.allocated = allocated;
            return this;
        }

        public Builder withMax(long max) {
            this.max = max;
            return this;
        }

        @Override
        public Memory get() {
            return build();
        }

        @Override
        public Memory build() {
            return validate(new Memory(this));
        }

        private static Memory validate(Memory instance) {
            checkMin(instance.used, 0, "used");
            checkMin(instance.free, 0, "free");
            checkMin(instance.allocated, 0, "allocated");
            checkMin(instance.max, 0, "max");
            return instance;
        }

    }

    public long getUsed() {
        return used;
    }

    public long getFree() {
        return free;
    }

    public long getAllocated() {
        return allocated;
    }

    public long getMax() {
        return max;
    }

}
