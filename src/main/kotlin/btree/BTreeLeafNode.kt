package btree

class BTreeEntry <K: Comparable<K>, V>(val key: K, val value: V)

class BTreeLeafNode<K: Comparable<K>, V> {
    val entries: MutableList<BTreeEntry<K,V>> = mutableListOf()
}