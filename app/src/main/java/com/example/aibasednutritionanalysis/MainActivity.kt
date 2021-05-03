package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.aibasednutritionanalysis.myapp.Companion.prefs
import com.example.aibasednutritionanalysis.register_info.Companion.birthday
import com.example.aibasednutritionanalysis.register_info.Companion.name
import com.example.aibasednutritionanalysis.register_info.Companion.phone
import com.example.aibasednutritionanalysis.register_info.Companion.sex

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logo = findViewById<ImageView>(R.id.mainlogo)

        //prefs(SharedPreferences) -> 회원정보 저장함
        prefs= MySharedPreferences(applicationContext)

        name= prefs.getString("NameKey","")
        phone=prefs.getString("PhoneKey","")
        sex=prefs.getString("SexKey","")
        birthday=prefs.getString("BirthdayKey","")

        println("MAIN : "+name+","+sex+","+phone+","+birthday)

        Handler().postDelayed({
            logo.visibility= View.INVISIBLE //로고 사라짐
            //이전에 이미 회원정보를 입력했다면 takeorselect_photo 페이지로 감
            if (register_info.name.length > 1 && register_info.phone.length >= 10 && register_info.sex.length == 1){
                val nextIntent = Intent(this, takeorselect_photo::class.java)
                startActivity(nextIntent)
            }
            //입력된 회원정보가 없는 경우(처음 어플 설치한 경우), register_info 페이지로 감
            else{
                val nextIntent = Intent(this, register_info::class.java)
                startActivity(nextIntent)
            }
        },3000) //3초 후 위의 코드를 실행함
    }
}