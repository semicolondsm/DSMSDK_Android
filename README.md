# DSMSDK_Android
이 문서는 안드로이드에서 DSM_Auth를 사용하는 방법을 안내합니다.    

**애플리케이션 설정 확인**
> Android SDK를 사용하려면 DSM_Auth에 프로젝트 정보가 등록되어 있어야 합니다. 자세한 내용은 [프로젝트 등록]()을 참고해주세요.


---


## 요구사항

DSMSDK를 사용하기 위한 최소 요구사양은 아래와 같습니다.

* Android api level 23(Android 6.0)이상

---
## 설치하기
1. Module 수준의 build.gradle에 다음 dependencies 를 추가해줍니다.
```gradle
implementation 'com.semicolon.dsm_sdk_v1:dsmauth:1.2.6'
```
2. __sync now__ 를 클릭합니다.

3. sdk를 사용할때 원할한 인터넷통신을 위해서 다음코드를 __AndroidManifest.xml__ 에 추가해줍니다.
```xml
android:usesCleartextTraffic="true"
```
---
## 초기화
1. 사용할 클래스에서 DsmSdk파일을 import 해야합니다.
```kotlin
import com.semicolon.dsm_sdk_v1.DTOuser
import com.semicolon.dsm_sdk_v1.DsmSdk
import com.semicolon.dsm_sdk_v1.token
```
2. 발급받은 client id,client secret, 그리고 프로젝트의 redirectURL(없을경우 발급받은 default redirectURL)을 통해서 DsmSdk를 초기화 해야합니다.
* DsmSdk.instance.initSDK()메서드를 사용합니다.
```kotlin
val instance=DsmSdk.instance

instance.initSDK("$client_id","$client_secret","$redirect_url")
```
---
## DSM_Auth 로그인

* DSM_Auth계정으로 로그인 하려면 instance내부에 있는 loginWithAuth()메서드를 사용해야합니다.
* loginWithAuth()메서드를 사용하는 방법은 다음과 같습니다.
  1. 첫번째 매개변수로 context를 줍니다.
  2. 두번째 매개변수로 토큰을 받았을때 실행할 콜백함수를 줍니다.
  3. 세번째 사용자 정보를 받았을때 실행할 콜백함수를 줍니다.
  4. 메서드를 실행합니다.
* name은 이름, email은 이메일,gcn은 학번(4자리)를 반환합니다
  
* 예시코드
```kotlin
val tokenCallback:(DTOtoken?,Throwable?)->Unit={token,error->    //토큰을 받아왔을때의 콜백메서드
    if(error!=null){
        Log.e("sdk",error)
    }
    else if(token!=null){
        val accessToken=token.access_token   //access토큰
        val refreshToken=token.refresh_token //refresh토큰
        Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
    }
}

val loginCallBack:(DTOuser?)->Unit={  //사용자 정보를 받아왔을때의 콜백메서드
    val name=it?.name                //이름
    val email=it?.email              //이메일
    val gcn=it?.gcn                  //학번
}
instance.loginWithAuth(this,callback,loginCallBack) //loginWithAuth메서드 호출
```
---
## AccessToken재발급
* DsmSdk는 access token을 재발급 받을수있는 __refreshToken()__ 메서드를 지원합니다.
* 파라미터로 로그인할때 받아온 refresh토큰과 access token을 재발급 받았을때 실행할 콜백 메서드를 넣어줍니다.
* 예시코드
```kotlin
val acessTokenCallBack:(accessToken:String)->Unit={
    Log.d("토큰",it)    //refreshToken이 잘못되거나 오류가 날 경우 null 이 반홥됩니다.
}
instance.refreshToken(refreshToken,acessTokenCallBack)
```
## 사용자 정보 받아오기
* DsmSdk는 accessToken으로 사용자의 정보를 받아오는 __getUserInformation()__ 메서드를 지원합니다.
* 파라미터로 access token과 정보를 받아왔을때 실행할 콜백메서드를 넣어줍니다.
* 예시코드
```kotlin
val checkToken:(DTOuser?)->Unit={
    if (it != null) { //accessToken이 잘못되면 null이 반환됩니다.
        Log.d("사용자",it.name)
        Log.d("사용자",it.email)
        Log.d("사용자",it.gcn)
    }
}
instance.getUserInformation(accessToken,checkToken)
```
---
## 더보기
* [DSMSDK 깃 허브](https://github.com/semicolonDSM/DSMSDK_Android)
* [example code](https://github.com/jaewonkim1468/DSM_SDK_TEST)