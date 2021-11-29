package com.company;

public class DuplicatePJException extends Exception
{
    DuplicatePJException(String s)
    {
        super(s);
        System.out.println("Aceasta PJ mai exista in baza de date");
    }
}