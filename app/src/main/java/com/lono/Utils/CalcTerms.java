package com.lono.Utils;

public class CalcTerms {

    public static double value_mensal (double val_term, int terms){
        return val_term * terms;
    }

    public static double value_anual (double val_term, int terms){ return (val_term * terms) * 11; }

    public static double close_value_anual (double val_term, int terms){
        return (val_term * terms) * 12;
    }

    public static double economy_plan_anual (double val_term, int terms){
        double total = (val_term * terms) * 12;
        double desconto = (val_term * terms) * 11;
        return total - desconto;
    }

}
