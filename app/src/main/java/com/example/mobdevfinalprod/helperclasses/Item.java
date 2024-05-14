package com.example.mobdevfinalprod.helperclasses;

public class Item {
    String exerciseName;
    String image;

    public Item(String exerciseName, String image) {
        this.exerciseName = exerciseName;
        this.image = image;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
