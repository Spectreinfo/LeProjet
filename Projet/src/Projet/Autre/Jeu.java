package Projet.Autre;


import java.util.ArrayList;
import Projet.Carte.Bloc;
import Projet.Carte.Lave;
import Projet.Carte.Salle;
import Projet.Personnage.*;
import Projet.GUI.Window;
import Projet.Object.*;

import java.util.Random;


public  class Jeu{
	
	Random random = new Random();
	
	public static int view =10; 
	public int vie =100;
	private int minimum_size=5;
	private int taille_max;
	private int nombre_de_salles;   
	public static int taille_de_la_carte;
	private Inventaire inventaire = new Inventaire(6,0,0,0); /// GOD WHY ? 
	private int emploi = 0;
	
	private ArrayList<Bloc> blocs= new ArrayList<Bloc>();
	public ArrayList<Personnage> players= new ArrayList<Personnage>();
	private ArrayList<Salle> Salles = new ArrayList<Salle>();
	
	private ArrayList<Objet> objets = new ArrayList<Objet>();
	private ArrayList<Potion> objetsPotions = new ArrayList<Potion>();
	private ArrayList<Arme> objetsArmes = new ArrayList<Arme>();
	private ArrayList<Armure> objetsArmures = new ArrayList<Armure>();
	private ArrayList<Thread> threadList = new ArrayList<Thread>();
	private ArrayList<Lave> laveList = new ArrayList<Lave>();
	
	
	
	private int rdl;
	private int rdh;
	private int rdx;
	private int rdy;
	
	private int nombreEnnemi;
	private int nbObjet;
	private int nLave;
	
	private int attaqueJ = 30; 
	private int attaqueE = 10; 
	private int attaqueB = 5; 
	
	
	private Window window;
	
	
	int[][] mapVisible = new int[2*view+1][2*view+1];
	
	public Jeu(int Taille, Window window){
		taille_de_la_carte=Taille +2*view ;
		taille_max = Math.round(Taille/6);
		nombre_de_salles = Math.round(Taille/10);
		nombreEnnemi= Math.round(Taille/5);
		nbObjet =Math.round(Taille/8);
		nLave =Math.round(Taille/10);
		
		this.window = window; 
		blocs.addAll(setBlocks());
		int taille = blocs.size();
		ArrayList<Integer> posRand = new ArrayList<Integer>();
		ArrayList<Integer> posRand2 = new ArrayList<Integer>();
		ArrayList<Integer> posRand3 = new ArrayList<Integer>();
		for(int k=0; k<=nLave; k++){
			posRand3.add(random.nextInt(taille));
			Bloc blocStart = blocs.get(posRand3.get(k)); 
			laveList.add(new Lave(this,attaqueB, blocStart.getPosX(),  blocStart.getPosY()));
		}
		for(int w=0; w<=nombreEnnemi; w++){
			posRand.add(random.nextInt(taille));
            Bloc blocStart = blocs.get(posRand.get(w)); 
            if(w==0){
            	players.add(new Joueur(this, vie, attaqueJ, 0, 0,   blocStart.getPosX(),  blocStart.getPosY()));
            }else{
            	players.add(new Ennemi(this, vie, attaqueE, 0, 0,   blocStart.getPosX(),  blocStart.getPosY()));
            	threadList.add(players.get(w).getThread());
            }
		}
		for(int n=0; n<nbObjet;n++){
			posRand2.add( random.nextInt(taille));
            Bloc blocStart = blocs.get(posRand2.get(n)); 
            if(n<=nbObjet/2){
            	objetsPotions.add(new Potion(blocStart.getPosX(),  blocStart.getPosY()));
            }else if(n>=nbObjet/2 && n<=nbObjet*3/4){
            	objetsArmes.add(new Arme(blocStart.getPosX(),  blocStart.getPosY()));
            }else{
            	objetsArmures.add(new Armure(blocStart.getPosX(),  blocStart.getPosY()));
            }
		}
		objets.addAll(objetsPotions);
		objets.addAll(objetsArmes);
		objets.addAll(objetsArmures);
				
		window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
	}

