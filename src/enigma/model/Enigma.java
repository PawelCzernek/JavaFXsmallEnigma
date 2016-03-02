package enigma.model;

/*
 * Model class for Enigma. 
 * It takes 3 rotors and one reverting rotor.
 * After encrypt each character it rotate first rotor one step.
 * When first rotor rotates full rotation, next rotor turn one step.
 * 
 * @author Paweł Czernek
 */

public class Enigma {
	
	private static final int CHARACTER_CODING_OFFSET = 32;
	
	private Rotor rotor1;
	private Rotor rotor2;
	private Rotor rotor3;
	private Rotor reversingRotor;
	
	public Enigma(int[] rotor1Table, int[] rotor2Table, int[] rotor3Table,int[] revRotor, int[] initTable){
		
		//Creating rotors initialized with data tables read from file
		rotor1 = new Rotor(rotor1Table);
		rotor2 = new Rotor(rotor2Table);
		rotor3 = new Rotor(rotor3Table);
		reversingRotor = new Rotor(revRotor);
		
		//Rotating rotors to start position
		rotor1.setStartRotationValue(initTable[0]);
		rotor2.setStartRotationValue(initTable[1]);
		rotor3.setStartRotationValue(initTable[2]);
		
	}
	
	public char encodeCharacter(char charToEncode){
		
		int character = charToEncode-CHARACTER_CODING_OFFSET;
		
		int rotor1output = rotor1.getRotorOutputAtIndex(character);
		int rotor2output = rotor2.getRotorOutputAtIndex(rotor1output);
		int rotor3output = rotor3.getRotorOutputAtIndex(rotor2output);
		int revRotorOutput = reversingRotor.getRotorOutputAtIndex(rotor3output);
		int rotor3backOutput = rotor3.getRotorIndexForValue(revRotorOutput);
		int rotor2backOutput = rotor2.getRotorIndexForValue(rotor3backOutput);
		int rotor1backOutput = rotor1.getRotorIndexForValue(rotor2backOutput);
		
		
		char encodedChar = (char) (CHARACTER_CODING_OFFSET + rotor1backOutput);
		rotor1.rotateRotor();
		if (rotor1.getRotateSteps() % 64 == 0){
			rotor2.rotateRotor();
		}
		if ((rotor1.getRotateSteps() % 64 == 0) && (rotor2.getRotateSteps() % 64 == 0)){
			rotor3.rotateRotor();
		}
		return encodedChar;
	}
}
