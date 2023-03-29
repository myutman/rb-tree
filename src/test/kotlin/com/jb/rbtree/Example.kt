package com.jb.rbtree

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Example {
    @Test
    fun example() {
        val setBlocking = RBTreeSetBlocking<Int>()

        setBlocking.addAll((0..99999).toList())
        val setsBlocking = (0..99).map { setBlocking.clone() }.toList()

        runBlocking {
            repeat(100) { i ->
                launch {
                    setsBlocking[i].retainAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }



        val setNonBlocking = RBTreeSetNonBlocking<Int>()

        setNonBlocking.addAll((0..99999).toList())
        val setsNonBlocking = (0..99).map { setNonBlocking.clone() }.toList()

        runBlocking {
            repeat(100) { i ->
                launch {
                    setsNonBlocking[i].retainAll((1000 * i..1000 * i + 999).toList())
                }
            }
        }
    }
}