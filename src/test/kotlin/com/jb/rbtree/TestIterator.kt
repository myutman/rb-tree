package com.jb.rbtree

import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class TestIterator {
    @Test
    fun checkIteratorBlockingStringSmall() {
        val set = RBTreeSetBlocking<String>()

        set.add("babba")
        set.add("baba")
        set.add("foo")
        set.add("bar")
        set.add("")
        set.add("aboba")
        set.add("abobus")
        set.add("abaca")
        set.add("abacan")
        set.add("aaba")

        val list = set.iterator().asSequence().toList()
        assertEquals(
            expected = arrayListOf("", "aaba", "abaca", "abacan", "aboba", "abobus", "baba", "babba", "bar", "foo"),
            actual = list
        )
    }

    @Test
    fun checkIteratorNonBlockingStringSmall() {
        val set = RBTreeSetNonBlocking<String>()

        set.add("babba")
        set.add("baba")
        set.add("foo")
        set.add("bar")
        set.add("")
        set.add("aboba")
        set.add("abobus")
        set.add("abaca")
        set.add("abacan")
        set.add("aaba")

        val list = set.iterator().asSequence().toList()
        assertEquals(
            expected = arrayListOf("", "aaba", "abaca", "abacan", "aboba", "abobus", "baba", "babba", "bar", "foo"),
            actual = list
        )
    }

    @Test
    fun checkIteratorBlockingIntLarge() {
        val set = RBTreeSetBlocking<Int>()

        val random = Random(339264239)
        val expected = (0..99999).toList()
        val input = expected.shuffled(random)

        for (e in input) {
            set.add(e)
        }

        val actual = set.iterator().asSequence().toList()
        assertEquals(
            expected = expected,
            actual = actual
        )
    }

    @Test
    fun checkIteratorNonBlockingIntLarge() {
        val set = RBTreeSetNonBlocking<Int>()

        val random = Random(339264239)
        val expected = (0..99999).toList()
        val input = expected.shuffled(random)

        for (e in input) {
            set.add(e)
        }

        val actual = set.iterator().asSequence().toList()
        assertEquals(
            expected = expected,
            actual = actual
        )
    }
}