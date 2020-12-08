package group3.piggybank;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

/**
 * This class creates an activity with a pie chart based on an imported library.
 * The pie chart can display the amount of expenses of each category.
 * <p>
 * Reference:
 * https://github.com/PhilJay/MPAndroidChart
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/14/2020
 */
public class ChartActivity extends AppCompatActivity {

    /**
     * Creates the activity and sets the toolbar.
     * Sets the properties of the pie chart and populates its values.
     *
     * @param savedInstanceState the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PieChart chart = findViewById(R.id.chart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        DataManager manager = DataManager.getDataManager();
        manager.eachCategoryAmount();

        //set up the data for the entries of the chart
        ArrayList<Double> eachCategoryAmount = manager.getEachCategoryAmount();
        ArrayList<String> categoryNames = manager.getCategoryNames();
        ArrayList<PieEntry> entries = new ArrayList<>();
        double total = 0;
        for (int i = 0; i < categoryNames.size(); i++) {
            entries.add(new PieEntry((float) (eachCategoryAmount.get(i) / eachCategoryAmount.size()),
                    categoryNames.get(i)));
            total += eachCategoryAmount.get(i);
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        TextView txtTotalCost = findViewById(R.id.txtTotalCost);
        txtTotalCost.setText("$ " + total);

        TextView txtTotalIncome = findViewById(R.id.txtTotalIncome);
        ArrayList<Money> moneyList = manager.getMoneyList();
        double income = 0.0;
        for (int i = 0; i < moneyList.size(); i++) {
            if (moneyList.get(i).getMoneyType().equals("Income")) {
                income += moneyList.get(i).getAmount();
            }
        }
        txtTotalIncome.setText("$ " + income);
    }

}
