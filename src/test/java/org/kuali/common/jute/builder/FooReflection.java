package org.kuali.common.jute.builder;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

public final class FooReflection {

    private final String field1;
    private final String field2;
    private final String field3;
    private final String field4;
    private final String field5;
    private final String field6;
    private final String field7;
    private final String field8;
    private final String field9;

    private FooReflection(Builder builder) {
        this.field1 = builder.field1;
        this.field2 = builder.field2;
        this.field3 = builder.field3;
        this.field4 = builder.field4;
        this.field5 = builder.field5;
        this.field6 = builder.field6;
        this.field7 = builder.field7;
        this.field8 = builder.field8;
        this.field9 = builder.field9;
    }

    public static FooReflection build() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<FooReflection> {

        private String field1 = "foo";
        private String field2 = "foo";
        private String field3 = "foo";
        private String field4 = "foo";
        private String field5 = "foo";
        private String field6 = "foo";
        private String field7 = "foo";
        private String field8 = "foo";
        private String field9 = "foo";

        public Builder withField1(String field1) {
            this.field1 = field1;
            return this;
        }

        public Builder withField2(String field2) {
            this.field2 = field2;
            return this;
        }

        public Builder withField3(String field3) {
            this.field3 = field3;
            return this;
        }

        public Builder withField4(String field4) {
            this.field4 = field4;
            return this;
        }

        public Builder withField5(String field5) {
            this.field5 = field5;
            return this;
        }

        public Builder withField6(String field6) {
            this.field6 = field6;
            return this;
        }

        public Builder withField7(String field7) {
            this.field7 = field7;
            return this;
        }

        public Builder withField8(String field8) {
            this.field8 = field8;
            return this;
        }

        public Builder withField9(String field9) {
            this.field9 = field9;
            return this;
        }

        @Override
        public FooReflection build() {
            return checkNoNulls(new FooReflection(this));
        }
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    public String getField4() {
        return field4;
    }

    public String getField5() {
        return field5;
    }

    public String getField6() {
        return field6;
    }

    public String getField7() {
        return field7;
    }

    public String getField8() {
        return field8;
    }

    public String getField9() {
        return field9;
    }

}
