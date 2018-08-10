package com.lono.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Price {

    public static String real (double price){
        String preco = null;
        final NumberFormat formatter = new DecimalFormat("#0.00");
        preco = formatter.format(price);
        return "R$ " + preco;
    }

    public static String us (double price){
        String preco = null;
        final NumberFormat formatter = new DecimalFormat("#0.00");
        preco = formatter.format(price);
        return "$ "+preco;
    }


}
