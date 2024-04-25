package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.math.Rectangle;
//TODO prendre en compre player

import aura_game.app.Objects.CollidableObject;

/**Cette classe permet de gérer les hitbox Rectangle de colission des Entités*/
public class Grid {

    /**Grille des entités. Chaque case (caseSize*caseSize) contient une liste (entités étant dedans). 
     * Rappel: tab[y] [x]*/
    private ListCollidableObject[][] grid;//Car on peut pas faire un tab2D de liste
    /*Grille des entités en superposition entre deux cases: dans la case 1 2 on retrouve toutes les entités en partie sur cette case */
    private ListCollidableObject[][] gridSuperposition;
    private int caseSize;//0-caseSize-1, caseSize à 2*caseSize-1
    /**Taille de la 2e [] de grid et gridSuperposition: X */
    private int sizeW; 
     /**Taille de la 1ʳᵉ [] de grid et gridSuperposition :Y*/
    private int sizeH; 

    public Grid(int caseSize){

        //int mapW = Game.getInstance().getRegion().getRegionWidth();
        int mapW = 3840 ;
        int mapH = 2880;
        //int mapH = Game.getInstance().getRegion().getRegionHeight();
        this.sizeH = (int)(mapH/caseSize)+1;//+1 au cas où
        this.sizeW = (int)(mapW/caseSize)+1;
        this.grid = new ListCollidableObject[sizeH][sizeW];
        this.gridSuperposition = new ListCollidableObject[sizeH][sizeW];
        //System.out.println(sizeH + "of" + sizeW );
        this.caseSize = caseSize;

        // Initialisez chaque case de la grille et de la grille de superposition
        for (int i = 0; i < sizeH; i++) {
            for (int j = 0; j < sizeW; j++) {
                grid[i][j] = new ListCollidableObject();
                gridSuperposition[i][j] = new ListCollidableObject();
            }
        }
    }

    /**Permet de connaitre la case (ou les cases de superpositions) de l'entité à partir de son rectangle hitbox (maj), 
     * @param rect le hitbox de l'objet dont on souhaite avoir la/s case (ou celles des superpositions s'il y en a plusieurs)
     *@return la liste des pairs numéros de case: s'il y en a 1 : c'est la grille principale, sinon, c'est la grille de superposition
     * Stocker cette Liste dans le CollidableObject permet de comparer avec la nouvelle pour savoir si une mise à jour est nécessaire,
     * Permet également de remove en mettant en parametre de remove cette liste-là
     * Rappel : tableau2D[y][x]
     */
    public List<Pair<Integer,Integer>> getCaseFor(Rectangle rect){
        List<Pair<Integer,Integer>> l = new ArrayList<>();
        int xd = (int)(rect.getX() /caseSize);//Case correspondante (la partie entiere)
        int xf = (int)((rect.getX()+rect.getWidth())/caseSize);
        int yd = (int)(rect.getY() /caseSize);
        int yf = (int)((rect.getY()+rect.getHeight())/caseSize);  
        for (int i = yd; i <= yf; i++){      
            for(int j = xd; j <= xf; j++ ){

                l.add(Pair.of(i, j));
            }
        }
        return l;
    }


