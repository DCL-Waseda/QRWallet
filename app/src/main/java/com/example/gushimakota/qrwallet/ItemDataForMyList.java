package com.example.gushimakota.qrwallet;

/**
 * Created by gushimakota on 16/06/10.
 */
public class ItemDataForMyList {

    private String item_;
    private String price_;

    public void setItemName(String item) {
        item_ = item;
    }

    public String getItemName() {
        return item_;
    }

    public void setItemPrice(int price) {
        price_ = String.valueOf(price);
    }

    public String getItemPrice() {
        return price_;
    }
}
