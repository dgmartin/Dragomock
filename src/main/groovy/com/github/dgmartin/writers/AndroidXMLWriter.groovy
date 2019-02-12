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

package com.github.dgmartin.writers

import com.github.dgmartin.objects.ExistingTranslation
import com.github.dgmartin.utils.DragoUtils
import groovy.xml.MarkupBuilder
import groovy.xml.MarkupBuilderHelper
import groovy.xml.XmlUtil
import org.apache.log4j.Logger
import org.gradle.util.TextUtil

/**
 * This writer is specifically designed to be used with the Android String Resource system and writes all key-value
 * pairs to appropriate XML file formats.
 *
 * @since 1.0
 */
class AndroidXMLWriter implements DragoWriter {
    Logger logger = Logger.getLogger(this.getClass())
    LinkedHashMap<String, String> translations = new LinkedHashMap<>()
    File outputFile = null
    String copyright = null


    AndroidXMLWriter() {
    }

    @Override
    void setOutputFile(File outputFile) {
        this.outputFile = outputFile
    }

    @Override
    File getOutputFile() {
        return outputFile
    }

    @Override
    void setCopyright(String copyright) {
        this.copyright = copyright
    }

    @Override
    String getCopyright() {
        return copyright
    }

    @Override
    void put(String key, String value) {
        String formattedValue = XmlUtil.escapeXml(value)
        translations.put(key, formattedValue)
    }

    @Override
    void write() {
        ArrayList<ExistingTranslation> existingTranslations = new ArrayList<>()
        if (outputFile.length() > 0) {
            logger.debug("File is not empty")

            logger.debug("Output file Raw:\n" + outputFile.getText('UTF-8'))
            Node translatedStrings = new XmlParser().parse(outputFile)
            logger.debug("Node: " + translatedStrings.toString())
            logger.debug("Translated node child count: " + translatedStrings.children().size())
            if (translatedStrings.children().size() > 0) {
                logger.debug("File contains XML")
                for (Node stringNode : (List<Node>) translatedStrings.children()) {
                    boolean translatable = Boolean.valueOf(stringNode.@translatable)
                    // We check if it contains the old "mock_translation" tag or the new tag to determine if its mock data
                    boolean isMock = (Boolean.valueOf(stringNode.@mock_translation) ||
                            Boolean.valueOf(stringNode.@dragomock))
                    String key = stringNode.@name
                    String value = stringNode.text()

                    logger.debug("Storing existing. Key: ${key} Value: ${value}")
                    ExistingTranslation existingTranslation = new ExistingTranslation(key, value, String.valueOf(translatable))
                    existingTranslation.setIsMock(String.valueOf(isMock))
                    existingTranslations.add(existingTranslation)
                }

                logger.debug("Existing Translations count: ${existingTranslations.size()}")
            }
        }

        def writer = new FileWriter(outputFile)
        def xmlMarkup = new MarkupBuilder(writer)
        xmlMarkup.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")
        if (DragoUtils.isNotEmpty(getCopyright())) {
            xmlMarkup.mkp.comment(getCopyright())
            xmlMarkup.mkp.yield(System.getProperty("line.separator"))
        }

        logger.trace("Existing transactions array: " + existingTranslations.toString())
        logger.trace("New Translations array: " + translations.toString())

        xmlMarkup.resources() {
            logger.debug("Writing Final File")

            logger.debug("Output File: " + outputFile.getAbsolutePath())
            logger.trace("PreEdit Output:\n" + outputFile.text + "\nEnd PreEdit Output")
            for (ExistingTranslation existingTranslation : existingTranslations) {

                logger.trace("Existing Key: " + existingTranslation.getKey() + " and Value: " +
                        existingTranslation.getValue())
                Map<String, String> existingElements = new HashMap<>()
                existingElements.put('dragomock', existingTranslation.isMock())
                existingElements.put('name', existingTranslation.getKey())

                xmlMarkup.string(existingElements, existingTranslation.getValue())
            }


            for (String key : translations.keySet()) {
                Map<String, String> newElements = new HashMap<>()
                newElements.put('dragomock', 'true')
                newElements.put('name', key)

                String value = translations.get(key)
                logger.trace("New Key: " + key + " and Value: " + value)
                xmlMarkup.string(newElements, value)
            }
        }

        writer.close()

        translations = null
        outputFile = null
    }
}
