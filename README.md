# Samplers
Samplers is a framework to build mobile citizen science applications.

# UNDER DEVELOPMENT

## Steps to use the framework
1. Create a JSON file named **SamplersConfig.json**
  - Format and options available are [here](#samplersconfigjson-format-and-options)
  - To check for sintax problems you can use [this JSON validador](https://jsonformatter.curiousconcept.com/)
  - You can use [this example **SamplersConfig.json** file](https://github.com/cientopolis/samplers/blob/master/SamplersConfig.json)
  
2. Create a new empty Android Studio project (without any activity)
  - The Minimun SDK Version must be **API17: Android 4.2 (Jelly Bean)** 

3. Import the framework library into your Android Studio project
  - Download the latest **samplersFramework.arr** from [here](https://github.com/cientopolis/samplers)
  - Import the library into the project: **File -> New -> New Module -> Import .JAR/.AAR Package**

4. Add the necessary dependencies
  - On your application build.gradle
  ```gradle
  dependencies {
    // here the standards dependencies created by Android Studio
    // ...

    // if not added automatically, add this dependency 
    // you should use the latest version e.j. 25.+
    compile 'com.android.support:design:24.2.1' 
    
    // the framework dependency
    compile project(":SamplersFramework")
  }
  ```

5. Copy your created JSON file **SamplersConfig.json** to the root directory of your Android Studio project

6. Copy the **samplers.gradle** file to the root directory of your Android Studio project

7. Edit the **samplers.gradle** file and set the **app_path** and **package_name** variables with the application path and package name of your application

8. Run the **Task samplers_config** from the Gradle panel (it will create MyMainSamplersActivity and MyTakeSampleActivity in your project)

9. Add the activities MyMainSamplersActivity and MyTakeSampleActivity to the **AndroidManifest.xml** 
```xml
    <activity
        android:name="MyMainSamplersActivity"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    
    <activity
        android:name="MyTakeSampleActivity"
        android:label="@string/title_activity_take_sample"
        android:theme="@style/AppTheme">            
    </activity>    
```
10. Run your application

11. Enjoy!

## SamplersConfig.json format and options
- aplication
  - title
  - wellcomeMessage
  
- workflow
  - actionLabel
  - tasks
      - type
      - title
      - options
      
  
- Example:
  ```json
  {
    "aplication": {
    "title" : "Samplers Hello World App",
    "wellcomeMessage" : "Wellcome to your first Samplers App!"
    } ,
    "workflow": {
    "actionLabel" : "Take a sample",
      "tasks": [
      {
      "type": "MultipleSelect",
      "title" : "Select what you see",
      "options" : ["Trees","Trash","Water","Animals"]
      },

      {
      "type": "SelectOne",
      "title" : "Select one",
      "options" : ["Option 1","Option 2","Option 3","Option 4"]
      }
      ]
    }
  }
  ```
