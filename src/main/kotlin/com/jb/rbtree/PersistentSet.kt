package com.jb.rbtree

interface PersistentSet<T> : Set<T> {
    fun clone(): PersistentSet<T>
    fun clear()
    fun add(element: T): Boolean
    fun addAll(elements: Collection<T>): Boolean
    fun remove(element: T): Boolean
    fun removeAll(elements: Collection<T>): Boolean
    fun retainAll(elements: Collection<T>): Boolean
}