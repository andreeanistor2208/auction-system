package com.company;

public interface MyIteratorInterface<E>
{
    void reset();   // merge inapoi la primul element

    E next(); // ia urmatorul element

    E currentItem(); // recupereaza elementul curent

    boolean hasNext(); // verifica daca mai avem elemente

    void remove(); // sterge ultimul element
}