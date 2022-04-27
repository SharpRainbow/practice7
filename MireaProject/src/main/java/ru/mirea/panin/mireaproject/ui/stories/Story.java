package ru.mirea.panin.mireaproject.ui.stories;

public class Story {
    private String description;
    private String image;

    public Story(String description, String image) {
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
