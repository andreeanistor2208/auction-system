package com.company;

public class Administrator extends Angajat
{
    int mProdAdaugate;

    Administrator(String name, int ID)
    {
        super(name, ID);
        mProdAdaugate = 0;
    }

    public int getNr()
    {
        return mProdAdaugate;
    }

    public void posteaza()
    {
        mProdAdaugate++;
    }

}