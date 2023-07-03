package btree

class BTree<K: Comparable<K>, V> {
    private var root: BTreeNode<K, V>? = null

    fun insert(key: K, value: V) {
        // find leaf node by key: k
        // insert entry
        // if overflow
        //  then split tree
    }

    fun delete(key: K){
        //
    }

    fun search(key: K): V? {
        return null
    }

    private

    fun split() {
        //
    }

    fun merge() {
        //
    }
}