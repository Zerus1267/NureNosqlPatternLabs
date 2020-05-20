package Model;

public class Trip extends IdentifierEntity {
	private long c_out;
	private long c_in;
	private int min_duration;
	private int min_price;

	public Trip() {
	}

	@Override
	public String getEntityName() {
		return "Trip num = " + getId();
	}

	public Trip(int min_duration, int min_price) {
		this.min_duration = min_duration;
		this.min_price = min_price;
	}

	public long getC_out() {
		return c_out;
	}

	public void setC_out(long c_out) {
		this.c_out = c_out;
	}

	public long getC_in() {
		return c_in;
	}

	public void setC_in(long c_in) {
		this.c_in = c_in;
	}

	public int getMin_duration() {
		return min_duration;
	}

	public void setMin_duration(int min_duration) {
		this.min_duration = min_duration;
	}

	public int getMin_price() {
		return min_price;
	}

	public void setMin_price(int min_price) {
		this.min_price = min_price;
	}
}
