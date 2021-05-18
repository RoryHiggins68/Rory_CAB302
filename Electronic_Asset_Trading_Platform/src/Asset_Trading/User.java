package Asset_Trading;

/**
 * A class representing the individual users within the organisation
 *
 *
 //* @param firstName the users first name
 //* @param lastName the users last name
 //* @param userName the user name for logging in to the market place
 //* @param team the team that this user is a part of
 */

public class User {

    String firstName;
    String lastName;
    String userName;
    String team;
//Temporary password variable for testing real password to be stored in data base not in plain text
    String password;

    public User(String firstName, String lastName, String userName,String team)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.team = team;
        this.password = "admin";


    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeam() {

        return team;

    }

    public String setTeam(String team) {
        this.team = team;
        return team;
    }
}
