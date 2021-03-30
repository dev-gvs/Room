package kz.gvsx.room.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnimalDao {
    @Query("SELECT * FROM animals")
    List<Animal> getAll();

    @Query("SELECT * FROM animals WHERE LOWER(name) LIKE '%' || :query || '%'" +
            "OR LOWER(genus) LIKE '%' || :query || '%'" +
            "OR LOWER(family) LIKE '%' || :query || '%'" +
            "OR LOWER(`order`) LIKE '%' || :query || '%'" +
            "OR LOWER(class) LIKE '%' || :query || '%'")
    List<Animal> search(String query);

    @Insert
    void insert(Animal animal);
}
