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
import static com.google.common.collect.Lists.transform;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public class Trees {

	public static <T> List<Node<T>> getLeaves(List<Node<T>> nodes) {
		List<Node<T>> leaves = newArrayList();
		for (Node<T> node : nodes) {
			leaves.addAll(getLeaves(node));
		}
		return leaves;
	}

	public static <T> List<Node<T>> getLeaves(Node<T> root) {
		List<Node<T>> leaves = newArrayList();
		List<Node<T>> nodes = breadthFirst(root);
		for (Node<T> node : nodes) {
			if (node.isLeaf()) {
				leaves.add(node);
			}
		}
		return leaves;
	}

	public static <T> List<T> breadthFirstElements(Node<T> node) {
		return transform(breadthFirst(node), new NodeElementFunction<T>());
	}

	public static <T> List<Node<T>> breadthFirst(List<Node<T>> nodes) {
		List<Node<T>> list = newArrayList();
		for (Node<T> node : nodes) {
			list.addAll(breadthFirst(node));
		}
		return list;
	}

	public static <T> List<Node<T>> breadthFirst(Node<T> node) {
		NodeTraverser<T> nt = NodeTraverser.create();
		Iterable<Node<T>> itr = nt.breadthFirstTraversal(node);
		return newArrayList(itr);
	}

	public static <T> List<Node<T>> postOrder(Node<T> node) {
		NodeTraverser<T> nt = NodeTraverser.create();
		Iterable<Node<T>> itr = nt.postOrderTraversal(node);
		return newArrayList(itr);
	}

	public static <T> List<Node<T>> preOrder(Node<T> node) {
		NodeTraverser<T> nt = NodeTraverser.create();
		Iterable<Node<T>> itr = nt.preOrderTraversal(node);
		return newArrayList(itr);
	}

	public static <T> String html(String title, Node<T> node) {
		Function<Node<T>, String> converter = NodeStringFunction.create();
		return html(title, ImmutableList.of(node), converter);
	}

	public static <T> String html(String title, Node<T> node, Function<Node<T>, String> converter) {
		return html(title, ImmutableList.of(node), converter);
	}

	public static <T> String html(String title, List<Node<T>> nodes) {
		Function<Node<T>, String> converter = NodeStringFunction.create();
		return html(title, nodes, converter);
	}

	public static <T> String html(String title, List<Node<T>> nodes, Function<Node<T>, String> converter) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\">\n");
		sb.append(" <th>" + title + "</th>\n");
		sb.append(" <tr>\n");
		sb.append("  <td>\n");
		for (Node<T> node : nodes) {
			sb.append(html(node, 3, converter));
		}
		sb.append("  </td>\n");
		sb.append(" </tr>\n");
		sb.append("</table>\n");
		return sb.toString();
	}

	public static <T> String html(Node<T> node) {
		Function<Node<T>, String> converter = NodeStringFunction.create();
		return html(node, 0, converter);
	}

	public static <T> String html(Node<T> node, Function<Node<T>, String> converter) {
		return html(node, 0, converter);
	}

	public static <T> String html(Node<T> node, int indent, Function<Node<T>, String> converter) {
		StringBuilder sb = new StringBuilder();
		String prefix = StringUtils.repeat(" ", indent);
		sb.append(prefix + "<table border=\"1\">\n");
		sb.append(prefix + " <tr>\n");
		sb.append(prefix + "  <td>" + converter.apply(node) + "</td>\n");
		List<Node<T>> children = node.getChildren();
		if (!children.isEmpty()) {
			sb.append(prefix + "  <td>\n");
			for (Node<T> child : children) {
				sb.append(html(child, indent + 3, converter));
			}
			sb.append(prefix + "  </td>\n");
		}
		sb.append(prefix + " </tr>\n");
		sb.append(prefix + "</table>\n");
		return sb.toString();
	}

}
