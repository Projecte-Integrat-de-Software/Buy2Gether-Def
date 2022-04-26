package com.pis.buy2gether.usecases.home.user.address;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pis.buy2gether.model.session.Session_to_refactor;

import java.util.HashMap;

public class AddressViewModel extends ViewModel {

    void saveAddressDB(String nom, String address, String tel, String cp){
        HashMap addressInfo = new HashMap();
        addressInfo.put("Address name",nom);
        addressInfo.put("Full address",address);
        addressInfo.put("Telephone",tel);
        addressInfo.put("Zip code",cp);

        Session_to_refactor.INSTANCE.saveAddressDB(getUser(),addressInfo);
    }

    private String getUser(){
        String emailUser = "unknown";
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            emailUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return emailUser;
    }

    void deleteAddressDB(String nom){
        Session_to_refactor.INSTANCE.deleteAddressDB(getUser(),nom);
    }

    public HashMap addressInfo(String nom, String address, String tel, String cp){
        HashMap addressInfo = new HashMap();
        addressInfo.put("Address name",nom);
        addressInfo.put("Full address",address);
        addressInfo.put("Telephone",tel);
        addressInfo.put("Zip code",cp);
        return addressInfo;
    }

    public Task getAddresses(){
        return Session_to_refactor.INSTANCE.getAddressesDB(getUser());
    }
}