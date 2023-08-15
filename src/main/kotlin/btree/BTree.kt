package btree

class BTree<K: Comparable<K>, V> {
    var root: BTreeNode<K,V>? = BTreeLeafNode()
//    private var leftLeafNode: BTreeLeafNode? = null
//    private var rightLeafNode: BTreeLeafNode? = null

//    fun insert(k: K, v: V) {
//        if (root == null){
//            val leafNode = BTreeLeafNode<K, V>()
//            leafNode.entries.add(BTreeEntry(k,v))
//            root = leafNode
//
//            leftLeafNode = leafNode
//            rightLeafNode = leafNode
//        } else{
//            val leafNode = findLeafNode(k)
//            leafNode.addEntry(k,v)
//
//            if (leafNode.entries.size > MAX_ENTRIES_PER_NODE){
//                splitLeafNode(leafNode)
//            }
//        }
//    }
//
//    private fun splitLeafNode(node: BTreeLeafNode<K,V>) {
//        var newLeafNode = BTreeLeafNode<K,V>()
//        val splitIndex = (MAX_ENTRIES_PER_NODE + 1) / 2
//        val splitKey = node.entries[splitIndex].key
//        val entriesToMove = node.entries.subList(splitIndex, node.entries.size).toMutableList()
//        newLeafNode.entries.addAll(entriesToMove)
//        entriesToMove.clear()
//
//        node.entries.subList(splitIndex, node.entries.size).clear()
//
//        newLeafNode.right = node.right
//        newLeafNode.left = node
//        node.right = newLeafNode
//
//        val parentNode: BTreeInternalNode<K,V>? = findParentNode(node)
//
//        if (parentNode != null){
//            parentNode.addNode(newLeafNode)
//
//            if (parentNode.children.size > MAX_ENTRIES_PER_NODE){
//                splitInternalNode(parentNode)
//            }
//        } else {
//            val internalNode = BTreeInternalNode<K,V>()
//            internalNode.addNode(node)
//            internalNode.addNode(newLeafNode)
//            root = internalNode
//        }
//    }
//
//    private fun splitInternalNode(node: BTreeInternalNode<K,V>) {
//
//        // node 와 internalNode 각각을 어떻게 해야 하지?
//        // internal node 를 2개로 나눈다는 건 parent node 에 new node 을 add 한다는 의미지
//        // add method 에서 split 까지 해야 할까?
//        var newInternalNode = BTreeInternalNode<K,V>()
//        val splitIndex = (MAX_ENTRIES_PER_NODE + 1) / 2
//
//        newInternalNode.keys = node.keys.subList(splitIndex, MAX_ENTRIES_PER_NODE + 1)
//        newInternalNode.children = node.children.subList(splitIndex, MAX_ENTRIES_PER_NODE + 1)
//
//        node.children.subList(splitIndex, MAX_ENTRIES_PER_NODE + 1).clear()
//        node.keys.subList(splitIndex, MAX_ENTRIES_PER_NODE + 1).clear()
//
//        val parentNode: BTreeInternalNode<K,V>? = findParentNode(node)
//    }
//
//    private fun findLeafNode(key: K): BTreeLeafNode<K,V> {
//        var currentNode = root
//
//        while (currentNode is BTreeInternalNode<*, *>) {
//            var tmpNode = currentNode as BTreeInternalNode<K, V>
//            var idx = 0
//            while (idx < tmpNode.keys.size && key > tmpNode.keys[idx]) {
//                idx ++
//            }
//            currentNode = tmpNode.children[idx]
//        }
//        return currentNode as BTreeLeafNode<K, V>
//    }
//
//    private fun findParentNode(target: BTreeNode<K,V>): BTreeInternalNode<K,V>? {
//        if (root == target){
//            return null
//        }
//        return findParentNode(root as BTreeInternalNode<K, V>, target)
//    }
//
//    private fun findParentNode(current: BTreeInternalNode<K,V>, target: BTreeNode<K,V>): BTreeInternalNode<K,V>?{
//        if (current.children.contains(target)){
//            return current
//        }
//        for (child in current.children){
//            if (child is BTreeInternalNode<*, *>){
//                val parent = findParentNode(child as BTreeInternalNode<K, V>, target)
//                if (parent != null){
//                    return parent
//                }
//            }
//        }
//        return null
//    }
//

