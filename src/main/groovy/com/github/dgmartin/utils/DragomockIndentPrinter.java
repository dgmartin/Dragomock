/*
 * Copyright 2020. Daniel Martin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dgmartin.utils;

import groovy.util.IndentPrinter;

import java.io.Writer;

/**
 * This is a simple extension of IndentPrinter that allows the developer to override the line endings.
 */
public class DragomockIndentPrinter extends IndentPrinter {
    private final String lineSeparator;
    private final boolean addNewlines;

    public DragomockIndentPrinter(Writer out, String indent, String lineSeparator) {
        this(out, indent, true, false, lineSeparator);
    }

    public DragomockIndentPrinter(Writer out, String indent, boolean addNewlines, boolean autoIndent) {
        this(out, indent, addNewlines, autoIndent, System.lineSeparator());
    }

    public DragomockIndentPrinter(Writer out, String indent, boolean addNewlines, boolean autoIndent, String lineSeparator) {
        super(out, indent, addNewlines, autoIndent);
        this.addNewlines = addNewlines;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void println() {
        if (this.addNewlines) {
            this.print(lineSeparator);
        }
    }
}
