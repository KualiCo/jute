package org.kuali.common.jute.ant;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Maps.newTreeMap;
import static com.google.common.collect.Ordering.natural;
import static com.google.common.io.Files.createParentDirs;
import static com.google.common.io.Files.write;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.kuali.common.jute.ant.annotation.Targets;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.tree.MutableNode;
import org.kuali.common.jute.tree.Node;
import org.kuali.common.jute.tree.Trees;

import com.google.common.base.Predicate;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class AntTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(concat(getUnitTestModules(), asList(new AntModule())));
            Key<List<Target>> key = Key.get(new TypeLiteral<List<Target>>() {}, Targets.class);
            List<Target> targets = natural().sortedCopy(injector.getInstance(key));
            info("targets -> %s", targets.size());
            Map<String, MutableNode<String>> nodes = newTreeMap();
            for (Target target : targets) {
                String name = target.getName();
                MutableNode<String> node = nodes.get(name);
                if (node == null) {
                    node = new MutableNode<String>(name);
                    nodes.put(name, node);

                }
                for (String dependency : target.getDepends()) {
                    MutableNode<String> child = nodes.get(dependency);
                    if (child == null) {
                        child = new MutableNode<String>(dependency);
                        nodes.put(dependency, child);
                    }
                    node.add(child);
                }
            }
            for (MutableNode<String> node : nodes.values()) {
                String html = Trees.html(node);
                File file = new File("./target/ant/" + node.getElement() + ".htm").getCanonicalFile();
                info("creating -> %s", file);
                createParentDirs(file);
                write(html, file, UTF_8);
            }
            // JsonService json = injector.getInstance(JsonService.class);
            // info("\n" + json.writeString(targets));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class NodeStringPredicate implements Predicate<Node<String>> {

        public NodeStringPredicate(String string) {
            this.string = string;
        }

        private final String string;

        @Override
        public boolean apply(Node<String> input) {
            return input.getElement().equals(string);
        }
    }

}
