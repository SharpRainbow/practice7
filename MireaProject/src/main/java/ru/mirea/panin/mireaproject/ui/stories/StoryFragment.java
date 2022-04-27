package ru.mirea.panin.mireaproject.ui.stories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import ru.mirea.panin.mireaproject.databinding.FragmentStoryBinding;

public class StoryFragment extends Fragment {
    private FragmentStoryBinding binding;
    public static ArrayList<String> saved;
    public static ArrayList<Story> stories;
    public static StoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        stories = new ArrayList<>();
        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StoryCreationActivity.class);
            startActivity(intent);
        });

        SharedPreferences preferences = requireContext().getSharedPreferences("Stories", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) preferences.getStringSet("storySet", new HashSet<>());
        saved = new ArrayList<>(set);

        Gson gson = new Gson();
        Uri image = Uri.parse("android.resource://ru.mirea.panin.mireaproject/drawable/mountains");
        stories.add(new Story("First story", String.valueOf(image)));
        for (String str : saved) {
            stories.add(gson.fromJson(str, Story.class));
        }
        RecyclerView rec = binding.list;
        adapter = new StoryAdapter(requireContext(), stories);
        rec.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}