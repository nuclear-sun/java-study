package com.sun.algorithm;

public class Morris {

    private static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value, TreeNode left, TreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    public static TreeNode buildTree() {

        TreeNode node1 = new TreeNode(1, null, null);
        TreeNode node2 = new TreeNode(2, null, null);
        TreeNode node3 = new TreeNode(3, null, null);
        TreeNode node4 = new TreeNode(4, null, null);
        TreeNode node5 = new TreeNode(5, null, null);
        TreeNode node6 = new TreeNode(6, null, null);
        TreeNode node7 = new TreeNode(7, null, null);
        TreeNode node8 = new TreeNode(8, null, null);

        node1.left = node2;
        node1.right = node3;
        node2.right = node4;
        node4.left = node6;
        node4.right = node7;
        node7.left = node8;
        node3.left = node5;

        return node1;
    }

    static void morris(TreeNode node) {

        TreeNode cur = node;
        while (cur != null) {
            access(cur);
            if(cur.left == null) {
                cur = cur.right;
            } else {
                // mostRight
                TreeNode mr = cur.left;
                while (mr.right != null && mr.right != cur) {
                    mr = mr.right;
                }
                // switch
                if(mr.right == null) {
                    mr.right = cur;
                    cur = cur.left;
                } else if(mr.right == cur) {
                    mr.right = null;
                    cur = cur.right;
                }
            }
        }
    }

    static void access(TreeNode node) {
        System.out.println(node.value);
    }

    public static void main(String[] args) {

        TreeNode root = buildTree();

        morris(root);
    }

}
