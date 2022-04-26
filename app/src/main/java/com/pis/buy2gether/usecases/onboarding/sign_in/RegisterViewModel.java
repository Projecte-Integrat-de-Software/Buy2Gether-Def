package com.pis.buy2gether.usecases.onboarding.sign_in;

import android.content.Context;
import android.content.Intent;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.pis.buy2gether.model.session.Session_to_refactor;
import com.pis.buy2gether.usecases.home.MainActivity;
import com.pis.buy2gether.provider.ProviderType;

import java.util.HashMap;

class RegisterViewModel extends ViewModel {

    private Context context;

    RegisterViewModel(Context context){ this.context = context; }

    void showAlert(Task task){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("S'ha produït un error autentificant l'usuari." + task.getException());
        builder.setPositiveButton("Acceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showHome(String email, ProviderType provider){
        Intent i = new Intent(context, MainActivity.class).putExtra("provider",provider.name()).putExtra("email",email);
        context.startActivity(i);
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(1000);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    public void saveUserInfo(String uid, String email, String username, ProviderType provider){
        HashMap userInfo = new HashMap();
        userInfo.put("email",email);
        userInfo.put("username",username);
        userInfo.put("provider",provider);
        Session_to_refactor.INSTANCE.saveDB("users",uid, userInfo);
    }
}
