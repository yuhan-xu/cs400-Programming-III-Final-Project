package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class SocialNetwork implements SocialNetworkADT {
 public String centralUser;
 public Person center = new Person (centralUser);
 public String output = "";
 
   
    // Stores the graph of people for the Social Network
    private Graph graph = new Graph();
 public LinkedList cmdList = new LinkedList();
   
    private static final File file = new File("log.txt");

    /**
     * Constructor for the SocialNetwork.
     */
    public SocialNetwork() {
        try {
            if (!file.createNewFile()) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
   
    /**
     * Sets two users as being friends in the Social Network. If either of the users is not
     * in the social network then they are added.
     *
     * @param user1 One of the two people being made friends
     * @param user2 The other person who is being made a friend
     * @return true if the two users are set as friends, false if the two users are already friends
     * @throws IllegalArgumentException if either user is null or the empty string
     * @throws IllegalNameException if either user contains invalid characters
     * @throws DuplicateNameException if the two users are equal
     */
    @Override
    public boolean addFriends(String user1, String user2) throws IllegalArgumentException,
    IllegalNameException, DuplicateNameException {
       
        checkNames(user1, user2);
       
        Person p1 = new Person(user1);
        Person p2 = new Person(user2);
       
        if (graph.addEdge(p1, p2) && graph.addEdge(p2, p1)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            	output += "a " + user1 + " " + user2 +"\n";
                writer.append("a " + user1 + " " + user2 + "\n");
                
            } catch (IOException e) {
                System.out.println("Error writing to log.txt");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
   
    /**
     * Removes the friendship between user1 and user2.
     *
     * @param user1 One of the people being unfriended
     * @param user2 The other user being unfriended
     * @return true if the friendship between user1 and user2 was removed, false if the two users
     *         were not friends
     * @throws IllegalArgumentException if either user is null or the empty string
     * @throws IllegalNameException if either user contains invalid characters
     * @throws DuplicateNameException if the two users are equal
     */
    @Override
    public boolean removeFriends(String user1, String user2) throws IllegalArgumentException,
    IllegalNameException, DuplicateNameException {
       
        checkNames(user1, user2);
       
        Person p1 = graph.getNode(user1);
        Person p2 = graph.getNode(user2);
       
        if (graph.removeEdge(p1, p2) && graph.removeEdge(p2, p1)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            	output += "r " + user1 + " " + user2 +"\n";
                writer.append("r " + user1 + " " + user2 + "\n");
            } catch (IOException e) {
                System.out.println("Error writing to log.txt");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
   
    /**
     * Adds a person to the Social Network.
     *
     * @param user the name of the person being added to the Social Network
     * @return true if the person is added the Social Network, false if the user is already in the
     *         graph
     * @throws IllegalArgumentException if user is null or the empty string
     * @throws IllegalNameException if user contains invalid characters
     */
    @Override
    public boolean addUser(String user) throws IllegalArgumentException, IllegalNameException {
        checkName(user);
       
        if (graph.addNode(new Person(user))) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                writer.append("a " + user + "\n");
                output += "a " + user +"\n";
            } catch (IOException e) {
                System.out.println("Error writing to log.txt");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
   
    /**
     * Removes a person from the Social Network.
     *
     * @param user the person being removed from the social network
     * @return true if the user was successfully removed, false if the user isn't in the Social
     *         Network
     * @throws IllegalArgumentException if user is null or the empty string
     * @throws IllegalNameException if user contains invalid characters
     */
    @Override
    public boolean removeUser(String user) throws IllegalArgumentException, IllegalNameException {
        checkName(user);
       
        Person person = graph.getNode(user);
       
        if (person == null)
            return false;
       
        if (graph.removeNode(person)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            	output += "r " + user +"\n";
                writer.append("r " + user + "\n");
            } catch (IOException e) {
                System.out.println("Error writing to log.txt");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
   
    /**
     * Generates a list of people the specified user is friends with in the Social Network
     *
     * @param user The person whose friends are compiled into a list
     * @return a string array of people who are friends with the passed in user
     * @throws IllegalArgumentException if user is null or the empty string
     * @throws IllegalNameException if user contains invalid characters
     */
    @Override
    public String[] getFriends(String user) throws IllegalArgumentException,
    IllegalNameException  {
       try {
        checkName(user);
       }catch(Exception e) {
        
       }
        // Stores the list of names of people who are friends with user
        List<String> listOfStrings = graph.getNeighbors(graph.getNode(user));
       
        String[] friends = new String[listOfStrings.size()];
       
        // converts the list of strings to a list of person objects
        for (int i = 0; i < friends.length; ++i)  
            friends[i] = listOfStrings.get(i);
       
        return friends;
    }
   
    /**
     * Given two users this a list of people who are friends with both users is returned
     *
     * @param user1 One of the users whose mutual friends are collected
     * @param user2 The other user whose mutual friends are collected
     * @return a list of people who are friends with both user1 and user2
     * @throws IllegalArgumentException if either user is null or the empty string
     * @throws IllegalNameException if either user contains invalid characters
     */
    @Override
    public List<Person> getMutualFriends(String user1, String user2) throws
    IllegalArgumentException, IllegalNameException {
       
        // Stores the list of people who are mutual friends
        List<Person> listOfFriends = new ArrayList<Person>();
        // stores the list of user1's friends as strings
        List<String> listOfStrings1 = graph.getNeighbors(graph.getNode(user1));
        // stores the list of user2's friends as strings
        List<String> listOfStrings2 = graph.getNeighbors(graph.getNode(user2));
        // Stores the list of mutual friends as strings
        List<String> listOfMutualStrings = new ArrayList<String>();
       
        checkName(user1);
        checkName(user2);
     
        // converts the list of strings to a list of person objects
        for (String temp : listOfMutualStrings)
            listOfFriends.add(graph.getNode(temp));
       
        return listOfFriends;
    }
   
   
    @Override
    public List<Graph> getConnectedComponents() {
        List<Graph> list = new ArrayList<Graph>();
        boolean[] visited = new boolean[graph.order()];
        List<String> nodesList = graph.getAllNodes();
        Queue<String> queue = new LinkedList<String>();
        String currentUser;
       
        for (int i = 0; i < nodesList.size(); ++i) {
            if (!visited[i]) {
                Graph currentGraph = new Graph();
                list.add(currentGraph);
                currentGraph.addNode(graph.getNode(nodesList.get(i)));
                queue.add(nodesList.get(i));
               
                List<String> friendsList;
                    currentUser = queue.poll();
                    friendsList = graph.getNeighbors(graph.getNode(currentUser));
                   
                    for (String friend : friendsList) {
                        currentGraph.addEdge(graph.getNode(currentUser), graph.getNode(friend));
                       
                        if (visited[nodesList.indexOf(friend)]) {
                            queue.add(friend);
                       
                    }
                }
            }
        }
       
        return list;
    }
   

 
   
    /**
     * Checks that the provided name is not null, the empty string, and only contains valid
     * characters: letters, digits, underscore, and apostrophe.
     *
     * @param user The name being checked
     * @throws IllegalArgumentException if user is null or the empty string
     * @throws IllegalNameException if user contains invalid characters
     */
    private void checkName(String user) throws IllegalArgumentException, IllegalNameException {
        if (user == null || user.equals(""))
            throw new IllegalArgumentException();
       
        char[] array = user.toCharArray();
       
        for (char c : array)
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '\''))
                throw new IllegalNameException();
    }
   
    /**
     * Checks, in the order provided, that the provided strings are not null, are not the empty
     * string, do not contain invalid characters, and are not the same string.
     *
     * @param user1 One string being checked
     * @param user2 The other string being checked
     * @throws IllegalArgumentException if either string is null or the empty string
     * @throws IllegalNameException if either string contains invalid characters
     * @throws DuplicateNameException if the two strings are equal
     */
    private void checkNames(String user1, String user2) throws IllegalArgumentException,
    IllegalNameException, DuplicateNameException {
        if (user1 == null || user2 == null || user1.equals("") || user2.equals(""))
            throw new IllegalArgumentException();
       
        char[] array1 = user1.toCharArray();
       
        for (char c : array1)
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '\''))
                throw new IllegalNameException();
       
        char[] array2 = user2.toCharArray();
       
        for (char c : array2)
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '\''))
                throw new IllegalNameException();
       
        if (user1.equals(user2))
            throw new DuplicateNameException();
    }

   
    /**
     * Returns a list of all the users (as strings) in the Social Network
     * @return a list of all the users (as strings) in the Social Network
     */
    //@Override
    public List<String> getAllUsers() {
        return graph.getAllNodes();
    }

 @Override
 public List<Person> getShortestPath(String vertex1, String vertex2) {
  // TODO Auto-generated method stub
  return null;
 }
 
 private void setCentralUser(String centralUser) {
     this.centralUser = centralUser;
//     try {
//   this.users = getFriends(centralUser);
//  } catch (IllegalArgumentException e) {
//   // TODO Auto-generated catch block
//   e.printStackTrace();
//  } catch (IllegalNameException e) {
//   // TODO Auto-generated catch block
//   e.printStackTrace();
//  }
   }

   @Override
  public void loadFromFile(File file) {

    try {
      BufferedReader bR = new BufferedReader(new FileReader(file));
      Scanner scnr1;
      String input = "";
      input = bR.readLine();
      while (input != null && !input.isEmpty()) {

        // System.out.println(input);
        scnr1 = new Scanner(input);
        String instruction = scnr1.next();

        switch (instruction) {
          case "s":
            centralUser = scnr1.next();
            output += "s " + centralUser +"\n";
            setCentralUser(centralUser);
            
            break;
          case "a":
            // add to social network
            String f1 = "";
            String f2 = "";
           // if (f2.isEmpty()) {
              try {
                f1 = scnr1.next().trim();
                f2 = scnr1.next().trim();
               
                } catch (IllegalArgumentException | NoSuchElementException e1) {
                  try {
                	  output += "a " + f1 +"\n";
                    addUser(f1);
                    break;
                  } catch (IllegalArgumentException e) {
                    System.out.println("illegal argument");
                  } catch (IllegalNameException e) {
                    System.out.println("illegal name");
                  }
                }
            try {
            	output += "a " + f1 + " " + f2 +"\n";
              addFriends(f1, f2);
            } catch (IllegalArgumentException | IllegalNameException | DuplicateNameException e2) {
              System.out.println("exception caught ");
            }
              
              
             // }
          //  } else {

            /*
             * try { addFriends(f1, f2); } catch (IllegalArgumentException e1) { // TODO
             * Auto-generated catch block e1.printStackTrace(); } catch (IllegalNameException e1) {
             * // TODO Auto-generated catch block e1.printStackTrace(); } catch
             * (DuplicateNameException e1) { // TODO Auto-generated catch block
             * e1.printStackTrace(); // } }
             */
            break;

          case "r":
            // remove
            String f3 = "";
            String f4 = "";
           // if (f4.isEmpty()) {
              try {
                f3 = scnr1.next().trim();
                f4 = scnr1.next().trim();
              } catch (NoSuchElementException e) {
                try {
                	output += "r " + f3 +"\n";
                  removeUser(f3);
                  
                } catch (IllegalArgumentException | IllegalNameException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
                }
              }
            //} else {
              try {
            	  output += "r " + f3 + " " + f4 +"\n";
                removeFriends(f3, f4);
              } catch (IllegalArgumentException | IllegalNameException | DuplicateNameException e) {
                e.printStackTrace();
              }

            //}
        }
        input = bR.readLine();
      }
      bR.close();
    } catch (NullPointerException e) {
      System.out.println("end of file");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

   @Override
   public void saveToFile(File file) {

     try {
       PrintWriter writer = new PrintWriter(file); // create a printwriter to write in file
       writer.print(output);
       writer.flush();
       writer.close();// close the file

     } catch (FileNotFoundException e) {
       e.printStackTrace();
     } catch (Exception e) {
       e.printStackTrace();
     }



   }
   public String getCentralUser() {
    return this.centralUser;
   }

 public int getTotalFriends() {
  return graph.size();
 }
 public int getTotalFriendships() {
  return graph.order();
 }
 public static void main(String[] args) {
     SocialNetwork sN = new SocialNetwork();
    // File file = new File("friends_001.txt");
     //sN.loadFromFile(file);
     //sN.getConnectedComponents();
     try {
	sN.addFriends("you", "me");
	sN.addFriends("him", "her");
	sN.addFriends("they", "us");
	sN.removeFriends("they", "us");
		
	} catch (IllegalArgumentException | IllegalNameException | DuplicateNameException e) {
		e.printStackTrace();
	}
     System.out.println(sN.getTotalFriends()+ " friends");
     System.out.println(sN.getTotalFriendships()+ " friendships");
   }
 
}