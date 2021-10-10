package com.informatix.intruder.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.informatix.intruder.R;

public class SettingsDataViewHolder extends RecyclerView.ViewHolder {
    EditText mobile1EditText, mobile2EditText,mobile3EditText;
    CheckBox checkboxBuzzer,checkboxRelay;
    Button editButton,done;
    public SettingsDataViewHolder(@NonNull View itemView) {
        super(itemView);
        mobile1EditText = itemView.findViewById(R.id.mobile1EditText);
        mobile2EditText = itemView.findViewById(R.id.mobile2EditText);
        mobile3EditText = itemView.findViewById(R.id.mobile3EditText);
        checkboxBuzzer = itemView.findViewById(R.id.checkboxBuzzer);
        checkboxRelay = itemView.findViewById(R.id.checkboxRelay);
        editButton = itemView.findViewById(R.id.editButton);
        done = itemView.findViewById(R.id.done);

    }
}
