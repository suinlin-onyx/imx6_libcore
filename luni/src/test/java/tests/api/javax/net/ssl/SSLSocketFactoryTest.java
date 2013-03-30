/*
 * Copyright (C) 2007 The Android Open Source Project
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
package tests.api.javax.net.ssl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import junit.framework.TestCase;

import tests.support.Support_PortManager;

public class SSLSocketFactoryTest extends TestCase {

    private ServerSocket ss;

    protected int startServer(String name) {
        int portNumber = Support_PortManager.getNextPort();
        try {
            ss = new ServerSocket(portNumber);
        } catch (IOException e) {
            fail(name + ": " + e);
        }
        return ss.getLocalPort();
    }

    /**
     * javax.net.ssl.SSLSocketFactory#SSLSocketFactory()
     */
    public void test_Constructor() {
        try {
            SocketFactory sf = SSLSocketFactory.getDefault();
            assertTrue(sf instanceof SSLSocketFactory);
        } catch (Exception e) {
            fail("Unexpected exception " + e.toString());
        }
    }

    /**
     * javax.net.ssl.SSLSocketFactory#getDefault()
     */
    public void test_getDefault() {
        assertNotNull("Incorrect default socket factory",
                SSLSocketFactory.getDefault());
    }

    /**
     * javax.net.ssl.SSLSocketFactory#createSocket(Socket s, String host, int port, boolean autoClose)
     */
    public void test_createSocket() {
        SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
        int sport = startServer("test_createSocket()");
        int[] invalid = {
                Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE
        };
        try {
            Socket st = new Socket("localhost", sport);
            Socket s = sf.createSocket(st, "localhost", sport, false);
            assertFalse(s.isClosed());
        } catch (Exception ex) {
            fail("Unexpected exception " + ex);
        }
        try {
            Socket st = new Socket("localhost", sport);
            Socket s = sf.createSocket(st, "localhost", sport, true);
            s.close();
            assertTrue(st.isClosed());
        } catch (Exception ex) {
            fail("Unexpected exception " + ex);
        }
        try {
            sf.createSocket(null, "localhost", sport, true);
            fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            // expected
        } catch (NullPointerException e) {
            // expected
        }
        for (int i = 0; i < invalid.length; i++) {
            try {
                Socket s = sf.createSocket(new Socket(), "localhost", 1080, false);
                fail("IOException wasn't thrown");
            } catch (IOException ioe) {
                // expected
            }
        }

        try {
            Socket st = new Socket("bla-bla", sport);
            Socket s = sf.createSocket(st, "bla-bla", sport, false);
            fail("UnknownHostException wasn't thrown: " + "bla-bla");
        } catch (UnknownHostException uhe) {
            // expected
        } catch (Exception e) {
            fail(e + " was thrown instead of UnknownHostException");
        }
    }

    /**
     * javax.net.ssl.SSLSocketFactory#getDefaultCipherSuites()
     */
    public void test_getDefaultCipherSuites() {
        try {
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            assertTrue("no default cipher suites returned",
                    sf.getDefaultCipherSuites().length > 0);
        } catch (Exception e) {
            fail("Unexpected exception " + e.toString());
        }
    }

    /**
     * javax.net.ssl.SSLSocketFactory#getSupportedCipherSuites()
     */
    public void test_getSupportedCipherSuites() {
        try {
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            assertTrue("no supported cipher suites returned",
                    sf.getSupportedCipherSuites().length > 0);
        } catch (Exception e) {
            fail("Unexpected exception " + e.toString());
        }
    }

}
