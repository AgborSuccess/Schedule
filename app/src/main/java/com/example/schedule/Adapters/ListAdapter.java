package com.example.schedule.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Core.AddNewTask;
import com.example.schedule.Database.DatabaseHandler;
import com.example.schedule.MainActivity;
import com.example.schedule.Models.ListItem;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

import nl.dionsegijn.konfetti.xml.KonfettiView;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

//    private Context context;
    private List<ListItem> todoList;
    private DatabaseHandler db;
    private MainActivity activity;

    public ListAdapter(DatabaseHandler db ,MainActivity activity) {
        this.db = db;
        this.activity = activity;
        this.todoList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        ListItem todo = todoList.get(position);

        holder.textViewTodo.setText(todo.getTodo());
        holder.ButtonChecked.setChecked(toBoolean(todo.getStatus()));

        if (todo.getStatus() == 1) {
            holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current status of the checkbox
                boolean currentStatus = holder.ButtonChecked.isChecked();

                // Update the status of the checkbox
                holder.ButtonChecked.setChecked(!currentStatus);

                // Update the text view with the strike-through effect based on the updated status
                if (!currentStatus) {
                    holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    // Play sound
                    MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.task_completed);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                } else {
                    holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                // Update the database with the updated status
                db.updateStatus(todo.getId(), currentStatus ? 0 : 1);
            }
        });

        holder.ButtonChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    db.updateStatus(todo.getId(), 1);
                    holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    // Play sound
                    MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.task_completed);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                } else {
                    db.updateStatus(todo.getId(), 0);
                    holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });


    }

    private boolean toBoolean(int status) {
        return status!= 0;
    }

    @Override
    public int getItemCount() {
        return todoList == null ? 0 : todoList.size();
//        return Math.max(todoList.size(), 0);
//        if (todoList.size() < 0){
//            return 0;
//        }else{
//            return todoList.size();
//        }
    }

    public void removeItem(int position) {
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void set_todo(List<ListItem> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext()
    {
        return activity;
    }

    public void deleteItem(int position){
        ListItem item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ListItem item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTodo());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public void addTask(ListItem listItem) {
        todoList.add(listItem);
        notifyDataSetChanged();
        db.insertTask(listItem);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTodo;
        CheckBox ButtonChecked;
        CardView listContainer;
        KonfettiView konfettiView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            konfettiView = activity.findViewById(R.id.konfettiView);
            textViewTodo = itemView.findViewById(R.id.todo);
            ButtonChecked = itemView.findViewById(R.id.checked_btn);
            listContainer = itemView.findViewById(R.id.list_container);
        }
    }
}