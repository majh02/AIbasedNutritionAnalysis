package com.example.aibasednutritionanalysis

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class takeorselect_photo : AppCompatActivity() {
    private val OPEN_GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE: Int = 100
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takeorselect_photo)
        val select_photo: Button = findViewById(R.id.select_photo)
        val take_photo: Button = findViewById(R.id.take_photo)
        val end = findViewById<Button>(R.id.button3)

        //이름, 성별, 휴대폰 번호, 생일 변수값 잘 전달 받았는지 확인
        var name:String=register_info.name
        var sex:String=register_info.sex
        var phone:String=register_info.phone
        var birthday:String=register_info.birthday
        println("activity_takeorselect_photo : "+name+","+sex+","+phone+","+birthday)

        settingPermission()
        select_photo.setOnClickListener { openGallery() }
        take_photo.setOnClickListener {
            startCapture()
        }

        end.setOnClickListener {
            val nextIntent = Intent(this, diet_record::class.java)
            startActivity(nextIntent)
        }
    }

    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(this@takeorselect_photo, "권한 허가", Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@takeorselect_photo, "권한 거부", Toast.LENGTH_SHORT)
                        .show()
                ActivityCompat.finishAffinity(this@takeorselect_photo) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
                .setPermissionListener(permis)
                .setRationaleMessage("카메라 사진 권한 필요")
                .setDeniedMessage("카메라 권한 요청 거부")
                .setPermissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .check()
    }

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
        val img_picture = findViewById<ImageView>(R.id.img_picture)

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

//    // 사진 파일을 만드는 메소드
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//                "JPEG_${timeStamp}_", /* prefix */
//                ".jpg", /* suffix */
//                storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//            Log.d("test", "currentPhotoPath : $currentPhotoPath")
//        }
//    }
//
//    private fun galleryAddPic() {
//        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
//            Log.d("test", "currentPhotoPath2 : $currentPhotoPath")
//            val f = File(currentPhotoPath)
//            mediaScanIntent.data = Uri.fromFile(f)
//            sendBroadcast(mediaScanIntent)
//        }
//    }


}