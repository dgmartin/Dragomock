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

/**
 * This interface provides the means to write a list of strings to file
 *
 * @since 1.0
 */
interface DragoWriter {

    /**
     * @param file The file that all output will be written to.
     *
     * @since 1.0
     */
    void setOutputFile(File file)

    /**
     * @return The {@link File} set as the output file
     *
     * @since 1.0
     */
    File getOutputFile()

    /**
     * @param copyright The optional copyright text that will be added to all output files.
     *
     * @since 1.0
     */
    void setCopyright(String copyright)

    /**
     * @return The copyright text set for this writer.
     *
     * @since 1.0
     */
    String getCopyright()

    /**
     * Used to add a key-value pair to be written to the output file.
     * @param key The string key for the pair.
     * @param value The string value for the pair
     *
     * @since 1.0
     */
    void put(String key, String value)

    /**
     * Writes all provided key-value pairs to the provided output file.
     *
     * @since 1.0
     */
    void write()
}