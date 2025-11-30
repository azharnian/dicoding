fun main() {
    // Salah
    // val text = "Paman berkata, "Jangan lupa belajar Kotlin hari ini""
    
    //Benar
    // val text = "Paman berkata, \"Jangan lupa belajar Kotlin hari ini\""
    // println(text)

    // val floatNumber: Float = 3.4f
    // println("Nilai Float: $floatNumber \n")

    // val anotherFloatNumber = -3.4f
    // println("Nilai Float: $anotherFloatNumber \n")

    // val doubleNumber: Double = 0.0
    // println("Nilai Double: $doubleNumber \n")

    // val anotherDoubleNumber = -10.0
    // println("Nilai Double: $anotherDoubleNumber \n")

    // // Bukan merupakan nilai kosong
    // val nama = ""
    // val score = 0
    
    // // null adalah nilai kosong
    // val namaUser: String = null
    // println(namaUser)

    // // Bukan merupakan nilai kosong
    // val nama = ""
    // val score = 0
    
    // // null adalah nilai kosong
    // val namaUser: String = null
    // println(namaUser)

    var namaUser: String? = null
    println(namaUser)
    
    namaUser = "Dicoding"
    println(namaUser)

    val judul: String = "Belajar Kotlin"
    var catatanBuku: String? = null
    var panjangCatatanBuku = catatanBuku?.length
    println("Buku $judul memiliki panjang catatan buku: $panjangCatatanBuku karakter")

    catatanBuku = "Wah, seru ya belajar Kotlin"
    panjangCatatanBuku = catatanBuku?.length
    println("Buku $judul memiliki panjang catatan buku: $panjangCatatanBuku karakter")
    
}