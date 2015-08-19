
Wildfly SSL Client Authentication
==================================
In order to ask for a client ssl authentication, we have to setup wildlfy standalone.xml, web.xml and jboss-web.xml in different section.


1) Update UNDERTOW: Set https listener so that ssl authentication could occur
------------------------------------------------------------------------------

		<subsystem xmlns="urn:jboss:domain:undertow:1.1">
            <buffer-cache name="default"/>
            <server name="default-server">
                <http-listener name="default" socket-binding="http"/>
                <https-listener name="https-listener" socket-binding="https" security-realm="MySecurityRealm"/>
                <host name="default-host" alias="localhost">
                    <location name="/" handler="welcome-content"/>
                    <access-log pattern="common" directory="${jboss.server.log.dir}" prefix="access"/>
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                </host>
            </server>
            <server name="default-server_secondary">
                <http-listener name="default_secondary" socket-binding="http_secondary"/>
                <https-listener name="https-listener_secondary" socket-binding="https_secondary" security-realm="MySecurityRealm_secondary" verify-client="REQUESTED"/>
                <host name="testfoo.com alias="testfoo.com">
                    <location name="/" handler="welcome-content"/>
                    <access-log pattern="common" directory="${jboss.server.log.dir}" prefix="access"/>
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                </host>
            </server>
            <servlet-container name="default" default-encoding="utf-8">
                <jsp-config/>
            </servlet-container>
            <handlers>
                <file name="welcome-content" path="${jboss.home.dir}/welcome-content"/>
            </handlers>
            <filters>
                <response-header name="server-header" header-name="Server" header-value="WildFly/8"/>
                <response-header name="x-powered-by-header" header-name="X-Powered-By" header-value="Undertow/1"/>
            </filters>
		</subsystem>


2) Update https-listener security realm to enable ssl authentication
------------------------------------------------------------------------------

So add <authentication> in order to setup the truststore path (the <ssl> section instead indicate the keystore for server certificate).

		<security-realm name="MySecurityRealm_secondary">
                <server-identities>
                    <ssl protocol="TLSv1">
                        <keystore path="sdi_keystore.jks" relative-to="jboss.server.config.dir" keystore-password="changeit" alias="testfoo.com" key-password="changeit"/>
                    </ssl>
                </server-identities>
                <authentication>
                    <truststore path="sdi_cacerts.jks" relative-to="jboss.server.config.dir" keystore-password="changeit"/>
                    <local default-user="$local"/>
                    <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
                </authentication>
		</security-realm>

3) Define a security-domain using login module (certificateRoles login module)
--------------------------------------------------------------------------------

In order to link the war application with wildfly configuration, define a security-domain using login module (certificateRoles login module)

		<security-domain name="sdi_webservice_client_cert_domain" cache-type="default">
                    <authentication>
                        <login-module code="CertificateRoles" flag="required">
                            <module-option name="verifier" value="org.jboss.security.auth.certs.AnyCertVerifier"/>
                            <module-option name="securityDomain" value="sdi_webservice_client_cert_domain"/>
                            <module-option name="rolesProperties" value="file:${jboss.server.config.dir}/app-roles.properties"/>
                        </login-module>
                    </authentication>
                    <jsse keystore-password="changeit" keystore-url="file:${jboss.server.config.dir}/sdi_keystore.jks" truststore-password="changeit" truststore-url="file:${jboss.server.config.dir}/sdi_cacerts.jks" client-auth="true"/>
		</security-domain>

Make also the app-roles.properities file with the format
certificate DN=nome_ruolo


# sample app-roles.properties file
----------------------------------
		/C=IN/ST=UP/L=Noida/O=JBoss/OU=Keycloak/CN=giriraj/emailAddress=server@gmail.com=SDI_USER

You have to escape the = symbol and space with backslash like the example.

4) Setup the client-cert authentication type, the role, and set as realm name the security domain name
--------------------------------------------------------------------------------------------------------

In the web.xml war setup the client-cert authentication type, the role, and set as realm name the security domain name

		<?xml version="1.0" encoding="UTF-8"?>
		<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
    		<session-config>
        		<session-timeout>
            		30
        		</session-timeout>
    		</session-config>
    		<security-constraint>
        	<display-name>Constraint1</display-name>
        	<web-resource-collection>
            	<web-resource-name>sdi</web-resource-name>
            	<description/>
            	<url-pattern>/*</url-pattern>
        	</web-resource-collection>
        	<auth-constraint>
            	<description/>
            	<role-name>SDI_USER</role-name>
        	</auth-constraint>
    		</security-constraint>
    		<login-config>
        		<auth-method>CLIENT-CERT</auth-method>
        		<realm-name>sdi_webservice_client_cert_domain</realm-name>
    		</login-config>
    		<security-role>
        	<description/>
        	<role-name>SDI_USER</role-name>
    		</security-role>
		</web-app>


5) Update jboss-web.xml (wildfly deployment descriptor) to set the security domain used
---------------------------------------------------------------------------------------

		<jboss-web>
    		<server-instance>default-server_secondary</server-instance>      
    		<virtual-host>testfoo.com</virtual-host>
    		<security-domain>sdi_webservice_client_cert_domain</security-domain>
		</jboss-web>
