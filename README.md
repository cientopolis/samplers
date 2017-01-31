# Samplers
Samplers is a framework to build mobile citizen science applications.

# UNDER DEVELOPMENT

## Steps to use the framework
1. Create a JSON file named **SamplersConfig.json**
  - Format and options available are [here](https://github.com/cientopolis/samplers/blob/master/README.md#samplersconfigjson-format-and-options)
  - To check for sintax problems you can use [this JSON validador](https://jsonformatter.curiousconcept.com/)
  - You can use [this example **SamplersConfig.json** file](https://github.com/cientopolis/samplers)
  
2. Create a new empty Android Studio project (without any activity)
  - The Minimun SDK Version must be **API17: Android 4.2 (Jelly Bean)** 

3. Import the framework library
  - Download the latest **samplersFramework.arr** from [here](https://github.com/cientopolis/samplers)
  - Import the library into the project: **File -> New -> New Module -> Import .JAR/.AAR Package**

4. Add the necessary dependencies
  - On yuour application build.gradle
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

5. paso 5

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
  ```javascript
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
