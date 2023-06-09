package com.jb.rbtree

/**
 * Blocking thread-safe implementation of red-black tree that implements [PersistentSet]. It supports iteration over
 * elements in the ascendant order
 *
 * @param T the type with full order relation of elements contained in the set. The set is covariant in its element type.
 * @constructor Create empty set
 */
class RBTreeSetBlocking<T : Comparable<T>> : PersistentSet<T> {
    private var state: RBTree<T>

    constructor() {
        this.state = RBTree()
    }

    private constructor(state: RBTree<T>) {
        this.state = state
    }

    override fun add(element: T): Boolean {
        val changed: Boolean
        synchronized(state) {
            val oldSize = state.size
            state = state.add(element)
            changed = state.size != oldSize
        }
        return changed
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val changed: Boolean
        synchronized(state) {
            val oldSize = state.size
            elements.forEach {
                state = state.add(it)
            }
            changed = state.size != oldSize
        }
        return changed
    }

    override fun clear() {
        state = RBTree()
    }

    /**
     * Iterator over the elements of a current state of the set in the ascending order
     */
    override fun iterator(): Iterator<T> {
        return state.iterator()
    }

    override fun remove(element: T): Boolean {
        val changed: Boolean
        synchronized(state) {
            val oldSize = state.size
            state = state.remove(element)
            changed = state.size != oldSize
        }
        return changed
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val changed: Boolean
        synchronized(state) {
            val oldSize = state.size

            elements.forEach {
                state = state.remove(it)
            }
            changed = state.size != oldSize
        }
        return changed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val changed: Boolean
        synchronized(state) {
            val oldState = state
            state = RBTree()

            elements.filter { oldState.contains(it) }.forEach {
                state = state.add(it)
            }
            changed = elements.size != state.size
        }
        return changed
    }

    override val size: Int
        get() = state.size

    override fun contains(element: T): Boolean {
        return state.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        val oldState = state
        return elements.all { oldState.contains(it) }
    }

    override fun isEmpty(): Boolean {
        return state.isEmpty()
    }

    override fun clone(): RBTreeSetBlocking<T> {
        return RBTreeSetBlocking(state)
    }
}