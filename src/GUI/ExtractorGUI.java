package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ExtractorGUI extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel l1,l2,l3,l4,l5;
	private JPanel p1,p2,p3,p4,p5;
	private JButton buttonExtract,buttonSelectFolder,buttonSavedFolder;
	private JTextField stringSearched,textFieldExtension;
	
	private String folderPath = "./",savedFolderPath = "./",savedFileName = "result_Extracted.txt";
	private ArrayList<String> foldersFiles = new ArrayList<>();
	private ArrayList<String> arrayStringSearched = new ArrayList<>();
	private String searchedString = "",extension = ".txt";
	
	private JCheckBox sensitivity;
	
	boolean sensitivityBool = false;
	
	final JFileChooser fileDialog = new JFileChooser();
	
	public ExtractorGUI(){
		
	}


	private void initGUI() {
		
		setTitle("Extractor - by Tushar Sharma");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(7, 1, 30, 30));
		
		p1 = new JPanel(new GridLayout(1, 2, 20, 20));
		l1 = new JLabel("Files Folder path(default: Current Folder): ");
		buttonSelectFolder = new JButton("Select Folder");
		p1.add(l1);
		p1.add(buttonSelectFolder);
		add(p1);
		
		
		
		p2 = new JPanel(new GridLayout(1, 2, 20, 20));
		l2 = new JLabel("String to be Searched :");
		stringSearched = new JTextField(40);
		stringSearched.setFont(new Font("Arial", Font.BOLD,20));
		p2.add(l2);
		p2.add(stringSearched);
		add(p2);
		
		p3 = new JPanel(new GridLayout(1,2,20,20));
		l3 = new JLabel("Sensitivity :");
		sensitivity = new JCheckBox("Sensitive Search");
		p3.add(l3);
		p3.add(sensitivity);
		p3.setAlignmentY(RIGHT_ALIGNMENT);
		add(p3);
		
		p4 = new JPanel(new GridLayout(1, 2, 20, 20));
		l4 = new JLabel("Extension :");
		textFieldExtension = new JTextField(40);
		textFieldExtension.setText(extension);
		textFieldExtension.setFont(new Font("Arial", Font.BOLD,20));
		p4.add(l4);
		p4.add(textFieldExtension);
		add(p4);
		
		p5 = new JPanel(new GridLayout(1, 1, 20, 20));
		buttonSavedFolder = new JButton("Change Folder/File Name(default: Current Folder/result_Extracted.txt)");
		p5.add(buttonSavedFolder);
		add(p5);
		
		
	
		buttonExtract = new JButton("Extract");
		add(buttonExtract,BorderLayout.SOUTH);
		
		l5 = new JLabel("By Tushar Sharma (linkedin @sharmatushar124)");
		l5.setHorizontalAlignment(SwingConstants.RIGHT);
		add(l5,BorderLayout.NORTH);
		
		
		
		
		buttonSelectFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileDialog.showOpenDialog(ExtractorGUI.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					folderPath = fileDialog.getSelectedFile().getAbsolutePath();
					l2.setText(folderPath);
					foldersFiles.clear();

				}
			}

			
		});
		
		buttonSavedFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileDialog.showSaveDialog(null);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					savedFolderPath = fileDialog.getSelectedFile().getParent();
					savedFileName = fileDialog.getSelectedFile().getName();
					
					createAndSaveData();
				}
				
			}

			
		});
		
		buttonExtract.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createAndSaveData();
				
			}
		});
		
		sensitivity.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				sensitivityBool = !sensitivityBool;
				
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void getListAllFile() {
		
		File selectedFolder = new File(folderPath);
		
		
		File[] listOfFiles = selectedFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(extension);
			}
		});
		
		for (File file : listOfFiles) {
			if(!file.isDirectory())
			foldersFiles.add(file.getName());
		}
		if(foldersFiles.contains(savedFileName)){
			foldersFiles.remove(foldersFiles.indexOf(savedFileName));
		}
	}
	
	
	private void createAndSaveData() {
		
		searchedString = stringSearched.getText();
		extension = textFieldExtension.getText();
		getListAllFile();
		
		if(!searchedString.equals("") || !searchedString.equals(null)){
			
			for (String string : foldersFiles) {
				
				SearchSaveDataInArray(string);
				
			}
			
			CreateFileAndSave();
			
			
		}
		else{
			
			JOptionPane.showMessageDialog(this, "Please enter characters to be searched...");
		}
		
		arrayStringSearched.clear();
		foldersFiles.clear();
	}

	private void CreateFileAndSave() {
		
		File file = new File(savedFolderPath.replace("\\", "\\\\") + "\\" + savedFileName);
		try (PrintWriter writer = new PrintWriter(file)){
			
			if(file.exists()){
			writer.print("");
			writer.close();
			}
			file.createNewFile();
			Files.write(Paths.get(file.getPath()), arrayStringSearched	, StandardCharsets.UTF_8,StandardOpenOption.TRUNCATE_EXISTING);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Something went wrong, Try Again!!");
		}
		
		
		
		
	}

	private void SearchSaveDataInArray(String string) {
		
		try(final BufferedReader br = new BufferedReader(new FileReader(folderPath.replace("\\", "\\\\")+ "\\" + string ))){
			
			String s = null;
					
			while((s = br.readLine()) != null){
				
				if(!sensitivityBool){
				if(s.toLowerCase().contains(searchedString.toLowerCase())){
					arrayStringSearched.add(s);
				}
				}
				else{
					if(s.contains(searchedString)){
						arrayStringSearched.add(s);
					}
				}
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		initGUI();
		
	}
	
	

}
