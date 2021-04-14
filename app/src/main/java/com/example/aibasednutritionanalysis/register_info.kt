package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.widget.*
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
            //성별 변수는 위쪽에 이미 저장 하였으므로 여기서는 저장하지 않음
            name = name_text.text.toString()
            phone = phone_text.text.toString()
            birthday = birthday_text.year.toString() + (birthday_text.month + 1).toString() + birthday_text.dayOfMonth.toString()

            //다음 액티비티로 넘어감
            val nextIntent = Intent(this, takeorselect_photo::class.java)
            startActivity(nextIntent)
        }
    }
    //    @RequiresApi(Build.VERSION_CODES.N)
    //    private fun showDatePicker(birthday_text: TextView) {
    //        val cal = Calendar.getInstance()
    //        DatePickerDialog(this, DatePickerDialog.OnDateSetListener(){
    //            datepicker, y, m, d ->
    //            Toast.makeText(this, "$y-$m-$d", Toast.LENGTH_SHORT).show()
    //        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
    //
    //        birthday = cal.get(Calendar.YEAR).toString() + (cal.get(Calendar.MONTH)+1).toString() + cal.get(Calendar.DATE).toString()
    //        birthday_text.setText(cal.get(Calendar.YEAR).toString()+"년 "+(cal.get(Calendar.MONTH)+1).toString()+"월 "+cal.get(Calendar.DATE).toString()+"일")
}