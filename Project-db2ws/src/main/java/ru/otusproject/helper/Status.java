package ru.otusproject.helper;

public class Status {

	Long status;

	public Status() {
		super();
		this.status = 0L;
	}

	public Status(Long status) {
		super();
		this.status = status;
	}

	public Status(Integer status) {
		super();
		this.status = status.longValue();
	}

	public void setBite(int pos, int value) {
		setBite(pos, (value == 1) ? true : false);
	}

	public void setBite(int pos, boolean value) {

		if (value) {
			// status = status | (1 << pos);
			status |= 1 << pos;
		} else if (!value) {
			// status = status & ~(1 << pos);
			status &= ~(1 << pos);
		}
	}

	public Long getBitInt(int position) {
		return (status >> position) & 1;
	}

	public Boolean getBit(int position) {
		return getBitInt(position) == 1;
	}

	@Override
	public String toString() {
		return "Status [" + status + " => " + print() + "]";
	}

	public String print() {
		return Long.toBinaryString(status);
	}

	public Integer intVal() {
		return status.intValue();
	}

}
