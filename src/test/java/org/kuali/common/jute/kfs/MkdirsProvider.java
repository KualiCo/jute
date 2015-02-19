package org.kuali.common.jute.kfs;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Ordering.natural;
import static com.google.common.collect.Sets.newHashSet;
import static org.kuali.common.jute.kfs.Functions.mkdirCommand;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.MoveRequests;

import com.google.common.collect.ImmutableList;

public final class MkdirsProvider implements Provider<List<String>> {

    @Inject
    public MkdirsProvider(@Basedir File basedir, NewSrcDirs newDirs, @MoveRequests List<MoveRequest> requests) {
        this.requests = copyOf(requests);
        this.basedir = basedir;
        this.newDirs = newDirs;
    }

    private final ImmutableList<MoveRequest> requests;
    private final File basedir;
    private final NewSrcDirs newDirs;

    @Override
    public List<String> get() {
        Set<File> dirs = newHashSet();
        for (MoveRequest request : requests) {
            dirs.add(request.getDst().getParentFile());
        }
        add(dirs, newDirs.getMain());
        add(dirs, newDirs.getTest());
        add(dirs, newDirs.getInfrastructure());
        add(dirs, newDirs.getIntegration());
        return copyOf(transform(natural().sortedCopy(dirs), mkdirCommand(basedir)));
    }

    private void add(Set<File> dirs, DirPair pair) {
        dirs.add(pair.getSource().getParentFile());
        dirs.add(pair.getResources().getParentFile());
    }

    public ImmutableList<MoveRequest> getRequests() {
        return requests;
    }

    public File getBasedir() {
        return basedir;
    }

}
