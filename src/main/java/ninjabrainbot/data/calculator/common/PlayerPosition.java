package ninjabrainbot.data.calculator.common;

public class PlayerPosition implements IPlayerPosition {

	private final double x, y, z, horizontalAngle, verticalAngle;
	private final boolean isInNether;

	public PlayerPosition(double x, double y, double z, double horizontalAngle, double verticalAngle, boolean isInNether) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.isInNether = isInNether;
	}

	@Override
	public double xInOverworld() {
		return x * (isInNether ? 8.0 : 1.0);
	}

	@Override
	public double zInOverworld() {
		return z * (isInNether ? 8.0 : 1.0);
	}

	@Override
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double yInPlayerDimension() {
		return y;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public double horizontalAngle() {
		return horizontalAngle;
	}

	@Override
	public double beta() {
		return verticalAngle;
	}

	@Override
	public boolean lookingBelowHorizon() {
		return verticalAngle > 0;
	}

	@Override
	public boolean isInOverworld() {
		return !isInNether;
	}

	@Override
	public boolean isNether() {
		return isInNether;
	}

}
