package com.example.aibasednutritionanalysis

import android.content.Context
import android.view.*
import android.widget.*

class diet_history_Adapter(val context: Context, val DB:List<diethistory>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item, null)

        //위에서 생성된 view를 res-layout-list_item.xml 파일의 각 View와 연결하는 과정
        val photo = view.findViewById<ImageView>(R.id.food_photo)
        val content = view.findViewById<TextView>(R.id.content)
        val cal = view.findViewById<TextView>(R.id.cal)
        val meal_time = view.findViewById<TextView>(R.id.meal_time)

        //DB에서 리스트뷰에 넣을 값 받기
        var output= DB[position]
        var photo_bitmap=output.photo
//        var photo2=output.photo2
//        var photo3=output.photo3

        photo.setImageBitmap(photo_bitmap) //리스트뷰에 사진 비트맵 채우기
        if(output.content.length>20){ //식단 기록 내용이 25자 초과 시, 축약된 형태로 보여줌
            var reduced_content:String=output.content.substring(0,20)+"..."
            content.text=reduced_content
        }
        else content.text=output.content //25자 이내면 그대로 보여줌
        cal.text=output.cal //작성된 날짜
        meal_time.text=output.meal_time //식사시간

        return view
    }

    override fun getItem(position: Int): Any {
        return DB[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return DB.size
    }
}