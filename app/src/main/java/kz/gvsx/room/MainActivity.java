package kz.gvsx.room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

import kz.gvsx.room.database.Animal;
import kz.gvsx.room.database.AnimalDao;
import kz.gvsx.room.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    // Строка, по которой форматируется отображение данных по животному из БД.
    private static final String ANIMAL_FORMAT = "%s\n├ Род: %s\n├ Семейство: %s\n├ Отряд: %s\n└ Класс: %s\n\n";

    private AnimalDao animalDao;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        animalDao = db.animalDao();

        textView = findViewById(R.id.textView);
        EditText searchField = findViewById(R.id.editTextSearchField);

        // Проверяем заполнена ли БД, если нет, то добавляем строки.
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean dbIsPopulated = sharedPref.getBoolean("db_is_populated", false);
        if (!dbIsPopulated) {
            populateDatabase();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("db_is_populated", true);
            editor.apply();
        }

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadAnimals(editable.toString());
            }
        });

        loadAnimals("");
    }

    private void loadAnimals(String query) {
        // Запускаем отдельный поток, в котором получаем данные из БД.
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Animal> animals;
            if (query.isEmpty()) {
                animals = animalDao.getAll();
            } else {
                animals = animalDao.search(query.toLowerCase());
            }
            // Обновляем textView в основном потоке
            runOnUiThread(() -> textView.setText(formatList(animals)));
        });
    }

    private String formatList(List<Animal> animalsList) {
        StringBuilder result = new StringBuilder();

        for (Animal animal : animalsList) {
            String formatted = String.format(ANIMAL_FORMAT, animal.name,
                    animal.genus, animal.family, animal.order, animal.animalClass);
            result.append(formatted);
        }

        return result.toString();
    }

    private void populateDatabase() {
        // Добавляем животных в отдельном потоке.
        Executors.newSingleThreadExecutor().execute(() -> {
                    animalDao.insert(new Animal(
                            "Волк", "Волки", "Псовые",
                            "Хищные", "Млекопитающие"));
                    animalDao.insert(new Animal(
                            "Медведь", "Медведи", "Медвежьи",
                            "Хищные", "Млекопитающие"));
                    animalDao.insert(new Animal(
                            "Гигантская акула-молот", "Акулы-молоты", "Молотоголовые акулы",
                            "Кархаринообразные", "Хрящевые рыбы"));
                    animalDao.insert(new Animal(
                            "Жираф", "Жирафы", "Жирафовые",
                            "Китопарнокопытные", "Млекопитающие"));
                    animalDao.insert(new Animal(
                            "Жако", "Тупохвостые попугаи", "Попугаевые",
                            "Попугаеобразные", "Птицы"));
                    animalDao.insert(new Animal(
                            "Олень Давида", "Олени Давида", "Оленевые",
                            "Парнокопытные", "Млекопитающие"));
                }
        );
    }
}