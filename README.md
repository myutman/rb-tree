# Implementation of persistent red-black tree

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
