Wildfly: Mutual(two way) client SSL Authentication
==================================================

Certificate authority (signing certificate) description for pkcs#12 format certificates:
-----------------------------------------------------------------------------------------

RootCA.p12        self signed

serverCert.p12    signed by RootCA

client.p12        signed by RootCA

client1.p12       signed by Server
