package org.kuali.common.jute.json.jackson;

import static com.google.common.io.BaseEncoding.base64;

import org.kuali.common.jute.json.JsonService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.google.inject.AbstractModule;

public class JacksonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BaseEncoding.class).annotatedWith(JsonBaseEncoding.class).toInstance(base64());
        bind(ObjectMapperProvider.class).toProvider(ObjectMapperProvider.Builder.class).asEagerSingleton();
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).asEagerSingleton();
        bind(JsonService.class).to(JacksonJsonService.class).asEagerSingleton();
    }
}
