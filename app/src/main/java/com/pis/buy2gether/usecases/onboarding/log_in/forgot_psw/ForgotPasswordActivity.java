package com.pis.buy2gether.usecases.onboarding.log_in.forgot_psw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pis.buy2gether.R;
import com.pis.buy2gether.usecases.onboarding.log_in.LoginActivity;
import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText email;
    private Button btn_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_remember_psw);

        //Hooks
        email = findViewById(R.id.txtin_email);
        btn_email = findViewById(R.id.btn_remember);

        btn_email.setOnClickListener(this::OnClick);
    }

    private void OnClick(View view) {
        String email = this.email.getText().toString().trim();

        // Validate the email format is correct
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Correu invàlid");
            return;
        }

        sendResetPswEmail(email);
    }

    private void sendResetPswEmail(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Correu enviat!",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Correu no existent",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
