package com.jb.rbtree

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestTreeSetBlocking {

    @Test
    fun testAdd() {
        val set = RBTreeSetBlocking<Int>()
        val elements = mutableSetOf<Int>()

        val random = Random(339264239)

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            set.add(element)
            elements.add(element)
        }

        Assertions.assertTrue(set.containsAll(elements))

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            Assertions.assertTrue(elements.contains(element) == set.contains(element))
        }
    }

    @Test
    fun testRemove() {
        val set = RBTreeSetBlocking<Int>()
        val elements = mutableSetOf<Int>()

        set.addAll((-100000..100000).toList())
        elements.addAll(-100000..100000)

        val random = Random(339264239)

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            set.remove(element)
            elements.remove(element)
        }

        Assertions.assertTrue(set.containsAll(elements))

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            Assertions.assertTrue(elements.contains(element) == set.contains(element))
        }
    }
}