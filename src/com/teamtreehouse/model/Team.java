package com.teamtreehouse.model;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class Team implements Comparable, Serializable {

  private String name;
  private String coach;
  private SortedSet<Player> teamPlayers = new TreeSet<>();

  public Team(String name, String coach) {
    this.name = name;
    this.coach = coach;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCoach() {
    return coach;
  }

  public void setCoach(String coach) {
    this.coach = coach;
  }

  public SortedSet<Player> getTeamPlayers() {
    return teamPlayers;
  }

  public void setTeamPlayers(SortedSet<Player> teamPlayers) {
    this.teamPlayers = teamPlayers;
  }



  @Override
  public String toString() {
    return "Team{" +
        "name='" + name + '\'' +
        ", coach='" + coach + '\'' +
        //", teamPlayers=" + teamPlayers +
        '}';
  }

  @Override
  public int compareTo(Object o) {
    Team team = (Team)o;
    if(equals(team)){
      return 0;
    }
    return name.compareTo(((Team) o).name);
  }
}
