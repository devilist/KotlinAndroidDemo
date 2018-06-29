package com.errang.app.kotlinandroiddemo.democomparejava.java;

import android.util.Log;

import com.errang.app.kotlinandroiddemo.democomparejava.kotlin.KBook;
import com.errang.app.kotlinandroiddemo.democomparejava.kotlin.KPerson;
import com.errang.app.kotlinandroiddemo.democomparejava.kotlin.Kfile1Kt;
import com.errang.app.kotlinandroiddemo.democomparejava.kotlin.S;
import com.errang.app.kotlinandroiddemo.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JTest {

    public void findMax() {
        List<JBook> bookList = new ArrayList<>();
        bookList.add(new JBook("book1", "author1", 19f));
        bookList.add(new JBook("book2", "author2", 32.19f));
        bookList.add(new JBook("book3", "author3", 24.3f));
        bookList.add(new JBook("book4", "author4", 12f));

        //====================================================================
        List<JBook> maxPriceBooks = new ArrayList<>();
        float maxPrice = 0;
        for (JBook book : bookList) {
            maxPrice = maxPrice > book.getPrice() ? maxPrice : book.getPrice();
        }
        for (JBook book : bookList) {
            if (book.getPrice() == maxPrice)
                maxPriceBooks.add(book);
        }
        //=================================================================
        Collections.sort(bookList, new Comparator<JBook>() {
            @Override
            public int compare(JBook o1, JBook o2) {
                return o1.getPrice() > o2.getPrice() ? 1 : 0;
            }
        });
        maxPrice = bookList.get(0).getPrice();
        for (JBook book : bookList) {
            if (book.getPrice() < maxPrice) break;
            else maxPriceBooks.add(book);
        }
    }

    public void printBooks(JCity city) {
        if (null == city || null == city.getBookStore()
                || null == city.getBookStore().getCategoryList()
                || city.getBookStore().getCategoryList().size() == 0)
            return;
        for (JBookCategory category : city.getBookStore().getCategoryList()) {
            if (null == category.getBooks()
                    || category.getBooks().size() == 0)
                continue;
            for (JBook book : category.getBooks()) {
                if (null == book)
                    continue;
                Log.d("printBooks", book.toString());
            }
        }
    }

    public void useKotlinClass() {
        KBook kBook = new KBook("aa", "bb", 14f, null);

        int eyeNum = KPerson.Companion.getEyeNum();
        int legNum = KPerson.legNum;
        int earNum = KPerson.earNum;
        int handNum = KPerson.Companion.getHandNum();
        KPerson.Companion.height();
        KPerson.Companion.weight();
        KPerson.weight();

        KPerson person = new KPerson("zengpu",1);
        String name = person.getName();
    }
}
