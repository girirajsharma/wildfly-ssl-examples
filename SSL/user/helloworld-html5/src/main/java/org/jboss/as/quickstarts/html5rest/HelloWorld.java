/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.html5rest;

import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.X509Certificate;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.bouncycastle.openssl.PEMWriter;
import org.jboss.as.quickstarts.pki.PemUtils;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS
 * is enabled And notice the @PathParam which expects the URL to contain /json/David or /xml/Mary
 * 
 * @author Giriraj Sharma
 */

@Path("/")
public class HelloWorld {
    @Inject
    HelloService helloService;
    
    private HttpServletRequest request;

    @Context public void setHttpServletRequest( HttpServletRequest request ) {
        this.request = request;
    }

    @POST
    @Path("/json/{name}")
    @Produces("application/json")
    public String getHelloWorldJSON(@PathParam("name") String name) {
        System.out.println("name: " + name);
        return "{\"result\":\"" + helloService.createHelloMessage(name) + "\"result\":\"" +
        		"\"client_cert_pem\":\"" + helloService.createHelloMessage(name) + "\"client_cert_pem\":\"" +
        		"}";
    }

    @POST
    @Path("/xml/{name}")
    @Produces("application/xml")
    public String getHelloWorldXML(@PathParam("name") String name) {
        System.out.println("name: " + name);
        return "<xml>"+
        			"<result>" + helloService.createHelloMessage(name) + "</result>"+
        			"<client_cert_pem>" + getPemFromCertificate(extractCertificate(request)) + "</client_cert_pem>"+
        	   "</xml>";
    }
    
    protected X509Certificate extractCertificate(HttpServletRequest req) {
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
        if (null != certs && certs.length > 0) {
            return certs[0];
        }
        throw new RuntimeException("No X.509 client certificate found in request");
    }
    
    public static String getPemFromCertificate(X509Certificate certificate) {
        if (certificate != null) {
            StringWriter writer = new StringWriter();
            PEMWriter pemWriter = new PEMWriter(writer);
            try {
                pemWriter.writeObject(certificate);
                pemWriter.flush();
                pemWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String s = writer.toString();
            return PemUtils.removeBeginEnd(s);
        } else {
            return null;
        }
    }

}
