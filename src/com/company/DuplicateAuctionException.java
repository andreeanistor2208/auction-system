package com.company;

public class DuplicateAuctionException extends Exception
{
    DuplicateAuctionException(String s)
    {
        super(s);
        System.out.println("Exista deja o licitatie pentru acest produs");
    }
}