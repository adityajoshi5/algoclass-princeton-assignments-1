import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Brute {

	private static final String SEPARATOR = " -> ";

	public static void main(String[] args) {
		String filename = args[0];

		In in = new In(filename);
		int N = in.readInt();
		Collection<Point> points = new ArrayList<Point>(N);
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points.add(new Point(x, y));
		}

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		Collection<Point[]> lines = new ArrayList<Point[]>();
		for (Point p : points) {
			for (Point q : points) {
				if (p.slopeTo(q) == Double.NEGATIVE_INFINITY) {
					continue;
				}
				for (Point r : points) {
					if (p.slopeTo(r) == Double.NEGATIVE_INFINITY
							|| q.slopeTo(r) == Double.NEGATIVE_INFINITY) {
						continue;
					}
					for (Point s : points) {
						if (p.slopeTo(s) == Double.NEGATIVE_INFINITY
								|| q.slopeTo(s) == Double.NEGATIVE_INFINITY
								|| r.slopeTo(s) == Double.NEGATIVE_INFINITY) {
							continue;
						}

						if (p.slopeTo(q) == p.slopeTo(r)
								&& p.slopeTo(q) == p.slopeTo(s)) {
							Point[] line = new Point[] { p, q, r, s };
							Arrays.sort(line);
							if (!lineDrawn(line, lines)) {
								line[0].drawTo(line[line.length - 1]);
								lines.add(line);
								StringBuilder sb = new StringBuilder();
								for (int i = 0; i < line.length; i++) {
									sb.append(line[i].toString());
									if (i < line.length - 1) {
										sb.append(SEPARATOR);
									}
								}
								StdOut.println(sb.toString());
							}
						}
					}
				}
			}
		}
	}

	private static boolean lineDrawn(Point[] line, Collection<Point[]> lines) {
		for (Point[] l : lines) {
			boolean equal = true;
			for (int i = 0; i < line.length; i++) {
				if (line[i].slopeTo(l[i]) != Double.NEGATIVE_INFINITY) {
					equal = false;
					break;
				}
			}
			if (equal) {
				return true;
			}
		}
		return false;
	}

}
