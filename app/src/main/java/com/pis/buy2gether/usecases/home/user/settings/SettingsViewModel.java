package com.pis.buy2gether.usecases.home.user.settings;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.pis.buy2gether.model.session.Session_to_refactor;

public class SettingsViewModel extends ViewModel {

    private Context context;

    public SettingsViewModel(Context context) {
        this.context = context;
    }

    public void clearSession(){
        Session_to_refactor.INSTANCE.clearDataSession(context);
    }

    /**
     * actualitzem la localització de l'usuari
     * @param ciutat
     */
    void change_UserCity(String ciutat){
        Session_to_refactor.INSTANCE.updateUser(getUser(),"city",ciutat);
    }

    /**
     * actualitzem el nom de l'usuari
     * @param nom
     */
    void change_Username(String nom){
        Session_to_refactor.INSTANCE.updateUser(getUser(),"username",nom);
    }


    /**
     * obtenim uid de l'usuari
     * @return
     */
    private String getUser(){
        String emailUser = "unknown";
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            emailUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return emailUser;
    }

}