package com.example.stdmanager.Statistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;

import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.StatisticListViewModel;
import com.example.stdmanager.models.Statistic;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Statistic> statistics = new ArrayList<>();
    private StatisticListViewModel listViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setControl();
        setEvent();
    }

    private void setControl() {
        this.listView = findViewById(R.id.lvListStatistic);
    }

    private void setEvent() {
        this.statistics.add(new Statistic(1, "Xếp loại", "Xem thống kê một lớp ở học kỳ nhất định có bao nhiêu học sinh giỏi, khá,... theo bảng điểm đã có "));
        this.statistics.add(new Statistic(2, "Phổ điểm tổng kết", "Xem phổ điểm của lớp học nào đó trong học kỳ nhất định"));
        this.statistics.add(new Statistic(3, "Giới tính", "Thống kê giới tính của lớp theo từng học kỳ "));

        this.listViewModel = new StatisticListViewModel(this, R.layout.activity_statistic_row, this.statistics);
        this.listView.setAdapter(listViewModel);
    }
}