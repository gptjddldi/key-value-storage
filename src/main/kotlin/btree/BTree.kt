package btree


class BTree<K: Comparable<K>, V> {
    var root: BTreeNode<K,V>? = BTreeLeafNode()

    fun insert(key: K, value: V) {
        root?.insert(key, value)
    }
    fun delete(key: K){
        root?.delete(key)
    }
    fun print() {
        val queue = ArrayDeque<BTreeNode<K,V>?>()
        queue.add(root)
        while(!queue.isEmpty()){
            var str = ""
            val num = queue.size
            for(i in 1..num){
                var cur = queue.removeFirst()
                str += "${cur?.keys} / "
                if(cur is BTreeInternalNode){
                    queue.addAll(cur.children)
                }
            }
            println(str)
        }
    }

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
            newInternalNode.keys.addAll( keys.subList(from, to))
            newInternalNode.children.addAll(children.subList(from, to + 1))

            keys.subList(from -1 , to).clear()
            children.subList(from, to + 1).clear()

            return newInternalNode
        }

        override fun delete(key: K) {
            val child = getChild(key)
            child.delete(key)

            if(child.isUnderflow()){
                var right = rightSibling(key)
                var left = leftSibling(key)
                right = if(left != null) child else right
                left = left ?: child
                if(right != null){
                    left.merge(right)
                    deleteChild(right.getFirstLeafKey())
                }
                if(left.isOverflow()){
                    val newNode = left.split()
                    insertChild(newNode.keys.first(), newNode)
                }
                if(root?.keys?.size == 0){
                    root = left
                }
            }
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
                newRoot.keys.add(newInternalNode.getFirstLeafKey())
                newRoot.children.add(this)
                newRoot.children.add(newInternalNode)
                root = newRoot
            }
        }

        override fun merge(node: BTreeNode<K, V>) {
            keys.add(node.getFirstLeafKey())
            keys.addAll(node.keys)
            children.addAll((node as BTreeInternalNode).children)
        }

        override fun getFirstLeafKey(): K {
            return children[0].getFirstLeafKey()
        }

        override fun isOverflow(): Boolean {
            return children.size > MAX_ENTRIES_PER_NODE
        }

        override fun isUnderflow(): Boolean {
            return children.size < MAX_ENTRIES_PER_NODE / 2
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
                children.add(childIndex + 1, node)
            }
        }

        private fun deleteChild(key: K) {
            val loc = keys.binarySearch(key)
            if(loc >= 0){
                keys.removeAt(loc)
                children.removeAt(loc + 1)
            }
        }
        private fun leftSibling(key: K): BTreeNode<K, V>? {
            val loc = keys.binarySearch(key)
            val childIndex = if(loc > 0) loc + 1 else -loc - 1
            if (childIndex > 0)
                return children[childIndex - 1]
            return null
        }
        private fun rightSibling(key: K): BTreeNode<K, V>? {
            val loc = keys.binarySearch(key)
            val childIndex = if(loc > 0) loc + 1 else -loc - 1
            if (childIndex < keys.size)
                return children[childIndex + 1]
            return null
        }
    }

    inner class BTreeLeafNode: BTreeNode<K, V> {
        var next: BTreeLeafNode? = null

        override var keys: MutableList<K> = mutableListOf()
        var values: MutableList<V> = mutableListOf()

        override fun getValue(key: K): V? {
            val loc = keys.binarySearch(key)
            return if (loc >=0) values[loc] else null
        }

        override fun split(): BTreeLeafNode {
            val newLeafNode = BTreeLeafNode()
            val from = (MAX_ENTRIES_PER_NODE + 1) / 2
            val to = MAX_ENTRIES_PER_NODE + 1

            newLeafNode.keys.addAll(keys.subList(from, to))
            newLeafNode.values.addAll(values.subList(from, to))

            keys.subList(from, to).clear()
            values.subList(from, to).clear()

            newLeafNode.next = next
            next = newLeafNode

            return newLeafNode
        }

        override fun delete(key: K) {
            val loc = keys.binarySearch(key)
            if (loc >= 0){
                keys.removeAt(loc)
                values.removeAt(loc)
            }
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
            keys.addAll(node.keys)
            values.addAll((node as BTreeLeafNode).values)
            next = node.next
        }

        override fun getFirstLeafKey(): K {
            return keys[0]
        }

        override fun isOverflow(): Boolean {
            return values.size > MAX_ENTRIES_PER_NODE
        }

        override fun isUnderflow(): Boolean {
            return keys.size < MAX_ENTRIES_PER_NODE / 2
        }
    }
}