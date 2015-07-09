package com.rekmock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by x153520 on 12/15/2014.
 */
public class Tree<T> {

    public Tree(T root) {
        this.root = createNode(root);
    }

    public Node createNode(T node) {
        return new Node(node);
    }

    public Node getRoot() {
        return root;
    }

    public class Node implements Iterable<Node> {
        private Node parent;
        private List<Node> children;
        private T node;

        public Node(T node) {
            this.node = node;
        }

        public Node addChild(T child) {
            if (this.children == null) {
                this.children = new ArrayList<Node>();
            }
            final Node childNode = new Node(child);
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }

        @Override
        public Iterator<Node> iterator() {
            if (this.children == null) {
                this.children = new ArrayList<Node>();
            }
            return this.children.iterator();
        }

        public T getValue() {
            return node;
        }
    }

    private Node root;


    public static <S> Tree<S> create(S root) {
        return new Tree<S>(root);
    }


}
