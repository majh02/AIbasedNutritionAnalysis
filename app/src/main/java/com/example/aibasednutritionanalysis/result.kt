package com.example.aibasednutritionanalysis

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeByteArray
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class result: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val photobox:ImageView=findViewById(R.id.img_box)
        photobox.setImageBitmap(takeorselect_photo.bitmap)


        //임시로 넣은 코드-> 나중에 수정할것임
        Handler().postDelayed({
            val nextIntent = Intent(this, diet_record::class.java)
            startActivity(nextIntent)
        },3000) //3초 후 위의 코드를 실행함
    }
}