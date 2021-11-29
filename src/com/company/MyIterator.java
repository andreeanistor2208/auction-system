package com.company;

public class MyIterator<E> implements MyIteratorInterface<E> {
    private E[] mContainer;
    private int mPos;

    public MyIterator(E[] container) {
        mContainer = container;
        mPos = 0;
    }

    @Override
    public void reset() {
        mPos = 0;
    }

    @Override
    public E next() {
        return mContainer[mPos++];
    }

    @Override
    public E currentItem() {
        return mContainer[mPos];
    }

    @Override
    public boolean hasNext() {
        if (mPos >= mContainer.length)
            return false;
        return true;
    }

    @Override
    public void remove()
    {
        if(mPos < mContainer.length && mPos > 0)
        {
            E[] copy = (E[]) new Object[mContainer.length - 1];

            for (int i = 0, j = 0; i < mContainer.length; i++)
                if (i != mPos)
                    copy[j++] = mContainer[i];


            mContainer = copy;
        }
    }
}