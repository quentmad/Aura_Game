package aura_game.app.rework;

import aura_game.app.MyCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


/**
     * Représente une région du jeu avec sa carte, ses objets et ses entités.
     */
    public class Region {

        /**
         * La texture de la carte de la région
         */
        private Texture texture;

        /**
         * Largeur de la région
         */
        private int regionWidth;

        /**
         * Hauteur de la région
         */
        private int regionHeight;

        /**
         * camera de la region, permet de décaler la map...
         */
        private MyCamera cam;

        /**
         * Composant d'interaction de la région
         */
        private InteractionComponent interactionComponent;

        private PhysicsMovableComponent physicsComponent;

        public Region(String nameMap,String namePixMap) {
            this.texture = new Texture(Gdx.files.internal("src/main/resources/" + nameMap + ".png"));
            this.regionWidth = texture.getWidth();
            this.regionHeight = texture.getHeight();
            this.cam = new MyCamera(200,90);
            this.interactionComponent = new InteractionComponent();
            this.physicsComponent = new PhysicsMovableComponent(namePixMap);

        }

        public Texture texture() {
            return texture;
        }

        public MyCamera camera() {
            return this.cam;
        }

        public int regionWidth() {
            return regionHeight;
        }

        public int regionHeight() {
            return regionHeight;
        }

        public InteractionComponent interactionComponent() {
            return interactionComponent;
        }

        public PhysicsMovableComponent physicsMovableComponent() {
            return physicsComponent;
        }


        /**
         * Ajoute un bloc sur la carte de la région. Le bloc est ajouté si il n'y a pas de collision avec le sol ou les autres entités.
         *
         * @param block le bloc à ajouter
         * @return true si le bloc a été ajouté, false sinon
         */
        public boolean addBlockOnMap(BlockEntity block) {
            if(this.physicsMovableComponent().isColliding(block,block.posC()) == 0){//CheckColission avec le sol et autres entités
                block.setOnGround(true);
                this.interactionComponent().abstractObjectsOnGround().add(block);
                physicsComponent.gridBlockEntity().add(block, physicsComponent.gridBlockEntity().getCaseFor(block.hitbox().approximativeHitbox));//TODO TEST
                return true;
            }else{
                System.out.println("error during placement of block "+ block.name() +" in case " +block.posC().x()+ ", " +block.posC().y()+ ": collision detected ! \n");}
                return false;
        }

    /**
     * Ajoute un bloc sans colission sur la carte de la région. Le bloc est ajouté si il n'y a pas de collision avec le sol ou les autres entités.
     *
     * @param ablock le bloc animated à ajouter
     * @return true si le bloc a été ajouté, false sinon
     */
    public boolean addNonCollidableBlockOnMap(AnimatedBlockEntity ablock) {
        if(this.physicsMovableComponent().isColliding(ablock,ablock.posC()) == 0){//CheckColission avec le sol et autres entités
            ablock.setOnGround(true);
            this.interactionComponent().abstractObjectsOnGround().add(ablock);
            return true;
        }else{
            System.out.println("error during placement of animated block "+ ablock.name() +" in case " + ablock.posC().x()+ ", " + ablock.posC().y()+ ": collision detected ! \n");}
        return false;
    }



}


