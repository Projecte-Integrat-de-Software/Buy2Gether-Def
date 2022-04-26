package com.pis.buy2gether.model.domain.pojo;

public class Comanda {
    private int id;
    private String data;
    private int status;
    private int stars;

    public Comanda() {
    }

    public Comanda(int id, String data, int status, int stars) {
        this.id = id;
        this.data = data;
        this.status = status;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
