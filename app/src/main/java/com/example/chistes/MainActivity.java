package com.example.chistes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private JokeApiService jokeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://v2.jokeapi.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jokeApi = retrofit.create(JokeApiService.class);

        findViewById(R.id.getJokeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJoke();
            }
                });

            }

    private void getJoke() {
        String selectedCategory = ((RadioButton) findViewById(R.id.categoryRadioGroup)
                .findViewById(R.id.anyCategoryRadioButton)).isChecked()
                ? "Any"
                : "Programming";
        String selectedLanguage = ((RadioButton) findViewById(R.id.languageRadioGroup)
                .findViewById(R.id.englishLanguageRadioButton)).isChecked()
                ? "en"
                : "es";
        Call<JsonObject> call = jokeApi.getJoke(selectedCategory, selectedLanguage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jokeObject = response.body();
                    displayJokeResult(jokeObject);
                } else {
                    displayErrorMessage("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                displayErrorMessage("Error: " + t.getMessage());
            }
        });
    }


    private void displayJokeResult(JsonObject jokeObject) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(jokeObject);
                TextView textView = findViewById(R.id.resultTextView);

                textView.setText(jsonString);

    }


            private void displayErrorMessage(String errorMessage) {
                TextView textView = findViewById(R.id.resultTextView);
                textView.setText(errorMessage);
            }

}