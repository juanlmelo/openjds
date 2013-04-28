// ConfigurationHolder
/**
 * Copyright (c) 2013      Juan Luis Melo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.openjds.client.holder;

public class ConfigurationHolder {
    final String url;
    final String[] tokens;
    final String environment;
    String properties;

    /**
     * Class that will hold the JNDI configurations.
     *
     * @param url
     * @param environment
     * @param tokens
     */
    public ConfigurationHolder(final String url, final String environment, final String... tokens) {
        this.url = url;
        this.tokens = tokens;
        this.environment = environment;

        if ( url == null || environment == null || tokens == null ||
                url.isEmpty() || environment.isEmpty() || tokens.length == 0 ) {
            throw new RuntimeException("Invalid constructor parameter(s)");
        }

    }

    /**
     * Class that will hold the JNDI configurations.
     *
     * @param url
     * @param environment
     * @param properties (for future use)
     * @param tokens
     */
    public ConfigurationHolder(final String url, final String environment, final String properties, final String... tokens) {
        this.url = url;
        this.tokens = tokens;
        this.environment = environment;
        this.properties = properties;

        if ( url == null || environment == null || tokens == null ||
                url.isEmpty() || environment.isEmpty() || tokens.length == 0 ) {
            throw new RuntimeException("Invalid constructor parameter(s)");
        }
    }

    public String getUrl() {
        return url;
    }

    public String[] getTokens() {
        return tokens;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
