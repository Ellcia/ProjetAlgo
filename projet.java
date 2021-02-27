import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class projet {
    static Random random = new Random();
	public static void main(String args[]) {
      
        int vsup = (int)Math.pow(10,2), // les valeurs des objets seront au hasard dans [0:vsup+1]
         csup = (int)Math.pow(10,3), // la capacité du sac sera au hasard dans [0:csup+1]
         nsup = (int)Math.pow(10,2); // le nombre d'objets sera au hasard dans [0:nsup+1]
         
      int   N = (int)Math.pow(10,6); // nombre de sacs aléatoires de la validation statistique
      // (N est le nombre de "runs" de la validation statistique)

      int[] valeursSGV = new int[N], // valeurs des N sacs aléatoires, "gloutons par valeurs"
            valeursSGD = new int[N]; // valeurs des N sacs aléatoires, "gloutons par densités"
      for (int r = 0; r < N; r++){ // r est le numéro du "run" de la validation statistique
         if (r%1000==0) System.out.print("."); // mettre un peu d'animation dans l'exécution...
         // génération d'un ensemble d'objets aléatoires en valeurs et tailles
         int n = random.nextInt(nsup+1); // nombre d'objets au hasard dans [0:nsup+1]
         int c = random.nextInt(csup+1),  // taille du sac aléatoire dans [0:csup+1]
         // Attention : erreur dans la ligne ci-dessous.
         // tsup  = c/10 ; // les objets seront de taille aléatoire dans [1:tsup+1]
         // La remplacer par :
         	tsup = 1 + c/10;   
         // Un ensemble de n objets aléatoires, valeurs dans [0:vsup+1] et tailles dans [1:tsup+1]
         objet[] objets = ObjetsAleatoires(n,vsup,tsup); 
         // remarque : pas d'objet de taille 0. Ils conduiraient à une densité v/t infinie.
         boolean[] sgpv = sacGloutonParValeurs(objets, c); // sac de contenance c, "glouton par valeurs"
         boolean[] sgpd = sacGloutonParDensites(objets, c); // id., "glouton par densités de valeurs"
         int valeurSGV = valeurDuSac(sgpv, objets), // valeur du sac "glouton par valeur"
            valeurSGD = valeurDuSac(sgpd, objets);   // valeur du sac "glouton par densités"   
         valeursSGV[r] = valeurSGV ; 
         valeursSGD[r] = valeurSGD ;
      }
      System.out.println();
      int medianeRecursive_gpv = medianeRecursive(valeursSGV), 
      medianeRecursive_gpd = medianeRecursive(valeursSGD);
      int medianeIterative_gpv = medianeIterative(valeursSGV), 
      medianeIterative_gpd = medianeIterative(valeursSGD);
      float    moyenne_gpv = moyenne(valeursSGV),
            moyenne_gpd = moyenne(valeursSGD);
      System.out.printf("medianes récursive et iterative et moyenne des valeurs des sacs : \n");
      System.out.printf("Sacs glouton par valeurs : %d, %d, %d\n", 
         medianeRecursive_gpv, medianeIterative_gpv, (int)moyenne_gpv);
      System.out.printf("Sacs glouton par densité : %d, %d, %d\n", 
         medianeRecursive_gpd, medianeIterative_gpd, (int)moyenne_gpd);
      EcrireDansFichier(valeursSGV,"valeursSGV.csv");
      EcrireDansFichier(valeursSGD,"valeursSGD.csv");
      System.out.println("Valeurs des sacs \"gloutons par valeurs\" dans le fichier " + "valeursSGV.csv");
      System.out.println("Valeurs des sacs \"gloutons par densités\" dans le fichier " + "valeursSGD.csv");
      // Pour générer les histogrammes : ouvrir une fenêtre terminal, 
      // se placer dans le répertoire contenant les fichiers "valeursSGV.csv" et "valeursSGD.csv"
      // et le fichier   histogramme.py
      // et lancer la commande  "python histogramme.py" (python 2.x)
      // ou "python3 histogramme.py" (python 3.x)
   	
      
	}

    // Exercice 1 : CALCUL DE LA MEDIANE

   public static int quickSelectRecursif(int p, int[] T){ // 1 <= p <= n;
   	int n = T.length;
   	return qselRecursif(p-1, T, 0, n);
   }
   static int qselRecursif(int p, int[] T, int i, int j){
    	int m = j - i; // 0 <= p < m
    
    	if (m==1 &&p==0){
        	return T[i]; //Taille du tableau < 2 donc seulement un élément et on le retourne
    	}
    	int k=segmenter(T, i, j); // on segmente le tableau
        
    	if (i <= p+i && (p + i) < k) // p + i est dans la partie gauche du tableau, on recommence la fonction avec un tableau réduit (allant de  i a k)
        	{return qselRecursif(p, T, i, k);}
        	
        if (p+i<=k && p+i<k+1) // p + i == k et k est a la bonne position, donc on retourne k
        	{return T[k];}  
        
    //	else if (k+1 <= (p+i) && (p + i) < j ) // p + i est dans la partie droite du tableau, on recommence la fonction avec un tableau réduit (allant de  k+1 a j)
        	return qselRecursif(p-(k-i), T, k, j);
   	
    }
   
   static int qselIteratif(int p, int[] T){ 
      int n = T.length; // 0 <= p < n 
      int pprime = p,  i = 0,  j = n ; // I(p’, i, j)
      while (!(pprime==0 && j-i==1)) { // I(p’,i,j) et non arrêt
          int k = segmenter(T, i, j); 
          int pppi = pprime + i ;
          if ( i <= pppi && pppi < k ) // I(p',i,k)
             {j=k;} // I(p’, i, j)
          else
          if ( k <= pppi && pppi < k+1 ) // I(0, k, k+1)
             { pprime=0;
             i=k;
             j=k+1;} // I(p’, i, j)
          else //  k+1 <= pppi && pppi < j ) // I(p'-(k+1-i),k+1,j)
             { pprime= pprime-(k+1-i); i=k+1;} // I(p’, i, j)
      // I(p’, i, j) et arrêt, donc la p-ème valeur de T[ 0 : n ] est T[i]
      }
      return T[i];
   }
   public static int quickSelectIteratif(int p, int[] T){ int n = T.length;
      // 1 <= p <= n;
      return qselIteratif(p-1, T);
   }
  

   static int medianeRecursive(int[] T){ int n = T.length;
   /* Retourne la valeur médiane de T[0:n]. C'est la valeur m du tableau
   telle que T contient autant de valeurs <= à m que de valeurs > à m.
   Exemple  : 0,1,2,3 ==> médiane = 1 (indice (4-1)/2 = 1)
               0,1,2 ==> médiane = 1 (indice (3-1)/2 = 1 )
   De façon générale : avec la convention 0 <= p < n, la valeur médiane est la 
   p = (n-1)/2 ème valeur de T.
   */       
      return quickSelectRecursif(1 + (n-1)/2, T); 
      // ou si l'on préfère : qselRecursif((n-1)/2, T, 0, T.length) 
   }
   static int medianeIterative(int[] T){ int n = T.length;
   /* Retourne la valeur médiane de T[0:n]. C'est la valeur du tableau
   telle que T contient autant de valeurs <= à la médiane que de valeurs >= à m.
   Exemple  : 0,1,2,3 ==> médiane = 1 (indice (4-1)/2 = 3/2 = 1)
               0,1,2 ==> médiane = 1 (indice (3-1)/2 = 2/2 = 1 )
   De façon générale, avec la convention 0 <= p < n, la valeur médiane est la 
   p = (n-1)/2 ème valeur de T. 
   */       
      return quickSelectIteratif(1 + (n-1)/2, T);
      // ou si l'on préfère : qselIteratif((n-1)/2, T)
   }
    static int segmenter(int[] T, int i, int j){
    // calcule une permutation des valeurs de T[i:j] qui vérifie 
    // T[i:k] ≤ T[k:k+1] < T[k+1:j], et retourne l'indice k.
    // I(k,j') : T[i:k] ≤ T[k:k+1] < T[k+1:j']
        int h = hasard(i,j); 
        permuter(T,i,h);
        int k = i, jp = k+1; // I(k,j') est vraie
        while (jp < j)
            if (T[jp] > T[k]) // I(k,j'+1) est vraie
                jp = jp+1;
            else { permuter(T,jp,k+1); 
                permuter(T,k+1,k);
                // I(k+1,j'+1) est vraie
                k = k+1; // I(k,j'+1) est vraie
                jp = jp+1; // I(k,j') est vraie
            }
            // I(k,j) vraie, i.e. T[i:k] ≤ T[k:k+1] < T[k+1:j]
        return k;
    }    
    static int hasard(int i, int j){ Random r = new Random();
    // retourne un indice au hasard dans l'intervalle [i:j]
        return i + r.nextInt(j-i); // nextInt(j-i) est dans [0:j-i]
    }
    static void permuter(int[] T, int i, int j){
        int ti = T[i];
        T[i] = T[j];
        T[j] = ti;
    }

    // EXERCICE 2 : 
   // Un objet est défini par son numéro i, sa valeur v, sa taille t, sa densité v/t
   static class objet{int i, v, t; float d;
    objet(int i, int v, int t, float d){ 
       this.i = i; this.v = v; this.t = t; this.d = d;
    }
 }
 // Un ensemble de n objets aléatoires à valeurs et tailles  dans [0:vsup+1] et [1:tsup+1]
 static objet[] ObjetsAleatoires(int n, int vsup, int tsup){
    objet[] E = new objet[n]; // ensemble de n objets
    for(int i =0; i<n; i++)
    {
      int v = hasard(0,vsup+1);
      int t = hasard(1,tsup+1);

      objet p = new objet(i,v,t,v/t); // on crée un nouvel objet de valeur v comprise entre 0 et vsup+1 et de taille t comprise entre 1 et tsup+1
  
       E[i] = p; //l'objet est ajouté à l'ensemble
    }

     return E;
 }
 
 static int valeurDuSac(boolean[] sac, objet[] Objets){int n = sac.length;
    objet[] ooi = new objet[n]; // objets dans l'ordre initial : 
    // ooi[0] est l'objet de numéro i = 0, ..., ooi[n-1] est l'objet de numéro n-1.
    for (int i =0; i< n;i++){
       ooi[i]=Objets[i];
    }

    // calcul de la valeur du sac
    int vds = 0; // valeur du sac 
    for (int i =0; i< n;i++){
       if(sac[i]){
          vds=vds+ooi[i].v; // si il'objet est dans le sac, sa valeur s'ajoute à la valeur du sac 
       }
    }

    return vds;
 }
 static boolean[] sac(objet[] Objets, int c){ 
 // Objets triés par valeurs décroissantes ou par densités décroissantes.
 // Retourne un sac glouton selon le critère du tri.
    int n = Objets.length, // cardinal de l'ensemble des objets
       r = c; // capacité disponible (restante) dans le sac ;
    boolean[] sac = new boolean[n];
    for (int i =0; i< n;i++)
  {
      if(Objets[i].t <= r) //si la taille de l'objet de dépasse pas celle restante du sac, alors il est mis dans le sac
      {
      r-= Objets[i].t;
      sac[i] = true;
      }
      else //sinon il n'est pas mis dans le sac
      {sac[i] = false;}
  }

    return sac;
 }
 static boolean[] sacGloutonParValeurs(objet[] Objets, int c){
    qspvd(Objets); // tri quicksort des objets par valeurs décroissantes
    return sac(Objets, c); // sac glouton par valeurs.
 }
 static boolean[] sacGloutonParDensites(objet[] Objets, int c){
    qspdd(Objets); // tri quicksort des objets par densités décroissantes
    return sac(Objets, c); // sac glouton par densités décroissantes
 }
 static void qspvd(objet[] Objets){// quickSort des objets par valeurs décroissantes
    qspvd(Objets, 0, Objets.length);
 }
 static void qspdd(objet[] Objets){// quickSort des objets par densités décroissantes
    qspdd(Objets, 0, Objets.length);
 }
 static void qspvd(objet[] Objets, int i, int j){
 // quicksort par valeurs décroissantes de Objets[i:j]
    if (j-i < 2) return; // valeurs de Objets[i:j] sont décroissantes
      int k = spvd(Objets, i, j); //  valeurs de Objets[i:k] >=  valeurs de Objets[k:k+1] >  valeurs de Objets[k+1:j]
      qspvd(Objets,i,k); //  valeurs de Objets[i:k] sont décroissantes
      qspvd(Objets,k+1,j); //  valeurs de Objets[k+1:j] sont décroissantes


      //  valeurs de Objets[i:k] et  valeurs de Objets[k+1:j] décroissantes et  valeurs de Objets[i:k] >=  valeurs de Objets[k:k+1] > valeurs de Objets[k+1:j]
      // donc  valeurs de Objets[i:j] décroissantes.

 }
 static void qspdd(objet[] Objets, int i, int j){// quicksort par densites décroissantes
    if (j-i < 2) return; // densités de Objets[i:j] sont décroissantes
      int k = spdd(Objets, i, j); //  densités de Objets[i:k] >=  densités de Objets[k:k+1] >  densités de Objets[k+1:j]
      qspdd(Objets,i,k); //  densités de Objets[i:k] sont décroissantes
      qspdd(Objets,k+1,j); //  densités de Objets[k+1:j] sont décroissantes


      //  densités de Objets[i:k] et  densités de Objets[k+1:j] décroissantes et  densités de Objets[i:k] >=  densités de Objets[k:k+1] > densités de Objets[k+1:j]
      // donc  densités de Objets[i:j] décroissantes.

 }
 static int spvd(objet[] Objets, int i, int j){
 // segmentation de Objets[i:j] par valeurs décroissantes
    // I(k,jp) : 
    //   valeurs de Objets[i:k] >= valeurs de Objets[k] > valeurs de Objets[k+1:jp]
      int h = hasard(i,j);
      permuter(Objets,i,h);
      int k = i, jp = k+1; // I(k,j') est vraie
      while (jp < j)
          if (Objets[jp].v < Objets[k].v) // I(k,j'+1) est vraie
              jp = jp+1;
          else { permuter(Objets,jp,k+1);
              permuter(Objets,k+1,k);
              // I(k+1,j'+1) est vraie
              k = k+1; // I(k,j'+1) est vraie
              jp = jp+1; // I(k,j') est vraie
          }


          // I(k,j) vraie, donc valeurs de Objets[i:k] >= valeurs de Objets[k:k+1] > valeurs de Objets[k+1:j]
    
    return k;
 }
 static int spdd(objet[] Objets, int i, int j){
 // segmentation de Objets[i:j] par densités décroissantes
    // I(k,jp) : 
    //   densités de Objets[i:k] >= densités de Objets[k:k+1] > densités de Objets[k+1:jp]  
    int h = hasard(i,j);
      permuter(Objets,i,h);
      int k = i, jp = k+1; // I(k,j') est vraie
      while (jp < j)
          if (Objets[jp].d < Objets[k].d) // I(k,j'+1) est vraie
              jp = jp+1;
          else { permuter(Objets,jp,k+1);
              permuter(Objets,k+1,k);
              // I(k+1,j'+1) est vraie
              k = k+1; // I(k,j'+1) est vraie
              jp = jp+1; // I(k,j') est vraie
          }


          // I(k,j) vraie, donc densités de Objets[i:k] >= densités de Objets[k:k+1] > densités de Objets[k+1:j]
    return k;
 }
 static void permuter(objet[] Objets, int i, int j){
    objet x = Objets[i]; Objets[i] = Objets[j]; Objets[j] = x;
 }   
 
 static void EcrireDansFichier(int[] V, String fileName){
    try { int n = V.length;
       PrintWriter ecrivain;
        ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        for (int i = 0; i < n-1; i++)
             ecrivain.println(V[i]);
         ecrivain.println(V[n-1]);
        ecrivain.close();
     }
    catch(IOException e){ System.out.println("Erreur écriture");}
   }
// CALCUL DE LA MOYENNE    
  static float moyenne(int[] T){int n = T.length;
     float s = 0; 
     for (int i = 0; i<n; i++) s = s + T[i];  
     return s/n;
  }
     
  // PROCEDURES NON UTILISEES mais utiles lors de la phase de mise au point du programme
  static void afficher(String s){System.out.print(s);}; 
  static void afficher(boolean[] B){ int n = B.length;
     for (int i = 0; i < n; i++) if (B[i]) System.out.print(i + " ");
     System.out.println();
  }
  static void afficher(int[] T){ int n = T.length;
     for (int i = 0; i < n; i++) System.out.print(T[i] + " ");
     System.out.println();
 }
 static void afficher(objet[] T){ int n = T.length;
    afficher("i-v-t-d : ");
    for (int i = 0; i < n; i++){ objet o = T[i];
       System.out.printf("%d-%d-%d-%f | ", o.i, o.v, o.t, o.d);
    }
    System.out.println();
 }
 static void newline(){System.out.println();}
 
 
}


