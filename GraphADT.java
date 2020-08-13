package application;
import java.util.List;

import java.util.Set;

public interface GraphADT {

	/**
	 * 
	 * Add the edge from person1 to person2 to this graph. (edge is directed and
	 * 
	 * unweighted)
	 * 
	 * 
	 * 
	 * If either person does not exist, person IS ADDED and then edge is
	 * created. No
	 * 
	 * exception is thrown.
	 *
	 * 
	 * 
	 * If the edge exists in the graph, no edge is added and no exception is
	 * thrown.
	 * 
	 * 
	 * 
	 * Valid argument conditions: 1. neither person is null 2. both vertices are
	 * in
	 * 
	 * the graph 3. the edge is not in the graph
	 * 
	 * 
	 * 
	 * @param person1
	 *            the first person (src)
	 * 
	 * @param person2
	 *            the second person (dst)
	 * 
	 */

	public boolean addEdge(Person person1, Person person2);

	/**
	 * 
	 * Remove the edge from person1 to person2 from this graph. (edge is
	 * directed
	 * 
	 * and unweighted) If either person does not exist, or if an edge from
	 * person1
	 * 
	 * to person2 does not exist, no edge is removed and no exception is thrown.
	 * 
	 * 
	 * 
	 * Valid argument conditions: 1. neither person is null 2. both vertices are
	 * in
	 * 
	 * the graph 3. the edge from person1 to person2 is in the graph
	 * 
	 * 
	 * 
	 * @param person1
	 *            the first person
	 * 
	 * @param person2
	 *            the second person
	 * 
	 */

	public boolean removeEdge(Person person1, Person person2);

	/**
	 * 
	 * Add new person to the graph.
	 *
	 * 
	 * 
	 * If person is null or already exists, method ends without adding a person
	 * or
	 * 
	 * throwing an exception.
	 * 
	 * 
	 * 
	 * Valid argument conditions: 1. person is non-null 2. person is not already
	 * in
	 * 
	 * the graph
	 * 
	 * 
	 * 
	 * @param person
	 *            the person to be added
	 * 
	 */

	public boolean addNode(Person person);

	/**
	 * 
	 * Remove a person and all associated edges from the graph.
	 * 
	 * 
	 * 
	 * If person is null or does not exist, method ends without removing a
	 * person,
	 * 
	 * edges, or throwing an exception.
	 * 
	 * 
	 * 
	 * Valid argument conditions: 1. person is non-null 2. person is not already
	 * in
	 * 
	 * the graph
	 * 
	 * 
	 * 
	 * @param person
	 *            the person to be removed
	 * 
	 */

	public boolean removeNode(Person person);

	/**
	 * 
	 * Get all the neighbor (adjacent-dependencies) of a person
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param person
	 *            the specified person
	 * 
	 * @return an List<Person> of all the adjacent vertices for specified person
	 * 
	 */

	public List<String> getNeighbors(Person person);

	/**
	 * 
	 * Returns a Set that contains all the vertices
	 * 
	 * 
	 * 
	 * @return a Set<Person> which contains all the vertices in the graph
	 * 
	 */

	public Person getNode(String person);

	/**
	 * 
	 * Returns a Set that contains all the vertices
	 * 
	 * 
	 * 
	 * @return a Set<Person> which contains all the vertices in the graph
	 * 
	 */

	public List<String> getAllNodes();

}
