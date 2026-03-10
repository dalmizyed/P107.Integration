/*
BinarySearchTree.java

Instantiable binary search tree class for CS 400 @ University of Wisconsin-Madison.
Designed around the provided BinaryNode.java class and SortedCollection.java interface.

K. Almizyed, 01/24/2026
*/

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

    protected BinaryNode<T> root = null;

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            System.err.println("ERROR in insert(): Cannot insert a null value. (NullPointerException)");
        }
        if (root == null) {
            root = new BinaryNode<>(data);
        } else {
            BinaryNode<T> newNode = new BinaryNode<>(data);
            insertHelper(newNode, root);
        }
    }

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree. When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BinaryNode<T> newNode, BinaryNode<T> subtree) {
        if (subtree != null) {
            if (newNode.getData().compareTo(subtree.getData()) <= 0) {
                if (subtree.getLeft() == null) {
                    subtree.setLeft(newNode);
                    newNode.setUp(subtree);
                } else {
                    insertHelper(newNode, subtree.getLeft());
                }
            } else {
                if (subtree.getRight() == null) {
                    subtree.setRight(newNode);
                    newNode.setUp(subtree);
                } else {
                    insertHelper(newNode, subtree.getRight());
                }
            }
        }
    }

    /**
     * Check whether data is stored in the tree.
     * @param find the value to check for in the collection
     * @return true if the collection contains data one or more times,
     * and false otherwise
     */
    @Override
    public boolean contains(Comparable<T> find) {
        if (root == null) {
            return false;
        } else {
            return containsHelper(find, root);
        }
    }

    /**
     * Performs a pre-order traversal to find the Comparable<T> variable provided. If it is found,
     * then the method returns true, otherwise it will return false. When the provided subtree
     * is null, this method returns false.
     */
    private boolean containsHelper(Comparable<T> find, BinaryNode<T> subtree) {
        if (subtree == null) {
            return false;
        }

        if (find.compareTo(subtree.getData()) == 0)  {
            return true;
        } else {
            if (find.compareTo(subtree.getData()) < 0) {
                return containsHelper(find, subtree.getLeft());
            }
            if (find.compareTo(subtree.getData()) > 0) {
                return containsHelper(find, subtree.getRight());
            }
        }

        return false;
    }

    public BinaryNode<T> get(Comparable<T> find) {
        if (root == null) {
            return null;
        } else {
            return getHelper(find, root);
        }
    }

    private BinaryNode<T> getHelper(Comparable<T> find, BinaryNode<T> subtree) {
        if (subtree == null) {
            return null;
        }

        if (find.compareTo(subtree.getData()) == 0)  {
            return subtree;
        } else {
            if (find.compareTo(subtree.getData()) < 0) {
                return getHelper(find, subtree.getLeft());
            }
            if (find.compareTo(subtree.getData()) > 0) {
                return getHelper(find, subtree.getRight());
            }
        }

        return null;
    }

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     * @return the number of values in the collection, including duplicates
     */
    @Override
    public int size() {
        if (root != null) {
           return sizeHelper(root);
        }
        return 0;
    }

    /**
     * Performs a pre-order traversal, counting each node visited until all subtrees have been visited.
     * @return the total number of nodes from the root node.
     * When the provided subtree is null, this method returns false.
     */
    private int sizeHelper(BinaryNode<T> subtree) {
        if (subtree != null) {
            return sizeHelper(subtree.getLeft()) + sizeHelper(subtree.getRight()) + 1;
        }
        return 0;
    }

    /**
     * Checks if the collection is empty.
     * @return true if the collection contains 0 values, false otherwise
     */
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        } else {
            int curSize = size();
            if (curSize == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears tree of all nodes by setting root to null.
     * Java garbage collector should delete all other nodes automatically.
     */
    @Override
    public void clear() {
        root = null;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "[ ]";
        }
        return root.toLevelOrderString();
    }
}