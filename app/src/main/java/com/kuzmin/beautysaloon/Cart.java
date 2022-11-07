package com.kuzmin.beautysaloon;

public class Cart {
    public String id, textProsed, textEmail, sound_id;

    public Cart() {
    }

    public Cart(String id, String textProsed, String textEmail, String sound_id) {
        this.id = id;
        this.textProsed = textProsed;
        this.textEmail = textEmail;
        this.sound_id = sound_id;
    }
}
