
![2](https://github.com/mjameer/PAM-Java-Integration/assets/11364104/1963a595-e871-48b0-aa4e-b4c7708c62a2)

## Environment:

Java 21, Spring Boot 3.2.4, httpclient5.x


## Exploring the Code: 

We have two main components in our application: PAMRestTemplate and ConnectPAM.

### PAMRestTemplate:

This class is responsible for setting up WebClient with SSL to communicate securely with CyberArk Vault.
We load the SSL certificate from the keystore and configure RestTemplate accordingly.

### ConnectPAMWebClient:

This class acts as a gateway for interacting with the CyberArk Vault. It utilizes WebClient to make a request to the CyberArk Vault URL and fetch the credentials.


