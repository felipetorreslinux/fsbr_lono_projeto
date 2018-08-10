package com.lono.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;

public class BrandCard {

    private static final String maskCard = "#### #### #### ####";
    private static final String maskValid = "##/####";

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher brand(final EditText editText, final ImageView imageView) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = BrandCard.unmask(s.toString());
                String mask;
                String defaultMask = getDefaultMask(str);
                switch (str.length()) {
                    case 16:
                        mask = maskCard;
                        break;
                    default:
                        mask = defaultMask;
                        break;
                }

                String brand = String.valueOf( s );
                if(brand.startsWith( "4" )){
                    imageView.setVisibility( View.VISIBLE );
                    imageView.setImageResource( R.drawable.visa );
                }else if(brand.startsWith( "5" )){
                    imageView.setVisibility( View.VISIBLE );
                    imageView.setImageResource( R.drawable.mastercard );
                }else if(brand.startsWith( "6" )){
                    imageView.setVisibility( View.VISIBLE );
                    imageView.setImageResource( R.drawable.hiper );
                }else{
                    imageView.setVisibility( View.GONE );
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

    public static TextWatcher valid (final EditText editText){
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = BrandCard.unmask(s.toString());
                String mask;
                String defaultMask = getDefaultMaskValid(str);
                switch (str.length()) {
                    case 6:
                        mask = maskValid;
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
        String defaultMask = maskCard;
        return defaultMask;
    }

    private static String getDefaultMaskValid(String str) {
        String defaultMask = maskValid;
        return defaultMask;
    }
}
