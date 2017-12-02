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

/**
 * This provides an interface for creating a way to handle the different files types. This interface provides method
 * for parsing out files. It is also responsible for providing the corresponding {@link DragoWriter}.
 *
 * @since 1.0
 */
interface DragoHandler {

    /**
     * @param inputFile The {@link File} of the original string file to be translated.
     *
     * @since 1.0
     */
    void setInputFile(File inputFile)

    /**
     * This is used to parse key-value pairs from the provided file.
     *
     * @param sourceFile The string file to be parsed.
     * @return {@link HashMap} containing the string key-value pairs parsed from the source file.
     *
     * @since 1.0
     */
    HashMap<String, String> parseStringMap(File sourceFile)

    /**
     * Provides the specific file for output based on the output directory and the current translation local.
     *
     * @param outputDir The {@link File} of the output directory.
     * @param local The current translation local.
     *
     * @return The specific output {@link File}.
     *
     * @since 1.0
     */
    File getTranslationFile(File outputDir, String local)

    /**
     * @return The {@link HashMap} containing the parsed key-value pairs from the origin file.
     *
     * @since 1.0
     */
    HashMap<String, String> getStringMap()

    /**
     * @return The corresponding {@link com.github.dgmartin.writers.DragoWriter} for this handler.
     *
     * @since 1.0
     */
    DragoWriter getWriter()

}