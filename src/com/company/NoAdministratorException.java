package com.company;

public class NoAdministratorException extends Exception
{
    NoAdministratorException(String s)
    {
        super(s);
        System.out.println("Nu avem adiministrator angajat");
    }
}