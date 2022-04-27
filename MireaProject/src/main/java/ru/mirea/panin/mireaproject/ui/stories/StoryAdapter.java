package ru.mirea.panin.mireaproject.ui.stories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import ru.mirea.panin.mireaproject.R;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Story> stories;
    private Context context;

    StoryAdapter(Context context, List<Story> stories) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.stories = stories;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView picView;
        final TextView description;

        ViewHolder(View view) {
            super(view);
            picView = view.findViewById(R.id.pic);
            description = view.findViewById(R.id.description);
        }
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        Story story = stories.get(position);
        String image = story.getImage();

        try {
            InputStream imageStream = context.getContentResolver().openInputStream(Uri.parse(image));
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            holder.picView.setImageBitmap(bitmap);
            holder.description.setText(story.getDescription());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
}
