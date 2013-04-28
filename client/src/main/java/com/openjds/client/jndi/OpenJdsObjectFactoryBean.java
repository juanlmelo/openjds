// OpenJdsObjectFactoryBean
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
package com.openjds.client.jndi;

import com.openjds.client.holder.ConfigurationHolder;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Enumeration;
import java.util.Hashtable;

public class OpenJdsObjectFactoryBean implements ObjectFactory {

    public static final String SEPARATOR = ";";
    public static final String RMI_SERVER_CONFIG_NAME = "url";
    public static final String ENVIRONMENT_CONFIG_NAME = "environment";
    public static final String TOKENS_CONFIG_NAME = "tokens";
    public static final String ADDITIONAL_PROPS_NAME = "properties";

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        String rmiUrl = null, environmentName = null, properties = null;
        String[] tokens = null;

        final Enumeration<RefAddr> props = ((Reference) obj).getAll();

        while (props.hasMoreElements()) {
            final RefAddr refAddr = props.nextElement();
            if (refAddr != null) {
                final String type = refAddr.getType();
                final Object content = refAddr.getContent();
                switch (type){
                    case RMI_SERVER_CONFIG_NAME:
                        rmiUrl = (String) content;
                        break;
                    case ENVIRONMENT_CONFIG_NAME:
                        environmentName = (String) content;
                        break;
                    case ADDITIONAL_PROPS_NAME:
                        properties = (String) content;
                        break;
                    case TOKENS_CONFIG_NAME:
                        final String tmp = ((String) content);
                        if (tmp != null){
                            tokens = tmp.split(SEPARATOR);
                        }
                        break;
                }
            }
        }
        return new ConfigurationHolder(rmiUrl, environmentName, properties, tokens);
    }
}
