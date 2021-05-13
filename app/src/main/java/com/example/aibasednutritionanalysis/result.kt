package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.aibasednutritionanalysis.MainActivity.Companion.DiethistoryDB
import com.example.aibasednutritionanalysis.MainActivity.Companion.NutritionDB
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class result: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val photobox:ImageView=findViewById(R.id.img_box)
        photobox.setImageBitmap(takeorselect_photo.bitmap)
        val foodname:TextView=findViewById(R.id.result_foodname)
        val result1:TextView=findViewById(R.id.result1)
        val result2:TextView=findViewById(R.id.result2)

        //임시로 라벨값은 랜덤으로 받도록 설정함->나중에 수정할것
        val rand =Random
        var label_num= rand.nextInt(50)

        //output :라벨값에 해당하는 엔티티
        var output=NutritionDB.nutritionDao().getnutrition(label_num)
        var food_name=output[0].food_name
        var one_time=output[0].one_time.toString()
        var energy=output[0].energy
        var carb=output[0].carb
        var sugar=output[0].sugar
        var protein=output[0].protein
        var fat=output[0].fat
        var fiber=output[0].fiber
        var sodium=output[0].sodium

        //텍스트박스에 영상 분석 결과 넣기
        foodname.setText(food_name+"(으)로 분석되었습니다")
        result1.setText("  1회 제공량\n" +"  1회 제공량 당 열량\n" +"  탄수화물\n"+"  당류\n"
                +"  단백질\n"+"  지방\n" +"  식이섬유\n"+"  나트륨")
        result2.setText(one_time +"g  \n" +energy+"kcal  \n" +carb+"g  \n"+sugar+"g  \n"
                +protein+"g  \n"+fat+"g  \n"+fiber+"g  \n"+sodium+"mg  ")

        //식단기록 DB에 추가
        var id= DiethistoryDB.diethistoryDao().getCount()
        val current_time=Calendar.getInstance().time
        var dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(current_time)
        var timeFormat=SimpleDateFormat("HH시mm분", Locale.KOREA).format(current_time)
        var photo=photobox.drawable.toBitmap()

        DiethistoryDB.diethistoryDao().insert(diethistory(id,dateFormat,timeFormat,food_name,photo))
    }

    //백버튼 누르면 메뉴화면으로 이동
    override fun onBackPressed() {
        super.onBackPressed()
        val nextIntent = Intent(this, menu::class.java)
        startActivity(nextIntent)
    }
}