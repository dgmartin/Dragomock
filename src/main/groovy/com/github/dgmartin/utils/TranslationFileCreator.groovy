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

package com.github.dgmartin.utils

import com.github.dgmartin.translators.DragoTranslator
import com.github.dgmartin.writers.DragoWriter
import org.apache.log4j.Logger

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * This class is in charged of managing the work of gathering all the data, utilizing the provided {@link
 * DragoTranslator} to translate and then write the strings to the provided {@link DragoWriter}
 *
 * @since 1.0
 */
class TranslationFileCreator {
    private Logger logger = Logger.getLogger(this.getClass())

    final String FORMATTED_STRING_PATTERN = /%[0-9]+\$[a-z]/
    final String NO_TRANSLATE_PATTERN = /<div class="notranslate">.*<\/div>/
    private Map<String, String> originMap
    private Map<String, String> translationMap
    private String originalLocalCode
    private String finalLocalCode
    private DragoTranslator translator
    private DragoWriter writer

    private TranslationFileCreator() {}

    /**
     * The main method in this class, it is used to pass the strings to the {@link DragoTranslator and relay the
     * output to the @link DragoWriter}
     *
     * @since 1.0
     */
    void translate() {
        logger.debug("Translation Map: " + translationMap.toString())
        for (String key : originMap.keySet()) {
            if (!translationMap.containsKey(key)) {
                logger.debug("Adding key: ${key}")
                def originalString = originMap.get(key)
                logger.debug("Original Text: ${originalString}")

                def protectedString = protectString(originalString)
                logger.debug("Protected String: ${protectedString}")
                def translatedString = translator.translateString(protectedString)

                logger.trace("key: ${key}, translatedString: ${translatedString}")
                if (translatedString) {
                    def finalString = unwrapString(translatedString)
                    logger.debug("Final String: ${finalString}")
                    writer.put(key, finalString)
                } else {
                    logger.warn("Error translating string")
                }
            }
        }
        writer.write()

        clean()
    }

    /**
     * Protects specific string patterns from being translated.
     *
     * @param The string to protect
     * @return The origin string with any matching patterns wrapped in no translate tags.
     *
     * @since 1.0
     */
    private String protectString(String original) {
        Pattern pattern = Pattern.compile(FORMATTED_STRING_PATTERN)
        Matcher matcher = pattern.matcher(original)
        StringBuffer buffer = new StringBuffer(original.length())
        while (matcher.find()) {
            String group = matcher.group()
            String noTranslatePattern = "<div class=\"notranslate\">%1\$s</div>"

            matcher.appendReplacement(buffer, Matcher.quoteReplacement(String.format(noTranslatePattern, group)))
        }
        matcher.appendTail(buffer)
        return buffer.toString()
    }

    /**
     * Removes protective tags from translated strings.
     *
     * @param The translated string.
     * @return The translated string with any protecttion tags removed.
     *
     * @since 1.0
     */
    private String unwrapString(String original) {
        Pattern pattern = Pattern.compile(NO_TRANSLATE_PATTERN)
        Matcher matcher = pattern.matcher(original)
        StringBuffer buffer = new StringBuffer(original.length())
        while (matcher.find()) {
            String group = matcher.group()

            group = group.replaceFirst("<div class=\"notranslate\">", " ")
            group = group.substring(0, group.length() - 6)
            group = group + " "

            matcher.appendReplacement(buffer, Matcher.quoteReplacement(group))
        }
        matcher.appendTail(buffer)
        return buffer.toString()
    }
    /**
     * There is currently a known issue in which case the strings are being duplicated during output. This method
     * cleans the values upon completion to avoid this problem. This will most likely be removed once the issue is
     * resolved.
     *
     * @since 1.0
     */
    private void clean(){
        originMap = null
        translationMap = null
        originalLocalCode = null
        finalLocalCode = null
        translator = null
        writer = null
    }

    /**
     * @param String map containing the key-value pairs meant to be translated
     *
     * @since 1.0
     */
    void setOriginMap(Map<String, String> originMap) {
        this.originMap = originMap
    }

    /**
     * @param String map used to compare to the original map. If a key is already contained in this map it will be
     * skipped during the translation process.
     *
     * @since 1.0
     */
    void setTranslationMap(Map<String, String> translationMap) {
        this.translationMap = translationMap
    }

    /**
     * @param The two character string that represents the language of the origin strings.
     *
     * @since 1.0
     */
    void setOriginalLocalCode(String originalLocalCode) {
        this.originalLocalCode = originalLocalCode
    }

    /**
     * @param The two character string that represents the language the strings will be translated to.
     *
     * @since 1.0
     */
    void setFinalLocalCode(String finalLocalCode) {
        this.finalLocalCode = finalLocalCode
    }

    /**
     * @param The {@link DragoTranslator} that will be used to translate the strings.
     *
     * @since 1.0
     */
    void setTranslator(DragoTranslator translator) {
        this.translator = translator
    }

    /**
     * @param The {@link DragoWriter} used to write the final translated string files.
     *
     * @since 1.0
     */
    void setWriter(DragoWriter writer) {
        this.writer = writer
    }

    /**
     * Builder class for the {@link TranslationFileCreator}
     *
     * @since 1.0
     */
    static class Builder {
        private Logger builderLogger = Logger.getLogger(this.getClass())
        private TranslationFileCreator fileBuilder

        Builder() {
            fileBuilder = new TranslationFileCreator()
        }

        /**
         * @param String map containing the key-value pairs meant to be translated
         *
         * @since 1.0
         */
        Builder setOriginMap(Map<String, String> originMap) {
            builderLogger.debug("Setting OriginMap")
            fileBuilder.setOriginMap(originMap)
            return this
        }

        /**
         * @param String map used to compare to the original map. If a key is already contained in this map it will be
         * skipped during the translation process.
         *
         * @since 1.0
         */
        Builder setTranslationMap(Map<String, String> translationMap) {
            builderLogger.debug("Setting TranslationMap")
            fileBuilder.setTranslationMap(translationMap)
            return this
        }

        /**
         * @param The two character string that represents the language of the origin strings.
         *
         * @since 1.0
         */
        Builder setOriginalLocalCode(String originalLocalCode) {
            builderLogger.debug("Setting OriginLocalCode")
            fileBuilder.setOriginalLocalCode(originalLocalCode)
            return this
        }

        /**
         * @param The two character string that represents the language the strings will be translated to.
         *
         * @since 1.0
         */
        Builder setFinalLocalCode(String finalLocalCode) {
            builderLogger.debug("Setting FinalLocalCode")
            fileBuilder.setFinalLocalCode(finalLocalCode)
            return this
        }

        /**
         * @param The {@link DragoTranslator} that will be used to translate the strings.
         *
         * @since 1.0
         */
        Builder setTranslator(DragoTranslator translator) {
            builderLogger.debug("Setting translator")
            fileBuilder.setTranslator(translator)
            return this
        }

        /**
         * @param The {@link DragoWriter} used to write the final translated string files.
         *
         * @since 1.0
         */
        Builder setWriter(DragoWriter writer) {
            builderLogger.debug("Setting writer")
            fileBuilder.setWriter(writer)
            return this
        }

        /**
         * @return The final {@link TranslationFileCreator} containing the information passed to this builder.
         *
         * @since 1.0
         */
        TranslationFileCreator build() {
            return fileBuilder
        }
    }
}

