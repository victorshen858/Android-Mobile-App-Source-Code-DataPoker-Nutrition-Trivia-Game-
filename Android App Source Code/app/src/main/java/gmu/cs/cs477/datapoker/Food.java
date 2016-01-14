package gmu.cs.cs477.datapoker;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by 2013vshen on 11/5/2015.
 */
//This class contains all the properties and data needed to make comparisons between two image views
public class Food {
    public String name = "", type = "", category="";
    public double calories = 0, totalFat = 0, cholesterol = 0, sodium = 0, potassium = 0, totalCarbohydrate = 0;
    public double dietaryFiber = 0, sugar = 0, protein = 0, vitaminA = 0, vitaminC = 0, calcium = 0, iron = 0;
    public double vitaminB6 = 0, magnesium = 0;
    public Bitmap b;
    public Food(String name, String type, String category, double calories, double totalFat, double cholesterol,
                double sodium, double potassium, double totalCarbohydrate, double dietaryFiber, double sugar,
                double protein, double vitaminA, double vitaminC, double calcium, double iron, double vitaminB6,
                double magnesium, Bitmap b) {
        //set properties for constructor
        this.name = name.toLowerCase();
        this.type = type.toLowerCase();
        this.category = category.toLowerCase();
        this.calories = calories;
        this.totalFat = totalFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.potassium = potassium;
        this.totalCarbohydrate = totalCarbohydrate;
        this.dietaryFiber = dietaryFiber;
        this.sugar = sugar;
        this.protein = protein;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.calcium = calcium;
        this.iron = iron;
        this.vitaminB6 = vitaminB6;
        this.magnesium = magnesium;
        this.b = b;
    }

    public String getFoodName(){
        return this.name;
    }

    public void setFoodName(String fn){
        this.name = fn.toLowerCase();
    }

    public String getFoodType(){
        return this.type;
    }

    public void setFoodType(String t){
        this.name = t.toLowerCase();
    }

    public String getCategory(){
        return this.category;
    }

    public void setCategory(String c){ this.category = c.toLowerCase();}

    public Bitmap getBitmap(){
        return this.b;
    }

    public void setBitmap(Bitmap bm){ this.b = bm; }

    public double getFoodValueFromCategory(){
        String category = this.category.toLowerCase();
        switch(category){
            case "calories":
                return this.calories;
            case "total fat":
                return this.totalFat;
            case "cholesterol":
                return this.cholesterol;
            case "sodium":
                return this.sodium;
            case "potassium":
                return this.potassium;
            case "total carbohydrate":
                return this.totalCarbohydrate;
            case "dietary fiber":
                return this.dietaryFiber;
            case "sugar":
                return this.sugar;
            case "protein":
                return this.protein;
            case "vitamin a":
                return this.vitaminA;
            case "vitamin c":
                return this.vitaminC;
            case "calcium":
                return this.calcium;
            case "iron":
                return this.iron;
            case "vitamin b6":
                return this.vitaminB6;
            case "magnesium":
                return this.magnesium;
            default:
                return -1.0;
        }

    }
}