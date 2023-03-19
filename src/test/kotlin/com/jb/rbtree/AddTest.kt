package com.jb.rbtree

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AddTest {

    @Test
    fun checkAddNonPersistent() {
        var tree = RBTree<Int>()

        assertFalse(tree.contains(5))
        tree = tree.add(5)
        assertTrue(tree.contains(5))
        assertFalse(tree.containsAll(listOf(5, 2)))
        tree = tree.add(2)
        assertTrue(tree.containsAll(listOf(5, 2)))
        assertFalse(tree.containsAll(listOf(5, 2, 1)))
        tree = tree.add(1)
        assertTrue(tree.containsAll(listOf(5, 2, 1)))
        assertFalse(tree.containsAll(listOf(5, 2, 1, 17)))
        tree = tree.add(17)
        assertTrue(tree.containsAll(listOf(5, 2, 1, 17)))
        assertFalse(tree.containsAll(listOf(5, 2, 1, 17, 99)))
        tree = tree.add(99)
        assertTrue(tree.containsAll(listOf(5, 2, 1, 17, 99)))
    }

}