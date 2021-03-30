package kz.gvsx.room.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "animals")
public class Animal {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @NonNull
    public String name;

    @NonNull
    public String genus;

    @NonNull
    public String family;

    @NonNull
    public String order;

    @NonNull
    @ColumnInfo(name = "class")
    public String animalClass;

    public Animal(@NonNull String name,
                  @NonNull String genus,
                  @NonNull String family,
                  @NonNull String order,
                  @NonNull String animalClass) {
        this.name = name;
        this.genus = genus;
        this.family = family;
        this.order = order;
        this.animalClass = animalClass;
    }

    public static Animal[] populateData() {
        return new Animal[]{
                new Animal("Волк", "Волки", "Псовые",
                        "Хищные", "Млекопитающие"),
                new Animal("Медведь", "Медведи", "Медвежьи",
                        "Хищные", "Млекопитающие"),
                new Animal("Гигантская акула-молот", "Акулы-молоты", "Молотоголовые акулы",
                        "Кархаринообразные", "Хрящевые рыбы"),
                new Animal("Жираф", "Жирафы", "Жирафовые",
                        "Китопарнокопытные", "Млекопитающие"),
                new Animal("Жако", "Тупохвостые попугаи", "Попугаевые",
                        "Попугаеобразные", "Птицы"),
                new Animal(
                        "Олень Давида", "Олени Давида", "Оленевые",
                        "Парнокопытные", "Млекопитающие")
        };
    }
}