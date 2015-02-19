package org.kuali.common.jute.kfs;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.io.Files.createParentDirs;
import static com.google.common.io.Files.write;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.Files;
import org.kuali.common.jute.kfs.annotation.MkdirCommands;
import org.kuali.common.jute.kfs.annotation.MoveCommands;

import com.google.common.base.Joiner;
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
            List<String> moves = injector.getInstance(Key.get(new TypeLiteral<List<String>>() {}, MoveCommands.class));
            List<String> mkdirs = injector.getInstance(Key.get(new TypeLiteral<List<String>>() {}, MkdirCommands.class));
            info("basedir -> %s", basedir);
            info("files   -> %s", files.size());
            info("mkdirs  -> %s", mkdirs.size());
            info("moves   -> %s", moves.size());
            File reorganize = new File(basedir, "reorganize");
            info("create  -> %s", reorganize);
            createParentDirs(reorganize);
            write(Joiner.on('\n').join(concat(asList("#!/bin/bash", "date"), mkdirs, moves, asList("date"))), reorganize, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
