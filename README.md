# Samplers
Samplers is a framework to build mobile citizen science applications.

# UNDER DEVELOPMENT

## Steps to use the framework

### Install

1. Create a new empty Android Studio project (without any activity)
    - The Minimun SDK Version must be **API17: Android 4.2 (Jelly Bean)** 

2. Import the framework library into your Android Studio project
    - Download the latest **samplersFramework.arr** from [here](https://github.com/cientopolis/samplers)
    - Import the library into the project: **File -> New -> New Module -> Import .JAR/.AAR Package**

3. Add the necessary dependencies
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

### Instantiate

#### Manually
1. Create a class instance of TakeSampleActivity

2. Create a Main Activity
    - You can create a class instance of SamplersMainActivity
    - You can create your own Main Activity

#### Using gradle class generator
1. Create a JSON file named **SamplersConfig.json**
    - Format and options available are [here](#samplersconfigjson-format-and-options)
    - To check for sintax problems you can use [this JSON validador](https://jsonformatter.curiousconcept.com/)
    - You can use [this example **SamplersConfig.json** file](https://github.com/cientopolis/samplers/blob/master/SamplersConfig.json) (don't forget to change the **app_path** and **package_name** on **project** section with the application path and package name of your application, and the **networkConfiguration** to successfully send samples)
  
2. Copy your created JSON file **SamplersConfig.json** to the root directory of your Android Studio project

3. Copy the **samplers.gradle** file to the root directory of your Android Studio project

4. Run the **Task samplers_config** from the Gradle panel (it will create MyMainSamplersActivity and MyTakeSampleActivity in your project)

5. Add the activities MyMainSamplersActivity and MyTakeSampleActivity to the **AndroidManifest.xml** 
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
6. Run your application

7. Enjoy!

## SamplersConfig.json format and options
- project
  - app_path
  - package_name
  
- aplication
  - title
  - wellcomeMessage
  - networkConfiguration
        - url
        - paramName
  
- workflow
  - actionLabel
  - tasks
      - type
      - title
      - options
      
  
- Example:
  ```json
  {
    "project": {
    "app_path" : "app/src/main/java/com/example/myApplication/",
    "package_name" : "com.example.myApplication"
    } ,
    
    "aplication": {
    "title" : "Samplers Hello World App",
    "wellcomeMessage" : "Wellcome to your first Samplers App!",
    "networkConfiguration" : {
        "url" : "http://192.168.1.10/samplers/upload.php",
        "paramName" : "sample"
        }
    } ,
    
    "workflow": {
    "actionLabel" : "Take a sample",
      "tasks": [
      {
        "type" : "Information",
        "text" : "Please, follow the instructions"
      },
      {
      "type": "MultipleSelect",
      "title" : "Select what you see",
      "options" : [
                    {
                     "id":1, 
                     "text":"Trees"
                    },
                    {
                     "id":2, 
                     "text":"Trash"
                    },
                    {
                     "id":3, 
                     "text":"Water"
                    },
                    {
                     "id":4, 
                     "text":"Animals"
                    }
                   ]
      },

      {
      "type": "SelectOne",
      "title" : "Select one",
      "options" : [
                    {
                     "id":1, 
                     "text":"Option 1"
                    },
                    {
                     "id":2, 
                     "text":"Option 2"
                    },
                    {
                     "id":3, 
                     "text":"Option 3"
                    },
                    {
                     "id":4, 
                     "text":"Option 4"
                    }
                   ]      
      },
      {
        "type" : "Photo",
        "text" : "Take a photo of your cat"
      }
      ]
    }
  }
  ```
