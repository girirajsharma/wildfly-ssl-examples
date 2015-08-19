
Wildfly SSL Client Authentication
==================================
Preface: This document is currently in raw state. It will be updated and refined to give an absolutely clear idea to secure war's using two way
mutual ssl on wildfly.

In order to ask for a client ssl authentication, we have to setup wildlfy standalone.xml, web.xml and jboss-web.xml in different section.


1) Update UNDERTOW: Set https listener so that ssl authentication could occur
------------------------------------------------------------------------------

        <subsystem xmlns="urn:jboss:domain:undertow:2.0">
            <buffer-cache name="default"/>
            <server name="default-server">
                <http-listener name="default" socket-binding="http" redirect-socket="https"/>
                <https-listener name="ssl" socket-binding="https" security-realm="ManagementRealm" verify-client="REQUIRED"/>
                <host name="default-host" alias="localhost">
                    ...
                    ...
                </host>
            </server>
            ...
            ...
        </subsystem>


2) Update https-listener security realm to enable ssl authentication
------------------------------------------------------------------------------

So add <authentication> in order to setup the truststore path (the <ssl> section instead indicate the keystore for server certificate).

		<security-realm name="ManagementRealm">
            <server-identities>
                <ssl>
                    <keystore path="RootCA.keystore" relative-to="jboss.server.config.dir" keystore-password="keypassword" key-password="keypassword"/>
                </ssl>
            </server-identities>
            <authentication>
                <truststore path="RootCA.truststore" relative-to="jboss.server.config.dir" keystore-password="keypassword"/>
                <local default-user="$local" skip-group-loading="true"/>
                <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
            </authentication>
            <authorization map-groups-to-roles="false">
                <properties path="mgmt-groups.properties" relative-to="jboss.server.config.dir"/>
            </authorization>
        </security-realm>

3) Define a security-domain using login module (certificateRoles login module)
--------------------------------------------------------------------------------

In order to link the war application with wildfly configuration, define a security-domain using login module (certificateRoles login module)

        <subsystem xmlns="urn:jboss:domain:security:1.2">
            <security-domains>
                <security-domain name="client_cert_domain" cache-type="default">
                    <authentication>
                        <login-module code="CertificateRoles" flag="required">
                            <module-option name="verifier" value="org.jboss.security.auth.certs.AnyCertVerifier"/>
                            <module-option name="securityDomain" value="client_cert_domain"/>
                            <module-option name="rolesProperties" value="file:${jboss.server.config.dir}/app-roles.properties"/>
                        </login-module>
                    </authentication>
                    <jsse keystore-password="keypassword" keystore-url="file:${jboss.server.config.dir}/RootCA.keystore" truststore-password="keypassword" truststore-url="file:${jboss.server.config.dir}/RootCA.truststore" client-auth="true"/>
                </security-domain>
                ...
                ...
        </subsystem>

Make also the app-roles.properities file with the format
CERTIFICATE_DN=ROLE_NAME

a sample app-roles.properties file:

		CN\=client,\ OU\=Keycloak,\ O\=JBoss,\ ST\=UP,\ C\=IN=JBossAdmin
        admin=JBossAdmin

You have to escape the = symbol and space with backslash like the example.

4) Setup the client-cert authentication type, the role, and set as realm name the security domain name
--------------------------------------------------------------------------------------------------------

In the web.xml war setup the client-cert authentication type, the role, and set as realm name the security domain name

        <security-constraint>
            <display-name>Constraint1</display-name>
            <web-resource-collection>
                <web-resource-name>admin</web-resource-name>
                <description/>
                <url-pattern>/*</url-pattern>
            </web-resource-collection>
            <auth-constraint>
                <description/>
                <role-name>JBossAdmin</role-name>
            </auth-constraint>
        </security-constraint>
        <login-config>
            <auth-method>CLIENT-CERT</auth-method>
            <realm-name>client_cert_domain</realm-name>
        </login-config>
        <security-role>
            <description/>
            <role-name>JBossAdmin</role-name>
        </security-role>


5) Update jboss-web.xml (wildfly deployment descriptor) to set the security domain used
---------------------------------------------------------------------------------------

        <jboss-web>
            <server-instance>default-server</server-instance>      
            <virtual-host>default-host</virtual-host>
            <security-domain>client_cert_domain</security-domain>
        </jboss-web>
