import btree.*

fun main(args: Array<String>) {
    println("Hello World!")
    val btree = BTree<Int, Any>()
    for(i in 1..11){
        btree.insert(i, "test$i")
    }
    btree.delete(1)
    btree.delete(2)
    btree.delete(4)
    btree.delete(5)
    btree.delete(6)
    btree.print()
    println("Hello World!")


}