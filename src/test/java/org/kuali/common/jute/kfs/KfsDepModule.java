package org.kuali.common.jute.kfs;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Ordering.natural;
import static com.google.common.io.Files.readLines;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.DepFragment;
import org.kuali.common.jute.kfs.annotation.DepVersions;
import org.kuali.common.jute.kfs.annotation.Deps;
import org.kuali.common.jute.kfs.annotation.DepsContainer;
import org.kuali.common.jute.kfs.annotation.ExistingProperties;
import org.kuali.common.jute.kfs.annotation.NewProperties;
import org.kuali.common.jute.kfs.annotation.PomContent;
import org.kuali.common.jute.kfs.annotation.PomLines;
import org.kuali.common.jute.system.User;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class KfsDepModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<List<String>>() {}).annotatedWith(Deps.class).toProvider(DepsProvider.class);
        bind(new TypeLiteral<List<List<String>>>() {}).annotatedWith(DepsContainer.class).toProvider(DepsContainerProvider.class);
        bind(new TypeLiteral<List<String>>() {}).annotatedWith(DepVersions.class).toProvider(DepVersionsProvider.class);
        bind(new TypeLiteral<String>() {}).annotatedWith(DepFragment.class).toProvider(DepFragmentProvider.class);
    }

    @Provides
    @Basedir
    protected File basedir(Environment env, User user) {
        try {
            String basedir = env.getProperty("kfs.basedir", user.getHome() + "/git/kfs");
            return new File(basedir).getCanonicalFile();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    @Provides
    @PomLines
    protected List<String> pomLines(@Basedir File basedir) {
        try {
            File pom = new File(basedir, "pom.xml");
            return readLines(pom, UTF_8);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    @Provides
    @PomContent
    protected String pomContent(@PomLines List<String> lines) {
        return Joiner.on('\n').join(lines);
    }

    @Provides
    @ExistingProperties
    protected List<String> existingProperties(@PomContent String content) {
        return Splitter.on('\n').trimResults().omitEmptyStrings().splitToList(substringBetween(content, "<properties>", "</properties>"));
    }

    @Provides
    @NewProperties
    protected List<String> newProperties(@ExistingProperties List<String> existing, @DepVersions List<String> deps) {
        return natural().immutableSortedCopy(concat(existing, deps));
    }

}
