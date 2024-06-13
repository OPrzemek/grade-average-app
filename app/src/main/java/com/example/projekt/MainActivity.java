package com.example.projekt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Konfiguracja Toolbara
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Konfiguracja ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    // Obsługa kliknięcia w "Home"
                } else if (id == R.id.nav_add) {
                    Intent intent = new Intent(MainActivity.this, AddGradeActivity.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Inicjalizacja bazy danych
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        // Pobranie danych z bazy
        final List<Grade>[] myDataset = new List[]{db.gradeDao().getAll()};

        // Ustawienie RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ustawienie adaptera
        MyAdapter adapter = new MyAdapter(myDataset[0]);
        recyclerView.setAdapter(adapter);

        // Przycisk do przechodzenia do AddGradeActivity
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGradeActivity.class);
                startActivity(intent);
            }
        });

        // Obliczanie średniej
        calculateAverage(myDataset[0], findViewById(R.id.averageTextView));

        // Usunięcie oceny
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Grade grade = myDataset[0].get(position);
                db.gradeDao().delete(grade);
                myDataset[0].remove(position);
                adapter.notifyItemRemoved(position);
                calculateAverage(myDataset[0], findViewById(R.id.averageTextView));
            }
        });

        // Przykład użycia Handlera do odświeżania danych co jakiś czas
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                myDataset[0] = db.gradeDao().getAll();
                adapter.setGrades(myDataset[0]);
                adapter.notifyDataSetChanged();
                calculateAverage(myDataset[0], findViewById(R.id.averageTextView));
                handler.postDelayed(this, 5000); // Odświeżanie co 5 sekund
            }
        };
        handler.post(runnable);
    }

    private void calculateAverage(List<Grade> myDataset, TextView averageTextView) {
        if (myDataset.size() == 0) {
            averageTextView.setText("Brak ocen");
            return;
        }
        double sum = 0;
        for (Grade grade : myDataset) {
            sum += grade.getValue();
        }
        double average = sum / myDataset.size();
        averageTextView.setText(String.format("Średnia: %.2f", average));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

