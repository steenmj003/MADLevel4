package com.hva.joris.gamebacklog;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

//Creates specific commands for interacting with the database
@Dao
public interface GameObjectDao {

    @Query("SELECT * FROM game")
    public List<GameObject> getAllGames();

    @Insert
    public void insertGames(GameObject games);

    @Delete
    public void deleteGames(GameObject games);

    @Update
    public void updateGames(GameObject games);
}
