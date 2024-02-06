package csx55.overlay.dijkstra;

import csx55.overlay.wireformats.*;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ShortestPath {
    Map<MessagingNodeInfo, Integer> distances;
    Map<MessagingNodeInfo, MessagingNodeInfo> predecessors;
    Graph graph;

    public ShortestPath(LinkInfo[] links) {
        graph = new Graph(links);
        distances = new HashMap<>();
        predecessors = new HashMap<>();
    }

    public Map<MessagingNodeInfo, MessagingNodeInfo> computeShortestPaths(MessagingNodeInfo source) {
        distances.put(source, 0);

        Queue<PriorityNode> queue = new PriorityQueue<>();

        for (MessagingNodeInfo vertex : graph.vertices()) {
            if (!vertex.equals(source)) {
                distances.put(vertex, Integer.MAX_VALUE);
                predecessors.put(vertex, null);
            }
            queue.add(new PriorityNode(vertex, distances.get(vertex)));
        }

        while (!queue.isEmpty()) {
            MessagingNodeInfo u = queue.remove().get();
            for (Graph.Edge edge : graph.adjacencyList(u)) {
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
