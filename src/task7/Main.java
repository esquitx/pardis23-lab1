package task7;

class Sword {

    synchronized void fight(Sword fighted) throws InterruptedException {
        System.out.println("Preparing to fight...");
        Thread.sleep(1000);
        fighted.flee();
        System.out.println("Finished the fight!");
    }

    synchronized void flee() throws InterruptedException {
        System.out.println("Preparing to fight...");
        Thread.sleep(1000);
        System.out.println("Oh no, he is running away!");

    }
}

class Fight extends Thread {
    private Sword sword1;
    private Sword sword2;

    // Initialize constructor
    public Fight(Sword sword1, Sword sword2) {
        this.sword1 = sword1;
        this.sword2 = sword2;
    }

    // Starts the fight
    @Override
    public void run() {

        try {
            sword1.fight(sword2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Sword excalibur = new Sword();
        Sword wilbur = new Sword();

        Thread fight1 = new Fight(excalibur, wilbur);
        fight1.start();
        Thread fight2 = new Fight(wilbur, excalibur);
        fight2.start();

    }

}
