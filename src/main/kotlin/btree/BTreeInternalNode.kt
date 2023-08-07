package btree

class BTreeInternalNode<K: Comparable<K>, V> : BTreeNode<K, V> {
    // len(keys) + 1 = len(children)
    var keys: MutableList<K> = mutableListOf()
    var children: MutableList<BTreeNode<K,V>> = mutableListOf()

    fun addLeafNode(node: BTreeLeafNode<K,V>){
        
    }
}