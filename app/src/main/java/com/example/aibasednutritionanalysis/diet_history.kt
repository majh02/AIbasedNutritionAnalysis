package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aibasednutritionanalysis.MainActivity.Companion.DiethistoryDB
import com.google.android.material.snackbar.Snackbar

class diet_history:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_history)

        val myview:View=findViewById(R.id.diet_history)
        val list:ListView=findViewById(R.id.list) //리스트뷰

        //식단기록 DB가 빈 상태가 아닐 때 -> 리스트뷰에 원래 저장되어있던 DB 채우기
        fill_list(list)

        list.onItemLongClickListener=object :AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                class MyUndoListener:View.OnClickListener{
                    override fun onClick(v:View){
                        DiethistoryDB.diethistoryDao().deletediethistory(position) //DB에서 해당 데이터 삭제
                        //리스트뷰 새로고침
                        fill_list(list)
                    }
                }
                //스낵바(알림창) 띄우기
                val mySnackbar = Snackbar.make(myview, "삭제하시겠습니까?", Snackbar.LENGTH_LONG)
                mySnackbar.setAction("삭제하기", MyUndoListener()) //삭제하기 버튼을 누르면 해당 식단기록 삭제
                mySnackbar.show()
                return true
            }
        }
    }

    //식단기록 DB가 빈 상태가 아닐 때 -> 리스트뷰에 원래 저장되어있던 DB 채우기
    fun fill_list(list:ListView){
        if(DiethistoryDB.diethistoryDao().getCount()!=0) {
            val output = DiethistoryDB.diethistoryDao().getAll()
            val diet_adpater = diet_history_Adapter(this, output)
            list.adapter = diet_adpater
        }
    }

    //백버튼 누르면 메뉴화면으로 이동
    override fun onBackPressed() {
        super.onBackPressed()
        val nextIntent = Intent(this, menu::class.java)
        startActivity(nextIntent)
    }
}