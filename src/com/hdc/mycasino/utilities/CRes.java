package com.hdc.mycasino.utilities;

import java.util.Random;
import java.util.Vector;

import com.hdc.mycasino.screen.Card;

public class CRes {
	// public static Image pointer;

	static Random r = new Random();

	public static int random(int a, int b) {
		if (a >= b) {
			return a;
		}
		return a + (r.nextInt(b - a));
	}

	// static public String getNameOfPocker(int number) {
	// int typePoker = number % 4;
	// String nameType = "";
	// switch (typePoker) {
	// case 0:
	// nameType = "bich";
	// break;
	// case 1:
	// nameType = "chuon";
	// break;
	// case 2:
	// nameType = "ro";
	// break;
	// case 3:
	// nameType = "co";
	// break;
	// }
	// String fullName;
	// int tmp = number / 4;
	// switch (tmp) {
	// case 12:
	// fullName = nameType + "2";
	// break;
	// case 11:
	// fullName = nameType + "1";
	// break;
	// default:
	// fullName = nameType + (tmp + 3);
	// break;
	// }
	// return fullName;
	// }
	public static int random(int a) {

		return r.nextInt() % a;
	}

	public static void removeAll() {
		RecordStore.removeAll();
	}

	public static void saveRMS(String filename, byte[] data) throws Exception {
		if (RecordStore.getNumRecords(filename) > 0) {
			RecordStore.setRecord(filename, data);
		} else {
			RecordStore.addRecord(filename, data);
		}
	}

	public static void deleteRMS(String filename) {
		RecordStore.deleteRecordStore(filename);
	}

	public static byte[] loadRMS(String filename) {
		byte[] data;
		try {
			data = RecordStore.getRecord(filename);
		} catch (Exception e) {
			return null;
		}
		return data;
	}

