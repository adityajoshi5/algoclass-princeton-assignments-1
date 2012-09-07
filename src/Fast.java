import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fast {

	private static final String SEPARATOR = " -> ";

	public static void main(String[] args) {
		String filename = args[0];

		In in = new In(filename);
		int N = in.readInt();
		List<Point> points = new ArrayList<Point>(N);
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points.add(new Point(x, y));
		}

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);

		Map<String, Point[]> lines = new HashMap<String, Point[]>();
		List<Point> sortablePoints = new ArrayList<Point>(points);
		for (Point p : points) {
			Collections.sort(sortablePoints, p.SLOPE_ORDER);
			double prevSlope = Double.NaN;
			List<Point> collinearPoints = new ArrayList<Point>();
			collinearPoints.add(p);
			for (int i = 1; i < N - 1; i++) {
				Point p1 = sortablePoints.get(i - 1);
				Point p2 = sortablePoints.get(i);
				Point p3 = sortablePoints.get(i + 1);
				double p1Slope = p.slopeTo(p1);
				double p2Slope = p.slopeTo(p2);
				double p3Slope = p.slopeTo(p3);
				if (p1Slope == p2Slope && p2Slope == p3Slope) {
					if (p3Slope == prevSlope) {
						collinearPoints.add(p3);
					} else {
						if (collinearPoints.size() > 3) {
							drawLine(collinearPoints, lines);
							collinearPoints.clear();
							collinearPoints.add(p);
						}
						collinearPoints.add(p1);
						collinearPoints.add(p2);
						collinearPoints.add(p3);
						prevSlope = p3Slope;
					}
				}
			}
			if (collinearPoints.size() > 3) {
				drawLine(collinearPoints, lines);
			}
			p.draw();
		}

		for (String lineStr : lines.keySet()) {
			Point[] line = lines.get(lineStr);
			line[0].drawTo(line[line.length - 1]);
			StdOut.println(lineStr);
		}
	}

	private static void drawLine(List<Point> collinearPoints,
			Map<String, Point[]> lines) {
		Collections.sort(collinearPoints);
		Point[] line = new Point[collinearPoints.size()];
		for (int i = 0; i < collinearPoints.size(); i++) {
			line[i] = collinearPoints.get(i);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length; i++) {
			sb.append(line[i].toString());
			if (i < line.length - 1) {
				sb.append(SEPARATOR);
			}
		}
		lines.put(sb.toString(), line);
	}

}
