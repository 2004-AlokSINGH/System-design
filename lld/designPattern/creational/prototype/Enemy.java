package creational.prototype;

public class Enemy implements EnemyProtoType {

    private boolean isAlive;
    private int healthPercent;
    private String weaponName;

    public Enemy(boolean isAlive, int healthPercent, String weaponName){
        this.isAlive=isAlive;
        this.healthPercent=healthPercent;
        this.weaponName=weaponName;
    }

    @Override
    public Enemy clone() {
        return new Enemy(isAlive, healthPercent, weaponName);
    }

   
    
    public void printStat() {
        System.out.printf("health %% : %d%n", this.healthPercent);
    }
}