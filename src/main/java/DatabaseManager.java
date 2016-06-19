/**
 * Created by Eduardo veras on 19-Jun-16.
 * Edited by Siclait on 19-Jun-16
 */

import Entity.*;
import Service.*;

import javax.persistence.PersistenceException;
import java.util.List;

public class DatabaseManager {

    // Singleton Constructor
    private DatabaseManager(){

    }

    public static void BootDataBase(){

        List<User> users = FetchAllUsers();

        if(users.size() == 0)
        {
            System.out.println("\n\nCreating Admins ...");

            UserORMService.GetInstance().Create(new User("admin", "Administrator", "The", "admin", true));
            UserORMService.GetInstance().Create(new User("Wardo", "Eduardo", "Veras", "1234", true));
            UserORMService.GetInstance().Create(new User("EmmJ", "Emmanuel", "Jaquez", "1234", true));
            UserORMService.GetInstance().Create(new User("Djsiclait", "Djidjelly", "Siclait", "1234", true));

            System.out.println("Admins created successfully!\n");
        }
        else
            System.out.println("\n\nDatabase already configured!\n");

    }

    /*
     * Database root Commands : Security/Authentification
     * Admin and Server only
     */
    public static boolean CheckUserCredentials(String username, String password){

        User user = UserORMService.GetInstance().Find(username);

        if(user == null) // User does not exist
            return false;
        else if(user.getPassword().equals(password)) // Password Correct
            return true;
        else // Password Incorrect
            return false;
    }

    public static boolean CheckUserCredentials(String username){

        User user = UserORMService.GetInstance().Find(username);

        if(user == null) // User does not exist
            return false;
        else
            return user.isAdmin();
    }

    // Exclusive to Admin
    public static void MakeAdmin(String username){

        if(!CheckUserCredentials(username)) {

            System.out.println("\n\nMaking new Admin ...");

            User user = UserORMService.GetInstance().Find(username);

            user.setAdmin(true);

            UserORMService.GetInstance().Edit(user);

            System.out.println("Admin created successfully!\n");
        }
        else
            System.out.println("\n\nUser, " + username + ", is already an Administrator!\n");
    }

    // Exclusive to Admin
    public static List<User> FetchAllUsers(){

        return UserORMService.GetInstance().FindAll();
    }

    // User Related Functions
    public static boolean CreateNewUser(String username, String firstName, String lastName, String password){

        try {

            UserORMService.GetInstance().Create(new User(username, firstName, lastName, password, false));
            return true;
        } catch (PersistenceException exp){
            System.out.println("User, " + username + ", already exist\n\n");
            return false;
        }
    }

    // Used for general user
    public static boolean DeleteUserAccount(String username){

        try{

            UserORMService.GetInstance().Delete(UserORMService.GetInstance().Find(username));
            return true;
        } catch (Exception exp){
            System.out.println("This user does not exist\n\n");
            return false;
        }
    }

    // TODO: Add change user password function

}
