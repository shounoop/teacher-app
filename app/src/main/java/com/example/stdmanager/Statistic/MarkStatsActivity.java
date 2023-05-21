package com.example.stdmanager.Statistic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.example.stdmanager.DB.ScoreDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.models.ReportTotal;
import com.example.stdmanager.models.Statistic;
import com.example.stdmanager.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class MarkStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private Subject subject;
    private AnyChartView anyChartView;

    private ScoreDBHelper db = new ScoreDBHelper(this);
    private ArrayList<ReportTotal> reportTotals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("Statistic");
        this.subject = (Subject) getIntent().getSerializableExtra("Subject");

        this.setControl();
        this.setData();
        this.setupChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);
        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        this.title.setText("Thống kê " + statistic.getTitle() + " môn " + subject.getTenMH());
    }

    private void setupChart() {
        this.reportTotals = this.db.getReportCountByScore(this.subject.getMaMH());

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < this.reportTotals.size(); i++) {
            data.add(new ValueDataEntry(this.reportTotals.get(i).getName(), this.reportTotals.get(i).getValue()));
        }

        Cartesian cartesian = AnyChart.column();
        Column column = cartesian.column(data);

        column.tooltip().titleFormat("Điểm: {%X}").position(Position.CENTER_BOTTOM).anchor(Anchor.CENTER_BOTTOM).offsetX(0d).offsetY(5d).format("Số lượng: {%Value}");

        cartesian.animation(true);

        cartesian.xAxis(0).title("Điểm");
        cartesian.yAxis(0).title("Số lượng");

        this.anyChartView.setChart(cartesian);
    }
}