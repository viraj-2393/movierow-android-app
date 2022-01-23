package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.movies.movierow.Adapters.TrendAdapter;
import com.movies.movierow.Adapters.YourFavAdapter;
import com.movies.movierow.Controllers.ViewModal;
import com.movies.movierow.Models.FavModal;
import com.movies.movierow.R;

import java.util.List;

public class YourFavs extends AppCompatActivity {
    ImageButton go_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_your_favs);
        String type = getIntent().getStringExtra("type");
        getData(type);

        //go back
        go_back = findViewById(R.id.go_back_button);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getData(String type){
       List<FavModal> favModals = new ViewModal(getApplication(),type,0).getAllFavs(type);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.your_favs);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        //gridLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        YourFavAdapter customAdapter = new YourFavAdapter(favModals, getApplicationContext(),YourFavs.this);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }
}