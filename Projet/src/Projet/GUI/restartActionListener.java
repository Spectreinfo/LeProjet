package Projet.GUI;
import java.awt.event.*; 
import javax.swing.JTextArea;
import Projet.GUI.*;
import Projet.Autre.*;

public class restartActionListener implements ActionListener {
	JTextArea textArea;
	int taille;
	public restartActionListener(JTextArea textArea){
		this.textArea = textArea; 
		}
	@Override
	public void actionPerformed(ActionEvent arg0) {
			taille = startActionListener.taille;
			Window window = new Window();
			Jeu jeu= new Jeu(taille, window);
	    	Keyboard keyboard = new Keyboard(jeu); 
	    	window.setKeyListener(keyboard);   	
	}
}
