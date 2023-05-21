package com.example.stdmanager.listViewModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stdmanager.Classroom.ClassroomActivity;
import com.example.stdmanager.R;
import com.example.stdmanager.Statistic.GenderStatsActivity;
import com.example.stdmanager.Statistic.MarkStatsActivity;
import com.example.stdmanager.Statistic.RankedStatsActivity;
import com.example.stdmanager.Statistic.StatisticActivity;
import com.example.stdmanager.Statistic.SubjectListActivity;
import com.example.stdmanager.models.Statistic;
import com.example.stdmanager.models.Student;

import java.util.ArrayList;

public class StatisticListViewModel extends ArrayAdapter<Statistic> {
    private Context context;
    private int resource;
    private ArrayList<Statistic> statistics;

    public StatisticListViewModel(@NonNull Context context, int resource, @NonNull ArrayList<Statistic> statistics) {
        super(context, resource, statistics);
        this.context = context;
        this.resource = resource;
        this.statistics = statistics;
    }

    public int count() {
        return statistics.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView title = convertView.findViewById(R.id.titleStatistic);
        TextView text = convertView.findViewById(R.id.textStatistic);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);

        Statistic statistic = statistics.get(position);

        title.setText(statistic.getTitle());
        text.setText(statistic.getText());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (statistic.getId()) {
                    case 1:
                        Intent intent1 = new Intent(context, RankedStatsActivity.class);
                        intent1.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, SubjectListActivity.class);
                        intent2.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, GenderStatsActivity.class);
                        intent3.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }
}
