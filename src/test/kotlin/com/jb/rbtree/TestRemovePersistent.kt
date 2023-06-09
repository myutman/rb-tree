package com.jb.rbtree

import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRemovePersistent {
    @Test
    fun checkRemovePersistentLargeWithSmallValues() {
        val randomizer = Random(339264239)

        var tree = RBTree<Int>()
        val set = mutableSetOf<Int>()

        for (i in 0..999) {
            val value = randomizer.nextInt(-1000, 1000)
            tree = tree.add(value)
            set.add(value)
        }

        val sets = arrayListOf(set.toSet())
        val trees = arrayListOf(tree)

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000)
            trees.add(trees[index].remove(value))
            assertTrue(trees.last().checkTreeInvariantsSatisfied(), "iteration $i, added value $value")
            val newSet = sets[index].toMutableSet()
            newSet.remove(value)
            sets.add(newSet.toSet())
        }

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000)
            assertEquals(sets[index].contains(value), trees[index].contains(value))
        }
    }

    @Test
    fun checkRemovePersistentLargeString() {
        val randomizer = Random(339264239)

        var tree = RBTree<String>()
        val set = mutableSetOf<String>()

        for (i in 0..999) {
            val value = randomizer.nextInt(-1000, 1000).toString()
            tree = tree.add(value)
            set.add(value)
        }

        val sets = arrayListOf(set.toSet())
        val trees = arrayListOf(tree)

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000).toString()
            trees.add(trees[index].remove(value))
            assertTrue(trees.last().checkTreeInvariantsSatisfied(), "iteration $i, added value $value")
            val newSet = sets[index].toMutableSet()
            newSet.remove(value)
            sets.add(newSet.toSet())
        }

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000).toString()
            assertEquals(sets[index].contains(value), trees[index].contains(value))
        }
    }
}