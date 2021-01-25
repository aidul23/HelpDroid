package com.example.helpdroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    public OnItemClickListener listener;
    private ArrayList<Pojo> list = new ArrayList<>();

    public Adapter(ArrayList<Pojo> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Pojo currentItem = list.get(position);
        holder.name.setText(currentItem.getName());
        holder.number.setText(currentItem.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView number;
        public ImageView callButton;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            number = itemView.findViewById(R.id.textViewNumber);
            callButton = itemView.findViewById(R.id.imageViewCall);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.OnItemClick(list.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Pojo pojo);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
