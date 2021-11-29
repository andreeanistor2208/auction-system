package com.company;

public class Mobila extends Produs
{
    private String mTip;
    private String mMaterial;

    public Mobila(int id, String nume, int an, double pmin,
                  String tip, String material)
    {
        super(id, nume, an, pmin);
        mTip = tip;
        mMaterial = material;
    }
}