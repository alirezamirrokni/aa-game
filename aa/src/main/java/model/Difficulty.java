package model;

public enum Difficulty {
    EASY("Easy", 5, 7000, 3.0, 5.0),
    MEDIUM("Medium", 10, 5000, 5.0, 10.0),
    HARD("Hard", 15, 3000, 10.0, 15.0);
    private String name;
    private int rotationSpeed;
    private int frozenTime;
    private double minAngle;
    private double maxAngle;

    private Difficulty(String name, int rotationSpeed, int frozenTime, double minAngle, double maxAngle) {
        this.name = name;
        this.rotationSpeed = rotationSpeed;
        this.frozenTime = frozenTime;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }


    public int getFrozenTime() {
        return frozenTime;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public static Difficulty getDifficultyByName(String name) {
        for (Difficulty difficulty : Difficulty.values())
            if (difficulty.name.equals(name))
                return difficulty;
        return null;
    }

    public double getMinAngle() {
        return minAngle;
    }

    public double getMaxAngle() {
        return maxAngle;
    }

    @Override
    public String toString() {
        return name;
    }
}
