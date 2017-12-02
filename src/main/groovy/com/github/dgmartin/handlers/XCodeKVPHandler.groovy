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

package com.github.dgmartin.handlers

import com.github.dgmartin.writers.DragoWriter
import com.github.dgmartin.writers.XCodeKVPWriter
import org.apache.log4j.Logger

/**
 * TODO Add documentation
 *
 * @since 1.0
 */
class XCodeKVPHandler implements DragoHandler {

    /*
    TODO Example strings - Remove when code is in place
    "attachments.title" = "Attachments";
    "attachments.noImage" = "No image attached";
    "attachments.draft" = "Draft";
    "attachments.source"= "Source";

    FileName: Localizable.strings

    RegEx
    Find a key/value pair:  ".*?"\s*=\s*".*?"
    Find quoted text: ".*?"
    */

    def keyValuePattern = ~/".*?"\s*=\s*".*?"/
    def quotedTextPattern = ~/".*?"/
    public static MOCK_TRANSLATION_TAG = "#MOCK TRANSLATION"

    Logger logger = Logger.getLogger(this.getClass())
    HashMap<String, String> originalMap

    @Override
    void setInputFile(File inputFile) {
        originalMap = parseStringMap(inputFile)
    }



    @Override
    HashMap<String, String> parseStringMap(File sourceFile) {
        HashMap<String, String> valueMap = new HashMap<String, String>()



        sourceFile.eachLine { line ->
            String keyValueRaw = line.find(keyValuePattern)
            if (keyValueRaw != null) {
                List<String> keyValueArray = keyValueRaw.findAll(quotedTextPattern)
                String key = keyValueArray.get(0)
                String value = keyValueArray.get(1)

                valueMap.put(key, value)
            }
        }

        return valueMap
    }

    @Override
    File getTranslationFile(File outputDir, String local) {
        def translationDirString = outputDir.getAbsolutePath() + "\\${local}.lproj"
        logger.debug("Translation Dir string:" + translationDirString)
        def translationDir = new File(translationDirString)
        if (!translationDir.exists()) {
            logger.debug("Translation directory \"" + translationDir.getAbsolutePath() +
                    "\" does not exist. Creating directory.")
            translationDir.mkdirs()
        }
        File translationFile = new File(translationDir.absolutePath, "Main.strings")
        return translationFile
    }

    @Override
    HashMap<String, String> getStringMap() {
        return originalMap
    }

    @Override
    DragoWriter getWriter() {
        return new XCodeKVPWriter()
    }
}
