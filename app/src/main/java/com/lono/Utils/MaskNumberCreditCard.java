package com.lono.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskNumberCreditCard {

    private static final String maskCreditCard = "#### #### #### ####";

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher mask(final EditText editText) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = MaskCPF.unmask(s.toString());
                String mask;
                String defaultMask = getDefaultMask(str);
                switch (str.length()) {
                    case 16:
                        mask = maskCreditCard;
                        break;
                    default:
                        mask = defaultMask;
                        break;
                }

                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void afterTextChanged(Editable s){}
        };
    }

    private static String getDefaultMask(String str) {
        String defaultMask = maskCreditCard;
        return defaultMask;
    }

    public static String maskCript (String card){
        String card_unmask = unmask(card);
        String[] splitcard = card_unmask.split("");
        String campo = "\u2022 \u2022 \u2022 \u2022 ";
        card = campo+" "+campo+" "+campo+" "+splitcard[13]+""+splitcard[14]+""+splitcard[15]+""+splitcard[16];
        return card;
    }


}
