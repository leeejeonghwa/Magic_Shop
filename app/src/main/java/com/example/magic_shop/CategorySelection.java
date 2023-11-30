package com.example.magic_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CategorySelection extends AppCompatActivity {

    private static CategorySelection instance = new CategorySelection();

    private Category selectedCategory;

    private int selectedDetailedCategory;

    public enum Category {
        TOP, OUTER, PANTS, SKIRT_ONE_PIECE, SHOES, BAG
    }

    private CategorySelection() {
        selectedCategory = Category.TOP;
    }

    public static CategorySelection getInstance() {
        return instance;
    }


    public void changeCategory(int selectedCategory) {
        for (Category category : Category.values()) {
            if (selectedCategory == category.ordinal()) {
                this.selectedCategory = category;
            }
        }
    }

    public void setSelectedDetailedCategory(int detailedCategory) {
        this.selectedDetailedCategory = detailedCategory;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public int getSelectedDetailedCategory() {
        return selectedDetailedCategory;
    }

}
