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

import com.github.dgmartin.writers.AndroidXMLWriter
import com.github.dgmartin.writers.DragoWriter
import org.apache.log4j.Logger

/**
 * This handler is specifically designed to be used with the Android String Resource system. It is responsible for
 * parsing out the Android Resource files and provides the corresponding {@link AndroidXMLWriter}.
 *
 * @since 1.0
 */
class AndroidHandler implements DragoHandler {

    Logger logger = Logger.getLogger(this.getClass())
    HashMap<String, String> originalMap

    @Override
    void setInputFile(File inputFile) {
        originalMap = parseStringMap(inputFile)
        logger.trace("OriginalMap: " + originalMap.toString())
    }

    @Override
    HashMap<String, String> parseStringMap(File sourceFile) {
        logger.debug("Parsing value map for: " + sourceFile.getAbsolutePath())
        HashMap<String, String> valueMap = new HashMap<String, String>()

        Node stringResources = new XmlParser().parse(sourceFile)

        for (Node stringNode : (List<Node>) stringResources.children()) {
            def translatable = stringNode.@translatable
            // We only skip a translation when a tag explicitly says that it is not translatable. Otherwise we continue.
            if (translatable != "false") {
                String key = stringNode.@name
                String value = stringNode.text()

                valueMap.put(key, value)
            }
        }
        logger.trace("Parsed Map: " + valueMap.toString())
        return valueMap
    }

    @Override
    File getTranslationFile(File outputDir, String local) {
        def translationDir =  new File(outputDir.getAbsolutePath() ,"values-" + local)
        logger.debug("Translation Dir: " + translationDir.getAbsolutePath())

        if (!translationDir.exists()) {
            logger.debug("Translation directory \"" + translationDir.getAbsolutePath() +
                    "\" does not exist. Creating directory.")
            translationDir.mkdirs()
        }
        File translationFile = new File(translationDir.absolutePath, "strings.xml")
        return translationFile
    }

    @Override
    HashMap<String, String> getStringMap() {
        return originalMap
    }

    @Override
    DragoWriter getWriter() {
        return new AndroidXMLWriter()
    }
}
