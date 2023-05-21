package com.example.stdmanager.Statistic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.stdmanager.DB.StudentOpenHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.models.ReportTotal;
import com.example.stdmanager.models.Statistic;

import java.util.ArrayList;
import java.util.List;

public class GenderStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private AnyChartView anyChartView;
    private StudentOpenHelper studentOpenHelper = new StudentOpenHelper(this);
    private ArrayList<ReportTotal> reportTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("detail");

        this.setControl();
        this.setData();
        this.setupPieChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);
        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        this.title.setText("Thống kê " + statistic.getTitle());
    }

    private void setupPieChart() {

        List<DataEntry> data = new ArrayList<>();
        this.reportTotals = this.studentOpenHelper.countByGender();

        for (int i = 0; i < this.reportTotals.size(); i++) {
            data.add(new ValueDataEntry(this.reportTotals.get(i).getName(), this.reportTotals.get(i).getValue()));
        }

        Pie pie = AnyChart.pie();

        pie.data(data);
        pie.palette(new String[]{"#ffcc80", "#aed581"});
        pie.title(this.statistic.getTitle());
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        this.anyChartView.setChart(pie);
    }
}