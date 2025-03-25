package com.example.petfoodstore_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.activities.FoodListActivity;
import com.example.petfoodstore_app.models.Food;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

public class FoodAdapter extends ArrayAdapter<Food> {
    private FoodListActivity context;
    private List<Food> foodList;

    public FoodAdapter(FoodListActivity context, List<Food> foodList) {
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
//        TextView categoryTextView = convertView.findViewById(R.id.food_category);
//        TextView petTypeTextView = convertView.findViewById(R.id.food_petType);
//        TextView foodTypeTextView = convertView.findViewById(R.id.food_foodType);
//        TextView createAtTextView = convertView.findViewById(R.id.food_createAt);

        nameTextView.setText(food.getName());
        descriptionTextView.setText(food.getDescription());
        String priceText = formatCurrencyVND(food.getPrice()) + " VND";
        priceTextView.setText(priceText);
//        categoryTextView.setText("Category: " + food.getCategory());
//        petTypeTextView.setText("Pet Type: " + food.getPetType());
//        foodTypeTextView.setText("Food Type: " + food.getFoodType());

        String formattedDate = formatDate(food.getCreate_at());
//        createAtTextView.setText("Created at: " + formattedDate);

        // Load image with Glide
        Glide.with(context).load(food.getImage()).into(imageView);

        Button addToCartButton = convertView.findViewById(R.id.btn_item_add_to_cart);
        addToCartButton.setOnClickListener(v -> showQuantityDialog(food));
        return convertView;
    }

    private void showQuantityDialog(Food food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Quantity");

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String quantityStr = input.getText().toString();
            if (!quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                context.addCartItem(food, quantity);
            } else {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public String formatCurrencyVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    private String formatDate(String rawDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate; // Nếu có lỗi thì giữ nguyên
        }
    }
}
