package org.example.session_manager;

import org.example.enums.FrontEndTypeEnum;
import org.example.model.user.User;

public class Session {
    private User user;
    private Token token;
    private FrontEndTypeEnum frontEndType;


    Session(User user, FrontEndTypeEnum frontEndType){
        this.user = user;
        this.token = new Token();
        this.frontEndType = frontEndType;
    }

    User getUser(){
        return user;
    }

    void setUser(User user){
        this.user = user;
    }

    Token getToken(){
        return token;
    }

    void setToken(Token token){
        this.token = token;
    }

    FrontEndTypeEnum getFrontEndType(){
        return frontEndType;
    }

    void setFrontEndType(FrontEndTypeEnum frontEndType){
        this.frontEndType = frontEndType;
    }

}
