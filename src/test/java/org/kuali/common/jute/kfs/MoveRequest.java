package org.kuali.common.jute.kfs;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = MoveRequest.Builder.class)
public final class MoveRequest {

    private final File src;
    private final File dst;

    private MoveRequest(Builder builder) {
        this.src = builder.src;
        this.dst = builder.dst;
    }

    public static MoveRequest build(File src, File dst) {
        return builder().withDst(dst).withSrc(src).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<MoveRequest> {

        private File src;
        private File dst;

        public Builder withSrc(File src) {
            this.src = src;
            return this;
        }

        public Builder withDst(File dst) {
            this.dst = dst;
            return this;
        }

        @Override
        public MoveRequest build() {
            return checkNoNulls(new MoveRequest(this));
        }
    }

    public File getSrc() {
        return src;
    }

    public File getDst() {
        return dst;
    }

}
