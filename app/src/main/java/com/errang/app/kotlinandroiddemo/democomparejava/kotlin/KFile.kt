package com.errang.app.kotlinandroiddemo.democomparejava.kotlin


class KBook0(var name: String, var author: String, var price: Float, val pubHouse: String = "")

data class KDataBook(var name: String, var author: String, var price: Float, val pubHouse: String = "")

class KBook(var name: String, var author: String, var price: Float, val pubHouse: String = "") {

    operator fun minus(other: KBook): Float = price - other.price

    operator fun compareTo(other: KBook): Int = when {
        price - other.price > 0 -> 1
        price - other.price < 0 -> 0
        author.toLowerCase() > other.author.toLowerCase() -> 1
        else -> 0
    }

    companion object {
        val filterPredicate = { book: KBook ->
            book.price > 10 && book.name.contains("kotlin") && book.pubHouse.contains("hangzhou")
        }
    }
}

fun KFindMax() {
    val bookList = listOf(KBook("book1", "author1", 19f),
            KBook("book1", "author2", 32.19f, "hanghzou"),
            KBook("book1", "author3", 24.3f, "shanghai"),
            KBook("book1", "author4", 12f))

    //=============================================================================
    lateinit var maxBooks: List<KBook>
    val maxprice: Float = bookList.maxBy { kBook -> kBook.price }?.price ?: 0f
    maxBooks = bookList.filter { it.price == maxprice }

    //=================================================================================
    // method 1
    maxBooks = bookList.sortedByDescending { it.price }.groupBy { it.price }.maxBy { it.key }!!.value

    // method 2
    maxBooks = bookList.sortedWith(compareByDescending { it.price }).groupBy { it.price }.maxBy { it.key }!!.value

    // method 3
    maxBooks = bookList.sortedWith(Comparator<KBook> { o1, o2 ->
        if (o1.price - o2.price > 0) 1 else 0
    }).groupBy { it.price }.maxBy { it.key }!!.value

    // method 4
    maxBooks = bookList.sortedWith(Comparator { o1, o2 -> if (o1 - o2 > 0) 1 else 0 })
            .groupBy { it.price }.maxBy { it.key }!!.value

    print(maxBooks)

    // java
    var maxPriceJ: Float = 0f
    for (book in bookList) {
        maxPriceJ = if (maxPriceJ > book.price) maxPriceJ else book.price
    }
    for (book in bookList) {
        if (book.price == maxPriceJ)
            maxBooks += book
    }

    //=====================================================================================

    val specalList0 = bookList.asSequence()
            .filter { it.price > 10 && it.name.contains("kotlin") && it.pubHouse.contains("hangzhou") }
            .sortedWith(Comparator { o1, o2 ->
                when {
                    o1 - o2 > 0 -> 1
                    o1 - o2 < 0 -> 0
                    o1.author.toLowerCase() > o2.author.toLowerCase() -> 1
                    else -> 0
                }
            })
            .groupBy { it.price }
            .toList()


//    val filterPredicate = { book: KBook ->
//        book.price > 10 && book.name.contains("kotlin") && book.pubHouse.contains("hangzhou")
//    }
    val specalList = bookList.asSequence()
            .filter(KBook.filterPredicate)
            .sortedWith(Comparator { o1, o2 ->  if (o1 > o2) 1 else 0 })
            .groupBy(KBook::price)  // 成员引用
            .toList()

}
