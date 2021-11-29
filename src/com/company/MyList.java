package com.company;

public class MyList<E> implements MyListInterface<E>
{
    private E[] mArray;

    public MyList(E[] elements)
    {
        mArray = elements;
    }

    @Override
    public MyIterator<E> iterator()
    {
            return new MyIterator(mArray);
    }
}