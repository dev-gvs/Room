package kz.gvsx.room;

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
}