/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 *
 * Copyright 2015 The Kuali Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.common.jute.kfs;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.StandardSystemProperty.JAVA_IO_TMPDIR;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.Files.asByteSource;
import static com.google.common.io.Files.createParentDirs;
import static com.google.common.io.Files.write;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.substringsBetween;
import static org.kuali.common.jute.base.Predicates.includesExcludes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.kuali.common.jute.tree.ImmutableNode;
import org.kuali.common.jute.tree.MutableNode;
import org.kuali.common.jute.tree.Node;
import org.kuali.common.jute.tree.Trees;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

public class DirProps extends TestCase {

    public void test() {
        try {
            File basedir = new File("/Users/jcaddel/git/kfs/src/main/resources");
            Properties props = new Properties();
            props.load(asByteSource(new File(basedir, "kfs-default-config.properties")).openStream());
            props.load(asByteSource(new File(basedir, "kfs-rice-default-config.properties")).openStream());
            props.putAll(System.getProperties());
            // ends with .directory, contains .dir. or is java.io.tmpdir
            Predicate<CharSequence> predicate = includesExcludes(asList("\\.directory$", ".*\\.dir\\..*", JAVA_IO_TMPDIR.key()), ImmutableList.<String> of());
            MutableNode<String> node1 = buildTree(props, "base.directory", predicate);
            MutableNode<String> node2 = buildTree(props, "java.io.tmpdir", predicate);
            MutableNode<String> root = new MutableNode<String>("root");
            root.add(node1, node2);
            Node<String> resolved = resolved(props, root);
            writeNode(resolved, "dirs");
            writeNode(root, "props");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void writeNode(Node<String> node, String code) throws IOException {
        String html = Trees.html(ImmutableNode.copyOf(node));
        File output = new File("./target/kfs/" + code + ".htm").getCanonicalFile();
        System.out.println(String.format("creating -> %s", output));
        createParentDirs(output);
        write(html, output, UTF_8);
    }

    private Node<String> resolved(Properties props, Node<String> node) {
        String element = node.getElement();
        if (!element.equals("root")) {
            element = resolved(props.getProperty(element), props);
        }
        MutableNode<String> newNode = new MutableNode<String>(element);
        for (Node<String> child : node.getChildren()) {
            MutableNode<String> newChild = MutableNode.copyOf(resolved(props, child));
            newNode.add(newChild);
        }
        return ImmutableNode.copyOf(newNode);
    }

    private String resolved(final String value, Properties props) {
        String[] tokens = substringsBetween(value, "${", "}");
        if (tokens == null) {
            return value;
        }
        String returnValue = value;
        for (String token : tokens) {
            String tokenValue = props.getProperty(token);
            if (tokenValue == null) {
                tokenValue = token;
            }
            returnValue = replace(value, "${" + token + "}", tokenValue);
        }
        return returnValue;
    }

    private MutableNode<String> buildTree(Properties props, String key, Predicate<CharSequence> predicate) {
        MutableNode<String> node = new MutableNode<String>(key);
        for (String child : getChildren(props, placeholder(key), predicate)) {
            node.add(buildTree(props, child, predicate));
        }
        return node;
    }

    private List<String> getChildren(Properties props, String placeholder, Predicate<CharSequence> predicate) {
        List<String> list = newArrayList();
        for (String key : filter(props.stringPropertyNames(), predicate)) {
            String value = props.getProperty(key);
            if (value.contains(placeholder)) {
                list.add(key);
            }
        }
        return list;
    }

    private String placeholder(String key) {
        return "${" + key + "}";
    }

}
