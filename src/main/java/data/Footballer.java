package data;

import java.util.List;

public class Footballer {

    private final String name;
    private int age;
    private final Gender gender;
    private final List<String> positions;

    public int getAge() {
        return age;
    }

    public Footballer(String name, int age, Gender gender, List<String> positions) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void increaseAge(){
        this.age++;
    }

    @Override
    public String toString(){
        return "\n" + "Footballer name: " + name + ", gender: " + gender + ", age: " + age + ", positions: " + positions;
     }

}
