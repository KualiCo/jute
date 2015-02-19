package org.kuali.common.jute.kfs;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.io.Files.relativePath;
import static org.kuali.common.jute.kfs.Predicates.childOf;
import static org.kuali.common.jute.kfs.Predicates.javaFile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.Files;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public final class MoveRequestProvider implements Provider<List<MoveRequest>> {

    @Inject
    public MoveRequestProvider(@Files List<File> files, OldSrcDirs oldDirs, NewSrcDirs newDirs) {
        this.files = copyOf(files);
        this.oldDirs = oldDirs;
        this.newDirs = newDirs;
    }

    private final OldSrcDirs oldDirs;
    private final NewSrcDirs newDirs;
    private final ImmutableList<File> files;

    @Override
    public List<MoveRequest> get() {
        MoveRequest mr1 = MoveRequest.build(oldDirs.getWork(), newDirs.getMain().getSource());
        MoveRequest mr2 = MoveRequest.build(oldDirs.getUnit(), newDirs.getTest().getSource());
        MoveRequest mr3 = MoveRequest.build(oldDirs.getIntegration(), newDirs.getIntegration().getSource());
        MoveRequest mr4 = MoveRequest.build(oldDirs.getInfrastructure(), newDirs.getInfrastructure().getSource());
        MoveRequest mr5 = MoveRequest.build(oldDirs.getWebapp(), newDirs.getWebapp());
        List<MoveRequest> requests = newArrayList(asList(mr1, mr2, mr3, mr4, mr5));
        requests.addAll(moveResources(oldDirs.getWork(), newDirs.getMain()));
        requests.addAll(moveResources(oldDirs.getInfrastructure(), newDirs.getInfrastructure()));
        requests.addAll(moveResources(oldDirs.getIntegration(), newDirs.getIntegration()));
        requests.addAll(moveResources(oldDirs.getUnit(), newDirs.getTest()));
        return copyOf(requests);
    }

    private List<MoveRequest> moveResources(File oldDir, DirPair pair) {
        List<File> resources = copyOf(filter(files, and(not(javaFile()), childOf(oldDir))));
        return copyOf(transform(resources, new MoveRequestFunction(oldDir, pair)));
    }

    private static class MoveRequestFunction implements Function<File, MoveRequest> {

        public MoveRequestFunction(File oldDir, DirPair pair) {
            this.pair = pair;
            this.oldDir = oldDir;
        }

        private final DirPair pair;
        private final File oldDir;

        @Override
        public MoveRequest apply(File input) {
            String path = relativePath(oldDir, input);
            File src = new File(pair.getSource(), path);
            File dst = new File(pair.getResources(), path);
            return MoveRequest.builder().withDst(dst).withSrc(src).build();
        }
    }

    public OldSrcDirs getOldDirs() {
        return oldDirs;
    }

    public NewSrcDirs getNewDirs() {
        return newDirs;
    }

    public List<File> getFiles() {
        return files;
    }

}
