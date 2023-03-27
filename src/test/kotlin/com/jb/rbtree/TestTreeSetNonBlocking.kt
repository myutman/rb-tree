package com.jb.rbtree

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestTreeSetNonBlocking {

    @Test
    fun testAdd() {
        val set = RBTreeSetNonBlocking<Int>()
        val elements = mutableSetOf<Int>()

        val random = Random(339264239)

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            set.add(element)
            elements.add(element)
        }

        assertTrue(set.containsAll(elements))

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            assertTrue(elements.contains(element) == set.contains(element))
        }
    }

    @Test
    fun testRemove() {
        val set = RBTreeSetNonBlocking<Int>()
        val elements = mutableSetOf<Int>()

        set.addAll((-100000..100000).toList())
        elements.addAll(-100000..100000)

        val random = Random(339264239)

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            set.remove(element)
            elements.remove(element)
        }

        assertTrue(set.containsAll(elements))

        repeat(100000) {
            val element = random.nextInt(from = -100000, until = 100000)
            assertTrue(elements.contains(element) == set.contains(element))
        }
    }
}