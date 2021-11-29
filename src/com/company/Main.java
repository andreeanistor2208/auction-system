package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Main {

    private static String trim(String in)
    {
        char quote = '"';
        if(in.indexOf(quote) == -1)
            return in;
        StringBuffer s = new StringBuffer(in);
        if(s.charAt(0) == quote)
            s.deleteCharAt(0);
        if(s.charAt(s.length() - 1) == quote)
            s.deleteCharAt(s.length() - 1);

        return s.toString();
    }

    public static void main(String[] args)
    {
	// write your code here
        AuctionHouse AH = AuctionHouse.getInstance();
        TablouCreator TC = new TablouCreator();
        MobilaCreator MC = new MobilaCreator();
        BijuterieCreator BC = new BijuterieCreator();

        try {
            RandomAccessFile fin = new RandomAccessFile("test1.in", "r");
            String command;
            while ((command = fin.readLine()) != null)
            {
                System.out.println(command);
                String[] comParts = command.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                //for(int i = 0; i < comParts.length; i++)
                //    System.out.println(comParts[i]);

                if(comParts[0].equals("addproduct"))
                {
                    String nume = comParts[1];
                    nume = trim(nume);
                    int an = Integer.parseInt(comParts[2]);
                    double pminim = Double.parseDouble(comParts[3]);
                    int id = AH.getNextProductID();
                    Produs p = null;
                    String tip = comParts[4];
                    tip = tip.toLowerCase();
                    if(tip.equals("tablou"))
                    {
                        /*
                        String numePictor = comParts[5];
                        numePictor = trim(numePictor);
                        String culoriCorect = comParts[6].toUpperCase();
                        Culori c = Culori.valueOf(culoriCorect);
                        p = new Tablou(id, nume, an, pminim, numePictor, c);
                        */
                        p = TC.CreateProdus(id, nume, an, pminim, comParts[5], comParts[6]);
                        //p = TC.CreateProdus(id, nume, an, pminim, numePictor, c);
                    }
                    else if(tip.equals("mobila"))
                        p = MC.CreateProdus(id, nume, an, pminim, trim(comParts[5]),
                                comParts[6]);
                    else if(tip.equals("bijuterie"))
                    {
                        /*
                        String material = comParts[5];
                        material = trim(material);
                        boolean pretios;
                        String valoros = comParts[6].toLowerCase();
                        if(valoros.equals("pretios") || valoros.equals("pretioasa"))
                            pretios = true;
                        else
                            pretios = false;

                        p = new Bijuterie(id, nume, an, pminim, material, pretios);
                        */
                        p = BC.CreateProdus(id, nume, an, pminim, comParts[5], comParts[6]);
                    }
                    try {
                        AH.addProduct(p);
                    }
                    catch(NoAdministratorException na)
                    {}
                }
                else if(comParts[0].equals("addbroker"))
                {
                    int id = AH.getNextBrokerID();
                    String nume = comParts[1];
                    nume = trim(nume);
                    Broker b = new Broker(nume, id, AH);
                    AH.addBroker(b);
                }
                else if(comParts[0].equals("addadministrator"))
                {
                    int id = AH.getNextAdminID();
                    Administrator a = new Administrator(trim(comParts[1]), id);
                    AH.addAdministrator(a);
                }
                else if(comParts[0].equals("addclient"))
                {
                    int id = AH.getNextLicitatiiID();
                    String tip = comParts[1].toUpperCase();
                    Client c = null;
                    if(tip.equals("PF"))
                        c = new PersoanaFizica(id, trim(comParts[2]), comParts[3], trim(comParts[4]));
                    else if(tip.equals("PJ"))
                    {
                        Companie comp = Companie.valueOf(comParts[4].toUpperCase());
                        double cSocial = Double.parseDouble(comParts[5]);
                        c = new PersoanaJuridica(id, trim(comParts[2]), trim(comParts[3]),
                                comp, cSocial);
                    }
                    try {
                        AH.addClient(c);
                    }
                    catch (DuplicatePFException dpf)
                    {}
                    catch (DuplicatePJException dpj)
                    {}
                }
                else if(comParts[0].equals("showproducts"))
                    AH.showProducts();
                else if(comParts[0].equals("addlicitatie"))
                {
                    int id = AH.getNextLicitatiiID();
                    int nrParticipanti = Integer.parseInt(comParts[1]);
                    int ProdID = Integer.parseInt(comParts[2]);
                    int nrPasi = Integer.parseInt(comParts[3]);
                    int type = AH.getProductType(ProdID);
                    Licitatie l = new Licitatie(id, nrParticipanti, ProdID, nrPasi, type);
                    try
                    {
                        AH.addLicitatie(l);
                    }
                    catch (InvalidProductIDException ipi)
                    {}
                    catch(DuplicateAuctionException da)
                    {}
                }
                else if(comParts[0].equals("client"))
                {
                    String client = comParts[1];
                    client = trim(client);
                    int prodID = Integer.parseInt(comParts[2]);
                    int idl = AH.getAuctionID(prodID);


                    if(AH.getLicitatie(prodID).getPornit() != true)
                    {
                        int nr = AH.getBrokerNumber();
                        Random random = new Random();
                        int bi = random.nextInt(nr);
                        Broker b = AH.alocareBroker(bi); //. addClient(c);
                        //Broker b = AH.alocareBroker(AH.getClient(client));
                        b.conect(idl, AH.getClient(client));

                        AH.getLicitatie(prodID).primesteClient();
                        AH.getLicitatie(prodID).addBroker(b);
                    }
                    else {
                        Produs p = AH.getProduct(prodID);
                        System.out.println("Licitatia pentru " + p.getName() + " a pornit deja");
                    }
                }


            }
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
