package com.company;

public class TablouCreator extends ProdusCreator
{

    public Produs CreateProdus(int id, String nume, int an, double pminim,
                               String date1, String date2)
    {
        String numePictor = trim(date1);
        String culoriCorect = date2.toUpperCase();
        Culori c = Culori.valueOf(culoriCorect);
        return new Tablou(id, nume, an, pminim, numePictor, c);
    }
}