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

package com.github.dgmartin.extensions

import com.github.dgmartin.constants.FileType
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * This extension defies the properties that are used to input data via the build script.
 *
 * @since 1.0
 */
class DragoPluginExtension {
    final Property<String> sourceLocal
    final Property<List<String>> locals
    final Property<FileType> fileType
    final Property<String> microsoftSubscriptionKey
    final Property<String> googleSubscriptionKey
    final Property<File> inputFile
    final Property<File> outputDir
    final Property<String> copyright

    DragoPluginExtension(Project project) {
        sourceLocal = project.objects.property(String.class)
        sourceLocal.set("en")

        locals = (Property<List<String>>) (Object) project.objects.property(List.class)

        fileType = project.objects.property(FileType.class)
        fileType.set(FileType.TYPE_ANDROID_XML)

        microsoftSubscriptionKey = project.objects.property(String)
        microsoftSubscriptionKey.set("")

        googleSubscriptionKey = project.objects.property(String.class)
        googleSubscriptionKey.set("")

        inputFile = project.objects.property(File)
        inputFile.set(new File("src/main/res/values/strings.xml"))

        outputDir = project.objects.property(File)

        copyright = project.objects.property(String.class)
    }

    @Override
    String toString() {
        return "source local: ${sourceLocal}" +
                " local: " + locals.toString() +
                " fileType: ${fileType}" +
                " microsoftSubscriptionKey" + microsoftSubscriptionKey.toString() +
                " googleSubscriptionKey: ${googleSubscriptionKey}" +
                " inputFile: " + inputFile.toString() +
                " outputDir: " + outputDir.toString() +
                " copyright: ${copyright}"
    }

    /**
     * @return The {@link Provider} containing the two letter string representing the local of the source file to be
     * translated.
     *
     * @since 1.0
     */
    Provider<String> getSourceLocalProvider() {
        sourceLocal
    }

    /**
     * @param sourceLocal The two letter string representing the local of the source file to be translated.
     *
     * @since 1.0
     */
    void setSourceLocal(String sourceLocal) {
        this.sourceLocal.set(sourceLocal)
    }

    /**
     * @return The {@link Provider} containing the string array of the two letter codes used to determine the the
     * output language files.
     *
     * @since 1.0
     */
    Provider<List<String>> getLocalsProvider() {
        locals
    }

    /**
     * @param locals The string array of the two letter codes used to determine the the output language files.
     *
     * @since 1.0
     */
    void setLocals(List<String> locals) {
        this.locals.set(locals)
    }

    /**
     * @return The {@link Provider} containing the {@link FileType} used to determine how the string files are parsed in
     * and written.
     *
     * @since 1.0
     */
    Provider<FileType> getFileTypeProvider() {
        fileType
    }

    /**
     * @param fileType The {@link FileType} used to determine how the string files are parsed in and written.
     *
     * @since 1.0
     */
    void setFileType(FileType fileType) {
        this.fileType.set(fileType)
    }

    /**
     * @return The {@link Provider} containing the Microsoft subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    Provider<String> getMicrosoftSubscriptionKeyProvider() {
        microsoftSubscriptionKey
    }

    /**
     * @param microsoftSubscriptionKey The Microsoft subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    void setMicrosoftSubscriptionKey(String microsoftSubscriptionKey) {
        this.microsoftSubscriptionKey.set(microsoftSubscriptionKey)
    }

    /**
     * @return The {@link Provider} containing the Google subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    Provider<String> getGoogleSubscriptionKeyProvider() {
        googleSubscriptionKey
    }

    /**
     * @param googleSubscriptionKey The Google subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    void setGoogleSubscriptionKey(String googleSubscriptionKey) {
        this.googleSubscriptionKey.set(googleSubscriptionKey)
    }

    /**
     * @return The {@link Provider} containing the {@link File} containing the strings to be translated.
     *
     * @since 1.0
     */
    Provider<File> getInputProvider() {
        inputFile
    }

    /**
     * @param inputFile The {@link File} containing the strings to be translated.
     *
     * @since 1.0
     */
    void setInputFile(File inputFile) {
        this.inputFile.set(inputFile)
    }

    /**
     * @return The {@link Provider} containing the output directory for the files containing the translated strings.
     *
     * @since 1.0
     */
    Provider<File> getOutputDirProvider() {
        outputDir
    }

    /**
     * @param outputDir The output directory for the files containing the translated strings.
     *
     * @since 1.0
     */
    void setOutputDir(File outputDir) {
        this.outputDir.set(outputDir)
    }

    /**
     * @return The {@link Provider} containing the optional copyright text that will be added to all output files.
     *
     * @since 1.0
     */
    Provider<String> getCopyrightProvider() {
        copyright
    }

    /**
     * @param copyright The optional copyright text that will be added to all output files.
     *
     * @since 1.0
     */
    void setCopyright(String copyright) {
        this.copyright.set(copyright)
    }
}
