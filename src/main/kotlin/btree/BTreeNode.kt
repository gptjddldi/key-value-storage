package btree

interface BTreeNode<K: Comparable<K>, V> {
    var keys: MutableList<K>

    fun getValue(key: K): V?
    fun delete(key: K)
    fun insert(key: K, value: V)
    fun split(): BTreeNode<K,V>
    fun merge(node: BTreeNode<K,V>)
    fun getFirstLeafKey(): K
    fun isOverflow(): Boolean
    fun isUnderflow(): Boolean
}