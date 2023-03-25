package com.jb.rbtree

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TestTreeSetBlockingMultithreading {

    @Test
    fun testAdd() {
        val set = RBTreeSetBlocking<Int>()

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
    }

    @Test
    fun testRemove() {
        val set = RBTreeSetBlocking<Int>()

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
    }
}