	public ArrayList<Bloc> setBlocks(){
	// cr�e une liste de blocs accessible
		boolean value;
		ArrayList<Bloc> blocs = new ArrayList<Bloc>();
		ArrayList<Salle> couloir = new ArrayList<Salle>();
		int i=0; 
		while(i<nombre_de_salles){
			value = true;
			rdl = minimum_size + random.nextInt(taille_max- minimum_size);
			rdh = minimum_size + random.nextInt(taille_max-minimum_size);
			rdx =view +random.nextInt(taille_de_la_carte-rdl-2*view);
			rdy =view +random.nextInt(taille_de_la_carte-rdh-2*view) ;
			Salle Salle= new Salle(rdx, rdy, rdl, rdh);
			if(i==0){
				Salles.add(Salle);
				i++;
			}else{
				for(int j=0;j<Salles.size(); j++ ){
					if(Salles.get(j).touch(Salle)){
						value =false;
						break;
					}
				}
				if(value){
					Salles.add(Salle);
					//couloir.addAll(Salles.get(i-1).salleLien(Salle));
				    i++;
					
				
				}
			
			}
		}
		for(int v=1; v<=nombre_de_salles-1; v++){
			couloir.addAll(Salles.get(v-1).salleLien(Salles.get(v)));
		}
		couloir.addAll(Salles.get(nombre_de_salles-1).salleLien(Salles.get(0)));
		Salles.addAll(couloir);
		for(Salle elem:Salles){
			ArrayList<Bloc> blocAdd= elem.inSalle();
			blocs.addAll(blocAdd);
		}
		return(blocs);
		}
	
	public int[][] getMap(){
		int[][] map = new int[this.taille_de_la_carte][this.taille_de_la_carte];
		for(int i = 0; i<this.taille_de_la_carte; i++)
			for(int j = 0; j<this.taille_de_la_carte; j++)
				map[i][j] = 1;
		for(Bloc bloc : blocs){
			int x = bloc.getPosX();
			int y = bloc.getPosY();
			map[x][y] = 0;
		}
		for(Potion potion: objetsPotions){
			int x = potion.getPosX();
			int y = potion.getPosY();
			map[x][y] = 4;
		}
		for(Armure armure: objetsArmures){
			int x = armure.getPosX();
			int y = armure.getPosY();
			map[x][y] = 5;
		}
		for(Arme arme: objetsArmes){
			int x = arme.getPosX();
			int y = arme.getPosY();
			map[x][y] = 6;
		}
		for(Lave lave:laveList){
			int x = lave.getPosX();
			int y = lave.getPosY();
			map[x][y] = 7;
		}
		for(int c=0;c<players.size(); c++){
			
			int x = players.get(c).getPosX();
			int y =  players.get(c).getPosY();
			if(c==0){
				map[x][y] = 2;
			}else{
				map[x][y] = 3;
			}
		}
		
		//System.out.println(map);
		return map;
	}
	public int[][] setVisibleMap(int[][] map){
		int a = players.get(0).getPosX();
		int b = players.get(0).getPosY();
		for(int i = a-view; i<=a+view; i++){
			int x= i - (a-view);
			for(int j =  b-view; j<= b+view; j++){
				int y= j - (b-view);
				mapVisible[x][y] = map[i][j];

			}
		}
	return(mapVisible);
	}
	
	public int[][] getVisibleMap(){
		return this.mapVisible; 
	}
	
	
	// Move 
	
