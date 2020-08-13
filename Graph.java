package application;

import java.util.ArrayList;
import java.util.List;

public class Graph implements GraphADT {
    public ArrayList<GraphNode> vertexList;
    private int size;//number of edges
    private int numNodes;
   
    /**
     * Class representing a graph node in the graph
     *
     *
     */
    private class GraphNode {
        Person user;
        // Stores the graphnodes that the current graph node points to
        ArrayList<GraphNode> next;
       
        /**
         * Creates a vertex with the given value
         *
         * @param vertex the value stored in the graph node
         */
        private GraphNode(Person user) {
        	
            this.user = user;
            this.next = new ArrayList<GraphNode>();
        }
    }
   
    /**
     * Default constructor
     */
    public Graph() {
    	this.vertexList = new ArrayList<GraphNode>();
    	this.size = 0;
    }

    /**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or
     * throwing an exception.
     *
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph
     */
    public boolean addNode(Person user) {
        // if indexOfVertex == 1 then there is already a graphnode in the graph with the graph
        // with the value to be added
        if (user == null || findTheIndexOfPerson(user) != -1) {
        	return false;
        }
            
        GraphNode newNode = new GraphNode(user);
       
        vertexList.add(newNode);
        //numNodes++;
        return true;
    }

    /**
     * Remove a vertex and all associated
     * edges from the graph.
     *
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges,
     * or throwing an exception.
     *
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph
     */
    public boolean removeNode(Person user) {
        int index = findTheIndexOfPerson(user);
        if (index == -1) {
            return false;
        }else {
        GraphNode node = vertexList.get(index);
        vertexList.remove(index);
        //size -= node.next.size();
        int arraySize = vertexList.size();
        for (int i = 0; i < arraySize; ++i) {
            if (vertexList.get(i).next.remove(node)) {
                //--numNodes;
            }
        }
       
        return true;
        }
    }

    /**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     *
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph
     * 3. the edge is not in the graph
     */
    public boolean addEdge(Person user1, Person user2) {
        if (user1 == null || user2 == null)
            return false;
        // variables used to determine if either vertex value needs to be added to the graph
        int index1 = findTheIndexOfPerson(user1);
       
        // if vertex1 is not in the graph then it is added and it's index is updated
        if (findTheIndexOfPerson(user1) == -1) {
            vertexList.add(new GraphNode(user1));
            index1 = vertexList.size() - 1;
        }
       
        int index2 = findTheIndexOfPerson(user2);
        // adds vertex2 to the graph if it is not already present
        if (index2 == -1) {
            vertexList.add(new GraphNode(user2));
            index2 = vertexList.size() - 1;
        }
       
        GraphNode node1 = vertexList.get(index1);
        GraphNode node2 = vertexList.get(index2);
        // adds edge from vertex1 to vertex2 if one isn't already there
        if (!(node1.next.contains(node2))) {
            node1.next.add(node2);
            size+=1;
            return true;
        }
       
        return false;
    }
   
    /**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     *
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
    public boolean removeEdge(Person user1, Person user2) {
        int index1 = findTheIndexOfPerson(user1);
        int index2 = findTheIndexOfPerson(user2);
        // if either vertex is null or not in the graph then nothing is done
        if (index1 == -1 || index2 == -1)
            return false;
       
        GraphNode node1 = vertexList.get(index1);
        GraphNode node2 = vertexList.get(index2);
        // decrements size if an edge is removed
        if (node1.next.remove(node2)) {
            size-=1;
            return true;
        }
       
        return false;
    }  

    /**
     * Returns a Set that contains all the vertices
     *
     */
    public List<String> getAllNodes() {
        // list used for storing the names of the graph
        List<String> list = new ArrayList<String>();
        // moves the names from the graph to list
        for (int i = 0; i < vertexList.size(); ++i)
            list.add(vertexList.get(i).user.getName());
       
        return list;
    }

    /**
     * Get all the neighbor (adjacent) vertices of a vertex
     *
     */
    public List<String> getNeighbors(Person user) {
        // stores the user's next
        List<String> list = new ArrayList<String>();
        // stores the index of the user in the graph
        int index = findTheIndexOfPerson(user);
        // if user is null or not in the graph then an empty list is returned
        if (index == -1)
            return list;
       
        GraphNode node = vertexList.get(index);
        // copies the names in the graphnode's successor arraylist to the list
        for (int i = 0; i < node.next.size(); ++i)
            list.add(node.next.get(i).user.getName());
       
        return list;
    }
   
    /**
     * Returns the number of edges in this graph.
     */
    public int size() {
        return this.size/2;
    }

    /**
     * Returns the number of vertices in this graph.
     */
    public int order() {
        return vertexList.size();
    }
   
    /**
     * Returns the index in vertexList of the GraphNode with the specified person. If the GraphNode
     * is not in vertexList or the person is null then -1 is returned.
     *
     * @param vertex the vertex being searched for
     * @return the index of the GraphNode with the specified vertex if it is in vertexList,
     *         -1 otherwise.
     */
    private int findTheIndexOfPerson(Person user) {
        if (user == null) {
            return -1;
        }
        for (int i = 0; i < vertexList.size(); i++) {
          String s1 = vertexList.get(i).user.name;
            if (user.name.equals(s1)) {
                return i;
            }
        }
       
        return -1;
    }

    @Override
    public Person getNode(String user) {
        if (user == null)
            return null;
       
        int index = findTheIndexOfPerson(new Person(user));
       
        if (index == -1)
            return null;
       
        return vertexList.get(index).user;
    }
}