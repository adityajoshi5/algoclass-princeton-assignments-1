
public class Percolation {

	private int gridSize;

	private WeightedQuickUnionUF linearGrid;
	private WeightedQuickUnionUF linearGridNoBackwash;

	private boolean[][] grid;

	public Percolation(int N) {
		// create N-by-N grid, with all sites blocked
		this.gridSize = N;
		linearGrid = new WeightedQuickUnionUF(gridSize * gridSize + 2);
		linearGridNoBackwash = new WeightedQuickUnionUF(gridSize * gridSize + 2);
		grid = new boolean[gridSize][gridSize];
	}

	public void open(int i, int j) {
		// open site (row i, column j) if it is not already
		validate(i, j);
		if (!isOpen(i, j)) {
			grid[i - 1][j - 1] = true;

			unite(i, j, i - 1, j);
			unite(i, j, i + 1, j);
			unite(i, j, i, j - 1);
			unite(i, j, i, j + 1);

			if (i == 1) { // connect to virtual top site
				linearGrid.union(0, xyTo1D(i, j));
				linearGridNoBackwash.union(0, xyTo1D(i, j));
			} 
			if (i == gridSize) { // connect to virtual bottom site
				linearGrid.union(1, xyTo1D(i, j));
			}
		}
	}

	public boolean isOpen(int i, int j) {
		// is site (row i, column j) open?
		validate(i, j);
		return grid[i - 1][j - 1];
	}

	public boolean isFull(int i, int j) {
		// is site (row i, column j) full?
		validate(i, j);
		return linearGridNoBackwash.connected(0, xyTo1D(i, j));
	}

	public boolean percolates() {
		// does the system percolate?
		return linearGrid.connected(0, 1);
	}

	private int xyTo1D(int i, int j) {
		validate(i, j);
		return (i - 1) * gridSize + j + 1;
	}

	private void unite(int i, int j, int m, int n) { // 1-based coordinates
		if (m > 0 && n > 0 && m <= gridSize && n <= gridSize && isOpen(m, n)) {
			linearGrid.union(xyTo1D(i, j), xyTo1D(m, n));
			linearGridNoBackwash.union(xyTo1D(i, j), xyTo1D(m, n));
		}
	}

	private void validate(int i, int j) {
		int x = i - 1;
		int y = j - 1;
		if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
			throw new IndexOutOfBoundsException(String.format(
					"Indexes i=%s and j=%s are out of bounds!", i, j));
		}
	}
}