	public void movePlayerLeft(){
		players.get(0).changeOrientation(0);
		if(players.get(0).canMove(-1,0)){
			actionLaveIn();
			players.get(0).move(-1, 0);
			actionLaveSt();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	public void movePlayerRight(){
		players.get(0).changeOrientation(2);
		if(players.get(0).canMove(1,0)){
			actionLaveIn();
			players.get(0).move(1, 0);	
			actionLaveSt();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	public void movePlayerDown(){
		players.get(0).changeOrientation(3);
		if(players.get(0).canMove(0, 1)){
			actionLaveIn();
			players.get(0).move(0, 1);
			actionLaveSt();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	public void movePlayerUp(){
		players.get(0).changeOrientation(1);
		if(players.get(0).canMove(0, -1)){
			actionLaveIn();
			players.get(0).move(0, -1);
			actionLaveSt();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	public void ennemiMove(){
		window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
	}

	// Attaque
		
	public void persoAttaque(){
		int[][] Map =getMap();
		int a = players.get(0).getPosX();
		int b = players.get(0).getPosY();
		int orient = players.get(0).getOrientation();
		int c =0;
		int d =0;
		if(orient ==0){
			 c=-1; 
		}else if(orient ==1){
			 d=-1; 
		}else if(orient ==2){
			c=1;
		}else if(orient ==3){
			d=1;
		}
		int x = a+c;
		int y = b+d;
		if(Map[a+c][b+d]==3){
			for(Personnage player:players){
				int r = player.getPosX();
				int t = player.getPosY();
				if(x==r && y==t){
					player.changeVie(players.get(0).getAttaque());
					if(inventaire.canThrowArme()){
						emploi+=1;
						if(emploi ==5){
							actionArme();
						}
					}
					if(player.getVie()<=0){
						player.getThread().interrupt();
						players.remove(player);
						loot(r, t);
						
						renitialise();
					}
					break;
				}
			}
		}
		window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		
	}
	public void joueurSouffre(int degat){
		System.out.println("j'ai mal");
		players.get(0).changeVie(degat-players.get(0).getArmure());
		window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		renitialise();
	}
	public void loot(int x, int y){
		int i = random.nextInt(6);
		if (i ==0){
			objetsPotions.add(new Potion(x,  y));
			objets.addAll(objetsPotions);
		}else if(i==1){
			objetsArmes.add(new Arme(x,  y));
			objets.addAll(objetsArmes);
		}else if(i==2){
			objetsArmures.add(new Armure(x,  y));
			objets.addAll(objetsArmures);
		}
	}
	public boolean isOnLave(){
		int t = players.get(0).getPosX();
		int v = players.get(0).getPosY();
		boolean value = false;
		for(Lave lave:laveList){
			if(t==lave.getPosX() && v==lave.getPosY()){
				value = true;
			}
		}
		return (value);
	}
	
	private void actionLaveIn(){
		if(isOnLave()){
			int t = players.get(0).getPosX();
			int v = players.get(0).getPosY();
			for(Lave lave:laveList){
				if(t==lave.getPosX() && v==lave.getPosY()){
					lave.pause();
				}
			}
		}
	}
	private void actionLaveSt(){
		if(isOnLave()){
			int t = players.get(0).getPosX();
			int v = players.get(0).getPosY();
			for(Lave lave:laveList){
				if(t==lave.getPosX() && v==lave.getPosY()){
					lave.launch();
					System.out.println("j'ai lanc�");
				}
			}
		}
	}
	
	
	
	// Utilise equipement
	
	public void actionPotion(){
		if(inventaire.canUtilisePotion()){
			inventaire.utilisePotion();
			players.get(0).heal(); 
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	
	public void actionArme(){
		if(inventaire.canThrowArme()){
			inventaire.throwArme();
			players.get(0).perdAttaque();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	public void actionArmure(){
		if(inventaire.canThrowArmure()){
			inventaire.throwArmure();
			players.get(0).perdArmure();
			window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
		}
	}
	
	public void ramasse(){
		if(inventaire.getEquipement()[0]<6){
			int x = players.get(0).getPosX();
			int y =  players.get(0).getPosY();
			int a,b; 
			for(Objet objet:objets){
				a = objet.getPosX();
				b = objet.getPosY();
				if (x==a && y==b){
					if(objet.getType()==0){
						inventaire.addPotion();
						objetsPotions.remove(objet);
						objets.remove(objet);
						break;
					}else if(objet.getType()==1){
						inventaire.addArmure();
						players.get(0).changeArmure();
						objetsArmures.remove(objet);
						objets.remove(objet);
						break;
					}else if(objet.getType()==2){
						inventaire.addArme();
						players.get(0).changeAttaque();
						objetsArmes.remove(objet);
						objets.remove(objet);
						break;
					}
				}
			}
		}
		window.draw(this.setVisibleMap(getMap()), players.get(0).getVie(), players.get(0).getAttaque(),players.get(0).getArmure(), inventaire.getEquipement(), players.size()-1);
	}

	//// Renitialise 
	public void renitialise(){
		if(players.size() ==1 ||  players.get(0).getVie()<=0){
			for (Thread thread: threadList){
				thread.interrupt();
			}
			window = new Window(players.size()-1);
		}
	}
}