    companion object{
        const val MAX_ENTRIES_PER_NODE = 4
    }

    inner class BTreeInternalNode: BTreeNode<K, V> {
        override var keys: MutableList<K> = mutableListOf()
        var children: MutableList<BTreeNode<K, V>> = mutableListOf()

        override fun getValue(key: K): V? {
            return getChild(key).getValue(key)
        }

        override fun split(): BTreeInternalNode {
            val from = (MAX_ENTRIES_PER_NODE) / 2 + 1
            val to = MAX_ENTRIES_PER_NODE

            val newInternalNode = BTreeInternalNode()
            newInternalNode.keys = keys.subList(from, to)
            newInternalNode.children = children.subList(from, to + 1)

            keys.subList(from - 1, to).clear()
            children.subList(from, to + 1).clear()

            return newInternalNode
        }

        override fun remove(key: K) {
            TODO("Not yet implemented")
        }

        override fun insert(key: K, value: V) {
            val child = getChild(key)
            child.insert(key, value)

            if (child.isOverflow()) {
                val newNode = child.split()
                insertChild(newNode.keys.first(), newNode)
            }
            if (root!!.isOverflow()){
                val newInternalNode = split()
                val newRoot = BTreeInternalNode()
                newRoot.keys.add(newInternalNode.keys.first())
                newRoot.children.add(this)
                newRoot.children.add(newInternalNode)
                root = newRoot
            }
        }

        override fun merge(node: BTreeNode<K, V>) {
            TODO("Not yet implemented")
        }

        override fun isOverflow(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isUnderflow(): Boolean {
            TODO("Not yet implemented")
        }

        private fun getChild(key: K): BTreeNode<K, V> {
            var loc = keys.binarySearch(key)
            loc = if (loc >= 0) loc + 1 else -(loc + 1)
            return children[loc]
        }

        private fun insertChild(key: K, node: BTreeNode<K,V>) {
            val loc = keys.binarySearch(key)
            val childIndex = if (loc >= 0) loc + 1 else -(loc + 1)
            if (loc >= 0){
                children[childIndex] = node
            } else {
                keys.add(childIndex, key)
                children.add(childIndex, node)
            }
        }
    }

    inner class BTreeLeafNode: BTreeNode<K, V> {
        var next: BTreeLeafNode? = null

        override var keys: MutableList<K> = mutableListOf()
        private var values: MutableList<V> = mutableListOf()

        override fun getValue(key: K): V? {
            val loc = keys.binarySearch(key)
            return if (loc >=0) values[loc] else null
        }

        override fun split(): BTreeLeafNode {
            val newLeafNode = BTreeLeafNode()
            val from = (MAX_ENTRIES_PER_NODE + 1) / 2
            val to = MAX_ENTRIES_PER_NODE

            newLeafNode.keys.addAll(keys.subList(from, to))
            newLeafNode.values.addAll(values.subList(from, to))

            keys.subList(from, to).clear()
            values.subList(from, to).clear()

            newLeafNode.next = next
            next = newLeafNode

            return newLeafNode
        }

        override fun remove(key: K) {
            TODO("Not yet implemented")
        }

        override fun insert(key: K, value: V) {
            val loc = keys.binarySearch(key)
            val valueIdx = if(loc >= 0) loc else -(loc + 1)
            if (loc >= 0) {
                values[valueIdx] = value
            } else {
                keys.add(valueIdx, key)
                values.add(valueIdx, value)
            }

            if (root!!.isOverflow()){
                val newLeafNode = split()
                val newRoot = BTreeInternalNode()
                newRoot.keys.add(newLeafNode.keys.first())
                newRoot.children.add(this)
                newRoot.children.add(newLeafNode)
                root = newRoot
            }
        }

        override fun merge(node: BTreeNode<K, V>) {
            TODO("Not yet implemented")
        }

        override fun isOverflow(): Boolean {
            return values.size > MAX_ENTRIES_PER_NODE
        }

        override fun isUnderflow(): Boolean {
            TODO("Not yet implemented")
        }
    }
}