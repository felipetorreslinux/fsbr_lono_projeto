package com.lono.Utils;

public class TypePlanProfile {

    public static String name (String type){
        String name = null;
        switch (type){
            case "F":
                name = "Free";
                break;
            case "P":
                name = "Plus";
                break;
            default:
                name = "+200";
        }
        return name;
    }
}
