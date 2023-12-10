package com.example.nabhackathon2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nabhackathon2023.base.customview.PieChartView
import com.example.nabhackathon2023.base.customview.Stub

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pieChart = findViewById<PieChartView>(R.id.pieChart)
        pieChart.setData(Stub.fakeData())
    }
}