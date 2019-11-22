package com.example.rottenpotatoes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.amplify.generated.graphql.CreateMovieMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateMovieInput;

public class AddMovieActivity extends AppCompatActivity {
    private static final String TAG = AddMovieActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Button btnAddItem = findViewById(R.id.btn_save);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save(){
        final String title = ((EditText) findViewById(R.id.editTxt_title)).getText().toString();
        final String genre = ((EditText) findViewById(R.id.editText_genre)).getText().toString();
        final String director = ((EditText) findViewById(R.id.editText_director)).getText().toString();
        final int year = Integer.parseInt(((EditText) findViewById(R.id.editText_year)).getText().toString());

        CreateMovieInput input = CreateMovieInput.builder()
                .title(title)
                .genre(genre)
                .director(director)
                .year(year)
                .build();

        CreateMovieMutation addMovieMutation = CreateMovieMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addMovieMutation).enqueue(mutateCallback);
    }

    //Mutation callback code
    private GraphQLCall.Callback<CreateMovieMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateMovieMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateMovieMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddMovieActivity.this, "Added movie", Toast.LENGTH_SHORT).show();
                    AddMovieActivity.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddMovieMutation", e);
                    Toast.makeText(AddMovieActivity.this, "Failed to add movie", Toast.LENGTH_SHORT).show();
                    AddMovieActivity.this.finish();
                }
            });
        }
    };
}
