package Core.Model;

public class Player {
    private String name;
    private int health;
    private int score;
    private int x;
    private int y;
    private int speed;

    public Player(String name, int x, int y) {
        this.name = name;
        this.health = 100;
        this.score = 0;
        this.x = x;
        this.y = y;
        this.speed = 5;
    }

    // Геттеры и сеттеры для полей класса

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }

    public void jump() {
        // Реализация прыжка
    }

    public void attack() {
        // Реализация атаки
    }

    public void takeDamage(int damage) {
        health -= damage; // Вычитаем у игрока здоровье
        if (health <= 0) {
            // Реализация смерти игрока
        }
    }

    public void collectCoin(int value) {
        score += value; // Добавляем игроку очки
    }


}
