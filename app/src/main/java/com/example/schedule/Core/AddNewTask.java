package com.example.schedule.Core;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Adapters.ListAdapter;
import com.example.schedule.Database.DatabaseHandler;
import com.example.schedule.Models.ListItem;
import com.example.schedule.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "Action Bottom Dialog";
    private EditText newTaskText;
    private Button newTaskBtn;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        View view = inflater.inflate(R.layout.activity_add_todo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.new_todo);
        newTaskBtn = getView().findViewById(R.id.add_todo);


        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if (task.length() > 0){
                newTaskBtn.setEnabled(true);
            }
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    newTaskBtn.setEnabled(false);
                }else{
                    newTaskBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTaskText.getText().toString();
                if (finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }else{
                    ListItem task = new ListItem();
                    task.setTodo(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // close the database
        if (db != null) {
            db.close();
        }
    }
}
