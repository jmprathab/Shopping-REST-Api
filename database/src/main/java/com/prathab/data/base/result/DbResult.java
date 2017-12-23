package com.prathab.data.base.result;

public class DbResult {
	private int numOfRows;
	private boolean isSuccessful;

	DbResult() {
		numOfRows = -1;
		isSuccessful = false;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean successful) {
		isSuccessful = successful;
	}
}
