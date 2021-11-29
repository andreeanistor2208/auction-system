package com.company;

public class Bijuterie extends Produs
{
    private String mMaterial;
    private boolean mPretioasa;

    public Bijuterie(int id, String nume, int an, double pret,
                     String material, boolean pretios)
    {
        super(id, nume, an, pret);
        mMaterial = material;
        mPretioasa = pretios;
    }

    public boolean getPretioasa()
    {
        return mPretioasa;
    }
}