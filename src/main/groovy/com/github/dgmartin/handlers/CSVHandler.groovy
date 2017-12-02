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
 * TODO Add documentation
 *
 * @since 1.0
 */
class CSVHandler implements DragoHandler{

    @Override
    void setInputFile(File inputFile) {

    }

    @Override
    HashMap<String, String> parseStringMap(File sourceFile) {
        return null
    }

    @Override
    File getTranslationFile(File outputDir, String local) {
        return null
    }

    @Override
    HashMap<String, String> getStringMap() {
        return null
    }

    @Override
    DragoWriter getWriter() {
        return null
    }
}
