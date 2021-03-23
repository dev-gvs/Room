package kz.gvsx.room.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnimalDao {
    @Query("SELECT * FROM animal")
    List<Animal> getAll();

    @Insert
    void insert(Animal animal);
}
