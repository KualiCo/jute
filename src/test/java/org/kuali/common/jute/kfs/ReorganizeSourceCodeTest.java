package org.kuali.common.jute.kfs;

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.Files;
import org.kuali.common.jute.kfs.annotation.MoveCommands;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class ReorganizeSourceCodeTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(concat(getUnitTestModules(), asList(new KfsModule())));
            File basedir = injector.getInstance(Key.get(File.class, Basedir.class));
            List<File> files = injector.getInstance(Key.get(new TypeLiteral<List<File>>() {}, Files.class));
            List<String> commands = injector.getInstance(Key.get(new TypeLiteral<List<String>>() {}, MoveCommands.class));
            info("basedir  -> %s", basedir);
            info("files    -> %s", files.size());
            info("commands -> %s", commands.size());
            info("%s", commands.iterator().next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
