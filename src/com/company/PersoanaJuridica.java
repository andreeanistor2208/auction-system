package com.company;

import java.util.Random;

public class PersoanaJuridica extends Client
{
    private Companie mType;
    private double mCapitalSocial;

    public PersoanaJuridica(int id, String nume, String adresa,
                            Companie ctip, double capital)
    {
        super(id, nume, adresa);
        mType = ctip;
        mCapitalSocial = capital;
    }

    public int nextBid(int oldValue, int type)
    {
        Random r = new Random();
        if(type == 1)
        {
            int factor = r.nextInt(4);
            return oldValue + (factor + 1) * 500;
        }
        if(type == 2)
        {
            int factor = r.nextInt(5);
            return oldValue + (factor + 1) * 1000;
        }
        else //type == 3
        {
            int factor = r.nextInt(10);
            return oldValue + (factor + 1) * 1500;
        }

    }
}