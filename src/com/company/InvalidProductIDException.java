package com.company;

public class InvalidProductIDException extends Exception
{
    InvalidProductIDException(String s)
    {
        super(s);
        System.out.println("Acest ID nu este valid");

    }
}