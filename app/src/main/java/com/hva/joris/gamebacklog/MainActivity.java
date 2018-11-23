package com.hva.joris.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameObjectAdapter.GameClickListener{
    public final static int GET_ALL_GAMES = 0;
    public final static int DELETE_GAME = 1;
    public final static int UPDATE_GAME = 2;
    public final static int INSERT_GAME = 3;
    private static final int REQUESTCODE_EDIT = 1234;
    private static final int REQUESTCODE_ADD = 4321;
    public static final String EXTRA_GAME = "GAME";

    public static AppDatabase appDatabase;

    private List<GameObject> gameObjects = new ArrayList<>();
    private GameObjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the recyclerview and set it's layout manager
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        //Get the db instance
        appDatabase = AppDatabase.getInstance(this);

        //Instantiate the adapter and add it to the recycleView
        adapter = new GameObjectAdapter(gameObjects, getResources(), this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GameAsyncTask gameAsyncTask = new GameAsyncTask(GET_ALL_GAMES);
        gameAsyncTask.execute();

        FloatingActionButton addGameButton = findViewById(R.id.addGameButton);
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,DetailsActivity.class),REQUESTCODE_ADD);
            }
        });

        //Swipe right to remove the gameObject
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                GameObject gameObject = gameObjects.get(position);
                GameAsyncTask gameAsyncTask = new GameAsyncTask(DELETE_GAME);
                gameAsyncTask.execute(gameObject);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUESTCODE_ADD){
                GameObject gameObject = data.getParcelableExtra(EXTRA_GAME);
                GameAsyncTask gameAsyncTask = new GameAsyncTask(INSERT_GAME);
                gameAsyncTask.execute(gameObject);
            }
            else if(requestCode == REQUESTCODE_EDIT) {
                GameObject gameObject = data.getParcelableExtra(EXTRA_GAME);
                GameAsyncTask gameAsyncTask = new GameAsyncTask(UPDATE_GAME);
                gameAsyncTask.execute(gameObject);
            }
        }
    }

    public void onGameDbUpdated(List list) {
        gameObjects = list;
        updateUI();
    }

    public void updateUI(){
        adapter.swapList(gameObjects);
    }

    @Override
    public void gameOnClick(int i) {
        GameObject gameObject = gameObjects.get(i);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(EXTRA_GAME, gameObject);
        startActivityForResult(intent, REQUESTCODE_EDIT);
    }

    public class GameAsyncTask extends AsyncTask<GameObject, Void, List> {
        private int taskCode;
        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
            }
            @Override
            protected List doInBackground(GameObject... gameObjects) {
                switch (taskCode) {
                    case DELETE_GAME:
                        appDatabase.gameObjectDao().deleteGames(gameObjects[0]);
                        break;
                    case UPDATE_GAME:
                        appDatabase.gameObjectDao().updateGames(gameObjects[0]);
                        break;
                    case INSERT_GAME:
                        appDatabase.gameObjectDao().insertGames(gameObjects[0]);
                        break;
            }
            return appDatabase.gameObjectDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List games){
            super.onPostExecute(games);
            onGameDbUpdated(games);
        }
    }
}
