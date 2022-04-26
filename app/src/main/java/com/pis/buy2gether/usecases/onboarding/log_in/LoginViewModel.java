package com.pis.buy2gether.usecases.onboarding.log_in;

import android.content.Context;
import android.content.Intent;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.pis.buy2gether.model.session.Session_to_refactor;
import com.pis.buy2gether.provider.ProviderType;
import com.pis.buy2gether.provider.services.FirebaseAuthenticationService;

import java.util.HashMap;

class LoginViewModel extends ViewModel {
    private Context context;

    LoginViewModel(Context context){ this.context = context; }

    void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("S'ha produït un error autentificant l'usuari.\n");
        builder.setPositiveButton("Acceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(1000);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    String getSession(Context context, String key){
        return Session_to_refactor.INSTANCE.getDataSession(context,key);
    }

    public void saveUserInfo(String userID, String email, String username, ProviderType provider){
        HashMap userInfo = new HashMap();
        userInfo.put("email",email);
        userInfo.put("username",username);
        userInfo.put("provider",provider);
        Session_to_refactor.INSTANCE.saveDB("users",userID, userInfo);
    }


    // Refactoring code
    public boolean login_main(String email, String password){
        try{
            FirebaseAuthenticationService.INSTANCE.email_signin(email, password);
        return true;
        }
        catch (RuntimeException e){
            return false;
        }
    }

    public boolean login_google(Intent data){
        try{
            FirebaseAuthenticationService.INSTANCE.google_signin(data);
            return true;
        }
        catch (RuntimeException e){
            return false;
        }
    }


}
