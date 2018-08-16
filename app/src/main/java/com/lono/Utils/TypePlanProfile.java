package com.lono.Utils;

public class TypePlanProfile {

    public static String name (String type){
        String name = null;
        switch (type){
            case "F":
                name = "Plano Free";
                break;
            case "P":
                name = "Plano Plus";
                break;
        }
        return name;
    }
}
