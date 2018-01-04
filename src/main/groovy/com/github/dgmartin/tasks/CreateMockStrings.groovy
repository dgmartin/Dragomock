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

package com.github.dgmartin.tasks

import com.github.dgmartin.constants.FileType
import com.github.dgmartin.handlers.AndroidHandler
import com.github.dgmartin.handlers.CSVHandler
import com.github.dgmartin.handlers.DragoHandler
import com.github.dgmartin.handlers.XCodeKVPHandler
import com.github.dgmartin.handlers.XCodePListHandler
import com.github.dgmartin.translators.DragoTranslator
import com.github.dgmartin.translators.GoogleTranslator
import com.github.dgmartin.translators.MicrosoftTranslator
import com.github.dgmartin.utils.DragoUtils
import com.github.dgmartin.utils.TranslationFileCreator
import com.github.dgmartin.writers.DragoWriter
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.http.util.TextUtils

/**
 * This is the main task for translating string files. The {@link DragoPluginExtension} data from the build script
 * is passed to this class at which point this takes over the responsibility of translating the files.
 *
 * @since 1.0
 */
class CreateMockStrings extends DefaultTask {
    final Property<String> sourceLocal
    final Property<List<String>> locals
    final Property<FileType> fileType
    final Property<String> microsoftSubscriptionKey
    final Property<String> googleSubscriptionKey
    final Property<File> inputFile
    final Property<File> outputDir

    DragoHandler handler

    CreateMockStrings() {
        logger.debug("Creating new CreateMockStrings instance.")
        logger.trace("project properties: " + project.properties.toString())
        this.sourceLocal = project.objects.property(String.class)
        this.locals = project.objects.property(List.class)
        this.fileType = project.objects.property(FileType.class)
        this.microsoftSubscriptionKey = project.objects.property(String.class)
        this.googleSubscriptionKey = project.objects.property(String.class)
        this.inputFile = project.objects.property(File.class)
        this.outputDir = project.objects.property(File.class)
    }

