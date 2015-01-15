package org.kuali.common.jute.json.jackson;

import static com.fasterxml.jackson.databind.MapperFeature.SORT_PROPERTIES_ALPHABETICALLY;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS;
import static com.google.common.io.BaseEncoding.base64;
import static java.lang.Boolean.parseBoolean;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import javax.inject.Inject;

import org.kuali.common.jute.env.EmptyEnvironment;
import org.kuali.common.jute.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.BaseEncoding;
import com.google.inject.Provider;

public final class ObjectMapperProvider implements Provider<ObjectMapper> {

    private final Environment env;
    private final BaseEncoding encoder;

    /**
     * Produces a new ObjectMapper with these characteristics:
     *
     * <ul>
     * <li>registered GuavaModule</li>
     * <li>registered ByteSourceModule - uses base64 encoding</li>
     * <li>use pretty print when serializing</li>
     * <li>sort object fields alphabetically when serializing</li>
     * <li>sort map entries based on their keys when serializing</li>
     * </ul>
     */
    @Override
    public ObjectMapper get() {
        boolean guava = parseBoolean(env.getProperty("jackson.module.guava", "true"));
        boolean byteSource = parseBoolean(env.getProperty("jackson.module.byteSource", "true"));
        boolean prettyPrint = parseBoolean(env.getProperty("jackson.prettyPrint", "true"));
        boolean sortProperties = parseBoolean(env.getProperty("jackson.sort.properties", "true"));
        boolean sortMaps = parseBoolean(env.getProperty("jackson.sort.maps", "true"));
        ObjectMapper mapper = new ObjectMapper();
        if (guava) {
            mapper.registerModule(new GuavaModule());
        }
        if (byteSource) {
            mapper.registerModule(new ByteSourceModule(encoder));
        }
        if (sortProperties) {
            mapper.configure(SORT_PROPERTIES_ALPHABETICALLY, true);
        }
        if (sortMaps) {
            mapper.configure(ORDER_MAP_ENTRIES_BY_KEYS, true);
        }
        if (prettyPrint) {
            // 'indent output' is a synonym for 'pretty print'
            mapper.configure(INDENT_OUTPUT, true);
        }
        return mapper;
    }

    public static ObjectMapperProvider build() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private ObjectMapperProvider(Builder builder) {
        this.env = builder.env;
        this.encoder = builder.encoder;
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ObjectMapperProvider>, Provider<ObjectMapperProvider> {

        private Environment env = EmptyEnvironment.INSTANCE;
        private BaseEncoding encoder = base64();

        @Inject
        public Builder withEnv(Environment env) {
            this.env = env;
            return this;
        }

        @Inject
        public Builder withEncoder(@JsonBaseEncoding BaseEncoding encoder) {
            this.encoder = encoder;
            return this;
        }

        @Override
        public ObjectMapperProvider get() {
            return build();
        }

        @Override
        public ObjectMapperProvider build() {
            return checkNoNulls(new ObjectMapperProvider(this));
        }

        public Environment getEnv() {
            return env;
        }

        public void setEnv(Environment env) {
            this.env = env;
        }

        public BaseEncoding getEncoder() {
            return encoder;
        }

        public void setEncoder(BaseEncoding encoder) {
            this.encoder = encoder;
        }

    }

    public Environment getEnv() {
        return env;
    }

    public BaseEncoding getEncoder() {
        return encoder;
    }

}
