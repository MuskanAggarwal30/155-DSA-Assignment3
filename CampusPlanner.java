import java.util.*;
import java.util.stream.Collectors;
public class CampusPlanner {
    static class Building {
        int id;
        String name;
        String details;
        public Building(int id, String name, String details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }
        public String toString() {
            return String.format("[%d] %s (%s)", id, name, details);
        }
    }
    static class BSTNode {
        Building data;
        BSTNode left, right;

        BSTNode(Building b) { data = b; }
    }

    static class BinarySearchTree {
        BSTNode root;
        public void insert(Building b) {
            root = insertRec(root, b);
        }
        private BSTNode insertRec(BSTNode node, Building b) {
            if (node == null) return new BSTNode(b);
            if (b.id < node.data.id) node.left = insertRec(node.left, b);
            else if (b.id > node.data.id) node.right = insertRec(node.right, b);
            else {
                node.data = b;
            }
            return node;
        }
        public Building search(int id) {
            BSTNode cur = root;
            while (cur != null) {
                if (id == cur.data.id) return cur.data;
                cur = id < cur.data.id ? cur.left : cur.right;
            }
            return null;
        }
        public List<Building> inorder() {
            List<Building> list = new ArrayList<>();
            inorderRec(root, list);
            return list;
        }
        private void inorderRec(BSTNode node, List<Building> list) {
            if (node == null) return;
            inorderRec(node.left, list);
            list.add(node.data);
            inorderRec(node.right, list);
        }
        public List<Building> preorder() {
            List<Building> list = new ArrayList<>();
            preorderRec(root, list);
            return list;
        }
        private void preorderRec(BSTNode node, List<Building> list) {
            if (node == null) return;
            list.add(node.data);
            preorderRec(node.left, list);
            preorderRec(node.right, list);
        }
        public List<Building> postorder() {
            List<Building> list = new ArrayList<>();
            postorderRec(root, list);
            return list;
        }
        private void postorderRec(BSTNode node, List<Building> list) {
            if (node == null) return;
            postorderRec(node.left, list);
            postorderRec(node.right, list);
            list.add(node.data);
        }
        public int height() {
            return heightRec(root);
        }
        private int heightRec(BSTNode node) {
            if (node == null) return 0;
            return 1 + Math.max(heightRec(node.left), heightRec(node.right));
        }
    }
    static class AVLNode {
        Building data;
        AVLNode left, right;
        int height;
        AVLNode(Building b) {
            data = b;
            height = 1;
        }
    }
    static class AVLTree {
        AVLNode root;
        public void insert(Building b) {
            root = insertRec(root, b);
        }
        private AVLNode insertRec(AVLNode node, Building b) {
            if (node == null) return new AVLNode(b);
            if (b.id < node.data.id) node.left = insertRec(node.left, b);
            else if (b.id > node.data.id) node.right = insertRec(node.right, b);
            else {
                node.data = b;
                return node;
            }
            node.height = 1 + Math.max(height(node.left), height(node.right));
            int balance = getBalance(node);
            if (balance > 1 && b.id < node.left.data.id) return rightRotate(node);
            if (balance < -1 && b.id > node.right.data.id) return leftRotate(node);
            if (balance > 1 && b.id > node.left.data.id) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && b.id < node.right.data.id) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            return node;
        }
        private int height(AVLNode n) { return (n == null) ? 0 : n.height; }
        private int getBalance(AVLNode n) { return (n == null) ? 0 : height(n.left) - height(n.right); }
        private AVLNode rightRotate(AVLNode y) {
            AVLNode x = y.left;
            AVLNode T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = 1 + Math.max(height(y.left), height(y.right));
            x.height = 1 + Math.max(height(x.left), height(x.right));
            return x;
        }
        private AVLNode leftRotate(AVLNode x) {
            AVLNode y = x.right;
            AVLNode T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = 1 + Math.max(height(x.left), height(x.right));
            y.height = 1 + Math.max(height(y.left), height(y.right));
            return y;
        }
        public int height() { return height(root); }
        public List<Building> inorder() { List<Building> l = new ArrayList<>(); inorderRec(root, l); return l; }
        private void inorderRec(AVLNode n, List<Building> l) { if (n == null) return; inorderRec(n.left, l); l.add(n.data); inorderRec(n.right, l); }
        public List<Building> preorder() { List<Building> l = new ArrayList<>(); preorderRec(root, l); return l; }
        private void preorderRec(AVLNode n, List<Building> l) { if (n == null) return; l.add(n.data); preorderRec(n.left,l); preorderRec(n.right,l); }
        public List<Building> postorder() { List<Building> l = new ArrayList<>(); postorderRec(root, l); return l; }
        private void postorderRec(AVLNode n, List<Building> l) { if (n == null) return; postorderRec(n.left,l); postorderRec(n.right,l); l.add(n.data); }
    }
    static class Graph {
        int n; 
        Map<Integer, Integer> idToIndex; 
        List<Building> indexToBuilding;
        List<List<Edge>> adjList;
        double[][] adjMatrix; 
        public Graph(List<Building> buildings) {
            indexToBuilding = new ArrayList<>(buildings);
            idToIndex = new HashMap<>();
            for (int i = 0; i < buildings.size(); i++) idToIndex.put(buildings.get(i).id, i);
            n = buildings.size();
            adjList = new ArrayList<>();
            for (int i = 0; i < n; i++) adjList.add(new ArrayList<>());
            adjMatrix = new double[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) adjMatrix[i][j] = Double.POSITIVE_INFINITY;
        }
        public void addEdge(int idA, int idB, double weight) {
            Integer ia = idToIndex.get(idA);
            Integer ib = idToIndex.get(idB);
            if (ia == null || ib == null) throw new IllegalArgumentException("Invalid building ID for edge");
            adjList.get(ia).add(new Edge(ia, ib, weight));
            adjList.get(ib).add(new Edge(ib, ia, weight)); 
            adjMatrix[ia][ib] = weight;
            adjMatrix[ib][ia] = weight;
        }
        public void printAdjList() {
            System.out.println("Adjacency List:");
            for (int i = 0; i < n; i++) {
                System.out.print(indexToBuilding.get(i).name + " -> ");
                String out = adjList.get(i).stream()
                        .map(e -> String.format("%s(%.1f)", indexToBuilding.get(e.to).name, e.weight))
                        .collect(Collectors.joining(", "));
                System.out.println(out);
            }
        }
        public void printAdjMatrix() {
            System.out.println("\nAdjacency Matrix (weights, INF if no edge):");
            System.out.print("      ");
            for (int i = 0; i < n; i++) System.out.printf("%15s", indexToBuilding.get(i).name);
            System.out.println();
            for (int i = 0; i < n; i++) {
                System.out.printf("%-6s", indexToBuilding.get(i).name);
                for (int j = 0; j < n; j++) {
                    if (Double.isInfinite(adjMatrix[i][j])) System.out.printf("%15s", "INF");
                    else System.out.printf("%15.1f", adjMatrix[i][j]);
                }
                System.out.println();
            }
        }
        public List<Building> BFS(int startId) {
            Integer s = idToIndex.get(startId);
            if (s == null) return Collections.emptyList();
            boolean[] vis = new boolean[n];
            Queue<Integer> q = new LinkedList<>();
            q.add(s); vis[s] = true;
            List<Building> order = new ArrayList<>();
            while (!q.isEmpty()) {
                int u = q.poll();
                order.add(indexToBuilding.get(u));
                for (Edge e : adjList.get(u)) {
                    if (!vis[e.to]) { vis[e.to] = true; q.add(e.to); }
                }
            }
            return order;
        }
        public List<Building> DFS(int startId) {
            Integer s = idToIndex.get(startId);
            if (s == null) return Collections.emptyList();
            boolean[] vis = new boolean[n];
            List<Building> order = new ArrayList<>();
            dfsRec(s, vis, order);
            return order;
        }
        private void dfsRec(int u, boolean[] vis, List<Building> order) {
            vis[u] = true;
            order.add(indexToBuilding.get(u));
            for (Edge e : adjList.get(u)) {
                if (!vis[e.to]) dfsRec(e.to, vis, order);
            }
        }
        public DijkstraResult dijkstra(int sourceId) {
            Integer s = idToIndex.get(sourceId);
            if (s == null) return new DijkstraResult(new double[0], new int[0]);
            double[] dist = new double[n];
            int[] parent = new int[n];
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(parent, -1);
            dist[s] = 0;
            PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.dist));
            pq.add(new Pair(s, 0));
            boolean[] visited = new boolean[n];
            while (!pq.isEmpty()) {
                Pair p = pq.poll();
                int u = p.node;
                if (visited[u]) continue;
                visited[u] = true;
                for (Edge e : adjList.get(u)) {
                    if (dist[e.to] > dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                        parent[e.to] = u;
                        pq.add(new Pair(e.to, dist[e.to]));
                    }
                }
            }
            return new DijkstraResult(dist, parent);
        }
        public List<EdgeDisplay> kruskalMST() {
            List<Edge> edges = new ArrayList<>();
            for (int u = 0; u < n; u++) {
                for (Edge e : adjList.get(u)) {
                    if (u < e.to) edges.add(new Edge(u, e.to, e.weight)); 
                }
            }
            edges.sort(Comparator.comparingDouble(ed -> ed.weight));
            UnionFind uf = new UnionFind(n);
            List<EdgeDisplay> mst = new ArrayList<>();
            for (Edge e : edges) {
                if (uf.find(e.from) != uf.find(e.to)) {
                    uf.union(e.from, e.to);
                    mst.add(new EdgeDisplay(indexToBuilding.get(e.from).name, indexToBuilding.get(e.to).name, e.weight));
                }
            }
            return mst;
        }
        static class Pair { int node; double dist; Pair(int node, double dist){ this.node=node;this.dist=dist; } }
        static class DijkstraResult { double[] dist; int[] parent; DijkstraResult(double[] d, int[] p){ dist=d; parent=p; } }
    }
    static class Edge {
        int from, to;
        double weight;
        public Edge(int from, int to, double weight) { this.from = from; this.to = to; this.weight = weight; }
    }
    static class EdgeDisplay {
        String a, b; double w;
        EdgeDisplay(String a, String b, double w) { this.a=a;this.b=b;this.w=w; }
        public String toString() { return String.format("%s -- %s : %.1f", a, b, w); }
    }
    static class UnionFind {
        int[] parent, rank;
        public UnionFind(int n) { parent = new int[n]; rank = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
        int find(int x) { if (parent[x] != x) parent[x] = find(parent[x]); return parent[x]; }
        void union(int a, int b) {
            a = find(a); b = find(b);
            if (a == b) return;
            if (rank[a] < rank[b]) parent[a] = b;
            else if (rank[a] > rank[b]) parent[b] = a;
            else { parent[b] = a; rank[a]++; }
        }
    }
    static class ExprNode {
        String val;
        ExprNode left, right;
        ExprNode(String v) { val = v; }
    }

    static class ExpressionTree {
        public static ExprNode buildFromPostfix(String[] postfix) {
            Stack<ExprNode> st = new Stack<>();
            Set<String> ops = new HashSet<>(Arrays.asList("+", "-", "*", "/", "^"));
            for (String token : postfix) {
                if (!ops.contains(token)) {
                    st.push(new ExprNode(token));
                } else {
                    ExprNode r = st.pop();
                    ExprNode l = st.pop();
                    ExprNode node = new ExprNode(token);
                    node.left = l; node.right = r;
                    st.push(node);
                }
            }
            return st.isEmpty() ? null : st.pop();
        }
        public static double evaluate(ExprNode node) {
            if (node == null) return 0;
            if (node.left == null && node.right == null) return Double.parseDouble(node.val);
            double L = evaluate(node.left);
            double R = evaluate(node.right);
            switch (node.val) {
                case "+": return L + R;
                case "-": return L - R;
                case "*": return L * R;
                case "/": return L / R;
                case "^": return Math.pow(L, R);
                default: throw new IllegalArgumentException("Unknown operator: " + node.val);
            }
        }
    }
    public static void main(String[] args) {
        List<Building> buildings = Arrays.asList(
                new Building(10, "Admin", "Administration Block"),
                new Building(5, "Library", "Central Library"),
                new Building(20, "CSE", "Computer Science Dept"),
                new Building(3, "HostelA", "Hostel Block A"),
                new Building(7, "Cafeteria", "Main Canteen"),
                new Building(15, "Gym", "Sports Complex")
        );
        BinarySearchTree bst = new BinarySearchTree();
        for (Building b : buildings) bst.insert(b);
        System.out.println("=== Binary Search Tree Traversals ===");
        System.out.println("Inorder: " + joinBuildingList(bst.inorder()));
        System.out.println("Preorder: " + joinBuildingList(bst.preorder()));
        System.out.println("Postorder: " + joinBuildingList(bst.postorder()));
        System.out.println("BST Height: " + bst.height());
        int searchId = 20;
        Building found = bst.search(searchId);
        System.out.println("Search for ID " + searchId + ": " + (found != null ? found : "Not found"));
        AVLTree avl = new AVLTree();
        for (Building b : buildings) avl.insert(b);
        avl.insert(new Building(1, "Gate", "Main Gate"));
        avl.insert(new Building(2, "Parking", "Visitor Parking"));
        avl.insert(new Building(30, "Auditorium", "Event Hall"));
        System.out.println("\n=== AVL Tree Traversals ===");
        System.out.println("Inorder: " + joinBuildingList(avl.inorder()));
        System.out.println("Preorder: " + joinBuildingList(avl.preorder()));
        System.out.println("Postorder: " + joinBuildingList(avl.postorder()));
        System.out.println("AVL Height: " + avl.height());
        System.out.println("BST Height (same data inserted order): " + bst.height()); 
        BinarySearchTree bst2 = new BinarySearchTree();
        List<Building> seq = new ArrayList<>(buildings);
        seq.add(new Building(1, "Gate", "Main Gate"));
        seq.add(new Building(2, "Parking", "Visitor Parking"));
        seq.add(new Building(30, "Auditorium", "Event Hall"));
        for (Building b : seq) bst2.insert(b);
        System.out.println("BST Height (with same inserts as AVL): " + bst2.height());
        List<Building> graphNodes = Arrays.asList(
                new Building(10, "Admin", "Administration Block"),
                new Building(5, "Library", "Central Library"),
                new Building(20, "CSE", "Computer Science Dept"),
                new Building(3, "HostelA", "Hostel Block A"),
                new Building(7, "Cafeteria", "Main Canteen")
        );
        Graph g = new Graph(graphNodes);
        g.addEdge(10, 5, 4.0);  
        g.addEdge(10, 20, 3.0); 
        g.addEdge(5, 7, 6.0);  
        g.addEdge(20, 7, 4.0); 
        g.addEdge(5, 3, 8.0);   
        g.addEdge(3, 7, 2.0);   
        System.out.println("\n=== Campus Graph ===");
        g.printAdjList();
        g.printAdjMatrix();
        System.out.println("\nBFS from Admin:");
        List<Building> bfsOrder = g.BFS(10);
        System.out.println(joinBuildingList(bfsOrder));
        System.out.println("\nDFS from Admin:");
        List<Building> dfsOrder = g.DFS(10);
        System.out.println(joinBuildingList(dfsOrder));
        Graph.DijkstraResult dr = g.dijkstra(10);
        int targetIdx = g.idToIndex.get(3);
        printDijkstraPath(g, dr, g.idToIndex.get(10), targetIdx);
        System.out.println("\nKruskal Minimum Spanning Tree:");
        List<EdgeDisplay> mst = g.kruskalMST();
        double total = 0.0;
        for (EdgeDisplay ed : mst) { System.out.println(ed); total += ed.w; }
        System.out.printf("Total cost of MST: %.1f\n", total);
        String[] postfix = {"20", "5", "*", "10", "4", "*", "+"};
        ExprNode exprRoot = ExpressionTree.buildFromPostfix(postfix);
        double value = ExpressionTree.evaluate(exprRoot);
        System.out.println("\nExpression evaluation (example): (20*5)+(10*4) = " + (int)value);
        System.out.println("\nAll operations completed. Use these outputs & screenshots in your report.");
    }
    static String joinBuildingList(List<Building> list) {
        return list.stream().map(b -> b.name + "(" + b.id + ")").collect(Collectors.joining(" -> "));
    }
    static void printDijkstraPath(Graph g, Graph.DijkstraResult dr, int sourceIdx, int targetIdx) {
        double dist = dr.dist[targetIdx];
        if (Double.isInfinite(dist)) {
            System.out.println("No path found.");
            return;
        }
        List<Integer> path = new ArrayList<>();
        int cur = targetIdx;
        while (cur != -1) {
            path.add(cur);
            cur = dr.parent[cur];
        }
        Collections.reverse(path);
        String nodes = path.stream().map(i -> g.indexToBuilding.get(i).name + "(" + g.indexToBuilding.get(i).id + ")").collect(Collectors.joining(" -> "));
        System.out.printf("\nShortest path (Dijkstra) from %s to %s: %s (Distance: %.1f)\n",
                g.indexToBuilding.get(sourceIdx).name,
                g.indexToBuilding.get(targetIdx).name,
                nodes, dist);
    }
}

    

