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
        if (root == null) {
            return RBTree(Node(element, null, null, Color.BLACK), 1)
        }

        if (contains(element)) {
            return this
        }

        val path = findPath(element)
        path.reverse()

        var currentNode = Node(element, null, null, Color.RED)

        var index = 0
        while (index < path.size && currentNode.color == Color.RED) {
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

            currentNode = makeBalancedAdd(currentNode, oldNode, oldParent, oldBrother)

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

        if (currentNode.color == Color.RED) {
            currentNode = Node(currentNode.value, currentNode.left, currentNode.right, Color.BLACK)
        }

        return RBTree(currentNode, size + 1)
    }

    private fun rotateRight(
        child: Node,
        parent: Node,
        newColorLeft: Color,
        newColorRight: Color
    ): Node {
        return Node(child.value, child.left, Node(parent.value, child.right, parent.right, newColorRight), newColorLeft)
    }

    private fun rotateLeft(
        parent: Node,
        child: Node,
        newColorLeft: Color,
        newColorRight: Color
    ): Node {
        return Node(child.value, Node(parent.value, parent.left, child.left, newColorLeft), child.right, newColorRight)
    }

    private fun makeBalancedAdd(currentNode: Node, oldNode: Node, oldParent: Node, oldBrother: Node?): Node {
        if (oldBrother?.color?:Color.BLACK == Color.RED) {
            val newNode = if (currentNode.value < oldNode.value) {
                Node(oldNode.value, currentNode, oldNode.right, Color.BLACK)
            } else {
                Node(oldNode.value, oldNode.left, currentNode, Color.BLACK)
            }
            val newBrother = Node(oldBrother!!.value, oldBrother.left, oldBrother.right, Color.BLACK)
            return if (newNode.value < oldParent.value) {
                Node(oldParent.value, newNode, newBrother, Color.RED)
            } else {
                Node(oldParent.value, newBrother, newNode, Color.RED)
            }
        } else {
            return if (oldNode.value < oldParent.value) {
                val newNode = if (currentNode.value < oldNode.value) {
                    Node(oldNode.value, currentNode, oldNode.right, oldNode.color)
                } else {
                    rotateLeft(oldNode, currentNode, oldNode.color, currentNode.color)
                }
                rotateRight(newNode, oldParent, Color.BLACK, Color.RED)
            } else {
                val newNode = if (currentNode.value > oldNode.value) {
                    Node(oldNode.value, oldNode.left, currentNode, oldNode.color)
                } else {
                    rotateRight(currentNode, oldNode, currentNode.color, oldNode.color)
                }
                rotateLeft(oldParent, newNode, Color.RED, Color.BLACK)
            }
        }
    }

    fun remove(element: T): RBTree<T> {
        if (!contains(element)) {
            return this
        }

        TODO("Not yet implemented")
    }

    private data class InvariantsStats<T: Comparable<T>>(val blacks: Int, val min: T?, val max: T?, val color: Color)

    private fun getTreeInvariantsStatsDFS(node: Node?): InvariantsStats<T>? {
        if (node == null) {
            return InvariantsStats(
                blacks = 1,
                min = null,
                max = null,
                color = Color.BLACK
            )
        }
        val statsLeft = getTreeInvariantsStatsDFS(node.left)
        val statsRight = getTreeInvariantsStatsDFS(node.right)

        return if (
            statsLeft == null
            || statsRight == null
            || (statsLeft.max != null && statsLeft.max >= node.value)
            || (statsRight.min != null && statsRight.min <= node.value)
            || (node.color == Color.RED && (statsLeft.color == Color.RED || statsRight.color == Color.RED))
            || (statsLeft.blacks != statsRight.blacks)
        ) { null } else {
            InvariantsStats(
                blacks = statsLeft.blacks + if (node.color == Color.BLACK) 1 else 0,
                min = statsLeft.min ?: node.value,
                max = statsRight.max ?: node.value,
                color = node.color
            )
        }
    }

    fun checkTreeInvariantsSatisfied(): Boolean {
        val stats = getTreeInvariantsStatsDFS(root)
        return (stats != null) && (stats.color == Color.BLACK)
    }
}