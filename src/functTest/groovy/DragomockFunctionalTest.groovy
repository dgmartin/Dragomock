/*
 * Copyright 2017  Daniel Martin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class DragomockFunctionalTest extends Specification {
    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    String microsoftSubscriptionKey

    def setup() {
        microsoftSubscriptionKey = System.getProperty('microsoftSubscriptionKey')
//        File stringsDir = testProjectDir.newFolder('res', 'values')
//        stringsDir.mkdirs()
//        androidEnglishStrings = new File(stringsDir.getAbsolutePath(), 'strings.xml')
//        androidEnglishStrings << new File('src/main/resources/testfiles/xml_strings.txt').text

        createMockFile("/res/values", "strings.xml",
                "src/main/resources/testfiles/xml_strings.txt")

        createMockFile("/Views/Storyboards/en.lproj", "Main.strings",
                "src/main/resources/testfiles/Main.strings")

        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'com.github.dgmartin.Dragomock'
            }"""
    }

//    def "can run normal translate"() {
//        buildFile << """
//        dragomock {
//                locals = ["ru", "de"]
//                fileType = "TYPE_ANDROID_XML" as com.github.dgmartin.constants.FileType
//                microsoftSubscriptionKey = '${microsoftSubscriptionKey}'
//                inputFile = file('/res/values/strings.xml')
//                outputDir = file('/res/')
//            }"""
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('createMockStrings')
//                .forwardOutput()
//                .withPluginClasspath()
//                .build()
//
//        then:
//        result.task(":createMockStrings").outcome == SUCCESS
//    }

    def "can run normal translate"() {

        String googleSubscriptionKey  = System.getProperty('googleSubscriptionKey')
        buildFile << """
        dragomock {  
                locals = ["ru"]
                fileType = "TYPE_ANDROID_XML" as com.github.dgmartin.constants.FileType
                googleSubscriptionKey = '${googleSubscriptionKey}'
                inputFile = file('/res/values/strings.xml')
                outputDir = file('/res/')
            }"""

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('createMockStrings')
                .forwardOutput()
                .withPluginClasspath()
                .build()

        then:
        result.task(":createMockStrings").outcome == SUCCESS
    }

//    def "can translate over previous translations"() {
//        File stringsDir = testProjectDir.newFolder('res', 'values-de')
//        stringsDir.mkdirs()
//        androidGermanStrings = new File(stringsDir.getAbsolutePath(), 'strings.xml')
//        androidGermanStrings << new File('src/main/resources/testfiles/xml_strings_de.txt').text

//        createMockFile("res/values-de", "strings.xml",
//                "src/main/resources/testfiles/xml_strings_de.txt")
//
//        buildFile << """
//        dragomock {
//                locals = ["de"]
//                fileType = "TYPE_ANDROID_XML" as com.github.dgmartin.constants.FileType
//                microsoftSubscriptionKey = '${microsoftSubscriptionKey}'
//                inputFile = file('/res/values/strings.xml')
//                outputDir = file('/res/')
//            }"""
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('createMockStrings')
//                .forwardOutput()
//                .withPluginClasspath()
//                .build()
//
//        then:
//        result.task(":createMockStrings").outcome == SUCCESS
//    }

//    def "Translate XCode KVP"() {
//        buildFile << """
//        dragomock {
//                locals = ["ru", "de"]
//                fileType = "TYPE_XCODE_KEY_VALUE_PAIR" as com.github.dgmartin.constants.FileType
//                microsoftSubscriptionKey = '${microsoftSubscriptionKey}'
//                inputFile = file('/Views/Storyboards/en.lproj/Main.strings')
//                outputDir = file('/Views/Storyboards/')
//            }"""
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('createMockStrings')
//                .forwardOutput()
//                .withPluginClasspath()
//                .build()
//
//        then:
//        result.task(":createMockStrings").outcome == SUCCESS
//    }


    void createMockFile(String dir, String name, String importData) {
        File stringsDir = new File(testProjectDir.getRoot().getAbsolutePath()+dir)
        stringsDir.mkdirs()
        File newFile = new File(stringsDir.getAbsolutePath(), name)
        if (importData) {
            newFile << new File(importData).text
        }
    }
}
