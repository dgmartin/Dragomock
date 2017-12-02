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

package com.github.dgmartin.constants

/**
 * This enum is used to determine the file type used for parsing and writing the string files.
 *
 * @since 1.0
 */
enum FileType {

    /**
     *  Used to specify resource XML files as defined by the Android operating system
     *  <a href="https://developer.android.com/guide/topics/resources/string-resource.html">here</a>
     *
     *  @see <a href="https://developer.android.com/guide/topics/resources/string-resource.html">String Resources</a>
     * @since 1.0
     */
    TYPE_ANDROID_XML,

    /**
     *  TODO javadoc
     *
     * @since 1.0
     */
    TYPE_XCODE_P_LIST,

    /**
     *  TODO javadoc
     *
     * @since 1.0
     */
    TYPE_XCODE_KEY_VALUE_PAIR,

    /**
     *  TODO javadoc
     *
     * @since 1.0
     */
    TYPE_CSV
}