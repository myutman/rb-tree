package com.jb.rbtree

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestTreeSetNonBlockingMultithreading {
    val set = RBTreeSetNonBlocking<Int>()

    @BeforeEach
    fun before() {
        set.clear()
    }


    @Test
    fun testAdd() {
        assertTrue { set.isEmpty() }

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
    fun testAddAll() {
        assertTrue { set.isEmpty() }

        runBlocking {
            repeat(100) { i ->
                launch {
                    set.addAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }

        assertTrue { set.containsAll((0..99999).toList()) }
    }

    @Test
    fun testRemove() {
        assertTrue { set.isEmpty() }

        set.addAll((0..99999).toList())

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

    @Test
    fun testRemoveAll() {
        assertTrue { set.isEmpty() }

        set.addAll((0..99999).toList())

        assertTrue { set.containsAll((0..99999).toList()) }

        runBlocking {
            repeat(100) { i ->
                launch {
                    set.removeAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }

        assertTrue { set.isEmpty() }
    }

    @Test
    fun testRetainAll() {
        assertTrue { set.isEmpty() }

        set.addAll((0..99999).toList())

        assertTrue { set.containsAll((0..99999).toList()) }

        runBlocking {
            repeat(100) { i ->
                launch {
                    set.retainAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }

        assertTrue { set.isEmpty() }
    }

    @Test
    fun testCloneAndRetainAll() {
        assertTrue { set.isEmpty() }

        set.addAll((0..99999).toList())

        assertTrue { set.containsAll((0..99999).toList()) }

        val sets = (0..99).map { set.clone() }.toList()

        runBlocking {
            repeat(100) { i ->
                launch {
                    sets[i].retainAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }

        repeat(100) { i ->
            assertTrue { sets[i].containsAll((1000 * i..1000 * i + 999).toList()) }
            assertEquals(expected = 1000, sets[i].size)
            assertFalse { sets[i].contains(1000 * i - 1) }
            assertFalse { sets[i].contains(1000 * i + 1000) }
        }

        assertTrue { set.containsAll((0..99999).toList()) }
    }
}