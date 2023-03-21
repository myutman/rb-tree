package com.jb.rbtree

class RBTreeSet<T : Comparable<T>>(private var state: RBTree<T> = RBTree()) : MutableSet<T> {

    constructor(elements: Collection<T>) : this() {
        addAll(elements)
    }

    override fun add(element: T): Boolean {
        val oldState = state
        val newState = oldState.add(element)
        val changed = newState.size != oldState.size
        state = newState
        return changed
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val oldState = state
        var newState = oldState
        for (element in elements) {
            newState = newState.add(element)
        }
        val changed = newState.size != oldState.size
        state = newState
        return changed
    }

    override fun clear() {
        state = RBTree()
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }

    override fun remove(element: T): Boolean {
        val oldState = state
        val newState = oldState.remove(element)
        val changed = newState.size != oldState.size
        state = newState
        return changed
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val oldState = state
        var newState = oldState

        for (element in elements) {
            newState = newState.remove(element)
        }
        val changed = newState.size != oldState.size
        state = newState
        return changed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val oldState = state
        var newState: RBTree<T> = RBTree()
        var changed = false

        for (element in elements) {
            if (oldState.contains(element)) {
                newState = newState.add(element)
            } else {
                changed = true
            }
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
        var contains = true

        for (element in elements) {
            contains = contains && oldState.contains(element)
        }
        return contains
    }

    override fun isEmpty(): Boolean {
        return state.isEmpty()
    }

    fun clone(): RBTreeSet<T> {
        return RBTreeSet(state)
    }
}