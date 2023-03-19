package com.jb.rbtree

class RBTree<T: Comparable<T>>: Set<T> {

    constructor() {
        root = null
        size = 0
    }

    private constructor(node: RBTree<T>.Node, size: Int) {
        root = node
        this.size = size
    }

    private enum class Color { BLACK, RED }
    private inner class Node(val value: T, val left: Node?, val right: Node?, val color: Color)

    private val root: Node?

    override val size: Int

    override fun contains(element: T): Boolean {
        var currentNode = root
        while (currentNode != null) {
            if (element < currentNode.value) {
                currentNode = currentNode.left
            } else if (element > currentNode.value) {
                currentNode = currentNode.right
            } else {
                return true
            }
        }
        return false
    }

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all {
            this.contains(it)
        }
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    private fun findPath(element: T): ArrayList<Node> {
        val path = arrayListOf<Node>()
        var currentNode = root
        while (currentNode != null) {
            path.add(currentNode)
            if (element < currentNode.value) {
                currentNode = currentNode.left
            } else if (element > currentNode.value) {
                currentNode = currentNode.right
            } else {
                break
            }
        }
        return path
    }

    fun add(element: T): RBTree<T> {
        if (contains(element)) {
            return this
        }

        val path = findPath(element)
        path.reverse()

        var currentNode = Node(element, null, null, Color.RED)

        var index = 0
        while (index < path.size && currentNode.color != Color.RED) {
            val oldNode = path[index]
            if (oldNode.color == Color.BLACK) {
                break
            }

            if (index + 1 == path.size) {
                val newNode = if (currentNode.value < oldNode.value) {
                    Node(oldNode.value, currentNode, oldNode.right, Color.BLACK)
                } else {
                    Node(oldNode.value, oldNode.left, currentNode, Color.BLACK)
                }
                currentNode = newNode
                index++
                break
            }

            val oldParent = path[index + 1]


            val oldBrother = if (oldNode.value < oldParent.value) {
                oldParent.right
            } else {
                oldParent.left
            }

            currentNode = makeBalanced(currentNode, oldNode, oldParent, oldBrother)

            index += 2
        }

        while (index < path.size) {
            val oldNode = path[index]
            currentNode = if (currentNode.value < oldNode.value) {
                Node(oldNode.value, currentNode, oldNode.right, oldNode.color)
            } else {
                Node(oldNode.value, oldNode.left, currentNode, oldNode.color)
            }
            index++
        }

        return RBTree(currentNode, size + 1)
    }

    private fun makeBalanced(currentNode: Node, oldNode: Node, oldParent: Node, oldBrother: Node?): Node {
        if (oldBrother?.color?: Color.BLACK == Color.RED) {
            val newNode = if (currentNode.value < oldNode.value) {
                Node(oldNode.value, currentNode, oldNode.right, Color.BLACK)
            } else {
                Node(oldNode.value, oldNode.left, currentNode, Color.BLACK)
            }
            val newBrother = Node(oldBrother!!.value, oldBrother.left, oldBrother.right, Color.BLACK)
            return if (newNode.value < oldParent.value) {
                Node(oldParent.value, newNode, newBrother, Color.RED)
            } else {
                Node(oldParent.value, newBrother, newNode, if (oldParent == root) Color.BLACK else Color.RED)
            }
        } else {
            return if (currentNode.value < oldNode.value && oldNode.value < currentNode.value) {
                /**
                 *           B
                 *         /  \
                 *        A    C
                 *       / \  / \
                 *      X  g d  e
                 *     / \
                 *    a  b
                 *
                 *          |
                 *          |
                 *          v
                 *
                 *         A
                 *       /  \
                 *      X    B
                 *     / \  / \
                 *    a  b g  C
                 *           / \
                 *          d  e
                 */
                Node(oldNode.value, currentNode, Node(oldParent.value, oldNode.right, oldBrother, Color.RED), Color.BLACK)
            } else if (currentNode.value > oldNode.value && oldNode.value < currentNode.value) {
                /**
                 *           B
                 *         /  \
                 *        A    C
                 *       / \  / \
                 *      a  X d  e
                 *        / \
                 *       b  g
                 *
                 *          |
                 *          |
                 *          v
                 *
                 *         X
                 *       /  \
                 *      A    B
                 *     / \  / \
                 *    a  b g  C
                 *           / \
                 *          d  e
                 */
                Node(currentNode.value, Node(oldNode.value, oldNode.left, currentNode.left, Color.RED), Node(oldParent.value, currentNode.right, oldBrother,
                    Color.RED), Color.BLACK)
            } else if (currentNode.value > oldNode.value && oldNode.value > currentNode.value) {
                /**
                 *           B
                 *         /  \
                 *        C    A
                 *       / \  / \
                 *      a  d e  X
                 *             / \
                 *            b  g
                 *
                 *          |
                 *          |
                 *          v
                 *
                 *           A
                 *         /  \
                 *        B    X
                 *       / \  / \
                 *      C  e b  g
                 *     / \
                 *    a  d
                 */
                Node(oldNode.value, Node(oldParent.value, oldBrother, oldNode.left, Color.RED), currentNode, Color.BLACK)
            } else {
                /**
                 *           B
                 *         /  \
                 *        C    A
                 *       / \  / \
                 *      a  d X  e
                 *          / \
                 *         b  g
                 *
                 *          |
                 *          |
                 *          v
                 *
                 *           X
                 *         /  \
                 *        B    A
                 *       / \  / \
                 *      C  b g  e
                 *     / \
                 *    a  d
                 */
                Node(currentNode.value, Node(oldParent.value, oldBrother, currentNode.left, Color.RED), Node(oldNode.value, currentNode.right, oldNode.right,
                    Color.RED), Color.BLACK)
            }
        }
    }

    fun remove(element: T): RBTree<T> {
        if (!contains(element)) {
            return this
        }

        TODO("Not yet implemented")
    }
}