package com.company;

public class DuplicatePFException extends Exception
{
    DuplicatePFException(String s)
    {
        super(s);
        System.out.println("Aceasta PF mai exista in baza de date");

    }
}