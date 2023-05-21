package com.example.stdmanager.Event;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.stdmanager.DB.EventDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.EventListViewModel;
import com.example.stdmanager.models.Event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity implements OnEvent {
    public static WeakReference<EventActivity> weakReference;
    private ListView listViewEvent;
    private ArrayList<Event> events;
    private EventListViewModel listViewModel;
    private SearchView EventSearch;
    private Button btn_addEvent;
    private int PositionUpdate = -1;
    private EventDBHelper db = new EventDBHelper(EventActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        weakReference = new WeakReference<>(EventActivity.this);

        db.deletedAndCreateTable();

        events = db.getAllEvents();

        setControl();
        setEvent();
        inItSearchWidgets();
    }

    private void setControl() {
        listViewEvent = findViewById(R.id.eventListView);
        EventSearch = findViewById(R.id.eventSearchView);
        btn_addEvent = (Button) findViewById(R.id.eventButtonCreation);
    }

    private void setEvent() {
        listViewModel = new EventListViewModel(this, R.layout.activity_event_element, events, this);
        listViewEvent.setAdapter(listViewModel);

        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, EventActivityCreation.class);
                startActivity(intent);
            }
        });
    }

    private void inItSearchWidgets() {
        EventSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Event> filteredEvent = new ArrayList<>();

                for (Event evt : events) {
                    if (evt.getNameEvent().toLowerCase().trim().contains(s.toLowerCase().trim())) {
                        filteredEvent.add(evt);
                    }
                }
                setFilteredEvent(filteredEvent);
                return false;
            }
        });
    }

    private void setFilteredEvent(ArrayList<Event> filtered) {
        EventListViewModel eventModel = new EventListViewModel(this, R.layout.activity_event_element, filtered, this);
        listViewEvent.setAdapter(eventModel);
    }

    public void AddEvent(Event event) {
        if (db.AddEvent(event)) {
            events.add(event);
            listViewModel.notifyDataSetChanged();
            Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Them that bai", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditEvent(Event event, int position) {
        PositionUpdate = position;
        Intent intent = new Intent(this, EventActivityUpdate.class);
        intent.putExtra("EV", event);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onEventDelete(Event event, int position) {
        db.DeleteEvent(event);
        listViewModel.DeleteItem(event, position);
    }

    @Override
    // handle the result returned by the child activity.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Event event = (Event) data.getSerializableExtra("Evupdate");
                listViewModel.UpdateItem(event, PositionUpdate);
            }
        }
    }
}