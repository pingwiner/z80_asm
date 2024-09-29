@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.dump(width: Int = 16) {
    val chars = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    var pos = 1
    for (b in this) {
        val x = b.toInt()
        print(chars[x shr 4])
        print(chars[x and 15])
        if (pos < width) {
            print(" ")
            if (pos % 4 == 0) print(" ")
            pos++
        } else {
            println()
            pos = 1
        }
    }
}

fun hiByte(x: UShort): UByte {
    val i = x.toInt()
    return (i shr 8).toUByte()
}

fun lowByte(x: UShort): UByte {
    val i = x.toInt()
    return (i and 0xFF).toUByte()
}