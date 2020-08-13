package application;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import java.util.Set;

public interface SocialNetworkADT {

/**
*
* Add the edge from vertex1 to vertex2
*
* to this graph. (edge is directed and unweighted)
*
*
*
* If either vertex does not exist,
*
* VERTEX IS ADDED and then edge is created.
*
* No exception is thrown.
*
*
*
* If the edge exists in the graph,
*
* no edge is added and no exception is thrown.
*
*
*
* Valid argument conditions:
*
* 1. neither vertex is null
*
* 2. both vertices are in the graph
*
* 3. the edge is not in the graph
*
*
*
* @param vertex1
*            the first vertex (src)
*
* @param vertex2
*            the second vertex (dst)
 * @throws DuplicateNameException 
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public boolean addFriends(String vertex1, String vertex2) throws IllegalArgumentException, IllegalNameException, DuplicateNameException;

/**
*
* Remove the edge from vertex1 to vertex2
*
* from this graph. (edge is directed and unweighted)
*
* If either vertex does not exist,
*
* or if an edge from vertex1 to vertex2 does not exist,
*
* no edge is removed and no exception is thrown.
*
*
*
* Valid argument conditions:
*
* 1. neither vertex is null
*
* 2. both vertices are in the graph
*
* 3. the edge from vertex1 to vertex2 is in the graph
*
*
*
* @param vertex1
*            the first vertex
*
* @param vertex2
*            the second vertex
 * @throws DuplicateNameException 
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public boolean removeFriends(String vertex1, String vertex2) throws IllegalArgumentException, IllegalNameException, DuplicateNameException;

/**
*
* Add new vertex to the graph.
*
*
*
* If vertex is null or already exists,
*
* method ends without adding a vertex or
*
* throwing an exception.
*
*
*
* Valid argument conditions:
*
* 1. vertex is non-null
*
* 2. vertex is not already in the graph
*
*
*
* @param vertex
*            the vertex to be added
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public boolean addUser(String vertex) throws IllegalArgumentException, IllegalNameException;

/**
*
* Remove a vertex and all associated
*
* edges from the graph.
*
*
*
* If vertex is null or does not exist,
*
* method ends without removing a vertex, edges,
*
* or throwing an exception.
*
*
*
* Valid argument conditions:
*
* 1. vertex is non-null
*
* 2. vertex is not already in the graph
*
*
*
* @param vertex
*            the vertex to be removed
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public boolean removeUser(String vertex) throws IllegalArgumentException, IllegalNameException;

/**
*
* Returns a Set that contains all the friends
*
*
*
* @return a Set<String> which contains all the vertices in the graph
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public String[] getFriends(String vertex) throws IllegalArgumentException, IllegalNameException;

/**
*
* Returns a Set that contains all the mutual friends
*
*
*
* @return a Set<String> which contains all the vertices in the graph
 * @throws IllegalNameException 
 * @throws IllegalArgumentException 
*
*/

public List<Person> getMutualFriends(String vertex1, String vertex2) throws IllegalArgumentException, IllegalNameException;

/**
*
* Returns a List that contains all the friends on the shortest
*
*
*
* @return a List<String> which contains all the vertices in the graph
*
*/

/**
* Find the shortest path between two people
*
* @param vertex1
*            name of the first person
* @param vertex2
*            name of the second person
* @return the list of people on the path
*/
public List<Person> getShortestPath(String vertex1, String vertex2);

/**
* Find the connected components
*
* @return a set of graph
*/
public List<Graph> getConnectedComponents();

/**
* Load from an existing file
*
* @param file
*            existing file
 * @throws FileNotFoundException 
*/
public void loadFromFile(File file) throws FileNotFoundException;

/**
* Save contents to a specific file
*
* @param file
*            the specific file
*/
public void saveToFile(File file);



}
