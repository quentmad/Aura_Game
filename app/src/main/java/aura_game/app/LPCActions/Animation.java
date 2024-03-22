package aura_game.app.LPCActions;

/**Animation (U L D R)*/
public class Animation {

    private int indexY;
    private int lastIndexX;

    /**Durée de chaque frame, étant pareil dans chaque direction*/
    private int[] framesDuration;

    /** L'animation :
     *  {@code true}: s'execute une fois en entier (autonome) quelque soit le tps d'appuie sur la touche.
     *  {@code false}: s'execute tant que la touche est enfoncé */
    private boolean autonomeExecution;

    private int beginX;


    /**
     * //Decrit ce constructeur
     *
     */
    public Animation(int indexY, int lastIndexX, int[] framesDuration, boolean autonomeExecution) {
        this.indexY = indexY;
        this.lastIndexX = lastIndexX;
        this.framesDuration = framesDuration;
        this.autonomeExecution = autonomeExecution;
        this.beginX = 0;
    }

    /**Cas où tous les frames ont tous la meme durée : 1tps */
    public Animation(int indexY, int lastIndexX, boolean autonomeExecution) {
        this.indexY = indexY;
        this.lastIndexX = lastIndexX;
        this.framesDuration = new int[lastIndexX+1];
        for(int i = 0; i <= lastIndexX; i++){
            framesDuration[i] = 2;//default duration frame
        }
        this.autonomeExecution = autonomeExecution;
        this.beginX = 0;
    }

    public int getIndexY() {
        return indexY;
    }

    public int getLastIndexX() {
        return lastIndexX;
    }

    public int getBeginX(){
        return beginX;
    }

    public boolean isAutonomeExecution() {
        return autonomeExecution;
    }

    /**@return la position X de la frame numéro index*/
    public int getIndexYOf(String direction){
        return switch (direction){
            case "U" -> indexY;
            case "L" -> indexY+1;
            case "D" -> indexY+2;
            case "R" -> indexY+3;
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }

    /**@return la durée de la frame numéro index (la durée est la meme dans chaque direction)*/
    public int getFrameDuration(int index){
        return framesDuration[index];
    }

}
