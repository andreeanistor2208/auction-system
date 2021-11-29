package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class AuctionHouse
{
    private static AuctionHouse single_instance = null;
    private ArrayList<Produs> mProducts;
    private ArrayList<Client> mClients;
    private ArrayList<Licitatie> mLicitatii;
    private ArrayList<Licitatie> mLicitatiiActive;
    private ArrayList<Broker> mBrokers;
    private ArrayList<Administrator> mAdministrators;

    private Object lock1 = new Object(); //pentru mLicitatii
    private Object lock2 = new Object(); // pentru mLicitatiiActive
    private Object lock3 = new Object(); //pentru mProduse
    private int mProductIDGen;
    private int mBrokerIDGen;
    private int mAdminIDGen;
    private int mLicitatiiIDGen;

    // private constructor restricted to this class itself
    private AuctionHouse()
    {
        mProducts =  new ArrayList<Produs>();
        mClients = new ArrayList<Client>();
        mLicitatii = new ArrayList<Licitatie>();
        mLicitatiiActive = new ArrayList<Licitatie>();
        mBrokers = new ArrayList<Broker>();
        mAdministrators = new ArrayList<Administrator>();

        mProductIDGen = 0;
        mBrokerIDGen = 800000;
        mAdminIDGen = 400000;
        mLicitatiiIDGen = 0;
    }

    // static method to create instance of Singleton class
    public static AuctionHouse getInstance()
    {
        if (single_instance == null)
            single_instance = new AuctionHouse();

        return single_instance;
    }

    public int getNextProductID()
    {
        mProductIDGen += (mProductIDGen % 3) + 1;
        return mProductIDGen;
    }

    public int getNextAdminID()
    {
        mAdminIDGen += (mAdminIDGen % 3) * 10 + 1;
        return mAdminIDGen;
    }

    public int getNextBrokerID()
    {
        mBrokerIDGen += (mBrokerIDGen % 4) * 5 + 1;
        return mBrokerIDGen;
    }

    public int getNextLicitatiiID()
    {
        mLicitatiiIDGen++;
        return mLicitatiiIDGen;
    }

    public int getBrokerNumber()
    {
        return mBrokers.size();
    }

    public void addProduct(Produs p) throws NoAdministratorException
    {
        if(mAdministrators.isEmpty())
            throw new NoAdministratorException("Nu pot adauga " + p.getName()
                    + " pentru ca nu am administrator ");
        int min = 0;
        boolean ePrimul = true;
        Iterator<Administrator> it = mAdministrators.iterator();
        while(it.hasNext())
        {
            Administrator a = it.next();
            if(ePrimul) {
                min = a.getNr();
                ePrimul = false;
            }
            else
            {
                if(min > a.getNr())
                    min = a.getNr();
            }
        }

        it = mAdministrators.iterator();
        Administrator b = null;
        while(it.hasNext())
        {
            Administrator a = it.next();
            if(a.getNr() == min)
            {
                b = a;
                break;
            }
        }

        b.posteaza();
        synchronized(lock3)
        {
            mProducts.add(p);
        }
    }

    public void addBroker(Broker b)
    {
        mBrokers.add(b);
    }

    public void addAdministrator(Administrator a)
    {
        mAdministrators.add(a);
    }

    public void addClient(Client c) throws DuplicatePFException, DuplicatePJException
    {
        Iterator<Client> it = mClients.iterator();
        while(it.hasNext())
        {
            Client curent = it.next();
            boolean isInstance = curent instanceof PersoanaFizica;
            boolean argIsInstance = c instanceof PersoanaFizica;
            if(isInstance && argIsInstance)
            {
                if(curent.getName().equals(c.getName()) &&
                    curent.getAdress().equals(c.getAdress()))
                    throw new DuplicatePFException("PF " + c.getName() + " "
                            + c.getAdress() + " exista deja");

            }

            boolean isPJInstance = curent instanceof  PersoanaJuridica;
            boolean argIsPJInstance = c instanceof PersoanaJuridica;
            if(isPJInstance && argIsPJInstance)
                if(curent.getName().equals(c.getName()))
                    throw new DuplicatePJException("PJ " + c.getName() + " exista deja");
        }

        mClients.add(c);
    }

    public void addLicitatie(Licitatie l) throws InvalidProductIDException,
            DuplicateAuctionException
    {
        Iterator<Produs> itp = mProducts.iterator();
        boolean gasit = false;
        while(itp.hasNext())
        {
            Produs p = itp.next();
            if(p.getID() == l.getProdusID())
                gasit = true;
        }
        if(!gasit)
            throw new InvalidProductIDException(l.getProdusID()
                    + "Nu exista in lista de produse sau a fost vandut");

        synchronized (lock1)
        {
            Iterator<Licitatie> itl = mLicitatii.iterator();
            while (itl.hasNext())
            {
                Licitatie lic = itl.next();
                if (lic.getProdusID() == l.getProdusID())
                    throw new DuplicateAuctionException("Exista deja o licitatie pentru "
                            + l.getProdusID());


            }

            mLicitatii.add(l);
        }
    }

    public void showProducts()
    {
        synchronized (lock3)
        {
            String[] simpleDisplayVector = new String[mProducts.size()];
            Integer[] idVector = new Integer[mProducts.size()];
            int j = 0;
            Iterator<Produs> it = mProducts.iterator();
            while (it.hasNext())
            {
                Produs p = it.next();
                simpleDisplayVector[j] = p.getName();
                idVector[j++] = p.getID();
            }

            MyList<String> simpleDisplayList = new MyList<String>(simpleDisplayVector);
            MyIterator<String> mit = simpleDisplayList.iterator();
            MyList<Integer> idList = new MyList<Integer>(idVector);
            MyIterator<Integer> mintit = idList.iterator();


            while (mit.hasNext())
                System.out.println(mintit.next() + " " + mit.next());
        }
    }

    public Client getClient(String name)
    {
        Iterator<Client> it = mClients.iterator();
        while(it.hasNext())
        {
            Client c = it.next();
            if(c.getName().equals(name))
                return c;
        }
        return null;
    }

    public Client getClient(int clientID)
    {
        Iterator<Client> it = mClients.iterator();
        while(it.hasNext())
        {
            Client c = it.next();
            if(c.getID() == clientID)
                return c;
        }
        return null;
    }

    public Broker alocareBroker(int index)
    {
        Broker b = mBrokers.get(index);

        return new Broker(b.getName(), b.getID(), b.getRef());
    }

    //ia licitatia dupa Id-ul produsului vandut
    public Licitatie getLicitatie(int prodID)
    {
        synchronized(lock1) {
            Iterator<Licitatie> it = mLicitatii.iterator();
            while (it.hasNext()) {
                Licitatie lic = it.next();
                if (lic.getProdusID() == prodID)
                    return lic;
            }
        }
        return null;
    }

    //ia licitatia dupa ID-ul licitatiei
    public Licitatie getLicitatie(int idLic, boolean t)
    {
        synchronized(lock1) {
            Iterator<Licitatie> it = mLicitatii.iterator();
            while (it.hasNext()) {
                Licitatie lic = it.next();
                if (lic.getID() == idLic)
                    return lic;
            }

        }
        return null;
    }

    public int getAuctionID(int prodID)
    {
        synchronized (lock1) {
            Iterator<Licitatie> it = mLicitatii.iterator();
            while (it.hasNext()) {
                Licitatie lic = it.next();
                if (lic.getProdusID() == prodID)
                    return lic.getID();
            }
        }
        return -1;
    }

    public int getProductType(int prodID)
    {
        Iterator<Produs> it = mProducts.iterator();
        while(it.hasNext())
        {
            Produs p = it.next();
            if(p.getID() == prodID)
            {
                boolean c1 = p instanceof Tablou;
                boolean c2 = p instanceof Mobila;
                boolean c3 = p instanceof Bijuterie;

                if(c1)
                    return 1;
                if(c2)
                    return 2;
                if(c3)
                    return 3;
            }

        }
        return -1;
    }

    public Produs getProduct(int pid)
    {
        Iterator<Produs> it = mProducts.iterator();
        while(it.hasNext())
        {
            Produs p = it.next();
            if(p.getID() == pid)
                return p;
        }
        return null;
    }

    public void remove(int IDLicitatie)
    {
        Licitatie l = getLicitatie(IDLicitatie);
        synchronized (lock1)
        {
           mLicitatii.remove(l);
        }
        synchronized(lock2)
        {
            mLicitatiiActive.remove(l);
        }
    }

    public void removeProduct(int pid)
    {
        synchronized (lock3)
        {
            Iterator<Produs> it = mProducts.iterator();
            while (it.hasNext())
            {
                Produs p = it.next();
                if (p.getID() == pid)
                {
                    it.remove();
                    return;
                }
            }
        }
    }
}