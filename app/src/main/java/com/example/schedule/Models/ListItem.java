package com.example.schedule.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.schedule.Adapters.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private int id;
    private int status;



    private String todo;
    private boolean isChecked;
    public static ArrayList<ListItem> mTodoList;
    public static ListAdapter adapter;


    public ListItem() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public static void setAdapterAndList(ListAdapter adapter, List<ListItem> todoList) {
        ListItem.adapter = adapter;
        mTodoList = (ArrayList<ListItem>) todoList;
        adapter.notifyDataSetChanged();
    }

    public void setTodoList(ArrayList<ListItem> todoList) {
        mTodoList = todoList;
        notifyDataSetChanged();
    }

}
