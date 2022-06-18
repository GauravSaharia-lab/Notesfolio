package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.saharia.notesell.Fragments.AddFragment;
import com.saharia.notesell.Fragments.FavouriteFragment;
import com.saharia.notesell.Fragments.HomeFragment;
import com.saharia.notesell.Fragments.TrendingFragment;
import com.saharia.notesell.Fragments.userProfileFragment;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
      ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction homeFrag= getSupportFragmentManager().beginTransaction();
        homeFrag.replace(R.id.container,new HomeFragment());
        homeFrag.commit();

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()){

                    case R.id.home:
                        transaction.replace(R.id.container,new HomeFragment());
                        break;

                    case R.id.profile:

                        transaction.replace(R.id.container,new userProfileFragment());
                        break;
                    case R.id.add:
                        transaction.replace(R.id.container,new AddFragment());
                        break;
                    case R.id.com:
                        transaction.replace(R.id.container,new TrendingFragment());
                        break;
                    case R.id.chats:
                        transaction.replace(R.id.container,new FavouriteFragment());
                        break;
                }
                transaction.commit();
                return  true;
            }
        });



    }
}