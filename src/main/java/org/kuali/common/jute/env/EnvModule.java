package org.kuali.common.jute.env;

import static org.kuali.common.jute.env.EnvironmentVariables.alternateKeyFunctions;
import static org.kuali.common.jute.inject.TypeLiterals.stringToStringFunctionList;

import com.google.inject.AbstractModule;

public class EnvModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(stringToStringFunctionList()).annotatedWith(AlternateKeyFunctions.class).toInstance(alternateKeyFunctions());
        bind(Environment.class).to(StandardEnvironment.class).asEagerSingleton();
    }
}
