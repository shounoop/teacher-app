package com.example.stdmanager.Event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stdmanager.DB.EventDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.models.Event;

import java.util.Date;


public class EventActivityUpdate extends AppCompatActivity {
    private Event event;
    private EditText NameEv, StartEv, EndEV, DateEv, PlaceEV;
    private Button ConfirmUpdate;
    private EventDBHelper eventDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

        event = (Event) getIntent().getSerializableExtra("EV");
        eventDBHelper = new EventDBHelper(this);

        this.setControl();
        this.setEvent();
    }

    private void setEvent() {
        this.ConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setNameEvent(NameEv.getText().toString().trim());
                event.setStartTime(StartEv.getText().toString());
                event.setEndTime(EndEV.getText().toString());
                event.setDay(DateEv.getText().toString());
                event.setPlace(PlaceEV.getText().toString());

                if (eventDBHelper.UpdateEvent(event)) {
                    Toast.makeText(EventActivityUpdate.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("Evupdate", event);

                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(EventActivityUpdate.this, "Cap nhat that bai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setControl() {
        this.NameEv = findViewById(R.id.eventNameupdate);
        this.StartEv = findViewById(R.id.StartEVupdate);
        this.EndEV = findViewById(R.id.endEVupdate);
        this.DateEv = findViewById(R.id.dateEVupdate);
        this.PlaceEV = findViewById(R.id.eventPlaceupdate);
        this.ConfirmUpdate = findViewById(R.id.eventUpdateButtonConfirm);

        this.NameEv.setText(event.getNameEvent());
        this.StartEv.setText(event.getStartTime());
        this.EndEV.setText(event.getEndTime());
        this.DateEv.setText(event.getDay());
        this.PlaceEV.setText(event.getPlace());
    }
}
