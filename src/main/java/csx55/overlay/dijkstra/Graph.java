package csx55.overlay.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csx55.overlay.wireformats.LinkInfo;
import csx55.overlay.wireformats.MessagingNodeInfo;

public class Graph {
    public class Edge {
        final public MessagingNodeInfo vertex;
        final public int weight;

        public Edge(MessagingNodeInfo node, int weight) {
            this.vertex = node;
            this.weight = weight;
        }
    }

    private Map<MessagingNodeInfo, List<Edge>> graph;

    public Graph(LinkInfo[] links) {
        graph = new HashMap<>();

        for (LinkInfo link : links) {
            MessagingNodeInfo nodeA = new MessagingNodeInfo(link.getHostnameA(), link.getPortA());
            MessagingNodeInfo nodeB = new MessagingNodeInfo(link.getHostnameB(), link.getPortB());

            graph.putIfAbsent(nodeA, new ArrayList<>());
            graph.putIfAbsent(nodeB, new ArrayList<>());

            graph.get(nodeA).add(new Edge(nodeB, link.getWeight()));
        }
    }

    public Set<MessagingNodeInfo> vertices() {
        return graph.keySet();
    }

    public List<Edge> adjacencyList(MessagingNodeInfo u) {
        return graph.get(u);
    }

    public Edge edge(MessagingNodeInfo u, MessagingNodeInfo v) {
        for (Edge edge : adjacencyList(u)) {
            if (edge.vertex.equals(v)) {
                return edge;
            }
        }

        return null;
    }
}
