package com.example.aibasednutritionanalysis

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.aibasednutritionanalysis.MainActivity.Companion.DiethistoryDB
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class diet_record : AppCompatActivity() {
    val items = arrayOf("식사시간", "아침","점심","저녁","간식") //스피너(드롭다운) 배열 선언
    private var count = 0
    private var save = 0
    private val OPEN_GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE: Int = 100
    private var currentPhotoPath: String = ""
    
    companion object{
        var text:String=""
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_record)

        val spinner:Spinner=findViewById(R.id.spinner) //spinner: 스피너(드롭다운)
        val Date:TextView=findViewById(R.id.date) //Date: 상단 날짜 표시
        val textbox:EditText=findViewById(R.id.textbox) //textbox: 본문
        val album: Button = findViewById(R.id.album) //album: 앨범 버튼
        val camera: Button = findViewById(R.id.camera) //camera: 카메라 버튼
        val end = findViewById<Button>(R.id.end) //end: 작성 완료 버튼
        val photo1:ImageView=findViewById(R.id.photo1) //photo1: 사진 등록 이미지뷰 1번
        val photo2:ImageView=findViewById(R.id.photo2) //photo2: 사진 등록 이미지뷰 2번
        val photo3:ImageView=findViewById(R.id.photo3) //photo3: 사진 등록 이미지뷰 3번

        // 사진이 하나도 등록되지 않았을 때를 위한 비트맵 변형
        val drawable = getDrawable(R.drawable.camera)
        val bitmapDrawable = drawable as BitmapDrawable
        val Camerabitmap = bitmapDrawable.bitmap

        //오늘의 날짜를 위쪽에 표시하기
        val cal = Calendar.getInstance()
        var year=cal.get(Calendar.YEAR).toString()
        var month=(cal.get(Calendar.MONTH)+1).toString()
        var day=cal.get(Calendar.DAY_OF_MONTH).toString()
        Date.setText(year+"년 "+month+"월 "+day+"일")

        //스피너(드롭다운) 어댑터와 연결
        var meal_time:String=""
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = myAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                    }
                    1 -> {
                    }
                    2 -> {
                    }
                    3 -> {
                    }
                    else -> {
                    }
                }
                meal_time=items[position] //meal_time: 식사시간 값 저장
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //앨범 버튼 선택시
        album.setOnClickListener {
            save=0
            openGallery() //갤러리 열기
            isempty(textbox, end, spinner) //작성완료 버튼 활성화할지 비활성화할지 정함
        }
        //카메라 버튼 선택시
        camera.setOnClickListener {
            save=0
            startCapture() //카메라 열기
            isempty(textbox, end, spinner) //작성완료 버튼 활성화할지 비활성화할지 정함
        }

        //작성완료 버튼 선택시
        end.setOnClickListener {
            //식사시간 선택이 되지 않은 경우
            if(spinner.selectedItem.toString()==items[0]) {
                Toast.makeText(this, "식사시간을 선택해주세요", Toast.LENGTH_LONG).show()
            }
            else {
                //본문이 비어있는 경우
                if(textbox.text.isNullOrEmpty()){
                    Toast.makeText(this, "식단기록을 작성해주세요", Toast.LENGTH_LONG).show()
                } else if(photo1.drawable.toBitmap()==Camerabitmap){ //사진이 하나도 등록되지 않은 경우
                    Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_LONG).show()
                }
                //식사시간 선택과 본문 모두 만족한 경우
                else {
                    end.isClickable = true
                    text = textbox.text.toString()
                    Toast.makeText(this, "저장됨", Toast.LENGTH_LONG).show()

                    // DiethistorDB에 Entity 삽입
                    var id=DiethistoryDB.diethistoryDao().getCount() //Primary Key
                    DiethistoryDB.diethistoryDao().insert(diethistory(id,Date.text.toString(),meal_time,text,photo1.drawable.toBitmap())) //INSERT

                    //다음 액티비티(식단 캘린더)로 이동
                    val nextIntent = Intent(this, diet_history::class.java)
                    startActivity(nextIntent)
                }
            }
        }
    }

    //식사시간 선택이 안된 경우, 본문에 글이 작성되지 않은 경우 작성완료 버튼 보이지 않음
    private fun isempty(textbox: EditText, end:Button, spinner:Spinner){
        if(spinner.selectedItem.toString()==items[0] && textbox.text.isNullOrEmpty()) {
            end.isClickable=false
        }
        else{
            end.isClickable=true
            end.setBackgroundColor(Color.parseColor("#66CCFF"))
        }
    }

    //갤러리에서 사진 선택 or 카메라 어플 실행 후 사진 찍기
    //selectortake_photo.kt 와 같음
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.aibasednutritionanalysis.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val img_picture:ImageView
        val img_picture1 = findViewById<ImageView>(R.id.photo1)
        val img_picture2 = findViewById<ImageView>(R.id.photo2)
        val img_picture3 = findViewById<ImageView>(R.id.photo3)

        if(count==0) img_picture=img_picture1
        else if(count==1) img_picture=img_picture2
        else if(count==2) img_picture=img_picture3
        else {
            Toast.makeText(this,"사진은 최대 3장까지 등록 가능합니다.",Toast.LENGTH_LONG).show()
            return
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                img_picture.setImageBitmap(bitmap)
                save=1
            } else {
                val decode = ImageDecoder.createSource(this.contentResolver,
                        Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)

                img_picture.setImageBitmap(bitmap)
                save=1
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    img_picture.setImageBitmap(bitmap)
                    save=1
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        else {
            Log.d("ActivityResult", "something wrong")
        }
        if(save==1) count++
    }
}