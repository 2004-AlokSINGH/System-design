package creational.prototype;

public class Client {

    public static void main(String[] args) {
        
        EnemyRegistry enemyRegistry=new EnemyRegistry();

        enemyRegistry.register("flying",new Enemy(false, 100, "sword"));
        enemyRegistry.register("gunner", new Enemy(false, 55, "gun"));


        // clone from registery

        Enemy e1=enemyRegistry.get("gunner");

        e1.printStat();

        Enemy e2=enemyRegistry.get("flying");
        e2.printStat();
        // get direct clone
        Enemy e3=e2.clone();
        e3.printStat();

    }
    
}
