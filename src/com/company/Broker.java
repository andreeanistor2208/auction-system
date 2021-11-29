package com.company;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.Random;
import javafx.util.Pair;

public class Broker extends Angajat {
    private ArrayList<Client> mClients;
    private ArrayList<Pair> mTable; //leaga ID client de ID lictatiei care liciteaza activ
    private AuctionHouse mHRef;
    private ArrayList<Pair> mAuctionPairs; //prima valoare ID licitatie,
                                           //a doua valoarea curenta licitata
    private ArrayList<Pair> mAuctionUpdates;//prima valoare ID licitatie
                                            //a doua valoare maximul dupa
                                            // ultima runda inchisa

    public Broker(String name, int id, AuctionHouse ref) {
        super(name, id);
        mClients = new ArrayList<Client>();
        mHRef = ref;
        mAuctionPairs = new ArrayList<Pair>();
        mTable = new ArrayList<Pair>();
        mAuctionUpdates = new ArrayList<Pair>();
    }

    public AuctionHouse getRef() {
        return mHRef;
    }

    public int getID() { return mID; }

    public int getValue(int idLic) {
        Iterator<Pair> it = mAuctionPairs.iterator();
        while (it.hasNext()) {
            Pair p = it.next();
            if ((int) p.getKey() == idLic)
                return (int) p.getValue();
        }
        return -1;
    }

    public void conect(int AID, Client c) {
        Pair p1 = new Pair(c.getID(), AID);
        mTable.add(p1);
        Pair p2 = new Pair(AID, 0);
        mAuctionPairs.add(p2);
        mClients.add(c);
    }

    public void update(int LID, int oferta)
    {
        //vad daca am upadate-uri vechi pentru aceasta oferta
        Iterator<Pair> itp = mAuctionUpdates.iterator();
        while(itp.hasNext())
        {
            Pair p = itp.next();
            if((int)p.getKey() == LID)
            {
                itp.remove();
                Pair p2 =  new Pair(LID, oferta);
                mAuctionUpdates.add(p2);
                return;
            }
        }
        //daca nu adaugat o pereche noua
        Pair p = new Pair(LID, oferta);
        mAuctionUpdates.add(p);
    }

    public int getLastM(int LID)
    {
        Iterator<Pair> itp = mAuctionUpdates.iterator();
        while(itp.hasNext())
        {
            Pair p = itp.next();
            if((int)p.getKey() == LID)
                return (int)p.getValue();
        }
        return -1;
    }

    public int nextBid(int IDlicitatie)
    {
        int lastm = getLastM(IDlicitatie);

        //vad daca valoarea licitata deja este cea maxim permisa,
        // si atunci dau din nou aceeasi valoare
        int maxim;
        Client c = mHRef.getClient(getClientID(IDlicitatie));
        //ptr tablou
        int type = mHRef.getLicitatie(IDlicitatie, true).getProductType();
        if(type == 1)
            maxim = c.getMaxTablouPrice();
        //ptr mobila
        else if(type == 2)
            maxim = c.getMaxFurniturePrice();
        //ptr bijuterie
        else
            maxim = c.getMaxJewelPrice();

        if(getValue(IDlicitatie) == maxim)
            return maxim;

        //raman pe loc daca s-a depasit deja maximul
        if(lastm >= maxim)
            return maxim;

        //daca nu o generez pe urmatoarea valoare
        int new_val = 0;
        int val = getValue(IDlicitatie);
        if(type == 1)
        {
            new_val = c.nextBid(val, 1);
            while(new_val < lastm)
                new_val = c.nextBid(new_val, 1);
        }
        else if(type == 2) {
            new_val = c.nextBid(val, 2);
            while(new_val < lastm)
                new_val = c.nextBid(new_val, 2);
        }
        else
        {
            new_val = c.nextBid(val, 3);
            while(new_val < lastm)
                new_val = c.nextBid(new_val, 3);
        }

        //daca depaseste maximul o limitez la maxim
        if(new_val > maxim)
            return maxim;
        return new_val;
    }

    public int getClientID(int IDLicitatie)
    {
        Iterator<Pair> it = mTable.iterator();
        while(it.hasNext())
        {
            Pair p = it.next();
            if((int)p.getValue() == IDLicitatie)
                return (int)p.getKey();
        }
        return -1;
    }

    public void remove(int IDLicitatie)
    {
        //curat perechea din mTable
        Iterator<Pair> it = mTable.iterator();
        while(it.hasNext())
        {
            Pair p = it.next();
            if((int)p.getValue() == IDLicitatie)
                it.remove();
        }
        //curat perechea din AuctionPair
        if(mAuctionUpdates.size() > 0) {
            it = mAuctionPairs.iterator();
            while (it.hasNext()) {
                Pair p = it.next();
                if ((int) p.getKey() == IDLicitatie)
                    it.remove();
            }
        }
        Iterator<Pair> it2 = mAuctionUpdates.iterator();
        while(it2.hasNext())
        {
            Pair p = it2.next();
            if((int)p.getKey() == IDLicitatie)
                it2.remove();
        }
    }

    public void getComision(Client c, int suma)
    {
        int com = 0;
        boolean c1 = c instanceof PersoanaJuridica;
        boolean c2 = c instanceof PersoanaFizica;
        if(c2)
        {
            if(c.getNrParticipari() < 5)
                com = 20;
            else
                com = 15;
        }
        else if(c1)
        {
            if(c.getNrParticipari() < 25)
                com = 25;
            else
                com = 10;
        }

        double comf = suma * com / 100.0;
        System.out.println(getName() + " primeste comision " + comf + " de la " + c.getName());
    }

    public void removeProduct(int pid)
    {
        mHRef.removeProduct(pid);
    }

}