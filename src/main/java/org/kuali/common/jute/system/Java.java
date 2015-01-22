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

import static com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH;
import static com.google.common.base.StandardSystemProperty.JAVA_CLASS_VERSION;
import static com.google.common.base.StandardSystemProperty.JAVA_EXT_DIRS;
import static com.google.common.base.StandardSystemProperty.JAVA_HOME;
import static com.google.common.base.StandardSystemProperty.JAVA_IO_TMPDIR;
import static com.google.common.base.StandardSystemProperty.JAVA_LIBRARY_PATH;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;
import static org.kuali.common.jute.system.SystemFiles.fromSystemProperty;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = Java.Builder.class)
public final class Java {

    private final File home;
    private final File tmpDir;
    private final String classVersion;
    private final ImmutableList<File> classPaths;
    private final ImmutableList<File> libraryPaths;
    private final ImmutableList<File> extensionDirs;
    private final RuntimeEnvironment runtime;
    private final VirtualMachine vm;

    private Java(Builder builder) {
        this.home = builder.home;
        this.tmpDir = builder.tmpDir;
        this.classVersion = builder.classVersion;
        this.classPaths = ImmutableList.copyOf(builder.classPaths);
        this.libraryPaths = ImmutableList.copyOf(builder.libraryPaths);
        this.extensionDirs = ImmutableList.copyOf(builder.extensionDirs);
        this.runtime = builder.runtime;
        this.vm = builder.vm;
    }

    public static Java build() {
        Builder builder = builder();
        builder.withHome(new File(JAVA_HOME.value()));
        builder.withTmpDir(new File(JAVA_IO_TMPDIR.value()));
        builder.withClassVersion(JAVA_CLASS_VERSION.value());
        builder.withClassPaths(fromSystemProperty(JAVA_CLASS_PATH));
        builder.withExtensionDirs(fromSystemProperty(JAVA_EXT_DIRS));
        builder.withLibraryPaths(fromSystemProperty(JAVA_LIBRARY_PATH));
        builder.withRuntime(RuntimeEnvironment.build());
        builder.withVm(VirtualMachine.build());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Java> {

        private File home;
        private File tmpDir;
        private String classVersion;
        private List<File> classPaths;
        private List<File> libraryPaths;
        private List<File> extensionDirs;
        private RuntimeEnvironment runtime;
        private VirtualMachine vm;

        public Builder withHome(File home) {
            this.home = home;
            return this;
        }

        public Builder withTmpDir(File tmpDir) {
            this.tmpDir = tmpDir;
            return this;
        }

        public Builder withClassVersion(String classVersion) {
            this.classVersion = classVersion;
            return this;
        }

        public Builder withClassPaths(List<File> classPaths) {
            this.classPaths = classPaths;
            return this;
        }

        public Builder withLibraryPaths(List<File> libraryPaths) {
            this.libraryPaths = libraryPaths;
            return this;
        }

        public Builder withExtensionDirs(List<File> extensionDirs) {
            this.extensionDirs = extensionDirs;
            return this;
        }

        public Builder withRuntime(RuntimeEnvironment runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder withVm(VirtualMachine vm) {
            this.vm = vm;
            return this;
        }

        @Override
        public Java build() {
            return checkNoNulls(new Java(this));
        }
    }

    public File getHome() {
        return home;
    }

    public File getTmpDir() {
        return tmpDir;
    }

    public String getClassVersion() {
        return classVersion;
    }

    public List<File> getClassPaths() {
        return classPaths;
    }

    public List<File> getLibraryPaths() {
        return libraryPaths;
    }

    public List<File> getExtensionDirs() {
        return extensionDirs;
    }

    public RuntimeEnvironment getRuntime() {
        return runtime;
    }

    public VirtualMachine getVm() {
        return vm;
    }

}
