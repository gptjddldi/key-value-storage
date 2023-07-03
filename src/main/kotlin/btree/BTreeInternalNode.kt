package btree

class BTreeInternalNode<K: Comparable<K>, V> {
    val keys: MutableList<K> = mutableListOf()
    val children: MutableList<BTreeNode<K,V>> = mutableListOf()
}