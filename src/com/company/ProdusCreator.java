package com.company;

public abstract class ProdusCreator
{
    protected String trim(String in)
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

    public abstract Produs CreateProdus(int id, String nume, int an, double pminim,
                                        String date1, String date2);
}