package ninjabrainbot.data.calculator.endereye;

/**
 * Represents an eye of ender throw.
 */
public class Throw implements IThrow {

	private final double x, y, z, alphaWithoutCorrection, beta;
	private final boolean nether;
	private final ThrowType throwType;

	private final IStdProfile stdProfile;
	private final int stdProfileNumber;
	private final double std;
	private final double correction;

	public Throw(double x, double y, double z, double alpha, double beta, boolean nether, ThrowType throwType, IStdProfile stdProfile) {
		this(x, y, z, alpha, beta, nether, 0, throwType, stdProfile);
	}

	private Throw(double x, double y, double z, double alpha, double beta, boolean nether, double correction, ThrowType throwType, IStdProfile stdProfile) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.throwType = throwType;
		alpha %= 360.0;
		if (alpha < -180.0) {
			alpha += 360.0;
		} else if (alpha > 180.0) {
			alpha -= 360.0;
		}
		this.alphaWithoutCorrection = alpha;
		this.beta = beta;
		this.nether = nether;
		this.correction = correction;

		this.stdProfile = stdProfile;
		stdProfileNumber = stdProfile.getInitialProfileNumber(this);
		std = stdProfile.getStd(stdProfileNumber);
	}

	@Override
	public IThrow withCorrection(double correction) {
		return new Throw(x, y, z, alphaWithoutCorrection, beta, nether, correction, throwType, stdProfile);
	}

	@Override
	public String toString() {
		return "x=" + x + ", z=" + z + ", alpha=" + alphaWithoutCorrection;
	}

	@Override
	public boolean isBoatThrow() {
		return throwType == ThrowType.Boat;
	}

	@Override
	public double xInOverworld() {
		return x * (nether ? 8.0 : 1.0);
	}

	@Override
	public double zInOverworld() {
		return z * (nether ? 8.0 : 1.0);
	}

	@Override
	public double horizontalAngle() {
		return alphaWithoutCorrection + correction;
	}

	@Override
	public double correction() {
		return correction;
	}

	@Override
	public double alphaWithoutCorrection() {
		return alphaWithoutCorrection;
	}

	@Override
	public int getStdProfileNumber() {
		return stdProfileNumber;
	}

	@Override
	public boolean isMcVersion1_12() {
		return throwType == ThrowType.McVersion1_12;
	}

	@Override
	public double getStd() {
		return std;
	}

//	private void updateStd() {
//		if (stdProfile == null)
//			return;
//		double newStd = stdProfile.getStd(stdProfileNumber);
//		if (newStd == std)
//			return;
//		std = newStd;
//		notifySubscribers(this);
//	}

}
