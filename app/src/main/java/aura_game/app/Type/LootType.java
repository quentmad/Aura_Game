package aura_game.app.Type;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Enumeration définissant la structure des items (arbres,armes...)
 * Attributs:
 * @param texturePath Le chemin d'accès à la texture de l'item.
 * param shadowTexturePath Le chemin d'accès à la texture d'ombre de l'item.
 * @param differenceProf La différence entre le bas de l'image et le 1e pixel de l'item(en y)
 * @param hitboxTall La taille de l'item en hauteur (1m70...)
 * @param listPolygon la liste des points du polygon hitbox de l'item
 * @param listColor la liste des couleurs possible pour ce itemType (red, green)
 */
public enum LootType {
    
    stick("stick","resource",1,3,32,"Ceci est un baton de bois. Tu peux t'en servir pour construire des trucs !"),

    branch("branch","resource",2,0,32,"Ceci est une branche. Tu peux t'en servir pour construire des trucs !"),

    leaf("leaf","resource",1,0,32,"Ceci est une feuille. Tu peux t'en servir pour construire des trucs !"),

    apple("apple","food",1,1,32,"Ceci est une pomme."),

    rock("rock","resource",1,2,32,"C'est un caillou, tu peux crafter des choses avec."),

    //TOOLS (avec ToolType en +)
    hache("hache","tool",1,18,64,"C'est une hache en pierre, tu peux t'en servir pour casser du bois... "),
    marteau("marteau", "tool",1,19,64,"C'est un marteau."),
    pioche("pioche", "tool",1,19,64,"C'est une pioche, tu peux casser la pierre ! Trop bien !"),
    pelle("pelle", "tool",1,15,64,"C'est une pelle, tu peux creuser quoi ! "),
    
    flail("flail", "melee",1,23,64,"C'est une flail !"),
    hache_de_guerre("hache_de_guerre", "melee",2,19,64,"C'est une hache de guerre. C'est un peu comme une double hache !"),
    mace("mace", "melee",1,23,64,"C'est une mace."),
    simple_staff("simple_staff", "melee",2,14,64,"C'est un simple staff."),
    diamond_staff("diamond_staff", "melee",2,16,64,"C'est un diamond staff."),
    gnarled_staff("gnarled_staff", "melee",2,16,64,"C'est un gnarled staff."),
    loop_staff("loop_staff", "melee",2,12,64,"C'est un loop staff."),
    longue_lance("longue_lance", "melee",2,21,64,"C'est une longue lance."),

    //ICI
    magic("magic", "melee",2,17,64,"C'est une magic."),
    magic_alt("magic_alt", "melee",2,7,64,"C'est une magic_alt."),
    trident("trident", "melee",2,9,64,"C'est un trident."),
    spear_metal("spear_metal", "melee",2,19,64,"C'est une spear metal."),
    spear_wooden("spear_wooden", "melee",2,19,64,"C'est une spear wooden."),

    arc_normal("arc_normal", "ranged",2,21,64,"C'est un arc."),
    arc_great("arc_great", "ranged",2,20,64,"C'est un super arc ."),
    fleches("fleches", "ammunition",1,26,64,"Ce sont des fleches."),
    arbalette("arbalette", "ranged",1,20,64,"C'est une arbalette."),

    //TODO où mettre bouclier ?

    longue_epee("longue_epee", "melee",2,18,64,"C'est une longue epee."),
    katana("katana", "melee",2,22,64,"C'est un katana."),
    sabre("sabre", "melee",1,17,64,"C'est un sabre."),
    rappier("rappier", "melee",1,16,64,"C'est une rappier."),
    poignard("poignard", "melee",1,25,64,"C'est un poignard.");
    

    /**stick, leaf */
    private final String name;
    private final String texturePath;
    /**Utilisé pour affichage dans le menu crafting des lootType */
    private final Texture texture;
    private final String shadowTexturePath;
    /**Type: resource, weapon, food */
    private final String type;
    /**Premier pixel en y par rapport au bas de l'image */
    private final int offY;//offY = diffferenceProf
    /**description, pour l'inventaire*/
    private final String description;
    private final int width;
    //TODO ID

    //TODO: pour les armes, food... OU faire un armeType avec loottype dedans
    LootType(String name, String type, int sizeShadow, int offY, int width, String description) {
        this.name = name;
        if(type != "tool" && type !=  "melee" &&  type != "ranged" && type != "ammunition"){
            this.texturePath = "src/main/resources/Loots/"+name+".png";
        }else{
            this.texturePath = "src/main/resources/Weapons/"+name+".png";
        }
        if(sizeShadow ==1){
            this.shadowTexturePath = "src/main/resources/Loots/shadow.png";
        }else{
            this.shadowTexturePath = "src/main/resources/Loots/shadowMed.png";
        }
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.type = type;
        this.offY = offY;
        this.width = width;
        this.description = description;
    }


    public String getName() {
        return name;
    }


    public String texturePath() {
        return texturePath;
    }

    /**Utilisé pour affichage dans le menu crafting des lootType */
    public Texture getTexture(){
        return texture;
    }

    public String shadowTexturePath() {
        return shadowTexturePath;
    }


    public String type() {
        return type;
    }

    /**Premier pixel en y par rapport au bas de l'image */
    public int offY() {
        return offY;
    }


    public String description() {
        return description;
    }

    public int width(){
        return width;
    }

}
