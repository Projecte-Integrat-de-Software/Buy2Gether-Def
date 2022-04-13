package com.pis.buy2gether.usecases.onboarding.log_in;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pis.buy2gether.usecases.home.ProviderType;
import com.pis.buy2gether.usecases.onboarding.sign_in.Register_view;
import com.pis.buy2gether.usecases.home.MainActivity;
import com.pis.buy2gether.R;

public class Log_in_view extends AppCompatActivity {


    private ImageButton log;
    private Button sign;
    private TextView name;
    private ImageView image;
    private TextInputEditText userEditText;
    private TextInputEditText pswEditText;
    private Button guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in_view);

        /* Comprovem si ja hi ha una sessió loguejada a l'app */
        session();

        log = findViewById(R.id.btn_login);
        sign = findViewById(R.id.login);

        guest = findViewById(R.id.invitado);

        userEditText = findViewById(R.id.txtin_username);
        pswEditText = findViewById(R.id.txtin_psw);

        name = findViewById(R.id.img_appname);
        image = findViewById(R.id.img_logo);

        log.setOnClickListener(v -> {
            if(!(userEditText.getText().toString().isEmpty()) && !(pswEditText.getText().toString().isEmpty())){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEditText.getText().toString(),pswEditText.getText().toString()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        showHome(task.getResult().getUser().getEmail(), ProviderType.BASIC);
                    }else{
                        showAlert();
                    }
                });
            }else{
                userEditText.startAnimation(shakeError());
                pswEditText.startAnimation(shakeError());
            }
        } );

        sign.setOnClickListener(v -> {
            Intent i;
            i = new Intent(Log_in_view.this, Register_view.class);
            startActivity(i);
            finish();
        } );

        guest.setOnClickListener(v -> {
            showHome("guest", ProviderType.GUEST);
        });

    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("S'ha produït un error autentificant l'usuari.");
        builder.setPositiveButton("Acceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String email, ProviderType provider){
        Intent i = new Intent(Log_in_view.this, MainActivity.class).putExtra("provider",provider.name()).putExtra("email",email);
        startActivity(i);
        finish();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(1000);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    /* Comprovem si ja hi ha una sessió guardada */
    private void session(){
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email",null);
        String provider = prefs.getString("provider",null);

        if(email != null && provider != null){
            showHome(email, ProviderType.valueOf(provider));
        }
    }

}