package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class menu: AppCompatActivity() {
    lateinit var layout:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        layout=findViewById(R.id.menu)
        val menu1:Button=findViewById(R.id.menu1)
        val menu2:Button=findViewById(R.id.menu2)
        val menu3:Button=findViewById(R.id.menu4)

        menu1.setOnClickListener{
            val nextIntent1 = Intent(this, takeorselect_photo::class.java)
            startActivity(nextIntent1)
        }
        menu2.setOnClickListener{
            val nextIntent2 = Intent(this, diet_record::class.java)
            startActivity(nextIntent2)
        }
        menu3.setOnClickListener{
            val nextIntent3 = Intent(this, register_info::class.java)
            startActivity(nextIntent3)
        }
    }

    var mBackWait:Long = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
        } else {
            finish() //액티비티 종료
        }
    }
}