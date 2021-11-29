package com.company;

import java.util.Random;

public abstract class Client implements Subscriber{
    private int mID;
    private String mNume;
    private String mAdresa;
    private int mNrParticipari;
    private int mNrLicitatiiCastigate;

    private int maxTablouPrice;
    private int maxJewelPrice;
    private int maxFurniturePrice;

    private Object lock1 = new Object(); //pentru mNrLicitatiiCastigate

    public Client(int id, String nume, String adresa) {
        mID = id;
        mNume = nume;
        mAdresa = adresa;
        mNrLicitatiiCastigate = mNrParticipari = 0;

        Random random = new Random();
        int rand = random.nextInt(1500);
        maxFurniturePrice = 3000 + rand;
        rand = random.nextInt(11000000);
        maxJewelPrice = 50000 + rand;
        rand = random.nextInt(600000);
        maxTablouPrice = 5000 + rand;

    }

    public String getName() {
        return mNume;
    }

    public String getAdress() {
        return mAdresa;
    }

    public int getID() {return mID; }

    public int getNrParticipari()
    {
        return mNrParticipari;
    }

    public int getNrLicitatiiCastigate()
    {
        synchronized (lock1) {
            return mNrLicitatiiCastigate;
        }
    }

    public int getMaxTablouPrice() { return maxTablouPrice; }

    public int getMaxJewelPrice() { return maxJewelPrice; }

    public int getMaxFurniturePrice() { return maxFurniturePrice; }

    public void participa()
    {
        mNrParticipari++;
    }

    public void castiga()
    {
        synchronized (lock1) {
            mNrLicitatiiCastigate++;
        }
    }
}