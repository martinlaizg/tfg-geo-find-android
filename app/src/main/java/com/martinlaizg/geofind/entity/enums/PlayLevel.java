package com.martinlaizg.geofind.entity.enums;

import com.martinlaizg.geofind.R;

public enum PlayLevel {
    // TODO: extraer a resources
    THERM("termometro"),
    COMPASS("brujula"),
    ANY("cualquiera");

    private String text;


    PlayLevel(String textName) {
        this.text = textName;
    }

    public String getText() {
        return text;
    }
}
