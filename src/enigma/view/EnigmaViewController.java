package enigma.view;

/*
 * Simple Enigma controller.
 * It loads encoding combinations into rotors from file (rotors.txt) 
 * and initial starting position of rotors from file (init.txt).
 * Next You set destination file to write encrypted text, and last
 * load text to encrypt from file.
 * 
 * @author Paweł Czernek
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;
import enigma.model.Enigma;
import enigma.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EnigmaViewController {

	// Set reference to mainApp
	private MainApp mainApp;
	Scanner in = null;

	// Back reference to MainApp
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	private String plainText = "";
	private String encodedText = "";
	private Enigma enigma = null;

	int[] rotor1Table = new int[64];
	int[] rotor2Table = new int[64];
	int[] rotor3Table = new int[64];
	int[] reverseRotorTable = new int[64];
	int[] initTable = new int[3];;

	public EnigmaViewController() {

	}

	@FXML
	private void handleOpenFile() {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt Files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			try {
				plainTextArea.setText("");
				
				in = new Scanner(file);
				in.useDelimiter(Pattern.compile("[\\r\\n]+"));

				while (in.hasNext()) {
					plainTextArea.setText(plainTextArea.getText() + in.nextLine().toUpperCase() + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleSaveFile() {
		PrintWriter out = null;

		FileChooser fChooser = new FileChooser();
		fChooser.setTitle("Zapisz wyniki");
		fChooser.getExtensionFilters().addAll(new ExtensionFilter("txt Files (*.txt)", "*.txt"));
		File selectedFile = fChooser.showSaveDialog(mainApp.getPrimaryStage());

		try {
			out = new PrintWriter(selectedFile);
			out.print(encryptedTextArea.getText());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			out.close();
		}

	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

	@FXML
	private TextArea plainTextArea;

	@FXML
	private TextArea encryptedTextArea;

	@FXML
	private void handleLoadRotors() {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt Files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			try {
				in = new Scanner(file);
				in.useDelimiter(Pattern.compile("[\\r\\n-]+"));

				rotor1Table = writeRotorTable(rotor1Table);
				rotor2Table = writeRotorTable(rotor2Table);
				rotor3Table = writeRotorTable(rotor3Table);
				reverseRotorTable = writeReversingRotor(reverseRotorTable);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void handleLoadInit() {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt Files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			try {
				in = new Scanner(file);
				in.useDelimiter(Pattern.compile("[\\r\\n]"));
				// System.out.println(in.next());
				in.next();
				for (int i = 0; i < 3; i++) {
					initTable[i] = in.nextInt();
				}
				
				enigma = new Enigma(rotor1Table, rotor2Table, rotor3Table, reverseRotorTable, initTable);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void handleEncrypt() {
		if (enigma == null) {
			encryptedTextArea.setText("Set rotors and init value first!");

		} else {

			plainText = plainTextArea.getText();

			for (int i = 0; i < plainText.length(); i++) {
				if (plainText.charAt(i) < 32 || plainText.charAt(i) > 95) {
					encodedText += plainText.charAt(i);
					continue;
				}
				encodedText += Character.toString(enigma.encodeCharacter(plainText.charAt(i)));
			}
			encryptedTextArea.setText(encodedText);
			encodedText = "";
		}

	}

	private int[] writeRotorTable(int[] table) {
		in.next();
		int outPin;
		int inPin;
		for (int i = 0; i < 64; i++) {
			outPin = in.nextInt();
			inPin = in.nextInt();
			table[inPin] = outPin;
		}
		return table;
	}

	private int[] writeReversingRotor(int[] table) {
		in.next();
		int outPin;
		int inPin;
		for (int i = 0; i < 32; i++) {
			outPin = in.nextInt();
			inPin = in.nextInt();
			table[inPin] = outPin;
			table[outPin] = inPin;
		}
		return table;
	}

}
