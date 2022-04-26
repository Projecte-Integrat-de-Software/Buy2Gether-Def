package com.pis.buy2gether.provider.services;

import android.content.Intent;
import com.pis.buy2gether.model.session.Session;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pis.buy2gether.provider.ProviderType;

import java.util.HashMap;

public enum FirebaseAuthenticationService {
    INSTANCE;

    private FirebaseAuth firebaseAuth;

    FirebaseAuthenticationService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public String google_signin(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        String email = account.getEmail();

        Session.INSTANCE.setSessionId(firebaseAuth.getCurrentUser().getUid());
        /* Finalitzem autentificant-nos a Firebase com a Login normal, amb email i contrasenya */
        if(account != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(t -> {
                if(t.isSuccessful()){
                    HashMap userInfo = new HashMap();
                    userInfo.put("email",email);
                    userInfo.put("username",account.getDisplayName());
                    userInfo.put("provider",ProviderType.GOOGLE);

                }else{
                    throw new RuntimeException("Error al autenticar-se a Firebase");
                }
            });
        }
        return email;
    }

    public void email_signin(String email,String psw){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,psw).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                throw new RuntimeException("Error al autenticar-se a Firebase");
            }
        });
    }

    public FirebaseAuth getFirebaseAuth() {
        return this.firebaseAuth;
    }
}
