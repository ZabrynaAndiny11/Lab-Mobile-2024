package com.example.praktikum_8;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    private Button btn_submit;
    private EditText et_title, et_desc;
    private DBConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConfig = new DBConfig(this);
        btn_submit = findViewById(R.id.btn_submit);
        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> showExitConfirmationDialog());

        btn_submit.setOnClickListener(v -> {
            String title = et_title.getText().toString().trim();
            String description = et_desc.getText().toString().trim();

            if (title.isEmpty()) {
                et_title.setError("Please enter your title");
            } else {
                dbConfig.insertData(title, description);
                Toast.makeText(AddActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel")
                .setMessage("Do you want to cancel adding the form?")
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    super.onBackPressed();
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }
}