# DSMSDK_Android
이 문서는 안드로이드에서 DSM_Auth를 사용하는 방법을 안내합니다. > 
**애플리케이션 설정 확인**
>
> Android SDK를 사용하려면 DSM_Auth에 프로젝트 정보가 등록되어 있어야 합니다. 자세한 내용은 [프로젝트 등록]()을 참고해주세요.


---


## 요구사항

DSMSDK를 사용하기 위한 최소 요구사양은 아래와 같습니다.

* Android api level 23(Android 6.0)이상

---
## 설치하기
1. Module 수준의 build.gradle에 다음 dependencies 를 추가해줍니다.
```gradle
implementation 'com.example.dsm_sdk_v1:dsmauth:1.1.3'
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
import com.example.dsm_sdk_v1.DTOuser
import com.example.dsm_sdk_v1.DsmSdk
import com.example.dsm_sdk_v1.token
```
2. 발급받은 client id,client secret, 그리고 프로젝트의 redirectURL(없을경우 발급받은 default redirectURL)을 통해서 DsmSdk를 초기화 해야합니다.
```kotlin
val instance=DsmSdk.instance

instance.initSDK("$client_id","$client_secret","$redirect_url")
```
---
## 최초 토큰,사용자 정보 받기
