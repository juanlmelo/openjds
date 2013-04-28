// DataSourceFactory
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
package com.openjds.client.factory;

import com.openjds.client.holder.ConfigurationHolder;
import com.openjds.common.IDataSourceRemoteService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class DataSourceFactory {

    /**
     * java:comp/env
     */
    public String JNDI_CONTEXT_LOOKUP = "java:comp/env";
    /**
     * openjds/DataSource
     */
    public String JNDI_BEAN_NAME = "openjds/DataSource";
    /**
     * rmi://127.0.0.1:1099/OpenJdsService
     */
    public String DEFAULT_RMI_SERVER_URL = "rmi://127.0.0.1:1099/Openjds";

    /**
     * This method is intended to be used as a factory for remote DataSource retrieval.<br/><br/>
     *
     *
     * It will make a JNDI lookup for JNDI_CONTEXT_LOOKUP + JNDI_BEAN_NAME and try to connect to the remote DataSource server specified in the 'url' parameter
     * of your resource object usually located in your application context.<br/>
     * It will also use the tokens specified in the resource to authenticate and get access to a given DataSource, environment is
     * specified by the 'environment' parameter in your context file. Additionally you can configure extra settings by using 'properties'.<br/><br/>
     *<br/>
     * <b>Resource Example:</b><br/><br/>
     *	 {@code
     *
     * 	<Resource auth="Container"
            name="openjds/DataSource"
            type="com.openjds.client.holder.ConfigurationHolder"
            factory="com.openjds.client.jndi.OpenJdsObjectFactoryBean"
            url="rmi://127.0.0.1:1099/Openjds" environment="dev" tokens="token1;token2;token3" />
    }
     *
     * @param applicationName
     * @return javax.sql.DataSource
     *
     * @throws RemoteException
     * @throws NamingException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public javax.sql.DataSource createRemoteDataSource(final String applicationName) throws RemoteException, NamingException, MalformedURLException, NotBoundException {
        /* I want it to have the less possible dependencies, so...   I'll just log it to console, don't hate me   */
        System.out.println("Open Java DataSource Server - looking up...");
        Context context = (Context) (new InitialContext()).lookup(JNDI_CONTEXT_LOOKUP);
        ConfigurationHolder holder = (ConfigurationHolder) context.lookup(JNDI_BEAN_NAME);
        IDataSourceRemoteService service = (IDataSourceRemoteService) Naming.lookup(holder.getUrl());
        System.out.println("Open Java DataSource Server - connected to " + holder.getUrl());
        return new org.apache.tomcat.jdbc.pool.DataSource(service.getConfig(applicationName, holder.getEnvironment(), holder.getTokens()));
    }

    /**
     * Tries to connect to the given RMI server specified in the rmiServer parameter, if an empty or null value is sent it will connect to the default RMI URL.</br></br>
     * If connection to the server is successful it will try to retrieve the named DataSource using the provided token id's.</br>
     * A RuntimeException will be thrown in case of any security error. Usually this method will be called by console applications.</br></br>
     *
     * This method won't require a Context resource to be configured.</br></br>
     *
     * @param applicationName
     * @param rmiURL
     * @param environmentName
     * @param tokens
     * @return
     * @throws RemoteException
     * @throws NamingException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public javax.sql.DataSource createRemoteDataSource(final String applicationName, String rmiURL, final String environmentName, final String... tokens) throws RemoteException, NamingException, MalformedURLException, NotBoundException {
        System.out.println("Open Java DataSource Server - looking up for non Jndi object...");
        IDataSourceRemoteService service = (IDataSourceRemoteService) Naming.lookup(rmiURL == null || rmiURL.isEmpty()?DEFAULT_RMI_SERVER_URL:rmiURL);
        System.out.println("Open Java DataSource Server - configured correctly RMI server " + rmiURL);
        return new org.apache.tomcat.jdbc.pool.DataSource(service.getConfig(applicationName, environmentName, tokens));
    }


    /**
     * Will try to connect to the default RMI server defined in DEFAULT_RMI_SERVER_URL.<br/><br/>
     * If connection to the server is successful it will try to retrieve the named DataSource using the provided token.<br/>
     * A RuntimeException will be thrown in case of any security error. Usually this method will be called by console applications.<br/><br/>
     *
     * This method won't require a Context resource to be configured since it's not Jndi based.<br/><br/>
     *
     * <b>IMPORTANT:</b> THIS METHOD WILL ALWAYS DEFAULT ENVIRONMENT TO 'dev'<br/><br/>
     *
     * @param dataSourceName
     * @param token
     * @return
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws NotBoundException
     * @throws NamingException
     */
    public javax.sql.DataSource createRemoteDataSource(final String dataSourceName,final String... token) throws MalformedURLException, RemoteException, NotBoundException, NamingException {
        return createRemoteDataSource(dataSourceName, null, "dev",token);
    }


    /*just to allow customization if required*/
    public DataSourceFactory setDefaultRmiServerUrl(final String dEFAULT_RMI_SERVER_URL) {
        this.DEFAULT_RMI_SERVER_URL = dEFAULT_RMI_SERVER_URL;
        return this;
    }

    public DataSourceFactory setJndiBeanName(final String jNDI_BEAN_NAME){
        this.JNDI_BEAN_NAME = jNDI_BEAN_NAME;
        return this;
    }

    public DataSourceFactory setJndiContextLookup(final String jNDI_CONTEXT_LOOKUP){
        this.JNDI_CONTEXT_LOOKUP = jNDI_CONTEXT_LOOKUP;
        return this;
    }
}
