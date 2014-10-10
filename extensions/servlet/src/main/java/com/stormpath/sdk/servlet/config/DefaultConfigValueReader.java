/*
 * Copyright 2014 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.sdk.servlet.config;

import com.stormpath.sdk.lang.Strings;

import javax.servlet.ServletContext;

public class DefaultConfigValueReader implements ConfigValueReader {

    protected Config getConfig(ServletContext sc) {
        return ConfigResolver.INSTANCE.getConfig(sc);
    }

    @Override
    public String readValue(ServletContext sc, String name, String defaultValue) {
        Config config = getConfig(sc);
        String value = config.get(name);
        return Strings.hasText(value) ? value : defaultValue;
    }
}
