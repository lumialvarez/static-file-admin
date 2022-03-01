package com.lmalvarez.fileadmin.service;

import com.lmalvarez.fileadmin.model.User;
import com.lmalvarez.tools.webservices.Rest;
import com.lmalvarez.tools.webservices.model.Request;
import com.lmalvarez.tools.webservices.model.Response;
import org.json.JSONObject;

import java.util.HashMap;

public class Session {
    public static User checkToken(String token){
        User user = new User();
        try{
            Request request = new Request();
            request.setUrl("https://services.lmalvarez.com/api/int/v1/auth/current");
            HashMap<String, String> h = new HashMap<>();
            h.put("Authorization", "Bearer " + token);
            request.setHeaders(h);
            Response response = Rest.get(request);
            if(response.getStatus() == 200){
                JSONObject json = new JSONObject(response.getContent());
                user.setUser(json.get("nombreUsuario").toString());
                user.setName(json.get("nombre").toString());
                user.setToken(token);
                user.setLogged(true);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return user;
    }
}
