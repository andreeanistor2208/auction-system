package com.company;

public abstract class Produs
{
    private int mID;
    private String mName;
    protected double mPretVanzare;
    protected double mPretMinim;
    private int mAn;

    public Produs(int id, String name, int an, double pretminim)
    {
        mID = id;
        mName = name;
        mAn = an;
        mPretMinim = pretminim;
    }

    public int getID()
    {
        return mID;
    }

    public String getName()
    {
        return mName;
    }

}