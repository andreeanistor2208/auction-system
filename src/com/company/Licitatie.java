package com.company;

import java.util.ArrayList;
import java.util.Iterator;

public class Licitatie extends Thread
{
    private int mID;
    private int mNrNecesar;
    private int mNrParticipanti;
    private int mIDProdus;
    private int mNrPasiMaxim;
    private boolean mPornit;
    private int mProductType; // 1 pentru tablou
                              // 2 pentru mobila
                              // 3 pentru bijuterie
    private ArrayList<Broker> mBrokers;

    public Licitatie(int id, int nrPart, int idProd, int nrPasi, int type)
    {
        mID = id;
        mNrNecesar = nrPart;
        mIDProdus = idProd;
        mNrPasiMaxim = nrPasi;
        mNrParticipanti = 0;
        mBrokers = new ArrayList<Broker>();
        mProductType = type;
        mPornit = false;
    }

    public int getProdusID()
    {
        return mIDProdus;
    }

    public int getID() { return mID; }

    public int getProductType() { return mProductType;}

    public boolean getPornit() { return mPornit; }

    public void primesteClient()
    {
        if(mPornit == true)
            return;
        mNrParticipanti++;
        if(mNrParticipanti == mNrNecesar) {
            mPornit = true;
            start();
        }
    }

    public void addBroker(Broker b)
    {
        mBrokers.add(b);
    }

    @Override
    public void run()
    {
        int pasi = mNrPasiMaxim;
        boolean dup = false;
        int oferta_maxima = 0;

        Broker highest = null;

        //Brokerii sunt anuntati de inceperea licitatiei
        notifysubscribers(0);
        while(pasi > 0)
        {
            Iterator<Broker> it = mBrokers.iterator();
            while (it.hasNext())
            {
                Broker b = it.next();
                int oferta = b.nextBid(mID);
                Client c = b.getRef().getClient(b.getClientID(mID));
                System.out.println(Thread.currentThread().getName() + " " + mID + " REM " + pasi
                            + " :: " + b.getName() + " ofera " + oferta
                            + " ( client " + c.getName() + " )");
                if (oferta > oferta_maxima) {
                    oferta_maxima = oferta;
                    highest = b;
                    dup = false;
                } else if (oferta == oferta_maxima)
                    dup = true;
            }

            //anunt brokerii de rezultatul unei runde
            notifysubscribers(oferta_maxima);
            pasi--;
        }
        if(dup)
        {
            int most_won = 0;
            Iterator<Broker> itb = mBrokers.iterator();
            while(itb.hasNext())
            {
                Broker b = itb.next();
                if(b.getValue(mID) == oferta_maxima)
                {
                    Client c = b.getRef().getClient(b.getClientID(mID));
                    if (c.getNrLicitatiiCastigate() > most_won)
                    {
                        most_won = c.getNrLicitatiiCastigate();
                        highest = b;
                    }
                }
            }
        }

        Produs p = highest.getRef().getProduct(mIDProdus);
        if(oferta_maxima >= p.mPretMinim)
        {
            int cid = highest.getClientID(mID);
            Client c = highest.getRef().getClient(cid);
            System.out.println(Thread.currentThread().getName() + " " + mID + " :: " + highest.getName()
                    + " castiga licitatia cu " + oferta_maxima
                    + " ( client " + c.getName() + " )");


            highest.getComision(c, oferta_maxima);
            c.castiga();
            //scot produsul si licitatia
            highest.removeProduct(mIDProdus);
            highest.getRef().remove(mID);
        }
        else
        {
            System.out.println(Thread.currentThread().getName() + " " + mID + " :: " + "Produsul nu s-a vandut");

            //scot licitatia din lista de licitatii printr-un broker aleator
            Broker b1 = mBrokers.get(0);
            //b1.getRef().remove(mID);

            Iterator<Broker> ib = mBrokers.iterator();
            while(ib.hasNext())
            {
                Broker b = ib.next();
                b.remove(mID);
            }

        }
        resetParticipants();
        //anunt brokerii de terminarea licitatie pentru a incheia
        //comunicarea dintre broker si client
        //brokerii tre sa scoata licitatia din lista
        notifysubscribers(-1);
    }

    private void resetParticipants()
    {
        mNrParticipanti = 0;
        mPornit = false;
        mBrokers.clear();
    }

    private void notifysubscribers(int of_maxima)
    {

        Iterator<Broker> it = mBrokers.iterator();
        while (it.hasNext())
        {
            Broker b = it.next();
            if(of_maxima > -1)
                b.update(mID, of_maxima);
            else
                b.remove(mID);
        }
    }
}