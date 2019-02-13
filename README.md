# Dragomock

> #### Dragoman
> \[drag-uh-muh n]  
> noun, plural dragomans, dragomen.  
> 1\. (in the Near East) a professional interpreter.  

Dragomock is a mock translation generator. It is used to create more natural mock translations that provide 
developers a better idea of how their code and UI will perform under more natural localization test cases. It can 
also be used for demonstrating the localization abilities in an app.

> **This is not a full translator. As such the output of this plugin should never be used in a published release 
build**

Dragomock is a tool for parsing in key-value string pairs running them through a translator API, and then outputting 
newly created translated files in a format that can be immediately used in an app. The plugin will only translate 
values that have not already been translated thus reducing repeat calls to the translator API for the same values.
>**Note:** If a developer makes a change to a string that has already been translated it is the developers job to 
re-translate or delete the string from the translated file(s)

While there is no cost to use this plugin, costs may apply depending on which translation API you plan to use 
(currently only the Microsoft Translator Text API).

## Usage
To setup Dragomock in your project add the following to the build.gradle file for the module that contains the String
 file to be translated:
~~~
dragomock {
    sourceLocal = "en"
    locals = ["ru", "de"]
    fileType = "TYPE_ANDROID_XML"
    microsoftSubscriptionKey = &lt;set Microsoft key here>
    inputFile = file('/res/values/strings.xml')
    outputDir = file('/res/')
    copyright = "Your copyright goes here"
    indentCount = 4
}
~~~

In the example above we are translating the strings.xml file from english ("en") to Russian ("ru") and German ("de").
 The file will be treated as an Android XML String resource file and all translation folders will be written to the
 "/res/" directory.

Below is a detailed list of the different parameters and there descriptions.

|Parameter Name|Required|Default|Details|
|--|--|--|--|
|sourceLocal|Optional|en|The two letter language code for the original language of the source file.|
|locals|Required|n/a|String array of the two letter language codes that you want translated.|
|fileType|Optional|TYPE_ANDROID_XML|Enum or string equivalent defining the values. See the [File Types](#file-types) section below for further details.|
|microsoftSubscriptionKey|Optional|n/a|The Microsoft Subscription key used to access the Microsoft Translation API. See the [Subscriptions](#subscriptions) section for further details.|
|googleSubscriptionKey|Optional|n/a|The Google Subscription key used to access the Google Cloud Translation API. See the [Subscriptions](#subscriptions) section for further details.|
|inputFile|Optional|&lt;root>/src/main/res/values/strings.xml|The source file that will be translated.|
|outputDir|Optional|&lt;inputFile>/../..|The directory file in which the translated file(s) will be written to.|
|copyright|Optional|n/a|Copyright text that will be added to all output files.|
|indentCount|Optional|2|The number of spaces used to indent during XML creation.|

## File Types

#### TYPE_ANDROID_XML

Used to read and output String.xml files using the standard Android project structure
Input should be a Android resource file containing only string resources
Translated files will be output as String resource file. Each requested language will be output in the following 
location:
> /**&lt;outputDir>**/Values-**&lt;local>**/Strings.xml

where outputDir is the provided output directory and local is the current two letter language code used for the 
translation

## Subscriptions

The subscriptions keys are used to determine which translation API to use. Dragomock  supports both the
Microsoft Translator Text API as well as the Google Cloud Translation API. For
security reasons it is best to set the keys via the "gradle.properties" file and reference them later.

To utilize the Microsoft Translator Text API you must provide a subscription key in the "microsoftSubscriptionKey" 
parameter. To obtain a key please see the documentation at the 
[Microsoft Translator Text API website](https://azure.microsoft.com/en-us/services/cognitive-services/translator-text-api/).

Alternatively to utilize the Google Cloud Translation API you must provide a subscription key in the "googleSubscriptionKey"
parameter. To obtain a key please see the documentation at the
[Google Cloud Translation API website](https://cloud.google.com/translate/).

Please note that at least one subscription key must be provided. Only one translator can be used at any given time.
If both subscription keys are provided the Google Translator service will take priority. If no subscription keys are provided an exception will be thrown.

## Release Notes

#### 0.8.00
 - Added optional "indentCount" field to control the indents on XML output
 - Updated XML attribute from "mock_translation" to "dragomock"
 - Fixed improper file creation on Mac computers

#### 0.7.00
 - Fixed memory leak due to not properly closing a file writer
 
#### 0.6.0
 - Added Translation Util project for testing and running Dragomock
 - Added the ability to rely on default values for many of the properties when declaring your DragoMock settings in build.gradle
 - Dragomock now supports the use of the Google Cloud Translation API
 - Added support for including copyright information into your generated files
 - DragoPluginExtension now allows for the FileType to be declared as a string value. FileType class import is no longer required when declaring your DragoMock settings in build.gradle
 - Code cleanup and updated documentation


## Planned Improvements

 - Addition of iOS Key-Value Pair String file translations
 - Addition of iOS P-Type String file translations
 - Addition of CSV String file translations
 - Ability to define multiple input files and matching outPut directories
 - JavaDoc improvements
 - Test improvements
 - Copy over xml headers
 
## Known Issues
 - Due to the way the Microsoft Translator handles them, you may see abnormalities when translating strings with 
 special characters or html encoded text.

## Contributing
This is my first open source project so contributions in the form of features, code style, and best practices are 
more than welcome. To do so submit a pull request and it will be reviewed when time allows.

## License
Copyright 2017 Daniel Martin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.