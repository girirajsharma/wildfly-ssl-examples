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
package org.jboss.as.quickstarts.rshelloworld;

import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.X509Certificate;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.bouncycastle.openssl.PEMWriter;
import org.jboss.as.quickstarts.pki.PemUtils;

/**
 * A simple JAX-RS 2.0 REST service which is able to say hello to the world using an injected HelloService CDI bean.
 * The {@link javax.ws.rs.Path} class annotation value is related to the {@link org.jboss.as.quickstarts.rshelloworld.HelloWorldApplication}'s path.
 * 
 * @author Giriraj Sharma
 * 
 */

@Path("/")
public class HelloWorld {
    @Inject
    HelloService helloService;
    
    private HttpServletRequest request;

    @Context public void setHttpServletRequest( HttpServletRequest request ) {
        this.request = request;
    }

    /**
     * Retrieves a JSON hello world message.
     * The {@link javax.ws.rs.Path} method annotation value is related to the one defined at the class level.
     * @return
     */
    @GET
    @Path("json")
    @Produces({ "application/json" })
    public JsonObject getHelloWorldJSON() {
        return Json.createObjectBuilder()
                .add("result", helloService.createHelloMessage("World"))
                .add("client_certificate_pem", getPemFromCertificate(extractCertificate(request)))
                .build();
    }

    /**
     * Retrieves a XML hello world message.
     * The {@link javax.ws.rs.Path} method annotation value is related to the one defined at the class level.
     * @return
     */
    @GET
    @Path("xml")
    @Produces({ "application/xml" })
    public String getHelloWorldXML() {
        return "<xml>"+
        		   "<result>" + helloService.createHelloMessage("World") + "</result>"+
                   "<client_certificate_pem>" + getPemFromCertificate(extractCertificate(request)) + "</client_certificate_pem>"+
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
