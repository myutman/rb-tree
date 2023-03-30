# Implementation of persistent red-black tree

## Example
<a href="https://github.com/myutman/rb-tree/blob/main/src/test/kotlin/com/jb/rbtree/Example.kt">Example file</a>

```kotlin
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
```

## Interface

### Persistent set
This project provides the interface of ```PersistentSet<T>``` which is basically an immutable set (it actually implements interface ```Set```) with additional modification methods. It's not actually a mutable set because each modification creates a brand new state. However this interface provides a fast cloning operation.

So, the set of methods is as follows:
* ```.contains()```, ```.containsAll()```, ```.size``` and ```.isEmpty()``` are the same as in ```Set```. Check them out <a href="https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/">here</a>.
* ```.add()``` adds the specified element to this instance of a collection.
* ```.addAll()``` adds all the elements of the specified collection to this instance of a collection.
* ```.remove()``` removes a single instance of the specified element from this instance of a collection, if it is present.
* ```.removeAll()``` removes all of this collection's instance's elements that are also contained in the specified collection.
* ```.retainAll()``` retains only the elements in this instance of a collection that are contained in the specified collection.
* ```.clear()``` removes all elements from this instance of a collection.
* ```.clone()``` clones the collection as a new instance.

### RBTreeSetBlocking
Thread-safe implementation of ```PersistentSet``` in blocking style using mutex. Additionally it supports an iterator over elements in the ascending order by a comparator in the ```.iterator()``` method, because it's based on red-black tree.

### RBTreeSetNonBlocking
Thread-safe implementation of ```PersistentSet``` in non-blocking style using ```AtomicReference``` and ```.compareAndSet()``` method. It also supports an iterator over elements in the ascending order by a comparator in the ```.iterator()``` method, because it's also based on red-black tree.

## Algorithm

### Non-persistent
The description of a non-persistent algorithm could be reached <a href="https://neerc.ifmo.ru/wiki/index.php?title=Красно-черное_дерево">here</a>.

### Persistent

To make the data structure described above persistent we can do the following thing. Each time we should change the contents of a node instead of it we create a new node. By content I mean stored value, color of node or link to another node. It's important to do to keep the previous tree structure intact. Thus I use class Node with immutable members to guarantee the safety of the structure.

Also because of persistency we can't afford storing the parent in the node, because once the root have changed, all his children must be recreated as soon as their children and so on, so we should recreate the entire tree which is $O(N)$ memort complexity per operation where $N$ is the current number of elements.

So instead of parent links to perform the operations I use the stack that contains nodes on the path from the root. It can be proven that for each node on the path we change at most $O(1)$ new nodes, so due to tree depth invariant it would be $O(\log N)$ new nodes per operation while the rest of the tree structure remains intact.

We can notice that each moifying operation creates a new root, so we can use the root as the access point to the entire tree state. So, the persistent structure can just store the root of the required tree state and to clone the structure it's just needed to copy the link to the root.

So the ```add()``` and ```remove()``` operations both works in a logarithmic time from the number of elements and use a logarithmic additional memory.

Operation ```clone()``` is performed in $O(1)$ time just by creaing a new tree with te same root. It requires almost no additional memory.

Operation ```contains()``` is not modifying and can be performed using a descent in a Binary Search Tree, which RBTree actually is. It's performed in logarithmic time and requires $O(1)$ additional memory.

## Build result
![TeamCity build status](https://myutman.teamcity.com/app/rest/builds/buildType:id:RbTree_Build,branch:name:main/statusIcon.svg)
