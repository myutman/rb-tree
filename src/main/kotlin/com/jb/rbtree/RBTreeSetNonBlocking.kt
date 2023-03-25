package com.jb.rbtree

import java.util.concurrent.atomic.AtomicReference

class RBTreeSetNonBlocking<T : Comparable<T>> : PersistentSet<T> {
    private val state: AtomicReference<RBTree<T>>

    constructor() {
        this.state = AtomicReference(RBTree())
    }

    private constructor(state: RBTree<T>) {
        this.state = AtomicReference(state)
    }

    override fun add(element: T): Boolean {
        var changed: Boolean
        do {
            val oldState = state.get()
            val newState = oldState.add(element)
            changed = newState.size != oldState.size
        } while (!state.compareAndSet(oldState, newState))
        return changed
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var changed: Boolean
        do {
            val oldState = state.get()
            var newState = oldState
            elements.forEach {
                newState = newState.add(it)
            }
            changed = newState.size != oldState.size
        } while (!state.compareAndSet(oldState, newState))
        return changed
    }

    override fun clear() {
        state.set(RBTree())
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }

    override fun remove(element: T): Boolean {
        var changed: Boolean
        do {
            val oldState = state.get()
            val newState = oldState.remove(element)
            changed = newState.size != oldState.size
        } while (!state.compareAndSet(oldState, newState))
        return changed
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var changed: Boolean
        do {
            val oldState = state.get()
            var newState = oldState

            elements.forEach {
                newState = newState.remove(it)
            }
            changed = newState.size != oldState.size
        } while (!state.compareAndSet(oldState, newState))
        return changed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var changed: Boolean
        do {
            val oldState = state.get()
            var newState: RBTree<T> = RBTree()

            elements.filter { oldState.contains(it) }.forEach {
                newState = newState.add(it)
            }

            changed = elements.size != newState.size
        } while (!state.compareAndSet(oldState, newState))
        return changed
    }

    override val size: Int
        get() = state.get().size

    override fun contains(element: T): Boolean {
        return state.get().contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        val oldState = state.get()
        return elements.all { oldState.contains(it) }
    }

    override fun isEmpty(): Boolean {
        return state.get().isEmpty()
    }

    override fun clone(): RBTreeSetNonBlocking<T> {
        return RBTreeSetNonBlocking(state.get())
    }
}