package com.jb.rbtree

/**
 * A generic unordered collection of elements that does not support duplicate elements, and supports
 * adding and removing elements by creating a new state of the inner structure and also supports cloning
 * as a new instance of a collection.
 *
 * @param T the type of elements contained in the set. The set is covariant in its element type.
 */
interface PersistentSet<T> : Set<T> {
    /**
     * Clones the collection as a new instance.
     *
     * @return New collection with exactly same elements
     */
    fun clone(): PersistentSet<T>

    /**
     * Removes all elements from this instance of a collection.
     */
    fun clear()

    /**
     * Adds the specified element to this instance of a collection.
     *
     * @return `true` if the element has been added, `false` if the collection does not support duplicates
     * and the element is already contained in the collection (in this case state is not modified).
     */
    fun add(element: T): Boolean

    /**
     * Adds all the elements of the specified collection to this instance of a collection.
     *
     * @return `true` if any of the specified elements was added to the collection, `false` if the collection was not
     * modified (in this case state is not modified).
     */
    fun addAll(elements: Collection<T>): Boolean

    /**
     * Removes a single instance of the specified element from this
     * instance of a collection, if it is present.
     *
     * @return `true` if the element has been successfully removed; `false` if it was not present in the collection
     * (in this case state is not modified).
     */
    fun remove(element: T): Boolean

    /**
     * Removes all of this collection's instance's elements that are also contained in the specified collection.
     *
     * @return `true` if any of the specified elements was removed from the collection, `false` if the collection was
     * not modified (in this case state is not modified).
     */
    fun removeAll(elements: Collection<T>): Boolean

    /**
     * Retains only the elements in this instance of a collection that are contained in the specified collection.
     *
     * @return `true` if any element was removed from the collection, `false` if the collection was not modified
     * (in this case state is not modified).
     */
    fun retainAll(elements: Collection<T>): Boolean
}