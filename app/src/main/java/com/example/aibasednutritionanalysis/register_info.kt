package com.example.aibasednutritionanalysis

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class register_info:AppCompatActivity() {
    //다른 액티비티에서도 변수를 쓰기 위해 java의 static 변수와 비슷한
    //kotlin의 companion object 사용
    companion object {
        var name: String = ""
        var sex: String = ""
        var phone: String = ""
        var birthday: String = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val name_text: EditText = findViewById(R.id.name)
        val sex_group: RadioGroup = findViewById(R.id.radioGroup)
        val phone_text: EditText = findViewById(R.id.phone)
        val birthday_text: DatePicker = findViewById(R.id.dpSpinner)
        val button: Button = findViewById(R.id.button2)

        //성별 라디오 버튼 그룹: sex_group
        sex_group.setOnCheckedChangeListener(object :
                RadioGroup.OnCheckedChangeListener { //라디오 버튼 체크가 바뀌는 이벤트가 일어나면
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.man -> { //man 선택시
                        sex = "남" //성별 변수를 "남"으로 설정
                    }
                    R.id.woman -> { //woman 선택시
                        sex = "여" //성별 변수를 "여"로 설정
                    }
                }
            }
        })

        //버튼(완료) 누르는 이벤트가 발생할 때
        button.setOnClickListener {
            //입력한 이름, 휴대폰 번호, 생일 값 변수에 저장
            name = name_text.text.toString()
            phone = phone_text.text.toString()
            birthday = birthday_text.year.toString() + (birthday_text.month + 1).toString() + birthday_text.dayOfMonth.toString()

            //prefs에 회원정보 저장
            myapp.prefs.setString("NameKey",name)
            myapp.prefs.setString("PhoneKey", phone)
            myapp.prefs.setString("SexKey", sex)
            myapp.prefs.setString("BirthdayKey", birthday)

            if (name.length > 1 && phone.length >= 10 && sex.length == 1) {
                button.setBackgroundColor(Color.parseColor("#66CCFF"))
                //다음 액티비티로 넘어감
                val nextIntent = Intent(this, takeorselect_photo::class.java)
                startActivity(nextIntent)
            } else {
                Toast.makeText(this, "정보를 모두 입력해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }
}