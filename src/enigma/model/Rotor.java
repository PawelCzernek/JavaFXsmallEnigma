package enigma.model;

/*
 * Model class for rotor. 
 * 
 * @author Paweł Czernek
 */

public class Rotor {
	
	private static int SIZE_OF_ROTOR = 64;
	private int[] rotorSection = new int[SIZE_OF_ROTOR];
	private int rotateSteps = 0;
	
	public Rotor(int[] initTable){
		rotorSection = initTable;
	}
	
	public int getRotorOutputAtIndex(int inputIndex){
		return rotorSection[inputIndex];
	}
	
	public int getRotorIndexForValue(int value){
		int index = -1;
		for(int i=0; i<SIZE_OF_ROTOR; i++){
			if (rotorSection[i] == value){
				index = i;
			}
		}
		return index;
	}
	
	public void setStartRotationValue(int rotateRotor){	
		for(int i=0; i<rotateRotor; i++){
			rotateRotor();
		}
	}
	
	public void rotateRotor(){
		int tempVal = rotorSection[0];
		for(int i=1; i<SIZE_OF_ROTOR; i++){
			rotorSection[i-1] = rotorSection[i];
		}
		rotorSection[SIZE_OF_ROTOR-1] = tempVal;
		rotateSteps++;
		
	}
	
	public int getRotateSteps(){
		return rotateSteps;
	}
	public static int getSizeOfRotor(){
		return SIZE_OF_ROTOR;
	}
	

}
