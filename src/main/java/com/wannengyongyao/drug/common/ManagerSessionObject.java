package com.wannengyongyao.drug.common;

import com.wannengyongyao.drug.model.Drug;
import com.wannengyongyao.drug.model.DrugUser;
import com.wannengyongyao.drug.util.DrugConstants;
import lombok.Data;

import javax.servlet.http.HttpSession;

@Data
public class ManagerSessionObject {
    private Integer id;
    private String name;

    public static ManagerSessionObject fromSession(HttpSession session){
        ManagerSessionObject mso = new ManagerSessionObject();
        mso.setId((Integer)session.getAttribute(DrugConstants.SESSION_USER_ID));
        mso.setName((String)session.getAttribute(DrugConstants.SESSION_USER_NAME));

        return mso;
    }

    public static void toSession(HttpSession session, DrugUser user){
        session.setAttribute(DrugConstants.SESSION_USER_ID, user.getId());
        session.setAttribute(DrugConstants.SESSION_USER_NAME, user.getName());
    }
}
