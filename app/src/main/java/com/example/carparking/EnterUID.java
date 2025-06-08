package com.example.carparking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EnterUID extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_uid);

        EditText uidEditText = findViewById(R.id.uid);
        Button enterButton = findViewById(R.id.enter);

        enterButton.setOnClickListener(v -> {
            String uid = uidEditText.getText().toString().trim();

            if (!uid.isEmpty()) {
                Intent intent = new Intent(EnterUID.this, MainActivity.class);
                intent.putExtra("uid", uid);  // gửi uid
                startActivity(intent);
            } else {
                Toast.makeText(EnterUID.this, "Vui lòng nhập UID", Toast.LENGTH_SHORT).show();
            }
        });



    }
}