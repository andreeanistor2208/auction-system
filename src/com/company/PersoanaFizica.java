package com.company;

import java.util.Random;

public class PersoanaFizica extends Client
{
    private String mDataNastere;

    public PersoanaFizica(int id, String nume, String dataNasterii, String adresa)
    {
        super(id, nume, adresa);
        mDataNastere = dataNasterii;
    }

    public int nextBid(int oldValue, int type)
    {
        Random r = new Random();
        if(type == 1)
        {
            int factor = r.nextInt(4);
            return oldValue + (factor + 1) * 1000;
        }
        if(type == 2)
        {
            int factor = r.nextInt(5);
            return oldValue + (factor + 1) * 500;
        }
        else //type == 3
        {
            int factor = r.nextInt(10);
            return oldValue + (factor + 1) * 1500;
        }
    }
}