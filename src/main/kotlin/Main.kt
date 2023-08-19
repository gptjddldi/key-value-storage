import btree.*

fun main(args: Array<String>) {
    println("Hello World!")
    val btree = BTree<Int, Any>()
    for(i in 1..9){
        btree.insert(i, "test$i")
    }
    btree.print()
    println("Hello World!")

}
