package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.petcare.R;
import com.example.petcare.db.AppDatabase;
import com.example.petcare.db.User;
import com.example.petcare.util.HashUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        db = AppDatabase.get(this);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        db.userDao().findByEmail(email).observe(this, user -> {
            if (user != null && user.passwordHash.equals(HashUtil.sha256(pass))) {
                // Save session
                getSharedPreferences("session", MODE_PRIVATE)
                        .edit()
                        .putLong("userId", user.id)
                        .apply();
                startMain();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMain() {
        long userId = getSharedPreferences("session", MODE_PRIVATE).getLong("userId", -1);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("userId", userId);
        startActivity(i);
    }
}