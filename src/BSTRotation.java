public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

      /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     */
    protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {
        if (child == null || parent == null) {
            return;
        }

        BinaryNode<T> grandparent = parent.getUp();

        boolean parentWasRoot = (parent == root);

        // Right Rotation: Child is to the left of parent.
        if (child == parent.getLeft()) {

            // Move child's right child to parent's left.
            if (child.getRight() != null) {
                BinaryNode<T> rightGrandchild = child.getRight();
                parent.setLeft(rightGrandchild);
                rightGrandchild.setUp(parent);
            } else {
                parent.setLeft(null);
            }

            // Swap child and parent.
            child.setRight(parent);
            parent.setUp(child);

        // Left Rotation: Child is to the right of parent.
        } else if (child == parent.getRight()) {

            // Move child's left child to parent's right.
            if (child.getLeft() != null) {
                BinaryNode<T> leftGrandchild = child.getLeft();
                parent.setRight(leftGrandchild);
                leftGrandchild.setUp(parent);
            } else {
                parent.setRight(null);
            }

            // Swap child and parent.
            child.setLeft(parent);
            parent.setUp(child);

        } else {
            // Child isn't actually a subtree of parent, do nothing.
            System.err.println("ROTATION ERROR: Child is not a subtree of parent.");
            return;
        }

        // Reconnect child to grandparent.
        if (grandparent != null) {
            child.setUp(grandparent);

            if (grandparent.getLeft() == parent) {
                grandparent.setLeft(child);
            } else if (grandparent.getRight() == parent) {
                grandparent.setRight(child);
            }
        }

        // If parent was originally root, then child should become root.
        if (parentWasRoot) {
            root = child;
            child.setUp(null);
        }
    }
}