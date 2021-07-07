이미지의 메타 데이터에서 이미지의 회전 정도를 가져와 세로로 더 긴 이미지의 사이즈를 줄일 수 있도록 만든 자바 코드입니다.\
사용하기 위해 2가지 라이브러리가 필요합니다.

```xml
<!-- https://mvnrepository.com/artifact/com.drewnoakes/metadata-extractor -->
<dependencies>
    <dependency>
        <groupId>com.drewnoakes</groupId>
        <artifactId>metadata-extractor</artifactId>
        <version>2.16.0</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.adobe.xmp/xmpcore -->
    <dependency>
        <groupId>com.adobe.xmp</groupId>
        <artifactId>xmpcore</artifactId>
        <version>6.1.11</version>
    </dependency>
</dependencies>
```
사진에 대한 정보를 가져온 뒤 사진의 회전 정보에 맞게 setRGB() 함수에서 사진을 재구성합니다.  