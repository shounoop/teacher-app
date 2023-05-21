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
import com.example.stdmanager.DB.ScoreDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.helpers.Alert;
import com.example.stdmanager.models.ReportScore;
import com.example.stdmanager.models.ReportTotal;
import com.example.stdmanager.models.Statistic;

import java.util.ArrayList;
import java.util.List;

public class RankedStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private AnyChartView anyChartView;
    private Alert alert;
    private ScoreDBHelper db = new ScoreDBHelper(this);
    private ArrayList<ReportTotal> reportTotals = new ArrayList<>();
    private String ranked[] = new String[]{"Giỏi", "Khá", "Trung bình", "Yếu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("detail");
        this.alert = new Alert(RankedStatsActivity.this);
        this.alert.normal();

        this.setControl();
        this.setData();
        this.setEvent();

        this.setupPieChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);

        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        title.setText("Thống kê " + statistic.getTitle());
    }

    private void setEvent() {
    }

    private void setupPieChart() {
        List<DataEntry> dataEntries = new ArrayList<>();
        ArrayList<ReportScore> reportScores = db.getReportScore();

        for (int i = 0; i < ranked.length; i++) {
            int count = 0;
            for (int j = 0; j < reportScores.size(); j++) {
                String pointRank = getXepLoai(reportScores.get(j).getDiem());
                if (pointRank.equals(ranked[i])) {
                    count++;
                }
            }

            reportTotals.add(new ReportTotal(ranked[i], Double.valueOf(count)));
            dataEntries.add(new ValueDataEntry(ranked[i], count));
        }

//        use the AnyChart library to create and configure a pie chart

//        initializes a pie chart instance.
        Pie pie = AnyChart.pie();

        pie.data(dataEntries);
        pie.palette(new String[]{"#61CDBB", "#E8A838", "#DC143C", "#473F97"});
        pie.title(statistic.getTitle());
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        anyChartView.setChart(pie);
    }

    private String getXepLoai(Double diem) {
        if (diem >= 8) {
            return "Giỏi";
        } else if (diem >= 6.5 && diem < 8) {
            return "Khá";
        } else if (diem >= 5 && diem < 6.5) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}