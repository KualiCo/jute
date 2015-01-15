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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

public final class ImmutableNode<T> extends MutableNode<T> {

	private static final String UOE_MSG = "not supported for immutable node's";

	public static <T> ImmutableNode<T> copyOf(Node<T> node) {
		if (node instanceof ImmutableNode) {
			return (ImmutableNode<T>) node;
		} else {
			return new ImmutableNode<T>(node);
		}
	}

	public ImmutableNode(Node<T> node) {
		checkNotNull(node);
		super.setElement(node.getElement());
		List<Node<T>> children = node.getChildren();
		for (Node<T> child : children) {
			super.add(children.size(), copyOf(child));
		}
	}

	@Override
	public void setElement(T element) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(List<MutableNode<T>> children) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(MutableNode<T> child1, MutableNode<T> child2) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(MutableNode<T> child1, MutableNode<T> child2, MutableNode<T> child3) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(MutableNode<T> child1, MutableNode<T> child2, MutableNode<T> child3, MutableNode<T> child4) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(MutableNode<T> child1, MutableNode<T> child2, MutableNode<T> child3, MutableNode<T> child4, MutableNode<T> child5) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(MutableNode<T> child) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void add(int index, MutableNode<T> child) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void remove(MutableNode<T> child) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void remove(int index) {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void removeAllChildren() {
		throw new UnsupportedOperationException(UOE_MSG);
	}

	@Override
	public void removeFromParent() {
		throw new UnsupportedOperationException(UOE_MSG);
	}

}
