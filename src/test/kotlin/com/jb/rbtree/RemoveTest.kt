package com.jb.rbtree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RemoveTest {
    @Test
    fun checkRemoveNonPersistent() {
        var tree = RBTree<Int>()


        for (value in listOf(5, 2, 1, 17, 99)) {
            tree = tree.add(value)
        }

        assertTrue(tree.containsAll(listOf(5, 2, 1, 17, 99)))
        for (value in listOf(5, 2, 1, 17, 99)) {
            assertTrue(tree.contains(value), "value = $value")
            tree = tree.remove(value)
            assertTrue(tree.checkTreeInvariantsSatisfied())
            assertFalse(tree.contains(value), "value = $value")
        }
    }

    @Test
    fun checkRemoveNonPersistentLargeWithSmallValues() {
        var tree = RBTree<Int>()

        val randomizer = Random(339264239)

        val set = mutableSetOf<Int>()
        for (i in 0..999) {
            val value = randomizer.nextInt(from = -1000, until = 999)
            tree = tree.add(value)
            set.add(value)
        }

        assertTrue(tree.containsAll(set))

        for (i in 0..999) {
            val value = randomizer.nextInt(from = -1000, until = 999)
            try {
                tree = tree.remove(value)
            } catch (e: NullPointerException) {
                throw RuntimeException("Null pointer i = $i, value = $value")
            }
            set.remove(value)
            assertFalse(tree.contains(value))
            assertTrue(tree.checkTreeInvariantsSatisfied(), "Invariants unsatisfied i = $i, value = $value")
        }

        assertTrue(tree.containsAll(set))

        for (i in 0..999) {
            val value = randomizer.nextInt(from = -1000, until = 999)
            assertEquals(tree.contains(value), set.contains(value))
        }

        assertTrue(tree.checkTreeInvariantsSatisfied())
    }
}