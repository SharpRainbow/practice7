package ru.mirea.panin.mireaproject.ui.stories;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;

import ru.mirea.panin.mireaproject.R;

public class StoryCreationActivity extends Activity {
    private ImageView imageView;
    private final int PICTURE_PICKED = 1;
    private Button btn;
    private Uri image;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        imageView = findViewById(R.id.imageView3);
        btn = findViewById(R.id.pickbtn);
        editText = findViewById(R.id.editText);
        btn.setOnClickListener(view -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK);
            pickPhoto.setType("image/*");
            startActivityForResult(pickPhoto, PICTURE_PICKED);
        });
        findViewById(R.id.savebtn).setOnClickListener(view -> {
            if (imageView.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "Can't save story without an image",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Story cur = new Story(editText.getText().toString(), String.valueOf(image));
            Gson gson = new Gson();
            StoryFragment.stories.add(cur);
            StoryFragment.saved.add(gson.toJson(cur));
            StoryFragment.adapter.notifyDataSetChanged();
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("Stories", MODE_PRIVATE);
            HashSet<String> hashSet = new HashSet<>(StoryFragment.saved);
            preferences.edit().putStringSet("storySet", hashSet).apply();
            this.finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_PICKED) {
            try {
                image = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(image);
                Bitmap selected = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selected);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
