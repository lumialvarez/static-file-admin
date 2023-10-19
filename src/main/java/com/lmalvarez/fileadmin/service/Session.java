package com.lmalvarez.fileadmin.service;

import com.lmalvarez.fileadmin.model.User;
import com.lmalvarez.tools.webservices.Rest;
import com.lmalvarez.tools.webservices.model.Request;
import com.lmalvarez.tools.webservices.model.Response;
import org.json.JSONObject;

public class Session {
    public static User checkToken(String token) {
        User user = new User();
        try {
            Request request = new Request();
            request.setUrl("https://services.lmalvarez.com/authorization/api/v1/ext/user/validate");
            String body = "{" +
                    "    \"token\": \"" + token + "\"" +
                    "}";
            request.setContent(body);
            request.setContentType("application/json");
            Response response =Rest.post(request);
            if (response.getStatus() == 200) {
                JSONObject json = new JSONObject(response.getContent());
                user.setUser(json.get("userName").toString());
                //user.setName(json.get("nombre").toString());
                user.setToken(token);
                user.setLogged(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
}
