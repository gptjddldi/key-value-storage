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

            if (leafNode.entries.size > MAX_ENTRIES_PER_NODE){
                splitLeafNode(leafNode)
            }
        }
    }

    private fun splitLeafNode(node: BTreeLeafNode<K,V>) {
        // leaf node 를 2개로 나눔
        // 부모에 대해 splitInternalNode 작업 진행
        var newLeafNode = BTreeLeafNode<K,V>()
        val splitIndex = node.entries.size / 2
        val splitKey = node.entries[splitIndex].key
        val entriesToMove = node.entries.subList(splitIndex, node.entries.size).toMutableList()
        newLeafNode.entries.addAll(entriesToMove)
        entriesToMove.clear()

        node.entries.subList(splitIndex, node.entries.size).clear()

        newLeafNode.right = node.right
        newLeafNode.left = node
        node.right = newLeafNode

        val parentNode: BTreeInternalNode<K,V>? = findParentNode(node)
        // parent 가 있는 경우 / 없는 경우
        // 있으면 addLeafNode / 없으면 새로 만들고, root 로 변경
        if (parentNode != null){
            parentNode.addLeafNode(newLeafNode)

            if (parentNode.children.size > MAX_ENTRIES_PER_NODE){
                splitInternalNode(parentNode)
            }
        } else {
            val internalNode = BTreeInternalNode<K,V>()
            internalNode.addLeafNode(node)
            internalNode.addLeafNode(newLeafNode)
//            internalNode.keys.add(splitKey)
//            internalNode.children.add(node)
//            internalNode.children.add(newLeafNode)
            root = internalNode
        }
    }

    private fun splitInternalNode(node: BTreeInternalNode<K,V>) {

    }

    private fun findLeafNode(key: K): BTreeLeafNode<K,V> {
        var currentNode = root

        while (currentNode is BTreeInternalNode<*, *>) {
            var tmpNode = currentNode as BTreeInternalNode<K, V>
            var idx = 0
            while (idx < tmpNode.keys.size && key > tmpNode.keys[idx]) {
                idx ++
            }
            currentNode = tmpNode.children[idx]
        }
        return currentNode as BTreeLeafNode<K, V>
    }

    private fun findParentNode(target: BTreeNode<K,V>): BTreeInternalNode<K,V>? {
        if (root == target){
            return null
        }
        return findParentNode(root as BTreeInternalNode<K, V>, target)
    }

    private fun findParentNode(current: BTreeInternalNode<K,V>, target: BTreeNode<K,V>): BTreeInternalNode<K,V>?{
        if (current.children.contains(target)){
            return current
        }
        for (child in current.children){
            if (child is BTreeInternalNode<*, *>){
                val parent = findParentNode(child as BTreeInternalNode<K, V>, target)
                if (parent != null){
                    return parent
                }
            }
        }
        return null
    }


    companion object{
        const val MAX_ENTRIES_PER_NODE = 4
    }
}