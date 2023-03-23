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

        var currentNode = Node(element, null, null, Color.RED)

        while (path.isNotEmpty() && currentNode.color == Color.RED) {
            val oldNode = path.removeLast()
            if (oldNode.color == Color.BLACK) {
                path.add(oldNode)
                break
            }

            if (path.isEmpty()) {
                currentNode = if (currentNode.value < oldNode.value) {
                    Node(oldNode.value, currentNode, oldNode.right, Color.BLACK)
                } else {
                    Node(oldNode.value, oldNode.left, currentNode, Color.BLACK)
                }
                break
            }

            val oldParent = path.removeLast()

            val oldBrother = if (oldNode.value < oldParent.value) {
                oldParent.right
            } else {
                oldParent.left
            }

            currentNode = makeBalancedAdd(currentNode, oldNode, oldParent, oldBrother)
        }

        while (path.isNotEmpty()) {
            val oldNode = path.removeLast()
            currentNode = if (currentNode.value < oldNode.value) {
                Node(oldNode.value, currentNode, oldNode.right, oldNode.color)
            } else {
                Node(oldNode.value, oldNode.left, currentNode, oldNode.color)
            }
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
        if (oldBrother?.color ?: Color.BLACK == Color.RED) {
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

        val path = findPath(element)
        val deleted = path.removeLast()

        if (deleted.left == null && deleted.right == null && deleted == root) {
            return RBTree()
        }

        var (currentNode: Node?, balanceSpoilt: Boolean) = if (deleted.left == null) {
            if (deleted.right == null) {
                val oldNode = path.removeLast()
                val newNode = if (oldNode.value < deleted.value) {
                    Node(oldNode.value, oldNode.left, null, oldNode.color)
                } else {
                    Node(oldNode.value, null, oldNode.right, oldNode.color)
                }
                path.add(newNode)
            }
            Pair(deleted.right, deleted.color == Color.BLACK)
        } else if (deleted.right == null) {
            Pair(deleted.left, deleted.color == Color.BLACK)
        } else {
            val path1 = arrayListOf<Node>()
            var node1: Node = deleted.right
            while (node1.left != null) {
                path1.add(node1)
                node1 = node1.left!!
            }
            path.add(Node(node1.value, deleted.left, deleted.right, deleted.color))
            path.addAll(path1)
            if (node1.right == null) {
                val oldNode = path.removeLast()
                val newNode =
                    if (oldNode.value <= node1.value) { /* == happens when it's exactly right son, so still this branch */
                        Node(oldNode.value, oldNode.left, null, oldNode.color)
                    } else {
                        Node(oldNode.value, null, oldNode.right, oldNode.color)
                    }
                path.add(newNode)
            }
            Pair(node1.right, node1.color == Color.BLACK)
        }

        while (balanceSpoilt && currentNode?.color != Color.RED && !path.isEmpty()) {
            val oldNode = path.removeLast()
            val oldBrother: Node = if ((currentNode == null && oldNode.right == null)
                || (currentNode != null && oldNode.value < currentNode.value)
            ) {
                oldNode.left
            } else {
                oldNode.right
            }!! /* Should be not null due to invariant */

            if (oldBrother.color == Color.RED) {
                val newParent = if (oldBrother.value > oldNode.value) {
                    rotateLeft(oldNode, oldBrother, Color.RED, Color.BLACK)
                } else {
                    rotateRight(oldBrother, oldNode, Color.BLACK, Color.RED)
                }

                val newNode = if (oldBrother.value > oldNode.value) {
                    newParent.left
                } else {
                    newParent.right
                }!! /* Should be not null due to invariant */
                path.add(newParent)
                path.add(newNode)
            } else {
                if (oldBrother.left?.color ?: Color.BLACK == Color.BLACK && oldBrother.right?.color ?: Color.BLACK == Color.BLACK) {
                    if (oldNode.color == Color.RED) {
                        balanceSpoilt = false
                    }
                    currentNode = if (oldBrother.value > oldNode.value) {
                        Node(
                            oldNode.value,
                            currentNode,
                            Node(oldBrother.value, oldBrother.left, oldBrother.right, Color.RED),
                            Color.BLACK
                        )
                    } else {
                        Node(
                            oldNode.value,
                            Node(oldBrother.value, oldBrother.left, oldBrother.right, Color.RED),
                            currentNode,
                            Color.BLACK
                        )
                    }
                } else if (oldBrother.value > oldNode.value) {
                    val newBrother = if (oldBrother.left?.color == Color.RED) {
                        rotateRight(oldBrother.left, oldBrother, Color.BLACK, Color.BLACK)
                    } else {
                        Node(
                            oldBrother.value,
                            oldBrother.left,
                            Node(oldBrother.right!!.value, oldBrother.right.left, oldBrother.right.right, Color.BLACK),
                            Color.BLACK
                        )
                    }
                    val newNode = Node(oldNode.value, currentNode, newBrother, oldNode.color)
                    currentNode = rotateLeft(newNode, newBrother, Color.BLACK, oldNode.color)
                    balanceSpoilt = false
                } else {
                    val newBrother = if (oldBrother.right?.color == Color.RED) {
                        rotateLeft(oldBrother, oldBrother.right, Color.BLACK, Color.BLACK)
                    } else {
                        Node(
                            oldBrother.value,
                            Node(oldBrother.left!!.value, oldBrother.left.left, oldBrother.left.right, Color.BLACK),
                            oldBrother.right,
                            Color.BLACK
                        )
                    }
                    val newNode = Node(oldNode.value, newBrother, currentNode, oldNode.color)
                    currentNode = rotateRight(newBrother, newNode, oldNode.color, Color.BLACK)
                    balanceSpoilt = false
                }
            }
        }

        if (balanceSpoilt && currentNode?.color == Color.RED) {
            currentNode = Node(currentNode.value, currentNode.left, currentNode.right, Color.BLACK)
        }

        while (!path.isEmpty()) {
            val oldNode = path.removeLast()
            currentNode = if ((currentNode == null && oldNode.left == null)
                || (currentNode != null && oldNode.value > currentNode.value)
            ) {
                Node(oldNode.value, currentNode, oldNode.right, oldNode.color)
            } else {
                Node(oldNode.value, oldNode.left, currentNode, oldNode.color)
            }
        }

        if (currentNode?.color == Color.RED) {
            currentNode = Node(currentNode.value, currentNode.left, currentNode.right, Color.BLACK)
        }

        return RBTree(currentNode!!, size - 1)
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