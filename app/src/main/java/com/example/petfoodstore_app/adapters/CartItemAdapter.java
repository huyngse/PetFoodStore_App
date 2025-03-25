package com.example.petfoodstore_app.adapters;

import android.text.InputType;
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
import com.example.petfoodstore_app.activities.CartActivity;
import com.example.petfoodstore_app.models.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends ArrayAdapter<CartItem> {
    private CartActivity context;

    public CartItemAdapter(CartActivity context, List<CartItem> cartItems) {
        super(context, 0, cartItems);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartItem cartItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.cart_item_product_image);
        TextView productName = convertView.findViewById(R.id.cart_item_product_name);
        TextView productQuantity = convertView.findViewById(R.id.cart_item_product_quantity);
        TextView productPrice = convertView.findViewById(R.id.cart_item_product_price);
        Button updateQuantityButton = convertView.findViewById(R.id.btn_cart_item_update);
        Button removeItemButton = convertView.findViewById(R.id.btn_cart_item_remove);

        Glide.with(context).load(cartItem.getImage()).into(imageView);
        productName.setText(cartItem.getName());
        String strQuantity = "Quantity: " + cartItem.getQuantity();
        productQuantity.setText(strQuantity);
        String strPrice = "Total: " + formatCurrencyVND(cartItem.getTotalAmount());
        productPrice.setText(strPrice);

        updateQuantityButton.setOnClickListener(v -> showUpdateDialog(cartItem));
        removeItemButton.setOnClickListener(v -> context.removeCartItem(cartItem.getCartId(), cartItem.getQuantity()));

        return convertView;
    }

    private void showUpdateDialog(CartItem cartItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Quantity");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(cartItem.getQuantity()));
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String quantityStr = input.getText().toString();
            if (!quantityStr.isEmpty()) {
                int newQuantity = Integer.parseInt(quantityStr);
                context.updateCartItem(cartItem.getCartId(), newQuantity);
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
}
