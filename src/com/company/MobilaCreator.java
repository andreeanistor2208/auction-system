package com.company;

public class MobilaCreator extends ProdusCreator
{
    public Produs CreateProdus(int id, String nume, int an, double pminim,
                               String date1, String date2)
    {
        return new Mobila(id, nume, an, pminim, trim(date1), date2);
    }
}