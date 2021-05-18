package Asset_Trading;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the individual teams within the organisation
 */

public class Teams {

    String teamName;
    User teamLeader;
    int credits;
    List<User> teamMembers = new ArrayList<>();

    public Teams(String teamName){
        this.teamName = teamName;
        this.credits = 0;

    }

    public List<User> GetTeamMembers(){

        return teamMembers;
    }

    public List<User> AddTeamMembers(User user){
        teamMembers.add(user);
        return teamMembers;
    }

    public String GetTeamName() {
        return teamName;
    }

    public String SetTeamName(String name) {
        teamName = name;
        return teamName;
    }

    public String getTeamName() {

        return teamName;
    }

    public int getCredits(){
        return credits;
    }

    public int addCredits(int credit) {
        credits = credits + credit;
        return credits;
    }

    public int deductCredits(int credit){
        credits = credits - credit;
        return credits;

    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public User getTeamLeader() {
        return teamLeader;
    }
}
