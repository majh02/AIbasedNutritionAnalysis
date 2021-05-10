package com.example.aibasednutritionanalysis

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class takeorselect_photo : AppCompatActivity() {
    private val OPEN_GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE: Int = 100
    private var currentPhotoPath: String = ""
    companion object{
        var bitmap: Bitmap? =null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takeorselect_photo)
        val photobox:ImageView=findViewById(R.id.img_picture)
        val select_photo: Button = findViewById(R.id.select_photo)
        val take_photo: Button = findViewById(R.id.take_photo)
        val end = findViewById<Button>(R.id.button3)

        settingPermission()
        select_photo.setOnClickListener { openGallery() }
        take_photo.setOnClickListener {
            startCapture()
        }

        photobox.setOnClickListener{
            Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_LONG).show()
        }

        end.setOnClickListener {
            if(bitmap==null) {
                Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_LONG).show()
            }
            else {
                end.setBackgroundColor(Color.parseColor("#66CCFF"))
                val nextIntent = Intent(this, result::class.java)
                startActivity(nextIntent)
            }
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
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                img_picture.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(this.contentResolver,
                        Uri.fromFile(file))
                bitmap = ImageDecoder.decodeBitmap(decode)
                img_picture.setImageBitmap(bitmap)
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl: Uri? = data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
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