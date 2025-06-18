package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		VBox root = new VBox(15);
		root.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

		Label title = new Label("Compress/Decompress File");
		title.setFont(Font.font("Segoe UI", 24));

		TabPane tabPane = new TabPane();

		Tab tabCompress = new Tab("Compress");
		Tab tabDecompress = new Tab("Decompress");

		tabCompress.setClosable(false);
		tabDecompress.setClosable(false);

		VBox compressBox = new VBox(10);
		Button compressBtn = new Button("Choose File to Compress");
		compressBtn.setFont(Font.font(16));

		TextArea compressArea = new TextArea();
		compressArea.setPrefHeight(400);
		compressArea.setEditable(false);

		compressBox.getChildren().addAll(compressBtn, compressArea);
		tabCompress.setContent(compressBox);

		VBox decompressBox = new VBox(10);
		Button decompressBtn = new Button("Choose File to Decompress");
		decompressBtn.setFont(Font.font(16));

		TextArea decompressArea = new TextArea();
		decompressArea.setPrefHeight(400);
		decompressArea.setEditable(false);

		decompressBox.getChildren().addAll(decompressBtn, decompressArea);
		tabDecompress.setContent(decompressBox);

		tabPane.getTabs().addAll(tabCompress, tabDecompress);

		root.getChildren().addAll(title, tabPane);

		Scene scene = new Scene(root, 1200, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Huffman Project");
		primaryStage.show();

		/// ------------------action button compress---------------

		compressBtn.setFont(Font.font(16));
		compressBtn.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(primaryStage);
			if (file != null) {
				long originalSize = file.length();
				compressArea.clear();
				compressArea.appendText("Original Size: " + originalSize + " bytes\n");

				try {
					Huffman.compress(file);

					File compressedFile = new File(Huffman.outFileName);
					long compressedSize = compressedFile.length();

					// double ratio = (double) compressedSize / originalSize;
					// double saving = (1 - raFStio) * 100;

					compressArea.appendText("Compressed Size: " + compressedSize + " bytes\n");
					double ratio = 1 - ((double) compressedSize / originalSize);
					compressArea.appendText(String.format("Compression Ratio: %.2f\n", ratio));

					compressArea.appendText("ASCII\tCharacter\tFrequency\tHuffCode\n");
					for (int k = 0; k < Huffman.huffCodeArray.length; k++) {
						if ((int) Huffman.huffCodeArray[k].character == 10
								|| (int) Huffman.huffCodeArray[k].character == 9)
							continue;
						compressArea.appendText(String.format("%d\t\t%s\t\t%d\t\t%s\n",
								(int) Huffman.huffCodeArray[k].character, Huffman.huffCodeArray[k].character,
								Huffman.huffCodeArray[k].counter, Huffman.huffCodeArray[k].huffCode));
					}
					compressArea.appendText(Huffman.builder.toString());
				} catch (IOException ex) {
					compressArea.setText("Error compressing file!\n" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		});

		// -------------------- action button decompress -----------

		decompressBtn.setOnAction(e -> {

			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(primaryStage);
			if (file != null) {
				String decompressedPath = Huffman.deCompress(file);
				decompressArea.setText("File decompressed successfully.\n");
				decompressArea.appendText("Decompressed file path: " + decompressedPath + "\n");
			}

		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}