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

import com.github.dgmartin.handlers.XCodeKVPHandler

/**
 * TODO Add documentation
 *
 * @since 1.0
 */
class XCodeKVPWriter implements DragoWriter {

    LinkedHashMap<String, String> translations = new LinkedHashMap<>()
    File outputFile = null
    String copyright = null
    String lineSeparator


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
    void setIndentCount(int indentCount) {

    }

    @Override
    int getIndentCount() {
        return 0
    }

    @Override
    void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator
    }

    @Override
    String getLineSeparator() {
        return lineSeparator
    }

    @Override
    void put(String key, String value) {
        translations.put(key, value)
    }

    @Override
    void write() {
        def writer = new FileWriter(outputFile)
        translations.keySet().each { key ->
            def value = translations.get(key)
            writer << {
                """"${key}" = "${value}";${XCodeKVPHandler.MOCK_TRANSLATION_TAG}"""
            }
        }
    }
}
