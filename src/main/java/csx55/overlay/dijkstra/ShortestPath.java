package csx55.overlay.dijkstra;

import csx55.overlay.wireformats.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ShortestPath {
    Map<MessagingNodeInfo, List<Edge>> graph;
    Map<MessagingNodeInfo, Integer> distances;
    Map<MessagingNodeInfo, MessagingNodeInfo> predecessors;

    public ShortestPath(LinkInfo[] links) {
        graph = new HashMap<>();
        distances = new HashMap<>();
        predecessors = new HashMap<>();

        for (LinkInfo link : links) {
            MessagingNodeInfo nodeA = new MessagingNodeInfo(link.getHostnameA(), link.getPortA());
            MessagingNodeInfo nodeB = new MessagingNodeInfo(link.getHostnameB(), link.getPortB());

            graph.putIfAbsent(nodeA, new ArrayList<>());
            graph.putIfAbsent(nodeB, new ArrayList<>());

            graph.get(nodeA).add(new Edge(nodeB, link.getWeight()));
        }
    }

    public Map<MessagingNodeInfo, MessagingNodeInfo> computeShortestPaths(MessagingNodeInfo source) {
        Queue<PriorityNode> queue = new PriorityQueue<>();

        distances.put(source, 0);

        for (MessagingNodeInfo vertex : graph.keySet()) {
            if (!vertex.equals(source)) {
                distances.put(vertex, Integer.MAX_VALUE);
                predecessors.put(vertex, null);
            }
            queue.add(new PriorityNode(vertex, 0));
        }

        while (!queue.isEmpty()) {
            MessagingNodeInfo u = queue.remove().get();
            for (Edge edge : graph.get(u)) {
                int alt = distances.get(u) + edge.weight;
                if (alt < distances.get(edge.vertex)) {
                    distances.put(edge.vertex, alt);
                    predecessors.put(edge.vertex, u);
                    queue.add(new PriorityNode(edge.vertex, alt));
                }
            }
        }

        return predecessors;
    }
}

class Edge {
    final public MessagingNodeInfo vertex;
    final public int weight;

    Edge(MessagingNodeInfo node, int weight) {
        this.vertex = node;
        this.weight = weight;
    }
}

class PriorityNode implements Comparable<PriorityNode> {
    MessagingNodeInfo wrappedNode;
    int nodePriority;

    public PriorityNode(MessagingNodeInfo node, int priority) {
        wrappedNode = node;
        nodePriority = priority;
    }

    @Override
    public int compareTo(PriorityNode o) {
        return Integer.compare(nodePriority, o.nodePriority);
    }

    public MessagingNodeInfo get() {
        return wrappedNode;
    }
}
