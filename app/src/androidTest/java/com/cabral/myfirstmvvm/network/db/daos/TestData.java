package com.cabral.myfirstmvvm.network.db.daos;

import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;

public class TestData {
   public static User userTestData= new User(6,
           "Mrs. Dennis Schulist",
           "Leopoldo_Corkery",
           "Karley_Dach@jasper.info",
           "1-477-935-8478 x6430",
           "ola.org");
    public static Address addressTestData=new Address(6,
            "Norberto Crossing",
            "Apt. 950",
            "South Christy",
            "23505-1337",
            -71.4197,
            71.7478
    );
    public static UserCompany companyTestData=new UserCompany(
            6,
            "Considine-Lockman",
            "Synchronised bottom-line interface",
            "e-enable innovative applications"
    );

    public static UserPostEntity userPostData=new UserPostEntity(
            3,
            6,
            "ea molestias quasi exercitationem repellat qui ipsa sit aut",
            "et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut"
            );
}
