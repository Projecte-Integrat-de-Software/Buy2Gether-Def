package com.pis.buy2gether.model.domain.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pis.buy2gether.model.domain.pojo.Comanda;

import java.util.ArrayList;

enum ComandasLifeData {
    INSTANCE;

    private final MutableLiveData<ArrayList<Comanda>> comanda;

    ComandasLifeData() {
        comanda = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Comanda>> getComanda() {
        return comanda;
    }

    public void setComanda(ArrayList<Comanda> comanda) {
        this.comanda.setValue(comanda);
    }

}

