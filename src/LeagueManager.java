import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class LeagueManager {

  public static final int MAX_PLAYERS = 11;
  public static final String INDEX_OUT_OF_BOUND = "Index out of bound";
  public static final String EXPERIENCED = "Experienced";
  public static final String INEXPERIENCED = "Inexperienced";
  public static final String ONLY_NUMBER_ARE_ALLOW = "Only numbers are allow";
  public static List<Player> playersToRegister = new ArrayList<>();
  public static List<Player> availablePlayers = new ArrayList<>(Arrays.asList(Players.load()));

  public static void main(String[] args) {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n", players.length);
    List<Team> teams = new ArrayList<>();
    // Your code here!

    String choice = "";

    while(!choice.equals("quit")){
      Scanner scanner = new Scanner(System.in);
      System.out.println("--------------------------");
      System.out.println("Menu");
      System.out.println("Create - Create a new team");
      System.out.println("Add - Add a player to a team");
      System.out.println("Remove - Remove a player to a team");
      System.out.println("Report - View a report of a team by height");
      System.out.println("Balance - View the League Balance Report");
      System.out.println("Roster - View roster");
      System.out.println("Registration - Add player to waiting list");
      System.out.println("Replace - Remove player from the League and replace it with the next player from the waiting list");
      System.out.println("Build - Build fair team automatically");
      System.out.println("Quit - Exits the program");
      System.out.println("--------------------------");

      System.out.print("Select an option: ");
      choice = scanner.nextLine();

      switch (choice) {
        case "create":
          createTeam(teams, scanner);
          break;
        case "add":
          try{
            Team selectedTeam = findTeam(teams, scanner);

            if(selectedTeam == null) {
              System.out.println("Create team first!");
            }else{
              Player player = findPlayer(scanner);
              addPlayerToTeam(selectedTeam, player, teams);
            }
          }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }catch (IndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }catch (NumberFormatException e){
            System.out.println(ONLY_NUMBER_ARE_ALLOW);
          }
          break;
        case "remove":
          try{
            Team selectedTeam = findTeam(teams, scanner);

            if(selectedTeam == null){
              System.out.println("Create team first!");
            }else {
              removePlayerFromTeam(scanner, selectedTeam);
            }
          }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }catch (IndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }
          break;
        case "report":
          try {
            Team selectedTeam = findTeam(teams, scanner);

            if(selectedTeam == null) {
              System.out.println("Create team first!");
            }else{
              displayReport(selectedTeam);
            }
          }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }catch (IndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }
          break;
        case "balance":
          try{
            reportLeagueBalance(teams);
          }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }catch (IndexOutOfBoundsException e) {
            System.out.println(INDEX_OUT_OF_BOUND);
          }
          break;
        case "roster":
          try{
            displayRoster(teams, scanner);
          }catch (NoSuchElementException e) {
            System.out.println("No assigned team for you");
          }
          break;
        case "registration":
          registerPlayer(scanner);
          break;
        case "replace":
          try {
            replacePlayer(scanner);
          }catch (IndexOutOfBoundsException e){
            System.out.println("There are no player in the waiting list");
          }
          break;
        case "build":
          buildTeamAutomatically(availablePlayers, scanner, teams);
          break;
        case "quit":
          System.out.println("Bye!");
      }
    }
  }

  private static void buildTeamAutomatically(List<Player> availablePlayers, Scanner scanner, List<Team> teams ){
    if (availablePlayers.size() < MAX_PLAYERS ){
      System.out.println("No enough players for a new team building...");
      System.out.println("Please select another option.");
      return;
    }

    Collections.shuffle(availablePlayers);

    System.out.print("What is the team name? ");
    String teamName = scanner.nextLine();
    System.out.print("What is the coach name? ");
    String coachName = scanner.nextLine();
    Team team = new Team(teamName, coachName);
    teams.add(team);

    System.out.println("Automatically Team Building...");

    List<Player> availablePlayersCopy = new ArrayList<>(availablePlayers);

    for(Player player : availablePlayersCopy){
      int inches = player.getHeightInInches();
      for(Player teamPlayer : team.getTeamPlayers()){
        inches = inches + teamPlayer.getHeightInInches();
      }
      double averageHeight = inches / (team.getTeamPlayers().size() + 1 );
      if((averageHeight > 42 && averageHeight < 45) && team.getTeamPlayers().size() < MAX_PLAYERS){
        team.getTeamPlayers().add(player);
        availablePlayers.remove(player);
        System.out.printf("Available Players %d%n", availablePlayers.size());
      }
    }
    System.out.printf("Team building for %s completed.%n", team.getName());
  }

  private static void replacePlayer(Scanner scanner) throws IndexOutOfBoundsException {
    System.out.println("Available player: ");
    int i = 0;
    for(Player player : availablePlayers){
      i++;
      System.out.printf("%d) %s %s (%d inches - %s)%n", i, player.getFirstName(), player.getLastName(),
          player.getHeightInInches(), player.isPreviousExperience()?"experienced":"inexperienced");
    }

    System.out.println("Which player do you want to replace from List? ");
    System.out.print("Select an option: ");
    int option = Integer.parseInt(scanner.nextLine());
    Player player = availablePlayers.get(option - 1);
    System.out.printf("%s %s will be replaced by %s %s%n",
        player.getFirstName(), player.getLastName(),
        playersToRegister.get(0).getFirstName(), playersToRegister.get(0).getLastName());
    availablePlayers.set((option - 1), playersToRegister.get(0));
    playersToRegister.remove(0);
  }

  private static void registerPlayer(Scanner scanner) {
    System.out.println("Current players in the waiting list: ");
    playersToRegister.stream().forEach(LeagueManager::displayPlayer);

    System.out.println("Please provide the following information for a new player");
    System.out.print("Firstname: ");
    String firstName = scanner.nextLine();

    System.out.print("Lastname: ");
    String lastName = scanner.nextLine();

    System.out.print("Inches: ");
    int inches = Integer.parseInt(scanner.nextLine());

    System.out.print("Experienced or Inexperienced, Please enter y(es) for Experienced or n(o) for Inexperienced: ");
    boolean isExperienced = (scanner.nextLine().matches("y|yes"))?true:false;

    Player player = new Player(firstName, lastName, inches, isExperienced);
    playersToRegister.add(player);

    System.out.printf("%s %s is in the waiting list%n", firstName, lastName);
  }

  private static void displayRoster(List<Team> teams, Scanner scanner) throws NoSuchElementException {
    System.out.printf("Please enter your name: ");
    String coach = scanner.nextLine();

    Team team = teams.stream()
        .filter(t -> t.getCoach().equals(coach))
        .findFirst()
        .get();

    System.out.printf("Players of the team %s %n", team.getName());
    for(Player player : team.getTeamPlayers()){
      System.out.println(player);
    }
  }

  private static void reportLeagueBalance(List<Team> teams) {
    if(teams.size() == 0) {
      System.out.println("Create team first!");
    }else{
      Collections.sort(teams);
      for(Team team : teams){
        Map<String, Integer> reports = new TreeMap<>();
        int numberExperiencedPlayers = 0;
        int numberInexperiencedPlayers = 0;
        for(Player player : team.getTeamPlayers()){
          if(player.isPreviousExperience()){
            numberExperiencedPlayers++;
          }else {
            numberInexperiencedPlayers++;
          }
        }
        reports.put(EXPERIENCED, numberExperiencedPlayers);
        reports.put(INEXPERIENCED, numberInexperiencedPlayers);
        double percentage = (numberExperiencedPlayers * 100)/team.getTeamPlayers().size();
        System.out.printf("Team %s coached by %s has %d experienced players and %d inexperienced players, %.1f %% of experienced players%n"
            , team.getName(), team.getCoach()
            , reports.get(EXPERIENCED), reports.get(INEXPERIENCED)
            , percentage);
      }
    }
  }

  private static void displayReport(Team selectedTeam) {
    List<Player> playersList = new ArrayList<>(selectedTeam.getTeamPlayers());
    Collections.sort(playersList, Player.PlayerHeightComparator);
    System.out.println("List of players by height");

    long count = playersList.stream()
        .filter((p -> p.getHeightInInches() >= 35 && p.getHeightInInches() <= 40))
        .count();
    System.out.printf("Players in the range 35 - 40 inches: %d players%n", count);
    playersList.stream()
        .filter((p -> p.getHeightInInches() >= 35 && p.getHeightInInches() <= 40))
        .forEach(LeagueManager::displayPlayer);

    count =  playersList.stream()
        .filter((p -> p.getHeightInInches() >= 41 && p.getHeightInInches() <= 46))
        .count();
    System.out.printf("Players in the range 41 - 46 inches: %d players%n", count);
    playersList.stream()
        .filter((p -> p.getHeightInInches() >= 41 && p.getHeightInInches() <= 46))
        .forEach(LeagueManager::displayPlayer);

    count = playersList.stream()
        .filter((p -> p.getHeightInInches() >= 47 && p.getHeightInInches() <= 50))
        .count();
    System.out.printf("Players in the range 47 - 50 inches: %d players%n", count);
    playersList.stream()
        .filter((p -> p.getHeightInInches() >= 47 && p.getHeightInInches() <= 50))
        .forEach(LeagueManager::displayPlayer);
  }

  private static void displayPlayer(Player player){
    System.out.printf("%s %s (%d inches - %s)%n", player.getFirstName(), player.getLastName(),
        player.getHeightInInches(), player.isPreviousExperience()?"experienced":"inexperienced");
  }

  private static void removePlayerFromTeam(Scanner scanner, Team selectedTeam) throws IndexOutOfBoundsException{
    int i = 0;
    for(Player teamPlayer : selectedTeam.getTeamPlayers()){
      i++;
      System.out.printf("%d) %s %s (%d inches - %s)%n", i, teamPlayer.getFirstName(), teamPlayer.getLastName(),
          teamPlayer.getHeightInInches(), teamPlayer.isPreviousExperience()?"experienced":"inexperienced");
    }
    System.out.println("Select an option: ");
    int option = Integer.parseInt(scanner.nextLine());
    List<Player> playersList = new ArrayList<>(selectedTeam.getTeamPlayers());
    Player toRemovePlayer = playersList.get(option - 1);
    selectedTeam.getTeamPlayers().remove(toRemovePlayer);
    availablePlayers.add(toRemovePlayer);
  }

  private static void addPlayerToTeam(Team selectedTeam, Player player, List<Team> teams) {
    if(selectedTeam.getTeamPlayers().contains(player)){
      System.out.println("Player already added!");
      System.out.println("Add another one");
    }else{
      if(selectedTeam.getTeamPlayers().size() < MAX_PLAYERS){

        for(Team team : teams){
          if(team.getTeamPlayers().contains(player)){
            System.out.printf("Player is already assigned to the team %s coached by %s %n", team.getName(), team.getCoach());
            return;
          }
        }
        selectedTeam.getTeamPlayers().add(player);
        availablePlayers.remove(player);
      }else{
        System.out.printf("The team %s has reached the maximum allowed number of player, which is %s%n",
            selectedTeam.getName(),
            MAX_PLAYERS);
      }
    }
  }

  private static Player findPlayer(Scanner scanner) throws ArrayIndexOutOfBoundsException, NumberFormatException{
    int option;
    System.out.println("Available players");
    int i = 0;
    /*Player[] players = Players.load();
    Arrays.sort(players);*/
    Collections.sort(availablePlayers);

    for(Player player: availablePlayers){
      i++;
      System.out.printf("%d) %s %s (%d inches - %s)%n", i, player.getFirstName(), player.getLastName(),
          player.getHeightInInches(), player.isPreviousExperience()?"experienced":"inexperienced");
    }

    System.out.println("Select an option: ");
    option = Integer.parseInt(scanner.nextLine());
    return availablePlayers.get(option -1);
  }

  private static Team findTeam(List<Team> teams, Scanner scanner) throws IndexOutOfBoundsException{
    System.out.println("Available teams: ");

    if(teams.size() == 0){
      return null;
    }else{
      Collections.sort(teams);
      IntStream.rangeClosed(1, teams.size())
          .mapToObj(j -> String.format("%d. %s", j, teams.get(j-1)))
          .forEach(System.out::println);

      System.out.println("Select an option: ");
      int option = Integer.parseInt(scanner.nextLine());
      return teams.get(option - 1);
    }
  }

  private static void createTeam(List<Team> teams, Scanner scanner) {

    if(availablePlayers.size() < MAX_PLAYERS){
      System.out.println("The are not enough player for a new team!");
    }else{
      System.out.print("What is the team name? ");
      String teamName = scanner.nextLine();
      System.out.print("What is the coach name? ");
      String coachName = scanner.nextLine();
      Team team = new Team(teamName, coachName);
      teams.add(team);
      System.out.printf("Team %s coached by %s %n%n", teamName, coachName);
    }
  }
}
