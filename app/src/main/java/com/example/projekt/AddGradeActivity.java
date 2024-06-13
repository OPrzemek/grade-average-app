package com.example.projekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class AddGradeActivity extends AppCompatActivity {
    private EditText gradeEditText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        gradeEditText = findViewById(R.id.gradeEditText);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gradeString = gradeEditText.getText().toString();
                if (!gradeString.isEmpty()) {
                    double gradeValue = Double.parseDouble(gradeString);
                    Grade grade = new Grade();
                    grade.setValue(gradeValue);
                    db.gradeDao().insertAll(grade);
                    finish(); // Powrót do MainActivity
                }
            }
        });
    }
}
