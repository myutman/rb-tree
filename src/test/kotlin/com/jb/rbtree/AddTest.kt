package com.jb.rbtree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

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
        assertTrue(tree.checkTreeInvariantsSatisfied())
        assertTrue(tree.containsAll(listOf(5, 2, 1)))
        assertFalse(tree.containsAll(listOf(5, 2, 1, 17)))
        tree = tree.add(17)
        assertTrue(tree.containsAll(listOf(5, 2, 1, 17)))
        assertFalse(tree.containsAll(listOf(5, 2, 1, 17, 99)))
        tree = tree.add(99)
        assertTrue(tree.containsAll(listOf(5, 2, 1, 17, 99)))
        assertTrue(tree.checkTreeInvariantsSatisfied())
    }

    @Test
    fun checkAddNonPersistentLarge() {
        var tree = RBTree<Int>()

        val randomizer = Random(339264239)

        val set = mutableSetOf<Int>()
        for (i in 0..999) {
            val value = randomizer.nextInt()
            tree = tree.add(value)
            set.add(value)
        }

        assertTrue(tree.containsAll(set))

        for (i in 0..999) {
            val value = randomizer.nextInt()
            assertEquals(set.contains(value), tree.contains(value))
        }

        assertTrue(tree.checkTreeInvariantsSatisfied())
    }

}