	public static void saveRMSString(String filename, String s) {
		try {
			saveRMS(filename, s.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String loadRMSString(String filename) {
		byte[] data = loadRMS(filename);
		if (data == null) {
			return null;
		} else {
			try {
				String s = new String(data, "UTF-8");
				return s;
			} catch (Exception e) {
				return new String(data);
			}
		}
	}

	public static void saveRMSInt(String file, int x) {
		try {
			saveRMS(file, new byte[] { (byte) x });
		} catch (Exception e) {
		}
	}

	public static int loadRMSInt(String file) {
		byte[] b = loadRMS(file);
		return b == null ? -1 : b[0];
	}

	// public static void saveRMS(String filename, byte[] data) throws Exception
	// {
	// RecordStore rec = RecordStore.openRecordStore(filename, true);
	// if (rec.getNumRecords() > 0) {
	// rec.setRecord(1, data, 0, data.length);
	// } else {
	// rec.addRecord(data, 0, data.length);
	// }
	// rec.closeRecordStore();
	// }
	//
	// public static byte[] loadRMS(String filename) {
	// RecordStore rec;
	// byte[] data;
	// try {
	// rec = RecordStore.openRecordStore(filename, false);
	// data = rec.getRecord(1);
	// rec.closeRecordStore();
	// } catch (Exception e) {
	// return null;
	// }
	// return data;
	// }

	public static void removeSetting1() {
		RecordStore.removeSettings1();
	}

	// public static void saveRMSString(String filename, String s) {
	// try {
	// saveRMS(filename, s.getBytes("UTF-8"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static String loadRMSString(String filename) {
	// byte[] data = loadRMS(filename);
	// if (data == null) {
	// return null;
	// } else {
	// try {
	// String s = new String(data, "UTF-8");
	// data = null;
	// return s;
	// } catch (Exception e) {
	// return new String(data);
	// }
	// }
	// }

	// public static void saveRMSInt(String file, int x) {
	// try {
	// saveRMS(file, new byte[] { (byte) x });
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// public static int loadRMSInt(String file) {
	// byte[] b = loadRMS(file);
	// return b == null ? -1 : b[0];
	// }
	//
	// public static void cleanVector(Vector vt) {
	// if (vt != null) {
	// vt.removeAllElements();
	// vt = null;
	// }
	// }

	// ///////////////////////tiến lên
	public static void SortCardTL(byte[] arrCard, byte type) {
		byte i, j;
		byte id;
		if (type == 0) {// Sắp xếp từ nhỏ đến lớn
			for (i = 0; i < arrCard.length; i++) {
				for (j = i; j < arrCard.length; j++) {
					if (arrCard[i] % 13 > arrCard[j] % 13
							|| (arrCard[i] % 13 == arrCard[j] % 13 && arrCard[i] > arrCard[j])) {
						id = arrCard[i];
						arrCard[i] = arrCard[j];
						arrCard[j] = id;
					}
				}
			}

		} else {
			SortCardTL(arrCard, (byte) 0);
			byte k = 0;
			int[] c = new int[arrCard.length];
			int[] tmp = new int[arrCard.length];
			for (i = 0; i < arrCard.length; i++) {
				c[i] = arrCard[i];
				tmp[i] = -1;
			}
			if (type == 1) {// sắp xếp theo sảnh
				k = (byte) sortArrTL(c, tmp, k, true);
				k = (byte) sortArrTL(c, tmp, k, false);
			} else {// sắp xếp theo đôi
				k = (byte) sortArrTL(c, tmp, k, false);
				k = (byte) sortArrTL(c, tmp, k, true);
			}
			for (i = 0; i < c.length; i++)
				if (c[i] != -1)
					tmp[k++] = c[i];

			for (i = 0; i < arrCard.length; i++)
				arrCard[i] = (byte) tmp[i];
			c = null;
			tmp = null;
		}
	}

	public static int sortArrTL(int[] c, int[] tmp, byte k, boolean isSanh) {
		byte i, j;
		byte count = 0;
		int change;
		for (i = 0; i < c.length; i++) {

			if (c[i] == -1)
				continue;

			count = 0;
			tmp[k + count] = c[i];

			for (j = (byte) (i + 1); j < c.length; j++) {
				if (count > c.length)
					break;
				if (isSanh) {
					if (c[j] != -1 && tmp[k + count] % 13 + 1 == c[j] % 13 && tmp[k + count] % 13 != 12 && c[j] != 12) {
						count++;
						tmp[k + count] = c[j];
					}
				} else {
					if (c[j] != -1 && tmp[count + k] % 13 == c[j] % 13 && tmp[count + k] != c[j]) {
						count++;
						tmp[count + k] = c[j];
					}
				}
			}
			change = (isSanh) ? 2 : 1;
			if (count >= change) {
				for (j = k; j < k + count + 1; j++) {
					for (int l = 0; l < c.length; l++) {
						if (c[l] != -1 && c[l] == tmp[j]) {
							c[l] = -1;
							break;
						}
					}
				}
				k += count + 1;
			}
		}
		return k;
	}

	// //////////////////////phỏm
	public static void sortArr(int[] m_arrTmp) {
		byte i, j;
		byte count;
		for (i = 0; i < m_arrTmp.length; i++) {
			for (j = i; j < m_arrTmp.length; j++) {
				if (m_arrTmp[i] % 13 > m_arrTmp[j] % 13
						|| (m_arrTmp[i] > m_arrTmp[j] && m_arrTmp[i] % 13 == m_arrTmp[j] % 13)) {
					count = (byte) m_arrTmp[i];
					m_arrTmp[i] = m_arrTmp[j];
					m_arrTmp[j] = count;
				}
			}
		}
	}

	// lấy từng phỏm ra cho từng mảng
	public static void checkListPhom(int[] m_arrTmp, int[] arrOutput) {
		byte i;
		int row = -1;
		byte type = -1;// -1 bac dau - 0 sanh - 1 culu
		for (i = 0; i < m_arrTmp.length - 1; i++) {
			// kết thúc
			if (m_arrTmp[i] == -1)
				return;

			// bắc đầu
			if (m_arrTmp[i] + 1 == m_arrTmp[i + 1] && m_arrTmp[i] / 13 == m_arrTmp[i + 1] / 13) {
				if (type == -1) {
					row++;
					type = 0;
					if (row > arrOutput.length)
						return;
					// if (row > 0)
					// arrOutput[row - 1] = i;
				} else if (type == 1) {
					type = -1;
					if (row >= 0)
						arrOutput[row] = i;
				}
			} else if (m_arrTmp[i] % 13 == m_arrTmp[i + 1] % 13 && m_arrTmp[i] != m_arrTmp[i + 1]) {
				if (type == -1) {
					row++;
					type = 1;
					if (row > arrOutput.length)
						return;
					// if (row > 0)
					// arrOutput[row - 1] = i;
				} else if (type == 0) {
					type = -1;
					if (row >= 0)
						arrOutput[row] = i;
				}
			} else {
				type = -1;
				if (row >= 0)
					arrOutput[row] = i;

			}
		}
		return;
	}

	private static int checkPhom(int[] m_arrTmp, int[][] arrOutput, int row, boolean isSanh) {
		// sanh cung mau
		byte i, j;
		byte count;
		sortArr(m_arrTmp);

		for (i = 0; i < m_arrTmp.length; i++) {

			if (m_arrTmp[i] == -1)
				continue;

			count = 0;
			arrOutput[row][count] = m_arrTmp[i];

			if (isSanh) {
				for (j = 0; j < m_arrTmp.length; j++) {
					if (count > m_arrTmp.length)
						break;

					if (m_arrTmp[j] != -1 && (arrOutput[row][count] + 1 == m_arrTmp[j])
							&& (arrOutput[row][count] / 13 == m_arrTmp[j] / 13)) {
						count++;
						arrOutput[row][count] = m_arrTmp[j];
						j = -1;
					}
				}
			} else {
				for (j = (byte) (i + 1); j < m_arrTmp.length; j++) {
					if (count > m_arrTmp.length)
						break;

					if (m_arrTmp[j] != -1 && arrOutput[row][count] % 13 == m_arrTmp[j] % 13
							&& arrOutput[row][count] != m_arrTmp[j]) {
						count++;
						arrOutput[row][count] = m_arrTmp[j];
					}
				}
			}

			if (count >= 2) {
				clearArr(m_arrTmp, arrOutput[row]);
				row++;
			} else
				resetArr(arrOutput[row]);
		}
		return row;
	}

	// xem svn 423
	public static void getPhom(int[] arrInput, int[][] arrOutput, int type) {
		byte i, j, row = 0;
		for (i = 0; i < arrOutput.length; i++)
			for (j = 0; j < arrOutput[i].length; j++)
				arrOutput[i][j] = -1;

		int[] m_arrTmp = new int[arrInput.length];
		for (i = 0; i < arrInput.length; i++) {
			m_arrTmp[i] = arrInput[i];
		}

		row = (byte) checkPhom(m_arrTmp, arrOutput, row, (type == 0));

		for (i = 0; i < arrInput.length; i++) {
			m_arrTmp[i] = arrInput[i];
		}

		row = (byte) checkPhom(m_arrTmp, arrOutput, row, (type == 1));
		m_arrTmp = null;
	}

	// /
	private static int countInArr(int[][] arrCheck, int card) {
		byte i;
		byte count = 0;
		byte j;
		for (i = 0; i < arrCheck.length; i++) {
			for (j = 0; j < arrCheck[i].length; j++) {
				if (card == arrCheck[i][j])
					count++;
			}
		}
		return count;
	}

	private static int findInList(int[] arr, int[] checkCard, int card) {
		byte i, k;
		for (k = 0; k < checkCard.length; k++) {
			if (checkCard[k] == -1 && card == checkCard[k])
				continue;
			for (i = 0; i < arr.length; i++) {
				if (arr[i] == -1)
					continue;
				if (arr[i] == checkCard[k])
					return i;
			}
		}
		return -1;
	}

	private static int countInArrFrom(int[] arr, int card, boolean left) {
		byte countLeft = 0;
		byte countRight = 0;
		byte keep = (byte) arr.length;
		byte i;
		for (i = 0; i < arr.length; i++) {
			if (arr[i] == card)
				keep = i;

			if (arr[i] != -1 && i < keep)
				countLeft++;
			else if (arr[i] != -1 && i > keep)
				countRight++;
		}
		if (left)
			return countLeft;
		else
			return countRight;
	}

	private static void clearArrRow(int[] arr, int index, boolean left) {
		// remove all row have card int arr where same this card
		byte i;
		byte find = -1;
		for (i = 0; i < arr.length; i++) {
			if (arr[i] == index)
				find = (byte) arr.length;

			if (left && find <= i)
				arr[i] = -1;
			if (!left && find >= i)
				arr[i] = -1;
		}
	}

	private static boolean clearInArrByList(int[][] arr, int[] checkCard, int row, int card) {
		byte i, j;
		if (countInArr(arr, card) <= 1)
			return false;

		byte k;
		boolean findTrouble = false;
		for (i = 0; i < arr.length; i++) {
			if (i != row) {
				for (j = 0; j < arr[i].length; j++) {
					// tim dc dong cung chua 1 la bai
					if (arr[i][j] != -1 && arr[i][j] == card) {
						// neu dong do co phan tu ko dc xoa
						k = (byte) findInList(arr[i], checkCard, card);
						if (k != -1) {
							// nếu la sanh
							if (arr[i][0] % 13 != card % 13) {
								if (j > k && countInArrFrom(arr[i], arr[i][j], true) >= 3) {
									clearArrRow(arr[i], arr[i][j], false);
									arr[i][j] = -1;
								} else if (j < k && countInArrFrom(arr[i], arr[i][j], false) >= 3) {
									clearArrRow(arr[i], arr[i][j], true);
									arr[i][j] = -1;
								} else {
									findTrouble = true;// coi lai của thằng
														// trước
								}
							} else {// neu la cu lu
								if (countInArr(arr[i]) > 3) {
									arr[i][j] = -1;
								} else {
									findTrouble = true;// coi lai cua thang
														// truoc
								}
							}
						} else {
							arr[i][j] = -1;
						}
					}
				}
			}
		}
		return findTrouble;
	}

	private static void clearNoPhom(int[][] arr) {
		byte i, j, k;
		byte begin, end;
		for (i = 0; i < arr.length; i++) {
			if (countInArr(arr[i]) < 3) {
				resetArr(arr[i]);
				continue;
			}
			begin = -1;
			end = -1;
			for (j = 0; j < arr[i].length; j++) {
				if (begin == -1 && arr[i][j] != -1)
					begin = (byte) arr[i][j];
				if (begin != -1 && arr[i][j] != -1 && begin % 13 != arr[i][j] % 13)
					begin = -2;
			}
			if (begin == -2) {
				begin = 0;
				end = 0;
				for (j = 0; j < arr[i].length; j++) {
					if (arr[i][j] != -1) {
						end++;
					} else {
						if (end >= 3)
							begin += end;
						else {
							for (k = begin; k < end; k++)
								arr[i][k] = -1;
						}
						end = 0;
					}
				}
			}
		}
	}

	public static void clearInArr(int[][] arr, int[] checkCard) {
		byte savePostoFind;
		byte i, j, k;
		for (i = 0; i < checkCard.length; i++) {
			if (checkCard[i] != -1) {
				for (j = 0; j < arr.length; j++) {
					savePostoFind = -2;
					for (k = 0; k < arr[j].length; k++) {
						if (checkCard[i] == arr[j][k] && savePostoFind == -2) {
							savePostoFind = j;
							break;
						}
					}
					if (savePostoFind != -2) {
						for (k = 0; k < arr[j].length; k++) {
							if (arr[j][k] != -1 && clearInArrByList(arr, checkCard, j, arr[j][k])) {
								arr[j][k] = -1;
							}
						}
						break;
					}
				}
			}
		}
		clearNoPhom(arr);
	}

	public static void clearPhom(int[][] arr) {
		byte i, j;
		// clear nhung cai giong
		byte savePostoFind;
		for (i = 0; i < arr.length; i++) {
			for (j = 0; j < arr[i].length; j++) {
				if (arr[i][j] != -1) {
					savePostoFind = (byte) arr[i][j];
					CRes.clearArr(arr, arr[i][j]);
					arr[i][j] = savePostoFind;
				}
			}
		}
		// clear nhung cai ko la phom
		clearNoPhom(arr);
	}

	// sap xep phom = tay\
	public static int sortHandPhom(int[] arr, int[] arrOut) {
		byte i;
		byte count = 0;
		arrOut[0] = arr[0];
		byte keep = 0;
		for (i = 0; i < arr.length; i++) {
			if (arr[i] != -1 && (arrOut[keep + count] + 1 == arr[i]) && (arrOut[keep + count] / 13 == arr[i] / 13)) {
				count++;
			} else if (count >= 2) {
				keep += count + 1;
				count = 0;
			} else {
				count = 0;
			}
			arrOut[keep + count] = arr[i];
		}
		for (i = 0; i < arr.length; i++) {
			if (arr[i] != -1 && arrOut[keep + count] % 13 == arr[i] % 13 && arrOut[keep + count] != arr[i])
				count++;
			else if (count >= 2) {
				keep += count + 1;
				count = 0;
			} else {
				count = 0;
			}
			arrOut[keep + count] = arr[i];
		}
		return keep;
	}

	public static void clearArr(int[][] arrOut, int card) {
		// remove all card int arr where same this card
		byte i, j;
		for (i = 0; i < arrOut.length; i++) {
			for (j = 0; j < arrOut[i].length; j++) {
				if (arrOut[i][j] == card) {
					arrOut[i][j] = -1;
					break;
				}
			}
		}
	}

	public static void clearArr(int[] arrOut, int[] arrClear) {
		// remove all card int arr where same this arrClear
		byte i, j;
		for (i = 0; i < arrOut.length; i++) {
			if (arrOut[i] != -1) {
				for (j = 0; j < arrClear.length; j++) {
					if (arrClear[j] != -1 && arrClear[j] == arrOut[i]) {
						arrOut[i] = -1;
						break;
					}
				}
			}
		}
	}

	public static void resetArr(int[] listArr) {
		int i;
		for (i = 0; i < listArr.length; i++) {
			listArr[i] = -1;
		}
	}

	public static int countInArr(int[] arrCheck) {
		int i;
		int count = 0;
		for (i = 0; i < arrCheck.length; i++) {
			if (-1 != arrCheck[i])
				count++;
		}
		return count;
	}

	public static boolean check3DoiThong(int[] arrCard) {
		byte i;
		for (i = 0; i < arrCard.length; i++) {
			if (arrCard[i] % 13 == arrCard[i + 1] % 13 && arrCard[i] < arrCard[i + 1]) {
				if (i == arrCard.length - 2)
					return true;
				i++;
			} else
				return false;
		}
		return false;
	}

	public static boolean check4Quy(int[] arrCard) {
		return ((arrCard[0] % 13 == arrCard[1] % 13 && arrCard[0] < arrCard[1])
				&& (arrCard[1] % 13 == arrCard[2] % 13 && arrCard[1] < arrCard[2]) && (arrCard[2] % 13 == arrCard[3] % 13 && arrCard[2] < arrCard[3]));
	}

	public static int countCard(Card[] c) {
		int i;
		int count = 0;
		for (i = 0; i < c.length; i++) {
			if (c[i].m_id != -1)
				count++;
		}
		return count;
	}

	public static void cleanVector(Vector vt) {
		// TODO Auto-generated method stub
		if (vt != null) {
			vt.removeAllElements();
			vt = null;
		}
	}
}
