package org.kuali.common.jute.kfs;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Ordering.natural;
import static com.google.common.io.Files.fileTreeTraverser;
import static org.kuali.common.jute.kfs.Functions.gitMoveCommand;

import java.io.File;
import java.util.List;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.Files;
import org.kuali.common.jute.kfs.annotation.MoveCommands;
import org.kuali.common.jute.kfs.annotation.MoveRequests;
import org.kuali.common.jute.system.User;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class KfsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<List<MoveRequest>>() {}).annotatedWith(MoveRequests.class).toProvider(MoveRequestProvider.class);
    }

    @Provides
    @Basedir
    protected File basedir(Environment env, User user) {
        String basedir = env.getProperty("kfs.basedir", user.getHome() + "/git/kfs");
        return new File(basedir);
    }

    @Provides
    @Files
    protected List<File> files(@Basedir File basedir) {
        return copyOf(natural().sortedCopy(copyOf(fileTreeTraverser().breadthFirstTraversal(basedir))));
    }

    @Provides
    protected OldSrcDirs oldSrcDirs(@Basedir File basedir) {
        OldSrcDirs.Builder builder = OldSrcDirs.builder();
        builder.withBasedir(basedir);
        builder.withInfrastructure(new File(basedir, "test/infrastructure/src"));
        builder.withIntegration(new File(basedir, "test/integration/src"));
        builder.withUnit(new File(basedir, "test/unit/src"));
        builder.withWork(new File(basedir, "work/src"));
        return builder.build();
    }

    @Provides
    protected NewSrcDirs newSrcDirs(@Basedir File basedir) {
        DirPair main = DirPair.build(new File(basedir, "src/main/java"), new File(basedir, "src/main/resources"));
        DirPair test = DirPair.build(new File(basedir, "src/test/java"), new File(basedir, "src/test/resources"));
        NewSrcDirs.Builder builder = NewSrcDirs.builder();
        builder.withBasedir(basedir);
        builder.withMain(main);
        builder.withTest(test);
        return builder.build();
    }

    @Provides
    @MoveCommands
    protected List<String> moveCommands(@Basedir File basedir, @MoveRequests List<MoveRequest> requests) {
        return copyOf(transform(requests, gitMoveCommand(basedir)));
    }

}
