package com.jb.rbtree

<<<<<<< HEAD
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
=======
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

import kotlinx.coroutines.runBlocking
>>>>>>> db9272e... Added tests for TreeSet implementations. Created test class for multithreading test

class TestTreeSetBlockingMultithreading {

    @Test
    fun testAdd() {
        val set = RBTreeSetBlocking<Int>()
<<<<<<< HEAD

        runBlocking {
            repeat(100) { i ->
                launch {
                    repeat(1000) { j ->
                        set.add(1000 * i + j)
                    }
                }
            }
        }

        assertTrue { set.containsAll((0..99999).toList()) }
=======
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
>>>>>>> db9272e... Added tests for TreeSet implementations. Created test class for multithreading test
    }

    @Test
    fun testRemove() {
        val set = RBTreeSetBlocking<Int>()
<<<<<<< HEAD

        set.addAll(0..99999)

        assertTrue { set.containsAll((0..99999).toList()) }

        runBlocking {
            repeat(100) { i ->
                launch {
                    repeat(1000) { j ->
                        set.remove(1000 * i + j)
                    }
                }
            }
        }

        assertTrue { set.isEmpty() }
=======
        val elements = mutableSetOf<Int>()

        set.addAll(-100000..100000)
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
>>>>>>> db9272e... Added tests for TreeSet implementations. Created test class for multithreading test
    }
}