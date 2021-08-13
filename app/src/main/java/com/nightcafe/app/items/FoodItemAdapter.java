package com.nightcafe.app.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nightcafe.app.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FoodItemAdapter extends FirebaseRecyclerAdapter<ItemModel,FoodItemAdapter.viewHolder> {

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name;
        RelativeLayout section;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            section = (RelativeLayout)itemView.findViewById(R.id.framelayout);

        }
    }

    public FoodItemAdapter(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull FoodItemAdapter.viewHolder holder, int position, ItemModel model) {


        try {
            holder.name.setText(model.getName());


            Glide.with(holder.image.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .circleCrop()
                    .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                    .into(holder.image);


            holder.section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppCompatActivity activity = (AppCompatActivity)view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SingleFoodItemFragment(model.getName(),model.getImage(),model.getRegular(),model.getLarge())).addToBackStack(null).commit();

                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
