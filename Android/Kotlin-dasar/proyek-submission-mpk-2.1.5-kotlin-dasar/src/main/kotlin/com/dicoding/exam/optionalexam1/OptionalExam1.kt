package com.dicoding.exam.optionalexam1

fun sumOfBigThree(vararg numbers: Int): Int {
    return numbers
        .sortedDescending()
        .take(3)
        .sum()
}
