package com.example.nikol.mathmania;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

class LanguageHandler {

    public static void changeLocale(Resources res, String loc) {
        Configuration config = new Configuration(res.getConfiguration());

        switch(loc) {
            case "no":
                config.locale = new Locale("no");
                System.out.println("Set Norwegian");
                break;
            case "de":
                config.locale = Locale.GERMAN;
                System.out.println("Set German");
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
