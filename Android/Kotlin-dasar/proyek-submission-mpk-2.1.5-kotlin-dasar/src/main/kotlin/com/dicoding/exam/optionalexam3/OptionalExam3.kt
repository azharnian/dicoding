package com.dicoding.exam.optionalexam3

// TODO
fun manipulateString(str: String, int: Int): String {
    var idx = str.length - 1
    while (idx >= 0 && str[idx].isDigit()) {
        idx--
    }

    if (idx == str.length - 1) {
        return str + int.toString()
    }

    val textPart = str.substring(0, idx + 1)
    val numberPart = str.substring(idx + 1)
    val numberValue = numberPart.toInt()

    val multiplied = numberValue * int
    return textPart + multiplied.toString()
}
