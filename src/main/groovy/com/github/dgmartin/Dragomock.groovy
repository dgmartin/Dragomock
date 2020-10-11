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

package com.github.dgmartin

import com.github.dgmartin.extensions.DragoPluginExtension
import com.github.dgmartin.tasks.CreateMockStrings
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * This is the main plugin called by the Dragomock Plugin for reading in string files and providing translated output.
 *
 * @since 1.0
 */
class Dragomock implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create("dragomock", DragoPluginExtension, project)
        def description = "Generate mock strings for this project. More details at https://github.com/dgmartin/Dragomock"
        project.task('createMockStrings',
                type: CreateMockStrings,
                description: description,
                group: "Dragomock") {
            sourceLocal = extension.getSourceLocalProvider()
            locals = extension.getLocalsProvider()
            fileType = extension.getFileTypeProvider()
            microsoftSubscriptionKey = extension.getMicrosoftSubscriptionKeyProvider()
            googleSubscriptionKey = extension.getGoogleSubscriptionKeyProvider()
            inputFile = extension.getInputProvider()
            outputDir = extension.getOutputDirProvider()
            copyright = extension.getCopyrightProvider()
            indentCount = extension.getIndentCount()
            lineSeparator = extension.getLineSeparator()

        }
    }
}
