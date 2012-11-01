package com.hdc.mycasino.model;

public class CardTest {
	private char type;
	private int value;
	private int realValueInGame;

	private boolean isOpened;

	public CardTest(char type, int value) {
		this.type = type;
		this.value = value;
	}

	public CardTest(int value, char type) {
		this.type = Character.toUpperCase(type);
		this.value = value;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.getValue() + "" + this.getType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardTest other = (CardTest) obj;
		if (type != other.type)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public int getRealValueInGame() {
		return realValueInGame;
	}

	public void setRealValueInGame(int realValueInGame) {
		this.realValueInGame = realValueInGame;
	}
}
