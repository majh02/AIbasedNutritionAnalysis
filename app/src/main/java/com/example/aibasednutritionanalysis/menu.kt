package com.example.aibasednutritionanalysis

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class menu: AppCompatActivity() {
    lateinit var layout:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        layout=findViewById(R.id.menu)
        val menu1:Button=findViewById(R.id.menu1)
        val menu2:Button=findViewById(R.id.menu2)
        val menu3:Button=findViewById(R.id.menu3)
        val menu4:Button=findViewById(R.id.menu4)

        settingPermission() //카메라 접근권한 허용

        menu1.setOnClickListener{
            val nextIntent1 = Intent(this, takeorselect_photo::class.java)
            startActivity(nextIntent1)
        }
        menu2.setOnClickListener{
            val nextIntent2 = Intent(this, diet_record::class.java)
            startActivity(nextIntent2)
        }
        menu3.setOnClickListener{
            val nextIntent3 = Intent(this, diet_history::class.java)
            startActivity(nextIntent3)
        }
        menu4.setOnClickListener{
            val nextIntent4 = Intent(this, register_info::class.java)
            startActivity(nextIntent4)
        }
    }

    // 카메라 권한 허용
    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                ActivityCompat.finishAffinity(this@menu) // 권한 거부시 앱 종료
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

    var mBackWait:Long = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
        } else {
            finish() //액티비티 종료
        }
    }
}