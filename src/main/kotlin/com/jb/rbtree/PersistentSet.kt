package com.jb.rbtree

interface PersistentSet<T>: MutableSet<T> {
    fun clone(): PersistentSet<T>
}