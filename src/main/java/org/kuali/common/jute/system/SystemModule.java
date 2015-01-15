package org.kuali.common.jute.system;

import static com.google.common.base.StandardSystemProperty.FILE_SEPARATOR;
import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;
import static com.google.common.base.StandardSystemProperty.PATH_SEPARATOR;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.kuali.common.jute.collect.ImmutableProperties;
import org.kuali.common.jute.system.annotation.EnvironmentVariables;
import org.kuali.common.jute.system.annotation.FileSeparator;
import org.kuali.common.jute.system.annotation.LineSeparator;
import org.kuali.common.jute.system.annotation.PathSeparator;
import org.kuali.common.jute.system.annotation.SystemCharset;
import org.kuali.common.jute.system.annotation.SystemLocale;
import org.kuali.common.jute.system.annotation.SystemProperties;
import org.kuali.common.jute.system.annotation.SystemTimezone;

import com.google.inject.AbstractModule;

public class SystemModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(User.class).toInstance(User.build());
        bind(OperatingSystem.class).toInstance(OperatingSystem.build());
        bind(Java.class).toInstance(Java.build());
        bindConstant().annotatedWith(LineSeparator.class).to(LINE_SEPARATOR.value());
        bindConstant().annotatedWith(PathSeparator.class).to(PATH_SEPARATOR.value());
        bindConstant().annotatedWith(FileSeparator.class).to(FILE_SEPARATOR.value());
        bindConstant().annotatedWith(SystemTimezone.class).to(TimeZone.getDefault().getID());
        bind(Charset.class).annotatedWith(SystemCharset.class).toInstance(Charset.defaultCharset());
        bind(Locale.class).annotatedWith(SystemLocale.class).toInstance(Locale.getDefault());
        bind(Properties.class).annotatedWith(SystemProperties.class).toInstance(ImmutableProperties.copyOf(System.getProperties()));
        bind(Properties.class).annotatedWith(EnvironmentVariables.class).toInstance(ImmutableProperties.copyOf(System.getenv()));
        bind(VirtualSystem.class).toProvider(VirtualSystem.Builder.class).asEagerSingleton();
    }
}
