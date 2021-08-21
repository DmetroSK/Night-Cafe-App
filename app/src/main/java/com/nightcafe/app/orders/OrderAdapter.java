package com.nightcafe.app.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nightcafe.app.R;
import de.hdodenhof.circleimageview.CircleImageView;


    public class OrderAdapter extends FirebaseRecyclerAdapter<CartItemModel, com.nightcafe.app.orders.OrderAdapter.viewHolder> {

        @NonNull
        @Override
        public com.nightcafe.app.orders.OrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent,false);
            return new com.nightcafe.app.orders.OrderAdapter.viewHolder(view);

        }

        class viewHolder extends RecyclerView.ViewHolder{
            CircleImageView image;
            TextView name,type,qty,price;



            public viewHolder(@NonNull View itemView) {
                super(itemView);

                image = (CircleImageView)itemView.findViewById(R.id.image1);
                name = (TextView)itemView.findViewById(R.id.itemName1);
                type = (TextView)itemView.findViewById(R.id.type1);
                qty = (TextView)itemView.findViewById(R.id.qty1);
                price = (TextView)itemView.findViewById(R.id.price1);

            }
        }


        public OrderAdapter(@NonNull FirebaseRecyclerOptions<CartItemModel> options1) {
            super(options1);
        }


        @Override
        protected void onBindViewHolder(@NonNull com.nightcafe.app.orders.OrderAdapter.viewHolder holder, int position, CartItemModel model1) {


            try {
                holder.name.setText(model1.getName());
                holder.type.setText(model1.getType());
                holder.qty.setText("*  "+model1.getQty()+" =");
                holder.price.setText("Rs. "+ model1.getPrice());


                Glide.with(holder.image.getContext())
                        .load(model1.getImage())
                        .placeholder(R.drawable.ic_main_logo_black)
                        .circleCrop()
                        .error(R.drawable.ic_main_logo_black)
                        .into(holder.image);



            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