    /**Utile pour getClosestObjectFrom()
     * Donne les paires correspondant aux cases voisines des cases de la liste.
     *
     * Cette méthode calcule et donne les indices des cases qui entourent les cases
     * spécifiées dans la liste gridIndex. Ce sont les cases situées à gauche, à droite,
     * en haut et en bas des cases limitrophes de la grille spécifiées, et qui ne dépassent pas les limites de la grille.
     * @param gridIndex La liste des indices des cases d'un rectangle sur la grille.
     * @return La liste des indices des cases voisin.
     */
    public List<Pair<Integer, Integer>> getNeighborsCasesFor(List<Pair<Integer, Integer>> gridIndex) {
        List<Pair<Integer, Integer>> gridSuperposition = new ArrayList<>();
        int minX = gridIndex.get(0).getRight() - 1;
        int minY = gridIndex.get(0).getLeft() - 1;
        int maxX = gridIndex.get(gridIndex.size() - 1).getRight() + 1;
        int maxY = gridIndex.get(gridIndex.size() - 1).getLeft() + 1;
        // Parcourir le rectangle composé des coins minX, minY, maxX et maxY
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if(x >= 0 && y >=0 && x <= sizeW && y <= sizeH ){
                    gridSuperposition.add(Pair.of(y, x));
                }
            }
        }
    
        return gridSuperposition;
    }
    

    /**
     * Retourne une liste d'objets en collision avec une zone de collision donnée, selon leur rectangle hitbox.
     *
     * @param zoneColission La zone de collision à tester.
     * @param itself L'objet dont on ne veut pas vérifier la collision.
     * @return Une liste des Collidable en collision avec la zone de collision spécifiée.
     */
    public ListCollidableObject getCollidingObjectsWithoutItself(Rectangle zoneColission, CollidableObject itself) {
        //Cases que survole zoneColission
        List<Pair<Integer, Integer>> pairsIndex = getCaseFor(zoneColission);
        ListCollidableObject inSameCases= new ListCollidableObject();
        ListCollidableObject objInColission = new ListCollidableObject();
        //On met dans une liste tous les objets présent (ou en partie au moins) dans les cases que survole zoneColission
        for (Pair<Integer, Integer> index : pairsIndex) {
            inSameCases.addAll(grid[index.getLeft()][index.getRight()]);
            inSameCases.addAll(gridSuperposition[index.getLeft()][index.getRight()]);
        }

        for (int i = 0; i < inSameCases.size(); i++) {
            if(isInZone(zoneColission, inSameCases.get(i))){
                objInColission.add(inSameCases.get(i));
            }
        }
        objInColission.remove(itself);
        return objInColission;
    }



    /**
     * Vérifie si un rectangle donné chevauche ou contient un autre rectangle, donc si objCol est présent dans la zone.
     *
     * @param zone Le rectangle à vérifier pour une collision.
     * @param objCol L'objet représenté par un rectangle (habituellement sa hitbox) avec lequel la collision est vérifiée.
     * @return {@code true} si une collision est détectée, {@code false} sinon.
     */
    public boolean isInZone(Rectangle zone, CollidableObject objCol){
        return (zone.overlaps(objCol.getHitboxFlat()));
    }

    /**TODO: attention pour l'instant si il ni a aucun objet dans les cases principales et voisines: on arrete le test là...
     * Rend l'object le plus proche de la hitboxFlat de objCible
     * @param objCible objet dont on souhaite connaitre l'objet le plus proche
     * @return
     */
    public CollidableObject getClosestObjectFrom(CollidableObject objCible){
        //On récupère tous les objets présent (au moins partiellement) dans l/es cases où est présent l'objet cible
        List<Pair<Integer, Integer>> pairsIndex = getCaseFor(objCible.getHitboxFlat());
        ListCollidableObject inSameCases= new ListCollidableObject();
        //On met dans une liste tous les objets présent (ou en partie au moins) dans les cases que survole zoneColission
        for (Pair<Integer, Integer> index : pairsIndex) {
            inSameCases.addAll(grid[index.getLeft()][index.getRight()]);
            inSameCases.addAll(gridSuperposition[index.getLeft()][index.getRight()]);
        }
        //Garde le CollidableObject dont la distance avec objCible est le plus court
        Pair<CollidableObject, Double> closestO= closestObject(inSameCases, objCible); 
        //Maintenant on verifie avec tous les objets dans les cases voisines qui peuvent potentiellement etre plus proche: 
        List<Pair<Integer, Integer>> neighborsCases = getNeighborsCasesFor(pairsIndex);
        ListCollidableObject neighborsObj = new ListCollidableObject();
        for (Pair<Integer, Integer> neighborsCase : neighborsCases) {
            //Si il ni avait aucun objet dans les cases principale, ou qu'une case voisine se trouve moins loin que la distance avec l'objet le plus proche actuel
            if (closestO.getRight() == 999999 || closestO.getRight() > (distanceBetween(objCible.getHitboxFlat(), new Rectangle(neighborsCase.getRight(), neighborsCase.getLeft(), 1, 1)))) {
                neighborsObj.addAll(grid[neighborsCase.getLeft()][neighborsCase.getRight()]);
                neighborsObj.addAll(gridSuperposition[neighborsCase.getLeft()][neighborsCase.getRight()]);
            }
        }
        Pair<CollidableObject, Double> neighborsclosest = closestObject(neighborsObj, objCible);
        if (neighborsclosest.getRight() < closestO.getRight()){
            return neighborsclosest.getLeft();
        }
        return closestO.getLeft();
    }

    /**Donne l'obj et la distance ayant la distance le plus court entre son hitboxflat et celui de l'objCible
     * @param potentialObj la liste des objets qu'on veut tester
     * @param objCible l'objet cible
     * @return la pair de l'obj et sa distance
     */
    public Pair<CollidableObject, Double> closestObject(ListCollidableObject potentialObj, CollidableObject objCible){
        double closest = 999999;
        CollidableObject closestObj = null;
        if(potentialObj.size() > 0){
            for (int i = 0; i < potentialObj.size(); i++) {
                double newdist = distanceBetween(objCible.getHitboxFlat(), potentialObj.get(i).getHitboxFlat());
                if(  newdist < closest){
                    closest = newdist;
                    closestObj = potentialObj.get(i);
                }
            }
        }
        return Pair.of(closestObj, closest);
    }

    /**@return la distance entre les centre des 
     * @param rect1
     * @param rect2
     */
    public double distanceBetween(Rectangle rect1, Rectangle rect2) {
        // Les coordonnées du centre des rectangles
        double centerX1 = rect1.getX() + rect1.getWidth() / 2.0;
        double centerY1 = rect1.getY() + rect1.getHeight() / 2.0;
        double centerX2 = rect2.getX() + rect2.getWidth() / 2.0;
        double centerY2 = rect2.getY() + rect2.getHeight() / 2.0;

        // Calcul de la distance entre les centres des rectangles
        double deltaX = centerX2 - centerX1;
        double deltaY = centerY2 - centerY1;

        // Utilisation du théorème de Pythagore pour calculer la distance
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return distance;
    }

    /**
     * Appelé lorsque l'obj bouge et que son hitbox a été mis à jour. On teste si sa position dans grille/grille superposition a changé, 
     * si c'est le cas on appelle remove() avec la liste actuelle, on défini ensuite la liste (stocké dans CollidableObject) au getCaseFrom() et on lance add().
     * @param obj
     * @return true s'il a été mis à jour, false sinon
     */
    public boolean update(CollidableObject obj) {//PB: get et set non généralisé en cas de plusieurs grid
        List<Pair<Integer, Integer>> currentGridList = obj.getListIndexGrid(); // Liste actuelle
        List<Pair<Integer, Integer>> newGridList = getCaseFor(obj.getHitboxFlat()); // Nouvelle liste
        //System.out.println("Test if need update for" + obj.getPosC_X()  +" "+ obj.getPosC_X() );
        if (!currentGridList.equals(newGridList)) { // Vérifie si la position a changé
            remove(obj, currentGridList); // Supprime l'objet de la grille/grille superposition
            add(obj, newGridList); // Ajoute l'objet à la grille/grille superposition au(x) index suivant
            //System.out.println("yes, updated for \n \n" + obj.getPosC_X() +" "+ obj.getPosC_X() );
            return true;
        }
        return false;
    }


    /**Ajoute obj à la grille /grille superposition de la manière suivante:
     * @param obj le CollidableObject qu'on veut rajouter dans la grille, en fonction de son hitbox
     * @param listIndex la liste des index des cases. S'il y a plusieurs élements, on les ajoute dans la grille de superposition,
     * Sinon on l'ajoute à la grille principale
     */
    public void add(CollidableObject obj, List<Pair<Integer,Integer>> listIndex){
        if(listIndex.size() == 1){
            //System.out.print("1");
            grid[listIndex.get(0).getLeft()][listIndex.get(0).getRight()].add(obj);
        }else{
            for (Pair<Integer, Integer> index : listIndex) {
                //System.out.print(listIndex.size());
                gridSuperposition[index.getLeft()][index.getRight()].add(obj);
            }
        }
        obj.setListIndexGrid(listIndex);// Définit la nouvelle liste
    }


    /**Retire obj à la grille (notamment en cas de mise à jour) de la manière suivante:
     * @param obj le CollidableObject qu'on veut retirer de la grille, en fonction de son hitbox
     * @param listIndex la liste des index des cases où il est présent. S'il y a plusieurs élements, il est présent dans la grille de superposition,
     * Sinon, il est présent dans la grille principale
     */   
    public void remove(CollidableObject obj, List<Pair<Integer,Integer>> listIndex){
        if(listIndex.size() == 1){
            grid[listIndex.get(0).getLeft()][listIndex.get(0).getRight()].remove(obj);
        }else{
            for (Pair<Integer, Integer> index : listIndex) {
                gridSuperposition[index.getLeft()][index.getRight()].remove(obj);
            }
        }
    }



/**
 * Vérifie si un index est valide par rapport à la taille de la grille.
 * @param index l'index à vérifier
 * @return true si l'index est valide, false sinon
 */
/**
private boolean isValidIndex(Pair<Integer, Integer> index) {
    int x = index.getLeft();
    int y = index.getRight();
    return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
}
*/
}
