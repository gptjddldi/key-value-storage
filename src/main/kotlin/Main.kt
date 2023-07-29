import btree.*

fun main(args: Array<String>) {
    println("Hello World!")
    val btree = BTree<Int, Any>()

    btree.insert(1,2)
    btree.insert(2,3)
    btree.insert(3,4)
    btree.insert(2,2)
    btree.insert(6,2)
    println("Hello World!")

}