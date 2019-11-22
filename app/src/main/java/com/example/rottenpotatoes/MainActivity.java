package com.example.rottenpotatoes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListMoviesQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mAdapter;

    private ArrayList<ListMoviesQuery.Item> mMovies;
    private final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
//
        ClientFactory.init(this);

        FloatingActionButton btnAddMovie = findViewById(R.id.btn_addMovie);
        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent addMovieIntent = new Intent(MainActivity.this, AddMovieActivity.class);
                MainActivity.this.startActivity(addMovieIntent);
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        // Query list data when we return to the screen
        query();
    }

    public void query(){
        ClientFactory.appSyncClient().query(ListMoviesQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListMoviesQuery.Data> queryCallback = new GraphQLCall.Callback<ListMoviesQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListMoviesQuery.Data> response) {

            mMovies = new ArrayList<>(response.data().listMovies().items());

            Log.i(TAG, "Retrieved list items: " + mMovies.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(mMovies);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };
}
