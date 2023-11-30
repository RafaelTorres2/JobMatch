package com.example.aplicaciontfg2.recyclerViewUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciontfg2.R;
import com.example.aplicaciontfg2.entidades.User;
import com.example.aplicaciontfg2.listeners.UserListener;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User> users;
    private UserListener userListener;
    private ConstraintLayout rootLayout;

    public UsersAdapter(){


    }

    public UsersAdapter(List<User> users, UserListener userListener){

        this.users = users;
        this.userListener = userListener;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemuser_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameItemUser, textViewEmailItemUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameItemUser = itemView.findViewById(R.id.textViewNameItemUser);
            textViewEmailItemUser = itemView.findViewById(R.id.textViewEmailItemUser);
            rootLayout = itemView.findViewById(R.id.rootLayoutItemUser);

        }

        public void setUserData(User user){

            textViewNameItemUser.setText(user.getName());
            textViewEmailItemUser.setText(user.getEmail());

            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userListener.onUserClicked(user);

                }
            });

        }

        public TextView getTextViewNameItemUser() {
            return textViewNameItemUser;
        }

        public void setTextViewNameItemUser(TextView textViewNameItemUser) {
            this.textViewNameItemUser = textViewNameItemUser;
        }

        public TextView getTextViewEmailItemUser() {
            return textViewEmailItemUser;
        }

        public void setTextViewEmailItemUser(TextView textViewEmailItemUser) {
            this.textViewEmailItemUser = textViewEmailItemUser;
        }

    }

}
