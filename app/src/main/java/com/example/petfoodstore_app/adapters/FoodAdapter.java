package com.example.petfoodstore_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.models.Food;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        super(context, 0, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        }

        Food food = foodList.get(position);

        ImageView imageView = convertView.findViewById(R.id.food_image);
        TextView nameTextView = convertView.findViewById(R.id.food_name);
        TextView descriptionTextView = convertView.findViewById(R.id.food_description);
        TextView priceTextView = convertView.findViewById(R.id.food_price);
        TextView categoryTextView = convertView.findViewById(R.id.food_category);
        TextView petTypeTextView = convertView.findViewById(R.id.food_petType);
        TextView foodTypeTextView = convertView.findViewById(R.id.food_foodType);
        TextView createAtTextView = convertView.findViewById(R.id.food_createAt);

        nameTextView.setText(food.getName());
        descriptionTextView.setText(food.getDescription());
        priceTextView.setText(food.getPrice() + " VND");
        categoryTextView.setText("Category: " + food.getCategory());
        petTypeTextView.setText("Pet Type: " + food.getPetType());
        foodTypeTextView.setText("Food Type: " + food.getFoodType());
        createAtTextView.setText("Created at: " + food.getCreate_at());

        // Load image with Glide
        Glide.with(context).load(food.getImage()).into(imageView);

        return convertView;
    }
}
