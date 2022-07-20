package com.adamscript.tomatetoapi.helpers.bridge;

import com.adamscript.tomatetoapi.models.entities.User;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.util.HashMap;

public class UserBridge implements TwoWayFieldBridge {

    public void set(String name, Object value, Document document, LuceneOptions luceneOptions){

        User user = (User) value;

        luceneOptions.addFieldToDocument("userId", user.getId(), document);
        luceneOptions.addFieldToDocument("username", user.getUsername(), document);
        luceneOptions.addFieldToDocument("displayName", user.getDisplayName(), document);
        luceneOptions.addFieldToDocument("avatarDefault", user.getAvatarDefault(), document);
        luceneOptions.addFieldToDocument("avatarMedium", user.getAvatarMedium(), document);
        luceneOptions.addFieldToDocument("avatarSmall", user.getAvatarSmall(), document);
        luceneOptions.addFieldToDocument("avatarExtrasmall", user.getAvatarExtrasmall(), document);

    }

    public Object get(String name, Document document){
        User user = new User();

        user.setId(document.get("userId"));
        user.setUsername(document.get("username"));
        user.setDisplayName(document.get("displayName"));
        user.setAvatarDefault(document.get("avatarDefault"));
        user.setAvatarMedium(document.get("avatarMedium"));
        user.setAvatarSmall(document.get("avatarSmall"));
        user.setAvatarExtrasmall(document.get("avatarExtrasmall"));

        return user;

    }

    @Override
    public String objectToString(Object value){
        User user = (User) value;

        return user.toString();
    }

}
