package org.kuali.common.jute.env;

import static org.kuali.common.jute.env.EnvironmentVariables.alternateKeyFunctions;

import java.util.List;

import org.kuali.common.jute.env.filter.annotation.CsvSplitter;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class EnvModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Splitter.class).annotatedWith(CsvSplitter.class).toInstance(Splitter.on(',').omitEmptyStrings().trimResults());
        bind(new TypeLiteral<List<Function<String, String>>>() {}).annotatedWith(AlternateKeyFunctions.class).toInstance(alternateKeyFunctions());
        bind(Environment.class).to(StandardEnvironment.class).asEagerSingleton();
    }
}
