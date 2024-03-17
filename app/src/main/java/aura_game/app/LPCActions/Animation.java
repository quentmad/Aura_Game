package aura_game.app.LPCActions;

/**Animation (U L D R)*/
public class Animation {

    private int indexY;
    private int lastIndexX;

    /**Durée de chaque frame, étant pareil dans chaque direction*/
    private int[] framesDuration;

    /** L'animation :
     *  {@code true}: s'execute une fois en entier quelque soit le tps d'appuie sur la touche.
     *  {@code false}: s'execute tant que la touche est enfoncé */
    private boolean singleExecution;

    private int beginX;


    /**
     * //Decrit ce constructeur
     *
     */
    public Animation(int indexY, int lastIndexX, int[] framesDuration, boolean singleExecution) {
        this.indexY = indexY;
        this.lastIndexX = lastIndexX;
        this.framesDuration = framesDuration;
        this.singleExecution = singleExecution;
        this.beginX = 0;
    }

    /**Cas où tous les frames ont tous la meme durée : 1tps */
    public Animation(int indexY, int lastIndexX, boolean singleExecution) {
        this.indexY = indexY;
        this.lastIndexX = lastIndexX;
        this.framesDuration = new int[lastIndexX+1];
        for(int i = 0; i <= lastIndexX; i++){
            framesDuration[i] = 2;//default duration frame
        }
        this.singleExecution = singleExecution;
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

    public boolean isSingleExecution() {
        return singleExecution;
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

/*
    public enum AnimationType{
        THRUST(4,7, new int[]{2,2,2,2,2,2,2}, true),
        WALK(8,8, new int[]{2,2,2,2,2,2,2,2}, false),
        SLASH(12,5, new int[]{2,2,2,3,4,5},true),
        SHOOT(16,12,new int[]{2,2,2,2,2,2,2,3,4,2,2,2,2}, true),
        HURT(20,1, new int[]{2}, true),
        JUMP(21,5, new int[]{2,2,2,2,2}, true),
        RUN(25,7, new int[]{2,2,2,2,2,2,2}, false),
        SIT(29, 2, new int[]{2,2}, false),
        IDLE(33, 1, new int[]{20,6}, false);

        private final int indexY;
        private final int lastIndexX;
        private final int[] framesDuration;
        private final boolean singleExecution;

        AnimationType(int indexY, int lastIndexX, int[] framesDuration, boolean singleExecution){
            this.indexY = indexY;
            this.lastIndexX = lastIndexX;
            this.framesDuration = framesDuration;
            this.singleExecution = singleExecution;
        }

        public int getIndexY() {
            return indexY;
        }

        public int getLastIndexX() {
            return lastIndexX;
        }

        public int[] getFramesDuration() {
            return framesDuration;
        }

        public boolean isSingleExecution() {
            return singleExecution;
        }
    }*/
}
