package btree
import kotlin.collections.ArrayDeque


class BTree<K: Comparable<K>, V> {
    var root: BTreeNode<K,V>? = BTreeLeafNode()

    fun insert(key: K, value: V) {
        root?.insert(key, value)
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
//                if(cur is BTreeLeafNode){
//                    print(cur.values)
//                }
            }
            println(str)
        }
    }

    companion object{
        const val MAX_ENTRIES_PER_NODE = 5
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

        override fun getFirstLeafKey(): K {
            return children[0].getFirstLeafKey()
        }

        override fun isOverflow(): Boolean {
            return children.size > MAX_ENTRIES_PER_NODE
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
                children.add(childIndex + 1, node)
            }
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

        override fun getFirstLeafKey(): K {
            return keys[0]
        }

        override fun isOverflow(): Boolean {
            return values.size > MAX_ENTRIES_PER_NODE
        }

        override fun isUnderflow(): Boolean {
            TODO("Not yet implemented")
        }
    }
}