    @Override
    String toString() {
        return "local: " + locals.toString() +
                " microsoftSubscriptionKey " + microsoftSubscriptionKey.toString() +
                " googleSubscriptionKey " + googleSubscriptionKey.toString() +
                " inputFile: " + inputFile.toString() +
                " outputDir: " + outputDir.toString()
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the source local for the translation. While this method is not called directly it is
     * necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setSourceLocal(Provider<String> sourceLocal) {
        this.sourceLocal.set(sourceLocal)
    }

    /**
     * @return The two letter string representing the local of the source file to be translated. This value is set in
     * the build script.
     *
     * @since 1.0
     */
    String getSourceLocal() {
        logger.trace("Returning SourceLocal")
        sourceLocal.isPresent() ? sourceLocal.get() : "en"
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the string array of the two letter codes used to determine the the output language files.
     * While this method is not called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setLocals(Provider<List<String>> locals) {
        this.locals.set(locals)
    }

    /**
     * @return The string array of the two letter codes used to determine the the output language files.
     *
     * @since 1.0
     */
    @Input
    String[] getLocals() {
        logger.trace("Returning Locals")
        locals.isPresent() ? locals.get() : ["ru", "de", "fr"]
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the {@link FileType} used to determine how the string files are parsed in and written.
     * While this method is not called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setFileType(Provider<FileType> fileType) {
        this.fileType.set(fileType)
    }

    /**
     * @return The {@link FileType} used to determine how the string files are parsed in and written.
     *
     * @since 1.0
     */
    String getFileType() {
        logger.trace("Returning FileType")
        fileType.get()
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the key for the Microsoft subscription used to enable the Translation API. While this
     * method is not called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setMicrosoftSubscriptionKey(Provider<String> microsoftSubscriptionKey) {
        this.microsoftSubscriptionKey.set(microsoftSubscriptionKey)
    }

    /**
     * @return The Microsoft subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    private String getMicrosoftSubscriptionKey() {
        logger.trace("Returning MicrosoftKey")
        microsoftSubscriptionKey.get()
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the key for the Google subscription used to enable the Translation API. While this
     * method is not called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setGoogleSubscriptionKey(Provider<String> googleSubscriptionKey) {
        this.googleSubscriptionKey.set(googleSubscriptionKey)
    }

    /**
     * @return The Google subscription used to enable the Translation API.
     *
     * @since 1.0
     */
    private String getGoogleSubscriptionKey() {
        logger.trace("Returning GoogleKey")
        googleSubscriptionKey.get()
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the input file used to supply the strings to be translated. While this method is not
     * called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setInputFile(Provider<File> inputFile) {
        this.inputFile.set(inputFile)
    }

    /**
     * @return The {@link File} used to supply the strings to be translated
     *
     * @since 1.0
     */
    @InputFile
    File getInputFile() {
        logger.trace("Returning InputFile")
        inputFile.get()
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * This is used to set the output directory for the files containing the translated strings. While this method is
     * not called directly it is necessary for the plugin to pass the data during initialization.
     * </p>
     *
     * @since 1.0
     */
    void setOutputDir(Provider<File> outputDir) {
        this.outputDir.set(outputDir)
    }

    /**
     * @return The output directory for the files containing the translated strings.
     *
     * @since 1.0
     */
    @OutputDirectory
    File getOutputDir() {
        logger.trace("Returning OutputDir")
        outputDir.isPresent() ? outputDir.get() : new File(getInputFile().getParentFile().getParent())
    }

    /**
     * <p>
     * <b>NOTE</b> Do Not Delete
     * </p>
     * <p>
     * While this method is not called directly in code it is the primary method called by the plugin.
     * </p>
     *
     * @since 1.0
     */
    @TaskAction
    def createMockStrings() {
        logger.info("Starting Create Mock Strings Task: " + toString())

        handler = getHandler()
        handler.setInputFile(getInputFile())
        getLocals().each { local ->
            generateStringsMap("${local}")
        }
    }

    /**
     * Used to generate the string files for a specific local
     *
     * @since 1.0
     */
    void generateStringsMap(String local) {
        logger.info("Starting translations for " + local.toString())
        File translationFile = handler.getTranslationFile(getOutputDir(), local)
        logger.info("Working with translation file: " + translationFile.absolutePath)
        if (translationFile.length() > 0) {
            logger.trace("Translation File Text:\n" + translationFile.text + "\nEnd Translation File Text")
        }

        Map<String, String> translationMap
        if (translationFile.exists()) {
            logger.info("Translation file already exists. Parsing current strings.")
            translationMap = handler.parseStringMap(translationFile)
        } else {
            logger.info("Translation file does not exist. Creating file.")
            translationFile.createNewFile()
            translationMap = new HashMap<String, String>()
        }

        DragoWriter writer = handler.getWriter()
        writer.setOutputFile(translationFile)

        buildStringList(translationMap, local, writer)

        logger.trace("OUTPUT for " + local.toString() + ":\n" + translationFile.text + "\nEND OUTPUT")
    }

    /**
     * @return The {@link DragoHandler} based on the {@link FileType} provided in the build script
     *
     * @since 1.0
     */
    private DragoHandler getHandler() {
        DragoHandler handler
        logger.debug("Getting handler for filetype: " + getFileType())
        switch (getFileType()) {
            case FileType.TYPE_XCODE_P_LIST:
                handler = new XCodePListHandler()
                break
            case FileType.TYPE_XCODE_KEY_VALUE_PAIR:
                handler = new XCodeKVPHandler()
                break
            case FileType.TYPE_CSV:
                handler = new CSVHandler()
                break
            case FileType.TYPE_ANDROID_XML:
            default:
                handler = new AndroidHandler()
                break
        }

        return handler
    }

    /**
     * @return The {@link DragoTranslator} based on which subscription key was provided in the build script.
     * Returns NULL if no value was provided
     *
     * @since 1.0
     */
    private DragoTranslator getTranslator(String local) {
        logger.debug("Creating New Translator")
        DragoTranslator translator

        if (DragoUtils.isNotEmpty(getGoogleSubscriptionKey())) {
            logger.debug("Creating Google Translator")
            println "Creating Google Translator"
            translator = new GoogleTranslator(getGoogleSubscriptionKey(), getSourceLocal(), local)
        } else if (DragoUtils.isNotEmpty(getMicrosoftSubscriptionKey())) {
            logger.debug("Creating Microsoft Translator")
            translator = new MicrosoftTranslator(getMicrosoftSubscriptionKey(), getSourceLocal(), local)
        } else {
            logger.error("Missing subscription!")
            throw new NullPointerException("No subscription key found. Must include one of MicrosoftSubscriptionKey " +
                    "or GoogleSubscriptionKey.")
        }
        println "Translator: " + translator

        return translator
    }

    /**
     * Used to generate a {@link TranslationFileCreator} and create the translated string file for a specific local.
     *
     * @since 1.0
     */
    void buildStringList(Map<String, String> translationMap, String local, DragoWriter writer) {
        TranslationFileCreator builder = new TranslationFileCreator.Builder()
                .setOriginMap(handler.getStringMap())
                .setTranslationMap(translationMap)
                .setOriginalLocalCode(getSourceLocal())
                .setFinalLocalCode(local)
                .setTranslator(getTranslator(local))
                .setWriter(writer)
                .build()
        builder.translate()
    }
}
