# myutils
my develope utils



## 获取Utils（通过Gradle方式）
首先，在项目的 `build.gradle（project）` 文件里面添加:

```gradle
allprojects {
	repositories {  

        maven { url "https://jitpack.io" }
		
    }
}
```

最后，在你需要用到utils的module中的 `build.gradle（module）` 文件里面添加：
```gradle
dependencies {  

        implementation 'com.github.gaoleilvzss:myutils:1.1.0' 
 
   
}
```  
