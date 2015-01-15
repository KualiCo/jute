/**
 * Copyright 2010-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.common.jute.tree;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import static com.google.common.collect.Lists.transform;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.List;

public abstract class AbstractNode<T> implements Node<T> {

    /**
     * Return true if this is the root node (ie it has no parent)
     */
    @Override
    public boolean isRoot() {
        return !getParent().isPresent();
    }

    /**
     * Return true if this is a leaf node (ie it has no children)
     */
    @Override
    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    /**
     * Returns the number of levels above this node. The distance from the root to this node. If this node is the root, returns 0.
     */
    @Override
    public int getLevel() {
        int level = 0;
        Node<T> ancestor = this;
        while (ancestor.getParent().isPresent()) {
            ancestor = ancestor.getParent().get();
            level++;
        }
        return level;
    }

    /**
     * Returns the path from the root, to get to this node. The last element in the path is this node.
     */
    @Override
    public List<Node<T>> getPath() {
        Node<T> ancestor = this;
        List<Node<T>> list = newArrayList();
        list.add(ancestor);
        while (ancestor.getParent().isPresent()) {
            ancestor = ancestor.getParent().get();
            list.add(ancestor);
        }
        return reverse(list);
    }

    /**
     * Returns the node elements in the path from the root, to get to this node. The last entry is the element from this node
     */
    @Override
    public List<T> getElementPath() {
        return transform(getPath(), Nodes.<T> nodeElementFunction());
    }

    /**
     * Return true if this node is a child of parent
     */
    @Override
    public boolean isChild(Node<T> parent) {
        checkNotNull(parent, "parent");
        return parent.getChildren().contains(this);
    }

    /**
     * Return true if this node is a parent of child
     */
    @Override
    public boolean isParent(Node<T> child) {
        checkNotNull(child, "child");
        return getChildren().contains(child);
    }

    /**
     * Return true if this node descends from parent OR is parent
     */
    @Override
    public boolean isAncestor(Node<T> parent) {
        checkNotNull(parent, "parent");
        return getPath().contains(parent);
    }

}
