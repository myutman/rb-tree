package com.jb.rbtree

internal class RBTree<T : Comparable<T>> : Set<T> {

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

    private inner class RBTreeIterator : Iterator<T> {
        private val pathToRoot: ArrayList<Node> = arrayListOf()
        var isNew: Boolean = true

        override fun hasNext(): Boolean {
            if (root == null) return false
            if (pathToRoot.isEmpty()) return isNew
            if (pathToRoot.last().right != null) return true
            for (index in (1..pathToRoot.size - 1).reversed()) {
                if (pathToRoot[index].value < pathToRoot[index - 1].value) return true
            }
            return false
        }

        override fun next(): T {
            if (root == null) throw NoSuchElementException()
            var lastChildIsRight = true
            if (pathToRoot.isNotEmpty() && pathToRoot.last().right == null) {
                while (pathToRoot.isNotEmpty() && lastChildIsRight) {
                    val removed = pathToRoot.removeLast()
                    lastChildIsRight = pathToRoot.isEmpty() || removed.value > pathToRoot.last().value
                }
            }
            if (!lastChildIsRight) {
                return pathToRoot.last().value
            }
            if (pathToRoot.isEmpty()) {
                if (isNew) {
                    isNew = false
                    pathToRoot.add(root)
                } else {
                    throw NoSuchElementException()
                }
            } else {
                pathToRoot.add(pathToRoot.last().right!!)
            }

            while (pathToRoot.last().left != null) {
                pathToRoot.add(pathToRoot.last().left!!)
            }
            return pathToRoot.last().value
        }
    }

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
        return RBTreeIterator()
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all {
            this.contains(it)
        }
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    private fun makeParent(parent: Node, child: Node?, color: Color? = null): Node {
        return if ((child == null && parent.left == null) || (child != null && child.value < parent.value)) {
            Node(parent.value, child, parent.right, color ?: parent.color)
        } else {
            Node(parent.value, parent.left, child, color ?: parent.color)
        }
    }

    private fun makeParent(parent: Node, child1: Node?, child2: Node?, color: Color? = null): Node {
        return if ((child1 == null && parent.left == null) || (child1 != null && child1.value < parent.value)) {
            Node(parent.value, child1, child2, color ?: parent.color)
        } else {
            Node(parent.value, child2, child1, color ?: parent.color)
        }
    }

    private fun removeChild(parent: Node, child: Node): Node {
        return if (parent.value <= child.value) {
            Node(parent.value, parent.left, null, parent.color)
        } else {
            Node(parent.value, null, parent.right, parent.color)
        }
    }

    private fun getBrother(parent: Node, child: Node?): Node? {
        return if ((child == null && parent.left == null) || (child != null && child.value < parent.value)) {
            parent.right
        } else {
            parent.left
        }
    }

    private fun cloneUpdateColor(node: Node, color: Color): Node {
        return if (node.color == color) {
            node
        } else {
            Node(node.value, node.left, node.right, color)
        }
    }

    private fun cloneUpdateValue(node: Node, value: T): Node {
        return if (node.value == value) {
            node
        } else {
            Node(value, node.left, node.right, node.color)
        }
    }

