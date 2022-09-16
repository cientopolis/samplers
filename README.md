# Samplers
Samplers is a framework to build mobile citizen science applications.


## Steps to use the framework

### Install

1. Create a new empty Android Studio project (without any activity)
    - The Minimun SDK Version must be **API17: Android 4.2 (Jelly Bean)** 

2. Import the framework library into your Android Studio project
    - Download the latest **samplersFramework.aar** from [here](https://github.com/cientopolis/samplers/releases/)
    - Import the library into the project: **File -> New -> New Module -> Import .JAR/.AAR Package**

3. Add the google repository
    - On your **project build.gradle**
      ```gradle
      allprojects {
          repositories {
              jcenter()
              google()
          }
      }
        
      ```
      
4. Add the necessary dependencies
    - On your **application build.gradle**
      ```gradle
      dependencies {
        // here the standards dependencies created by Android Studio
        // ...

        // if not added automatically, add this dependency 
        // you should use the latest version e.j. 25.+
        compile 'com.android.support:design:24.2.1' 
        compile 'com.android.support.constraint:constraint-layout:1.0.2'

        // if you will use maps and location services, add this dependencies (you should use the latest version)
        compile ('com.google.android.gms:play-services-location:12.0.1')
        compile ('com.google.android.gms:play-services-maps:12.0.1')
        
        // if you will use authentication with Google, add this dependencies (you should use the latest version)
        compile ('com.google.android.gms:play-services-auth:12.0.1')

        // the framework dependency
        compile project(":samplersFramework")
      }
      ```

### Instantiate
Instantiation can be done manually or using gradle class generator.

#### Manually

1. Create a Main Activity
    - You can create a class inherited from **SamplersMainActivity**. You will need to:
        1. Implement the **getWorkflow()** method and return the Workflow to pass to the TakeSampleActivity
        2. Override the **onCreate()** method and set the NetworkConfiguration:
            ```java
            NetworkConfiguration.setURL("http://192.168.1.10/samplers/upload.php");
            NetworkConfiguration.setPARAM_NAME_SAMPLE("sample");
            // Optional if you will use authentication
            NetworkConfiguration.setPARAM_NAME_USER_ID("user_id");
            NetworkConfiguration.setPARAM_NAME_AUTHENTICATION_TYPE("authentication_type");
            ```
    
    - You can create your own Main Activity and start the TakeSampleActivity like this:
        ```java
        Workflow workflow = new Workflow();
        // ... populate the workflow as desired
        
        Intent intent = new Intent(this, TakeSampleActivity.class);        
        intent.putExtra(TakeSampleActivity.EXTRA_WORKFLOW, workflow);
        startActivity(intent);
        ```
    You will need to set the NetworkConfiguration like the above example too.

#### Using gradle class generator
1. Create a JSON file named **SamplersConfig.json**
    - Format and options available are in the [wiki](https://github.com/cientopolis/samplers/wiki/SamplersConfig.json:-Format-and-options)
    - To check for sintax problems you can use [this JSON validador](https://jsonformatter.curiousconcept.com/)
    - You can use [this example **SamplersConfig.json** file](https://github.com/cientopolis/samplers/blob/master/SamplersConfig.json) (don't forget to change the **app_path** and **package_name** on **project** section with the application path and package name of your application, the **networkConfiguration** to successfully send samples, and your Google Maps API KEY if you want to use Location and Maps services)
  
2. Copy your created JSON file **SamplersConfig.json** to the root directory of your Android Studio project

3. Download the latest **samplers.gradle** and **samplersclassgenerator.jar** files from [**realeases**](https://github.com/cientopolis/samplers/releases/) and and copy them to the root directory of your Android Studio project

4. Link samplers.gradle with your **application build.gradle** (it will create MyMainSamplersActivity in your project)
    - On your **application build.gradle** add this line at the end
        ```gradle
        apply from: '../samplers.gradle'
        ```
    - Save and Sync when asked
    

5. Delete (or customize) the auto-generated **style.xml** file in the **res/values** directory

6. Run your application

7. Enjoy!

