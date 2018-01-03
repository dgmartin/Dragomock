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

package com.github.dgmartin.translators

import groovy.json.JsonSlurper
import org.apache.log4j.Logger

/**
 * TODO Add documentation
 *
 * @since 1.0
 */
class GoogleTranslator implements DragoTranslator {

    Logger logger = Logger.getLogger(this.getClass())
    private String googleSubscriptionKey
    private String sourceLocal
    private String translationLocal

    GoogleTranslator(String googleSubscriptionKey, String sourceLocal, String translationLocal) {
        this.googleSubscriptionKey = googleSubscriptionKey
        this.sourceLocal = sourceLocal
        this.translationLocal = translationLocal
    }

    @Override
    String translateString(String value) {
        String translation = fetchTranslation(value)
        return translation
    }

    private String fetchTranslation(String value) {
        String translation = null

        def encodedEnglishString = URLEncoder.encode(value, 'UTF-8')
        def queryString = "source=${sourceLocal}" +
                "&target=${translationLocal}" +
                "&q=${encodedEnglishString}" +
                "&format=text" +
                "&model=nmt" +
                "&key=${googleSubscriptionKey}"

        def connection = new URL("https://translation.googleapis.com/language/translate/v2")
                .openConnection() as HttpURLConnection
        connection.with {
            doOutput = true
            requestMethod = 'POST'
            outputStream.withWriter { writer ->
                writer << queryString
            }
        }

        logger.debug("Connection: ${connection.toString()}")
        println "Connection: ${connection.toString()}"

        logger.debug('Response code: ' + connection.responseCode)
        logger.debug('Response message: ' + connection.responseMessage)

        println 'Response code: ' + connection.responseCode
        println 'Response message: ' + connection.responseMessage

        if (connection.responseCode == 200) {
            def json = new JsonSlurper().parseText(connection.inputStream.text)
            println json.toString()

            ArrayList translationArray = json.data.translations
            logger.debug("TranslationArray: ${translationArray}")

            translation = translationArray[0].translatedText.toString()
            logger.debug("Translation: ${translation}")
        }

        return translation
    }

}
