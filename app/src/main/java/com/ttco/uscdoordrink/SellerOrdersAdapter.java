package com.ttco.uscdoordrink;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SellerOrdersAdapter extends ArrayAdapter<SellerOrder> {
    private final Context context;
    private final SellerOrder[] values;

    public SellerOrdersAdapter(Context context, SellerOrder[] values) {
        super(context, -1, values); // I think you pass -1 here because the resource id
        // is only used in the getView function of the ArrayAdapter, which is what I am overriding
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.seller_order_element, parent, false);

        TextView textView1 = (TextView) rowView.findViewById(R.id.order_name);
        textView1.setText("Drink name: " + values[position].drink);

        TextView textView2 =  (TextView) rowView.findViewById(R.id.order_time);
        textView2.setText("Order start time: " + values[position].startTime);

        TextView textView3 =  (TextView) rowView.findViewById(R.id.order_customer);
        textView3.setText("Customer name: " + values[position].customer_name);
        return rowView;
    }
}
