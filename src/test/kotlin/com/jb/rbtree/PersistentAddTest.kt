package com.jb.rbtree

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PersistentAddTest {
    @Test
    fun checkAddPersistentSmall() {
        val randomizer = Random(339264239)

        val sets = arrayListOf(setOf<Int>())
        val trees = arrayListOf(RBTree<Int>())

        for (i in 0..25) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt()
            trees.add(trees[index].add(value))
            assertTrue(trees.last().checkTreeInvariantsSatisfied(), "iteration $i, added value $value")
            val newSet = sets[index].toMutableSet()
            newSet.add(value)
            sets.add(newSet.toSet())
        }

        for (i in 0..25) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt()
            Assertions.assertEquals(sets[index].contains(value), trees[index].contains(value))
        }
    }

    @Test
    fun checkAddPersistentLarge() {
        val randomizer = Random(339264239)

        val sets = arrayListOf(setOf<Int>())
        val trees = arrayListOf(RBTree<Int>())

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt()
            trees.add(trees[index].add(value))
            assertTrue(trees.last().checkTreeInvariantsSatisfied(), "iteration $i, added value $value")
            val newSet = sets[index].toMutableSet()
            newSet.add(value)
            sets.add(newSet.toSet())
        }

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt()
            Assertions.assertEquals(sets[index].contains(value), trees[index].contains(value))
        }
    }

    @Test
    fun checkAddPersistentLargeWithSmallValues() {
        val randomizer = Random(339264239)

        val sets = arrayListOf(setOf<Int>())
        val trees = arrayListOf(RBTree<Int>())

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000)
            trees.add(trees[index].add(value))
            assertTrue(trees.last().checkTreeInvariantsSatisfied(), "iteration $i, added value $value")
            val newSet = sets[index].toMutableSet()
            newSet.add(value)
            sets.add(newSet.toSet())
        }

        for (i in 0..999) {
            val index = randomizer.nextInt(from = 0, until = trees.size)
            val value = randomizer.nextInt(-1000, 1000)
            Assertions.assertEquals(sets[index].contains(value), trees[index].contains(value))
        }
    }
}