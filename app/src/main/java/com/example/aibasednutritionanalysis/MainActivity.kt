package com.example.aibasednutritionanalysis

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.aibasednutritionanalysis.myapp.Companion.prefs
import com.example.aibasednutritionanalysis.register_info.Companion.birthday
import com.example.aibasednutritionanalysis.register_info.Companion.name
import com.example.aibasednutritionanalysis.register_info.Companion.phone
import com.example.aibasednutritionanalysis.register_info.Companion.sex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var NutritionDB:nutritionDB
        lateinit var DiethistoryDB:diethistoryDB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logo = findViewById<ImageView>(R.id.mainlogo)

        //Room Database
        NutritionDB = Room.databaseBuilder(this, nutritionDB::class.java, "nutritiondb").allowMainThreadQueries().build()
        NutritionDB.nutritionDao().deleteAll()
        DiethistoryDB = Room.databaseBuilder(this, diethistoryDB::class.java, "diethistorydb").allowMainThreadQueries().build()

        if(NutritionDB.nutritionDao().getCount()==0) {
            val assetManager: AssetManager = resources.assets
            val inputStream: InputStream = assetManager.open("NutritionDB.txt")

            inputStream.bufferedReader().readLines().forEach {
                var token = it.split("\t")
                var input = nutrition(token[0].toInt(), token[1], token[2].toInt(), token[3].toDouble(), token[4].toDouble(),
                        token[5].toDouble(), token[6].toDouble(), token[7].toDouble(), token[8].toDouble(), token[9].toDouble())
                CoroutineScope(Dispatchers.Main).launch {
                    NutritionDB?.nutritionDao()?.insert(input)
                }
            }
        }

        //prefs(SharedPreferences) -> 회원정보 저장함
        prefs= MySharedPreferences(applicationContext)

        name= prefs.getString("NameKey", "")
        phone=prefs.getString("PhoneKey", "")
        sex=prefs.getString("SexKey", "")
        birthday=prefs.getString("BirthdayKey", "")

        Handler().postDelayed({
            logo.visibility = View.INVISIBLE //로고 사라짐
            //이전에 이미 회원정보를 입력했다면 takeorselect_photo 페이지로 감
            if (name.length > 1 && phone.length >= 10 && sex.length == 1) {
                val nextIntent = Intent(this, menu::class.java)
                startActivity(nextIntent)
            }
            //입력된 회원정보가 없는 경우(처음 어플 설치한 경우), register_info 페이지로 감
            else {
                val nextIntent = Intent(this, register_info::class.java)
                startActivity(nextIntent)
            }
        }, 3000) //3초 후 위의 코드를 실행함
    }
}