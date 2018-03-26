package com.teamtreehouse.model;

import java.io.Serializable;
import java.util.Comparator;

public class Player implements Comparable<Player>, Serializable {
  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName;
  private int heightInInches;
  private boolean previousExperience;

  public Player(String firstName, String lastName, int heightInInches, boolean previousExperience) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.heightInInches = heightInInches;
    this.previousExperience = previousExperience;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getHeightInInches() {
    return heightInInches;
  }

  public boolean isPreviousExperience() {
    return previousExperience;
  }

  @Override
  public int compareTo(Player other) {
    // We always want to sort by last name then first name
    Player player = other;
    if(equals(player)){
      return 0;
    }

    if(lastName.equals(player.lastName)){
      return firstName.compareTo(player.firstName);
    }

    return lastName.compareTo(player.lastName);
  }

  public static Comparator<Player> PlayerHeightComparator
      = new Comparator<Player>() {
    public int compare(Player player1, Player player2) {

      //ascending order
      return player1.heightInInches - player2.heightInInches;

      //descending order
      //return
    }

  };


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Player)) return false;

    Player player = (Player) o;

    if (heightInInches != player.heightInInches) return false;
    if (previousExperience != player.previousExperience) return false;
    if (!firstName.equals(player.firstName)) return false;
    return lastName.equals(player.lastName);

  }

  @Override
  public int hashCode() {
    int result = firstName.hashCode();
    result = 31 * result + lastName.hashCode();
    result = 31 * result + heightInInches;
    result = 31 * result + (previousExperience ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Player{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", heightInInches=" + heightInInches +
        ", previousExperience=" + previousExperience +
        '}';
  }
}
