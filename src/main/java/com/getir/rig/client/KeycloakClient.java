package com.getir.rig.client;

import com.getir.rig.dto.CustomerDto;
import com.getir.rig.exception.LoginFailedException;
import com.getir.rig.exception.UserCreationException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);


    @Value("${keycloak.credentials.secret}")
    private String SECRETKEY;

    @Value("${keycloak.resource}")
    private String CLIENTID;

    @Value("${keycloak.auth-server-url}")
    private String AUTHURL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${keycloak.admin.username}")
    private String A_USERNAME;

    @Value("${keycloak.admin.password}")
    private String A_PASSWORD;


    public String getToken(String username, String password)  {

        String responseToken = null;

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "password"));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
            urlParameters.add(new BasicNameValuePair("username", username));
            urlParameters.add(new BasicNameValuePair("password", password));
            urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

            responseToken = sendPost(urlParameters);

        return responseToken;

    }

    public int createUserInKeyCloak(CustomerDto customerDto) {

        int statusId = 0;
        try {

            UsersResource userRessource = getKeycloakUserResource();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(customerDto.getUserName());
            user.setEmail(customerDto.getEmail());
            user.setFirstName(customerDto.getFirstName());
            user.setLastName(customerDto.getLastname());
            user.setEnabled(true);

            // Create user
            Response result = userRessource.create(user);
            logger.debug(" Keycloak has created user. Response code is : " + result.getStatus());
            statusId = result.getStatus();

            if (statusId == 201) {

                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                logger.debug("User has been created with userID " + userId);

                // Define password credential
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(customerDto.getPassword());

                // Set password credential
                userRessource.get(userId).resetPassword(passwordCred);

                // set role
                RealmResource realmResource = getRealmResource();
                RoleRepresentation savedRoleRepresentation = realmResource.roles().get("user").toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

                logger.debug("Username : " + customerDto.getUserName() + " has been created successfully.");

            }

            else if (statusId == 409) {
                logger.debug("Username : " + customerDto.getUserName() + " already present");
                throw new LoginFailedException("User already present");
            } else {
                logger.debug("Username : " + customerDto.getUserName() + " could not be created.");
                throw new LoginFailedException("Undefined status code " + statusId);
            }

        } catch (Exception e) {
            throw  new UserCreationException("User could not be created.", e);
        }

        return statusId;

    }
    private String sendPost(List<NameValuePair> urlParameters)  {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");

        StringBuffer result = new StringBuffer();
        try {

            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }catch (Exception e){
            throw new LoginFailedException("Login failed" , e);
        }

        return result.toString();

    }
    private UsersResource getKeycloakUserResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("admin").password("admin")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = kc.realm(REALM);
        UsersResource userRessource = realmResource.users();

        return userRessource;
    }

    private RealmResource getRealmResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("admin").password("admin")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = kc.realm(REALM);

        return realmResource;

    }

}