    private fun rotate(
        parent: Node,
        child: Node,
        newParentColor: Color,
        newChildColor: Color,
    ): Node {
        return if (child.value < parent.value) {
            Node(child.value, child.left, Node(parent.value, child.right, parent.right, newChildColor), newParentColor)
        } else {
            Node(child.value, Node(parent.value, parent.left, child.left, newChildColor), child.right, newParentColor)
        }
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

    private fun findRemovePath(element: T): ArrayList<Node> {
        val path = findPath(element)

        if (path.last().left != null && path.last().right != null) {
            val deleted = path.removeLast()
            val path1 = arrayListOf<Node>()
            var node1: Node = deleted.right!!
            while (node1.left != null) {
                path1.add(node1)
                node1 = node1.left!!
            }
            path.add(cloneUpdateValue(deleted, node1.value))
            path.addAll(path1)
            path.add(node1)
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

        var currentNode = Node(element, null, null, Color.RED)

        while (path.isNotEmpty() && currentNode.color == Color.RED) {
            val oldNode = path.removeLast()
            if (oldNode.color == Color.BLACK) {
                path.add(oldNode)
                break
            }

            /**
             * Should actually never been called because root is never red
             */
            if (path.isEmpty()) {
                currentNode = makeParent(oldNode, currentNode, Color.BLACK)
                break
            }

            val oldParent = path.removeLast()
            val oldBrother = getBrother(oldParent, oldNode)
            currentNode = makeBalancedAdd(currentNode, oldNode, oldParent, oldBrother)
        }

        while (path.isNotEmpty()) {
            val oldNode = path.removeLast()
            currentNode = makeParent(oldNode, currentNode)
        }
        currentNode = cloneUpdateColor(currentNode, Color.BLACK)

        return RBTree(currentNode, size + 1)
    }

    private fun makeBalancedAdd(currentNode: Node, oldNode: Node, oldParent: Node, oldBrother: Node?): Node {
        if ((oldBrother?.color ?: Color.BLACK) == Color.RED) {
            val newNode = makeParent(oldNode, currentNode, Color.BLACK)
            val newBrother = cloneUpdateColor(oldBrother!!, Color.BLACK)
            return makeParent(oldParent, newNode, newBrother, Color.RED)
        } else {
            val newNode = if ((currentNode.value < oldNode.value) == (oldNode.value < oldParent.value)) {
                makeParent(oldNode, currentNode)
            } else {
                rotate(oldNode, currentNode, oldNode.color, currentNode.color)
            }
            return rotate(oldParent, newNode, Color.BLACK, Color.RED)
        }
    }

    fun remove(element: T): RBTree<T> {
        if (!contains(element)) {
            return this
        }

        if (root?.left == null && root?.right == null) {
            return RBTree()
        }

        val path = findRemovePath(element)
        val deleted = path.removeLast()

        var currentNode: Node? = if (deleted.left == null) {
            if (deleted.right == null) {
                val oldNode = path.removeLast()
                val newNode = removeChild(oldNode, deleted)
                path.add(newNode)
            }
            deleted.right
        } else {
            deleted.left
        }
        var balanceSpoilt = (deleted.color == Color.BLACK)

        while (balanceSpoilt && currentNode?.color != Color.RED && !path.isEmpty()) {
            val oldNode = path.removeLast()
            val oldBrother: Node = getBrother(oldNode, currentNode)!! /* Should be not null due to invariant */

            if (oldBrother.color == Color.RED) {
                val newParent = rotate(oldNode, oldBrother, Color.BLACK, Color.RED)
                val newNode =
                    getBrother(newParent, getBrother(newParent, oldNode))!! /* Should be not null due to invariant */
                path.add(newParent)
                path.add(newNode)
            } else {
                val (balanceFixed, newNode) = makeBalancedRemove(oldBrother, oldNode, currentNode)
                balanceSpoilt = !balanceFixed
                currentNode = newNode
            }
        }

        if (balanceSpoilt && currentNode?.color == Color.RED) {
            currentNode = cloneUpdateColor(currentNode, Color.BLACK)
        }

        while (!path.isEmpty()) {
            val oldNode = path.removeLast()
            currentNode = makeParent(oldNode, currentNode)
        }
        currentNode = cloneUpdateColor(currentNode!!, Color.BLACK)

        return RBTree(currentNode, size - 1)
    }

    private fun makeBalancedRemove(oldBrother: Node, oldNode: Node, currentNode: Node?): Pair<Boolean, Node?> {
        return if ((oldBrother.left?.color ?: Color.BLACK) == Color.BLACK && (oldBrother.right?.color
                ?: Color.BLACK) == Color.BLACK
        ) {
            val newBrother = cloneUpdateColor(oldBrother, Color.RED)
            Pair(oldNode.color == Color.RED, makeParent(oldNode, newBrother, currentNode, Color.BLACK))
        } else {
            val (oldNephew, otherNephew) = if (oldBrother.value > oldNode.value) {
                Pair(oldBrother.left, oldBrother.right)
            } else {
                Pair(oldBrother.right, oldBrother.left)
            }
            val newBrother = if (oldNephew?.color == Color.RED) {
                rotate(oldBrother, oldNephew, Color.BLACK, Color.BLACK)
            } else {
                makeParent(oldBrother, cloneUpdateColor(otherNephew!!, Color.BLACK), Color.BLACK)
            }
            val newNode = makeParent(oldNode, currentNode, newBrother)
            Pair(true, rotate(newNode, newBrother, oldNode.color, Color.BLACK))
        }
    }

    private data class InvariantsStats<T : Comparable<T>>(val blacks: Int, val min: T?, val max: T?, val color: Color)

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
        ) {
            null
        } else {
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