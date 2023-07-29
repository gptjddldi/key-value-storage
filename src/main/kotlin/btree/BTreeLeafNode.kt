package btree

class BTreeEntry <K: Comparable<K>, V>(val key: K, val v: V)

class BTreeLeafNode<K: Comparable<K>, V> : BTreeNode<K, V> {
    val entries: MutableList<BTreeEntry<K,V>> = mutableListOf()
    var left: BTreeLeafNode<K,V>? = null
    var right: BTreeLeafNode<K,V>? = null

    fun addEntry(k: K, v: V){
        var idx = 0
        while (entries.size > idx && entries[idx].key < k) {
            idx ++
        }
        entries.add(idx, BTreeEntry<K,V>(k,v))
    }
}