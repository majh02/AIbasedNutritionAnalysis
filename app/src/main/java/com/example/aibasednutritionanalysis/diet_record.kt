package com.example.aibasednutritionanalysis

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.view.get
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer as timer1

class diet_record : AppCompatActivity() {
    val items = arrayOf("식사시간", "아침","점심","저녁","간식") //스피너(드롭다운) 배열 선언
    private val OPEN_GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE: Int = 100
    private var currentPhotoPath: String = ""
    
    companion object{
        var text:String=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_record)

        val spinner:Spinner=findViewById(R.id.spinner) //spinner: 스피너(드롭다운)
        val Date:TextView=findViewById(R.id.date) //Date: 상단 날짜 표시
        val textbox:EditText=findViewById(R.id.textbox) //textbox: 본문
        val album: Button = findViewById(R.id.album) //album: 앨범 버튼
        val camera: Button = findViewById(R.id.camera) //camera: 카메라 버튼
        val end = findViewById<Button>(R.id.end) //end: 작성 완료 버튼

        //오늘의 날짜를 위쪽에 표시하기
        val cal = Calendar.getInstance()
        var year=cal.get(Calendar.YEAR).toString()
        var month=(cal.get(Calendar.MONTH)+1).toString()
        var day=cal.get(Calendar.DAY_OF_MONTH).toString()
        Date.setText(year+"년 "+month+"월 "+day+"일")

        //스피너(드롭다운) 어댑터와 연결
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
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //앨범 버튼 선택시
        album.setOnClickListener {
            openGallery() //갤러리 열기
            isempty(textbox, end, spinner) //작성완료 버튼 활성화할지 비활성화할지 정함
        }
        //카메라 버튼 선택시
        camera.setOnClickListener {
            startCapture() //카메라 열기
            isempty(textbox, end, spinner) //작성완료 버튼 활성화할지 비활성화할지 정함
        }

        //작성완료 버튼 선택시
        end.setOnClickListener {
//            val nextIntent = Intent(this, diet_record::class.java)
//            startActivity(nextIntent)

            //식사시간 선택이 되지 않은 경우
            if(spinner.selectedItem.toString()==items[0]) {
                Toast.makeText(this, "식사시간을 선택해주세요", Toast.LENGTH_LONG).show()
            }
            else {
                //본문이 비어있는 경우
                if(textbox.text.isNullOrEmpty()){
                    Toast.makeText(this, "식단기록을 작성해주세요", Toast.LENGTH_LONG).show()
                }
                //식사시간 선택과 본문 모두 만족한 경우
                else {
                    end.isClickable = true
                    text = textbox.toString()
                    Toast.makeText(this, "저장됨", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //식사시간 선택이 안된 경우, 본문에 글이 작성되지 않은 경우 작성완료 버튼 보이지 않음
    private fun isempty(textbox: EditText, end:Button, spinner:Spinner){
        if(spinner.selectedItem.toString()==items[0] && textbox.text.isNullOrEmpty()) {
            end.visibility=View.INVISIBLE
            end.isClickable=false
        }
        else{
            end.visibility=View.VISIBLE
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
        val img_picture = findViewById<ImageView>(R.id.imageView)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                img_picture.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(this.contentResolver,
                        Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
                img_picture.setImageBitmap(bitmap)
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    img_picture.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }
}