package com.company;

public class Angajat
{
    private String mName;
    protected int mID;

    public Angajat(String name, int id)
    {
        mName = name;
        mID = id;
    }

    public String getName()
    {
        return mName;
    }
}