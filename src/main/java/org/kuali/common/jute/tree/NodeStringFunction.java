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

import static org.kuali.common.jute.base.Precondition.checkNotNull;

import com.google.common.base.Function;

public final class NodeStringFunction<T> implements Function<Node<T>, String> {

    public NodeStringFunction(Function<T, String> function) {
        checkNotNull(function, "function");
        this.function = function;
    }

    /**
     * Convert the element contained in each node to a string by calling it's toString() method
     */
    public static <T> NodeStringFunction<T> create() {
        return create(new ToStringFunction<T>());
    }

    /**
     * Convert the element contained in each node to a string by invoking {@code function} on it
     */
    public static <T> NodeStringFunction<T> create(Function<T, String> function) {
        return new NodeStringFunction<T>(function);
    }

    private final Function<T, String> function;

    @Override
    public String apply(Node<T> node) {
        checkNotNull(node, "node");
        checkNotNull(node.getElement(), "node.element");
        return function.apply(node.getElement());
    }

    private static class ToStringFunction<T> implements Function<T, String> {

        @Override
        public String apply(T element) {
            checkNotNull(element, "element");
            return element.toString();
        }
    }

}
