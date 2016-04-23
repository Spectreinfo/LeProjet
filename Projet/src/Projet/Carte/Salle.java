package Projet.Carte;
import java.lang.Math.*;
import java.util.ArrayList;

public class Salle {
	
//case haut gauche (l positif vers la droite, h positif vers le bas)	
private int x;
private int y;
// dimensions de la salle ( l = largeur, h =hauteur):

private int l;
private int h;

// constructeur:
public Salle(int x,int y,int l,int h){
	this.x = x;
	this.y = y;
	this.l = l;
	this.h = h;
	  
}

// getters:
public int getSalleX(){
	return this.x;
}
public int getSalleY(){
	return this.y;
}
public int getSalleL(){
	return this.l;
}
public int getSalleH(){
	return this.h;
}

// return "True" si les deux salles se superposent.

public Boolean touch(Salle s2){

	return (x<=s2.x+s2.l && s2.x<=x+l && y<=s2.y+s2.h && s2.y<=y+h);
		
}

public ArrayList<Bloc> inSalle(){
	ArrayList<Bloc> listeDeBloc = new ArrayList<Bloc>(); 
	for(int i=x; i<=x+l; i++){
		for(int j=y; j<=y+h; j++){
			Bloc bloc = new Bloc(i,j); 
			listeDeBloc.add(bloc); 
		}
	}
	return listeDeBloc;
	
}

public ArrayList<Salle> salleLien(Salle s2){//renvoie la liste des deux salles formant le couloir rejoignant deux salles. 
	ArrayList<Salle> couloirs = new ArrayList<Salle>();
	if(Math.abs(x-s2.x)<=Math.abs(y-s2.y)){	
		if(x-s2.x>0){
			Salle couloirhor = new Salle(s2.x,y,x-s2.x-1, 1);
			couloirs.add(couloirhor);
			if(y-s2.y>0){
				Salle couloirver = new Salle(s2.x,s2.y+s2.h+1,1, y-s2.y-s2.h-2);
				couloirs.add(couloirver);
			}else if(y-s2.y<0){
				Salle couloirver = new Salle(s2.x, y+2,1, s2.y-y-3);
				couloirs.add(couloirver);
			}
		}else if(x-s2.x<0){
			Salle couloirhor = new Salle(x,s2.y,s2.x-x-1, 1);
			couloirs.add(couloirhor);
			if(s2.y-y>0){
				Salle couloirver = new Salle(x,y+h+1,1, s2.y-y-h-2);
				couloirs.add(couloirver);
			}else if(s2.y-y<0){
				Salle couloirver = new Salle(x,s2.y+2,1, y-s2.y-3);
				couloirs.add(couloirver);
			}
		}
	}else if(Math.abs(x-s2.x)>Math.abs(y-s2.y)){
		if(y-s2.y>0){
			Salle couloirver = new Salle(x, s2.y, 1 ,y-s2.y-1);
			couloirs.add(couloirver);
			if(x-s2.x>0){
				Salle couloirhor = new Salle(s2.x+s2.l+1, s2.y, x-s2.x-s2.l-2,1);
				couloirs.add(couloirhor);
			}else if(x-s2.x<0){
				Salle couloirhor = new Salle(x+2, s2.y,s2.x-x-2,1);
				couloirs.add(couloirhor);
			}	
		}else if(y-s2.y<0){
			Salle couloirver = new Salle(s2.x, y, 1 ,s2.y-y-1);
			couloirs.add(couloirver);
			if(x-s2.x<0){
				Salle couloirhor = new Salle(x+l+1, y, s2.x-x-l-2,1);
				couloirs.add(couloirhor);
			}else if(x-s2.x>0){
				Salle couloirhor = new Salle(s2.x+2, y,x-s2.x-2,1);
				couloirs.add(couloirhor);
		}	
		}
	}
	if(x-s2.x==0){
		if(y-s2.y>0){
			Salle couloirver = new Salle(x, s2.y+s2.h+1, 1, y-s2.y-s2.h-2);
			couloirs.add(couloirver);
		}else if(y-s2.y<0){
			Salle couloirver = new Salle(x, y+h+1, 1, s2.y-y-h-2);
			couloirs.add(couloirver);
		}
	}
    if(y-s2.y==0){
		if(x-s2.x>0){
			Salle couloirhor = new Salle(s2.x+s2.l+1, y, s2.x-x-l-2,1);
			couloirs.add(couloirhor);
		}else if(x-s2.x<0){
			Salle couloirhor = new Salle(x+l+1, y, x-s2.x-s2.l-2,1);
			couloirs.add(couloirhor);
		}
	}
	return(couloirs);

}
}
	
