package com.example.tambre_who.tallcamera;

/**
 * Created by tambre-who on 10/20/19.
 */

public class ListViewCell {
    String food;
    String time;

    public ListViewCell(String food, String time) {
        this.food = food;
        this.time = time;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFood() {
        return food;
    }

    public String getTime() {
        return time;
    }
}
