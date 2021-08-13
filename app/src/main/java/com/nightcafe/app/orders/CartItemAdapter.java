package com.nightcafe.app.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafe.app.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class CartItemAdapter extends FirebaseRecyclerAdapter<CartItemModel,CartItemAdapter.viewHolder> {

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent,false);
        return new viewHolder(view);

    }

    class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name,type,qty,price;
        ImageView remove;
        LinearLayout hide;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.itemName);
            type = (TextView)itemView.findViewById(R.id.type);
            qty = (TextView)itemView.findViewById(R.id.qty);
            price = (TextView)itemView.findViewById(R.id.price);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            hide = (LinearLayout) itemView.findViewById(R.id.hideSection);

        }
    }


    public CartItemAdapter(@NonNull FirebaseRecyclerOptions<CartItemModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull CartItemAdapter.viewHolder holder, int position, CartItemModel model) {


        try {
            holder.name.setText(model.getName());
            holder.type.setText(model.getType());
            holder.qty.setText("*  "+model.getQty()+" =");
            holder.price.setText("Rs. "+ model.getPrice());

            String key = this.getRef(position).getKey();

            Glide.with(holder.image.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .circleCrop()
                    .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                    .into(holder.image);


            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //get local database phone value
                    SharedPreferences shared = holder.remove.getContext().getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
                    String UserPhone = (shared.getString("phone"," "));

                    //firebase database query
                    DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference("Users")
                            .child(UserPhone).child("Cart");

                    //remove values from firebase
                    DatabaseReference ItemRef = cartItemRef.child(key);
                    ItemRef.removeValue();
                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
