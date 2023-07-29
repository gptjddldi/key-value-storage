package btree

class BTree<K: Comparable<K>, V> {
    private var root: BTreeNode<K, V>? = null
    private var leftLeafNode: BTreeLeafNode<K,V>? = null
    private var rightLeafNode: BTreeLeafNode<K,V>? = null
    fun insert(k: K, v: V) {
        if (root == null){
            val leafNode = BTreeLeafNode<K, V>()
            leafNode.entries.add(BTreeEntry(k,v))
            root = leafNode

            leftLeafNode = leafNode
            rightLeafNode = leafNode
        } else{
            val leafNode = findLeafNode(k)

            leafNode.addEntry(k,v)
        }
    }

    private fun findLeafNode(key: K): BTreeLeafNode<K, V> {
        var currentNode = leftLeafNode as BTreeLeafNode
        while(true) {
            var idx = 0
            while(currentNode.entries.size > idx){
                if(currentNode.entries[idx].key > key){
                    return currentNode
                }
                idx ++
            }
            if (currentNode.right != null) {
                currentNode = currentNode.right as BTreeLeafNode<K, V>
            } else return currentNode
        }
    }
}