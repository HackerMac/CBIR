package match;

/**
 * 几种特征度量方法
 * 
 */
public class FeatureMeasure {
	/**
	 * 欧几里得距离度量
	 * 
	 * @param vector1
	 *            数组1
	 * @param vector2
	 *            数组2
	 * @return
	 */
	public static double euclideanDistance(double[] vector1, double[] vector2) {
		double distance = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				double temp = (vector1[i] - vector2[i])
						* (vector1[i] - vector2[i]);
				distance += temp;
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * 欧几里得距离度量
	 * 
	 * @param vector1
	 *            数组1
	 * @param vector2
	 *            数组2
	 * @return
	 */
	public static double euclideanDistance2(double[] vector1, double[] vector2) {
		double distance = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length - 1; i++) {// 先将离心率去掉观察
				double temp = (vector1[i] - vector2[i])
						* (vector1[i] - vector2[i]);
				distance += temp;

			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * 欧几里得距离度量
	 * 
	 * @param vector1
	 *            数组1
	 * @param vector2
	 *            数组2
	 * @return
	 */
	public static double euclideanDistance(double[][] vector1,
			double[][] vector2) {
		double distance = 0;
		if ((vector1.length == vector2.length)
				& (vector1[0].length == vector2[0].length)) {
			for (int i = 0; i < vector1.length; i++) {
				for (int j = 0; j < vector1[0].length; j++) {
					double temp = (vector1[i][j] - vector2[i][j])
							* (vector1[i][j] - vector2[i][j]);
					distance += temp;
				}
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}

	/**
	 * 相交法度量
	 * 
	 * @param vector1
	 *            数组1
	 * @param vector2
	 *            数组2
	 * @return
	 */
	public static double intersectDistance(double[] vector1, double[] vector2) {
		double distance = 0, sum = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				distance += Math.min(vector1[i], vector2[i]);
				sum += vector1[i];
			}
			distance = distance / sum;
			// if(distance == 1.0){
			// distance = 0;
			// }
		}
		return 1 - distance;
	}

	/**
	 * 中心距法度量
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static double centerMomentDistance(float[][] vector1,
			float[][] vector2) {
		// 假设权重均为1/3
		double distance = 0;

		for (int i = 0; i < vector1.length; i++) {
			distance += 1.0 / 3 * Math.pow(vector1[0][i] - vector2[0][i], 2)
					+ 1.0 / 3 * Math.pow(vector1[1][i] - vector2[1][i], 2)
					+ 1.0 / 3 * Math.pow(vector1[2][i] - vector2[3][i], 2);
		}
		distance = Math.sqrt(distance);
		// System.out.println(distance);

		return distance;
	}

	/**
	 * 夹角余弦衡量 余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，夹角等于0，即两个向量相等，这就叫"余弦相似性"。
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static double Cosine(double[] vector1, double[] vector2) {
		double distance = 0, one = 0, two = 0, three = 0;
		if (vector1.length == vector2.length) {
			for (int i = 0; i < vector1.length; i++) {
				one += vector1[i] * vector2[i];
				two += vector1[i] * vector1[i];
				three += vector2[i] * vector2[i];
			}
			distance = 1 - one / (Math.sqrt(two) * Math.sqrt(three));
			if (distance > -0.0000000001 && distance < 0.00000000001) {// 判断是不是0
				distance = 0;
			}

		}
		return distance;
	}

}
