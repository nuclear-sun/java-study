package com.sun.algorithm;

import java.util.*;

/**
 * AC 自动机
 */
public class ACAutomata {

    private static class Node {

        private char ch;
        private List<Node> children;
        private Node parent;
        private Node fail;
        private boolean isTerminate;

        public Node(char ch) {
            this.ch = ch;
            this.children = new ArrayList<>();
            this.isTerminate = false;
        }

        public Node addChild(char child) {
            Node childNode = new Node(child);
            this.children.add(childNode);
            childNode.parent = this;
            return childNode;
        }

        public Node findChild(char ch) {
            for(Node p: children) {
                if(p.ch == ch) {
                    return p;
                }
            }
            return null;
        }

        public List<Node> getChildren() {
            return this.children;
        }

        public Node getParent() {
            return this.parent;
        }

        public void setTerminate() {
            this.isTerminate = true;
        }

        public boolean isTerminate() {
            return this.isTerminate;
        }

        public void setFail(Node fail) {
            this.fail = fail;
        }

        public Node getFail() {
            return this.fail;
        }

        public boolean equalCharWith(Node that) {
            return this.ch == that.ch;
        }

        public char getChar() {
            return ch;
        }

        public String toString() {
            StringJoiner joiner = new StringJoiner(",");
            for(Node p: this.children) {
                joiner.add(String.valueOf(p.getChar()));
            }

            return "{\n" +
                    "  payload: " + this.ch + "\n" +
                    "  children: [" + joiner + "]\n" +
                    "  fail: " + (this.fail == null ? null : this.fail.ch) + "\n" +
                    "}";
        }

        public void deepPrint() {

            Queue<Node> queue = new LinkedList<>();
            queue.add(this);

            while (!queue.isEmpty()) {
                Node curr = queue.poll();
                System.out.println(curr.toString());
                queue.addAll(curr.getChildren());
            }
        }
    }

    private Node root;

    public ACAutomata(Collection<String> patterns) {
        root = new Node('\0');
        buildTree(root, patterns);
    }

    private String collectWord(final Node terminate) {
        StringBuilder sb = new StringBuilder();
        Node p = terminate;
        while (p != root) {
            sb.append(p.getChar());
            p = p.getParent();
        }
        return sb.reverse().toString();
    }

    public List<String> exact(String origin) {
        if(origin == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        Node curr = root;

        for(int i=0, length = origin.length(); i < length; i++) {
            char ch = origin.charAt(i);

            while (curr != null) {
                Node child = curr.findChild(ch);
                if(child != null) {
                    if(child.isTerminate()) {
                        String word = collectWord(child);
                        result.add(word);
                    }
                    curr = child;
                    break;
                } else {
                    curr = curr.getFail();
                }
            }

            if(curr == null) {
                curr = root;
            }
        }
        return result;
    }

    private static void buildTree(Node root, Collection<String> patterns) {

        if(patterns == null || patterns.size() == 0) {
            return;
        }

        // 构建 Trie 树
        for (String pat : patterns) {
            Node curr = root;
            for (int i = 0, last = pat.length() - 1; i <= last; i++) {
                char ch = pat.charAt(i);
                Node matchedChildNode = curr.findChild(ch);

                if (matchedChildNode != null) {
                    curr = matchedChildNode;
                } else {
                    curr = curr.addChild(ch);
                }
                if (i == last) {
                    curr.setTerminate();
                }
            }
        }
        // 构建 fail 指针
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            Node parent = curr.getParent();
            if(parent != null) {
                Node parentFail = parent.getFail();
                Node otherChild = null;
                while (parentFail != null && (otherChild = parentFail.findChild(curr.getChar())) == null) {
                    parentFail = parentFail.getFail();
                }

                if(otherChild != null) {
                    curr.setFail(otherChild);
                } else {
                    curr.setFail(root);
                }
            }
            queue.addAll(curr.getChildren());
        }
    }

    public static void main(String[] args) {

        String[] words = {
            "ash",
            "shex",
            "bcd",
            "sha"
        };

        ACAutomata acAutomata = new ACAutomata(Arrays.asList(words));
        List<String> result = acAutomata.exact("ashe");
        System.out.println(result);

        String[] words2 = {
                "社会性",
                "死亡",
                "神经病",
                "发神经"
        };

        ACAutomata han = new ACAutomata(Arrays.asList(words2));
        List<String> result2 = han.exact("你这是发神经病吗");

        System.out.println(result2);
    }


}
