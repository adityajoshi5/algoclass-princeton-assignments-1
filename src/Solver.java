import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {

	private final class SearchNode {

		private Board board;
		private SearchNode prevNode;
		private int moves = 0;

		public SearchNode(Board board, SearchNode prevNode, int moves) {
			this.board = board;
			this.prevNode = prevNode;
			this.moves = moves;
		}

		public boolean isGoal() {
			return board.isGoal();
		}

		public Board getBoard() {
			return board;
		}

		public int getMoves() {
			return moves;
		}

		public SearchNode getPrevNode() {
			return prevNode;
		}

		public int getPriority() {
			return board.manhattan();
		}

		public Iterable<SearchNode> neighbors() {
			Iterable<Board> neighbors = board.neighbors();
			Set<SearchNode> searchNodes = new HashSet<SearchNode>();
			for (Board neighborBoard : neighbors) {
				if (prevNode != null
						&& neighborBoard.equals(prevNode.getBoard())) {
					continue;
				}
				searchNodes.add(new SearchNode(neighborBoard, this, moves + 1));
			}
			return searchNodes;
		}

		public String toString() {
			return board.toString();
		}
	}

	private static Comparator<SearchNode> comparator = new Comparator<Solver.SearchNode>() {
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			if (o1 == null || o2 == null) {
				throw new NullPointerException();
			}
			int priority1 = o1.getPriority() + o1.getMoves();
			int priority2 = o2.getPriority() + o2.getMoves();
			if (priority1 > priority2) {
				return 1;
			} else if (priority1 < priority2) {
				return -1;
			} else {
				return 0;
			}
		}
	};

	private SearchNode goalNode;

	public Solver(Board initial) {
		// find a solution to the initial board (using the A* algorithm)
		MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(comparator);
		MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(comparator);

		minPQ.insert(new SearchNode(initial, null, 0));
		minPQTwin.insert(new SearchNode(initial.twin(), null, 0));

		SearchNode currentNode = minPQ.delMin();
		SearchNode currentNodeTwin = minPQTwin.delMin();
		while (!currentNode.isGoal() && !currentNodeTwin.isGoal()) {
			Iterable<SearchNode> neighbors = currentNode.neighbors();
			for (SearchNode neighbor : neighbors) {
				minPQ.insert(neighbor);
			}
			if (!minPQ.isEmpty()) {
				currentNode = minPQ.delMin();
			}

			Iterable<SearchNode> neighborsTwin = currentNodeTwin.neighbors();
			for (SearchNode neighbor : neighborsTwin) {
				minPQTwin.insert(neighbor);
			}
			if (!minPQTwin.isEmpty()) {
				currentNodeTwin = minPQTwin.delMin();
			}
		}

		if (currentNode.isGoal()) {
			goalNode = currentNode;
		} else {
			goalNode = null;
		}
	}

	public boolean isSolvable() { // is the initial board solvable?
		return goalNode != null;
	}

	public int moves() {
		// min number of moves to solve initial board; -1 if no solution
		if (goalNode != null) {
			return goalNode.getMoves();
		} else {
			return -1;
		}
	}

	public Iterable<Board> solution() {
		// sequence of boards in a shortest solution; null if no solution
		if (goalNode == null) {
			return null;
		}

		SearchNode current = goalNode;
		List<Board> solution = new ArrayList<Board>();
		while (current != null) {
			solution.add(current.getBoard());
			current = current.getPrevNode();
		}
		Collections.reverse(solution);
		return solution;
	}

	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}