package com.example.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import com.example.schedule.Adapters.ListAdapter;
import com.example.schedule.Core.AddNewTask;
import com.example.schedule.Core.DialogCloseListener;
import com.example.schedule.Database.DatabaseHandler;
import com.example.schedule.Models.ListItem;
import com.example.schedule.Users.LoginActivity;
import com.example.schedule.Users.SignUpActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private ArrayList<ListItem> mTodoList;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton add_btn;
    SearchView searchView;
    private DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Init Views

        add_btn = findViewById(R.id.add_btn);
        searchView = findViewById(R.id.searchView);
        dbHandler = new DatabaseHandler(this);
        dbHandler.openDatabase();


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewTask().newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


//        createExampleList();
        buildRecyclerView();




        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.LEFT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mAdapter.getContext());
                    builder.setTitle("Delete Task");
                    builder.setMessage("Are you Sure you want to Delete This");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAdapter.deleteItem(position);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAdapter.notifyItemChanged(position);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    mAdapter.editItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurretlyActive){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurretlyActive);

                Drawable icon;
                ColorDrawable background;

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;
                if(dX>0){
                    icon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_baseline_edit);
                    background = new ColorDrawable(ContextCompat.getColor(mAdapter.getContext(), R.color.myAccent));
                }else{
                    icon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_baseline_delete_24);
                    background = new ColorDrawable(Color.RED);
                }

                assert icon != null;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if(dX>0){
//          Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());
                }else if(dX < 0){
                    //          Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth() ;
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                }else{
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(mRecyclerView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTodoList.clear();
                mTodoList.addAll(dbHandler.searchTasks(newText));
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

//    private void createExampleList() {
//        mTodoList = new ArrayList<>();
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//        mTodoList.add(new ListItem("Task 1", false));
//        mTodoList.add(new ListItem("Task 2", false));
//        mTodoList.add(new ListItem("Task 3 this is a very very very long task that should be used as a marquies tetx sdsdsdsds", false));
//    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListAdapter(dbHandler,this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mTodoList = (ArrayList<ListItem>) dbHandler.getAllTask();
        Collections.reverse(mTodoList);
        mAdapter.set_todo(mTodoList);

    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        mTodoList = (ArrayList<ListItem>) dbHandler.getAllTask();
        Collections.reverse(mTodoList);
        mAdapter.set_todo(mTodoList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close the database
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}