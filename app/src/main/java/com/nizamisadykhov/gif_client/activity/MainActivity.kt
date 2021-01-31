package com.nizamisadykhov.gif_client.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nizamisadykhov.gif_client.R
import com.nizamisadykhov.gif_client.adapter.GiffPagerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем ViewPager и устанавливаем в него адаптер
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = GiffPagerAdapter(supportFragmentManager, this@MainActivity)

        // Передаём ViewPager в TabLayout
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
    }
}