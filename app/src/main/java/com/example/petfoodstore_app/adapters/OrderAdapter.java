package com.example.petfoodstore_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petfoodstore_app.R;
import com.example.petfoodstore_app.models.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        // Lấy dữ liệu đơn hàng
        Order order = orderList.get(position);

        // Khởi tạo các view trong layout item_order
        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
        TextView tvCustomerEmail = convertView.findViewById(R.id.tvCustomerEmail);
        TextView tvCustomerPhone = convertView.findViewById(R.id.tvCustomerPhone);
        TextView tvCustomerAddress = convertView.findViewById(R.id.tvCustomerAddress);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        TextView tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);

        // Gán dữ liệu vào view
        tvProductName.setText("Product: " + (order.getProductName() != null ? order.getProductName() : "N/A"));
        tvCustomerName.setText("Customer: " + (order.getCustomerName() != null ? order.getCustomerName() : "N/A"));
        tvCustomerEmail.setText("Email: " + (order.getCustomerEmail() != null ? order.getCustomerEmail() : "N/A"));
        tvCustomerPhone.setText("Phone: " + (order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A"));
        tvCustomerAddress.setText("Address: " + (order.getCustomerAddress() != null ? order.getCustomerAddress() : "N/A"));
        tvQuantity.setText("Quantity: " + order.getQuantity());
        tvTotalAmount.setText("Total: $" + order.getTotalAmount());

        return convertView;
    }
}