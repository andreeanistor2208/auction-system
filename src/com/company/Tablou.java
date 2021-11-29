package com.company;

public class Tablou extends Produs
{
    private String mPictor;
    private Culori mCuloare;

    public Tablou(int id, String nume, int an, double pminim, String numePictor, Culori c)
    {
        super(id, nume, an, pminim);
        mPictor = numePictor;
        mCuloare = c;
    }

}