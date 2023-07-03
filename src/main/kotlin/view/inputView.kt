package view

fun startInput() {
    while(true) {
        val cur = readln()
        print(cur)

        if (cur == "exit"){
            break
        }
    }
}