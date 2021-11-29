package com.company;

public class BijuterieCreator extends ProdusCreator
{
    public Produs CreateProdus(int id, String nume, int an, double pminim,
                               String date1, String date2)
    {
        String material = date1;
        material = trim(material);
        boolean pretios;
        String valoros = date2.toLowerCase();
        if(valoros.equals("pretios") || valoros.equals("pretioasa"))
            pretios = true;
        else
            pretios = false;
        return new Bijuterie(id, nume, an, pminim, material, pretios);
    }
}