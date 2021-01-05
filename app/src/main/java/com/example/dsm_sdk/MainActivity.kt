package com.example.dsm_sdk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dsm_sdk_v1.DsmSdk

class MainActivity : AppCompatActivity() {
    val instance=DsmSdk.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val callback:(token?,Throwable?)->Unit={token,error->                                      //토큰을 받아왔을때의 콜백함수
            if(error!=null){
                Toast.makeText(this,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
            }
            else if(token!=null){
                Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
            }
        }


        val loginCallBack:(DTOuser?)->Unit={                                            //사용자 정보를 받아왔을때의 콜백함수
            val name=it?.name                //이름
            val email=it?.email              //이메일
            val gcn=it?.gcn                  //학번
            if(it!=null){
                Toast.makeText(this,"$name,$email,$gcn",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"null",Toast.LENGTH_SHORT).show()
            }
        }
        instance.loginWithAuth("hello","hello",applicationContext,callback,loginCallBack)
    }
}