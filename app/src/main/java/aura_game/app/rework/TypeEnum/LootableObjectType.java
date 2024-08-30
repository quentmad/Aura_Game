package aura_game.app.rework.TypeEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum LootableObjectType {
        stick(32,3, "resource",1,"Ceci est un baton de bois. Tu peux t'en servir pour construire des trucs !"),
        branch(32,0, "resource",2,"Ceci est une branche. Tu peux t'en servir pour construire des trucs !"),
        leaf(32,0, "resource",1,"Ceci est une feuille. Tu peux t'en servir pour construire des trucs !"),
        apple(32,1, "food",1,"Ceci est une pomme."),
        rock(32,2, "resource",1,"C'est un caillou, tu peux crafter des choses avec."),
        //TOOLS (avec ToolType en +)
        hache(64,18, "tool",1,"C'est une hache en pierre, tu peux t'en servir pour casser du bois... "),
        marteau(64,19, "tool",1,"C'est un marteau."),
        pioche(64,19, "tool",1,"C'est une pioche, tu peux casser la pierre ! Trop bien !"),
        pelle(64,15, "tool",1,"C'est une pelle, tu peux creuser quoi ! "),

        flail(64,23, "melee",1,"C'est une flail !"),
        hache_de_guerre(64,19, "melee",2,"C'est une hache de guerre. C'est un peu comme une double hache !"),
        mace(64,23, "melee",1,"C'est une mace."),
        simple_staff(64,14, "melee",2,"C'est un simple staff."),
        diamond_staff(64,16, "melee",2,"C'est un diamond staff."),
        gnarled_staff(64,16, "melee",2,"C'est un gnarled staff."),
        loop_staff(64,12, "melee",2,"C'est un loop staff."),
        longue_lance(64,21, "melee",2,"C'est une longue lance."),

        magic(64,17, "melee",2,"C'est une magic."),
        magic_alt(64,7, "melee",2,"C'est une magic_alt."),
        trident(64,9, "melee",2,"C'est un trident."),
        spear_metal(64,19, "melee",2,"C'est une spear metal."),
        spear_wooden(64,19, "melee",2,"C'est une spear wooden."),

        arc_normal(64,21, "ranged",2,"C'est un arc."),
        arc_great(64,20, "ranged",2,"C'est un super arc ."),
        fleches(64,26, "ammunition",1,"Ce sont des fleches."),
        arbalette(64,20, "ranged",1,"C'est une arbalette."),

        longue_epee(64,18, "melee",2,"C'est une longue epee."),
        katana(64,22, "melee",2,"C'est un katana."),
        sabre(64,17, "melee",1,"C'est un sabre."),
        rappier(64,16, "melee",1,"C'est une rappier."),
        poignard(64,25, "melee",1,"C'est un poignard.")


    ;


    /**Nom pour charger le fichier png*/

    //  From AbstractObject
    private final int imageWidth;
    private final int imageHeight;
    private final int contentImageWidth;
    private final int contentImageHeight;
    private final int offsetY;

    //FROM LootableObject
    /**Type: resource, weapon, food */
    private final String type;
    private final String texturePath;
    private final String shadowTexturePath;
    private Texture texture;

    /**Description, pour l'inventaire*/
    private final String description;

    LootableObjectType(int imageWidth, int imageHeight, int contentImageWidth, int contentImageHeight, int offsetY, String type, int shadowSize,  String description) {
        if(!type.equals("tool") && !type.equals("melee") && !type.equals("ranged") && !type.equals("ammunition")){
            this.texturePath = "src/main/resources/Loots/"+this.name()+".png";
        }else{
            this.texturePath = "src/main/resources/Weapons/"+this.name()+".png";
        }
        if(shadowSize ==1){
            this.shadowTexturePath = "src/main/resources/Loots/shadow.png";
        }else{
            this.shadowTexturePath = "src/main/resources/Loots/shadowMed.png";
        }
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.contentImageWidth = contentImageWidth;
        this.contentImageHeight = contentImageHeight;
        this.offsetY = offsetY;
        this.type = type;
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.description = description;
    }


    LootableObjectType(int size, int offsetY, String type, int shadowSize,  String description) {
        if(!type.equals("tool") && !type.equals("melee") && !type.equals("ranged") && !type.equals("ammunition")){
            this.texturePath = "src/main/resources/Loots/"+this.name()+".png";
        }else{
            this.texturePath = "src/main/resources/Weapons/"+this.name()+".png";
        }
        if(shadowSize ==1){
            this.shadowTexturePath = "src/main/resources/Loots/shadow.png";
        }else{
            this.shadowTexturePath = "src/main/resources/Loots/shadowMed.png";
        }
        this.imageWidth = size;
        this.imageHeight = size;
        this.contentImageWidth = size;
        this.contentImageHeight = size;
        this.offsetY = offsetY;
        this.type = type;
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.description = description;
    }

    public String type() {
        return type;
    }

    public String texturePath() {
        return texturePath;
    }

    public String shadowTexturePath() {
        return shadowTexturePath;
    }

    public Texture texture(){
        return texture;
    }

    public String description() {
        return description;
    }

    public int imageWidth() {
        return imageWidth;
    }

    public int imageHeight() {
        return imageHeight;
    }

    public int contentImageWidth() {
        return contentImageWidth;
    }

    public int contentImageHeight() {
        return contentImageHeight;
    }

    public int offsetY() {
        return offsetY;
    }


}
