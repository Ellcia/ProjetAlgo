
// Exercice 1 : CALCUL DE LA MEDIANE

   static int qselRecursif(int p, int[] T, int i, int j){ int m = j-i; // 0 <= p < m
   int m = j-i; // 0 <= p < m
    
    if (m < 2){
        return T[i];
    }
      
    int k=segmenter(T, i, j);
    System.out.println("i et j et k et p:" + i + " " + j +" " + k + " " + p);
    
    if (p + i== k)
        {return T[k];}
        
    else if (i <= p + i && p + i < k)
        {return qselRecursif(p, T, i, k);}
        
    else if (k+1 <= p + i && p + i <= j )
        {return qselRecursif(p, T, k+1, j);}
        
    return T[k] ;
    }





   public static int quickSelectRecursif(int p, int[] T){ // 1 <= p <= n;
      int n = T.length;
      return qselRecursif(p-1, T, 0, n);
   }
   static int qselIteratif(int p, int[] T){ 
      int n = T.length; // 0 <= p < n 
      int pprime = p,  i = 0.,  j = n ; // I(p’, i, j)
      while (!(pprime>j && pprime<i)) { // I(p’,i,j) et non arrêt
          int k = segmenter(T, i, j); 
          int pppi = pprime + i ;
          if ( i < = pppi && pppi < k ) // I(…, …, …)
             { … } // I(p’, i, j)
          else
          if ( k <= pppi && pppi < k+1 ) // I(…, …, …)
             { …… } // I(p’, i, j)
          else //  k+1 <= pppi && pppi < j ) // I(…, …, …)
             { ……..} // I(p’, i, j)
      // I(p’, i, j) et arrêt, donc la p-ème valeur de T[ 0 : n ] est …
      return …;
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

