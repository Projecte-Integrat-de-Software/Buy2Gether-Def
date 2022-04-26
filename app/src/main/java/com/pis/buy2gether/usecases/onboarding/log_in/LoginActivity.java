package com.pis.buy2gether.usecases.onboarding.log_in;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pis.buy2gether.provider.ProviderType;
import com.pis.buy2gether.usecases.onboarding.log_in.forgot_psw.ForgotPasswordActivity;
import com.pis.buy2gether.usecases.onboarding.sign_in.RegisterActivity;
import com.pis.buy2gether.usecases.home.MainActivity;
import com.pis.buy2gether.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    private int GOOGLE_SIGN_IN = 100;

    private ImageButton log;
    private Button sign, guest;
    private TextView name, forgotPassword;
    private ImageView image;
    private TextInputEditText userEditText, pswEditText;
    private ImageButton google_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in_view);
        viewModel = new LoginViewModel(this);
        /* Comprovem si ja hi ha una sessió loguejada a l'app */
        session();

        /* Recuperem els widgets */
        setup();

        log.setOnClickListener(v -> {
            String email = userEditText.getText().toString();
            String psw = pswEditText.getText().toString();

            /* Check if user filled both email and password text fields. */
            if(!(email.isEmpty()) && !(psw.isEmpty())){
                if(!viewModel.login_main(email, psw)){
                    viewModel.showAlert();
                }
                showHome(email, ProviderType.BASIC);
            }else{
                userEditText.startAnimation(viewModel.shakeError());
                pswEditText.startAnimation(viewModel.shakeError());
            }
        } );

        sign.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        } );

        guest.setOnClickListener(v -> {
            showHome("guest", ProviderType.GUEST);
        });

        google_signin.setOnClickListener(v -> {
            GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail().build();

            GoogleSignInClient googleClient = GoogleSignIn.getClient(this,googleConf);
            googleClient.signOut();
            /* Realitzem l'intent amb el ID de GOOGLE_SIGN_IN */
            startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void showHome(String email, ProviderType provider){
        Intent i = new Intent(LoginActivity.this, MainActivity.class).putExtra("provider",provider.name()).putExtra("email",email);
        startActivity(i);
        finish();
    }

    /* Comprovem si ja hi ha una sessió guardada */
    private void session(){
        String email = viewModel.getSession(this, "email");
        String provider = viewModel.getSession(this,"provider");

        if(email != null && provider != null){
            showHome(email, ProviderType.valueOf(provider));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Si la resposta retornada és igual l'ID de GOOGLE_SIGN_IN, la resposta d'aquest activity correspon al de Google */
        if(requestCode == GOOGLE_SIGN_IN){
            if(!viewModel.login_google(data)){
                viewModel.showAlert();
            }
            showHome("prueba", ProviderType.GOOGLE);
    }
    }

    private void setup(){
        log = findViewById(R.id.btn_login);
        sign = findViewById(R.id.login);
        google_signin = findViewById(R.id.btn_google_signin);
        guest = findViewById(R.id.invitado);

        userEditText = findViewById(R.id.txtin_username);
        pswEditText = findViewById(R.id.txtin_psw);

        name = findViewById(R.id.img_appname);
        image = findViewById(R.id.img_logo);
        forgotPassword = findViewById(R.id.btn_rememberpsw);
    }
}