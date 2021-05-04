package stream;

import data.Footballer;
import data.Gender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    List<Footballer> footballerList;

    @BeforeEach
    public void init() {
        this.footballerList = getFootballers();
    }

    @Test
    public void filter() {
        List<Footballer> collect = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.FEMALE))
                .filter(footballer -> footballer.getAge() > 23)
                .collect(Collectors.toList());

        Assertions.assertTrue(collect.size() == 2);
    }

    @Test
    public void map(){

        long femalesMoreThan24 = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.FEMALE))
                .map(Footballer::getAge)
                .filter(age -> age > 24)
                .count();
        //Number of footballers
        Assertions.assertEquals(2, femalesMoreThan24);
    }

    @Test
    public void flatMap(){
        //List<String> expected = List.of("CF", "CAM", "LF", "CM", "CAM", "GK", "CM", "CDM");
        String expected = "CF,CAM,LF,CM,CAM,GK,CM,CDM";
        String allPositionsOfMaleLessThan30y = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.MALE))
                .filter(footballer -> footballer.getAge() < 30)
                .map(Footballer::getPositions)
                .flatMap(Collection::stream)
                .collect(Collectors.joining(","));

        Assertions.assertEquals(expected, allPositionsOfMaleLessThan30y);
    }

    @Test
    public void shorter_flatMap(){

        String expected = "CF,CAM,LF,CM,CAM,GK,CM,CDM";
        String allPositionsOfMaleLessThan30y = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.MALE))
                .filter(footballer -> footballer.getAge() < 30)
                .flatMap(footballer -> footballer.getPositions().stream())
                .collect(Collectors.joining(","));

        Assertions.assertEquals(expected, allPositionsOfMaleLessThan30y);
    }

    @Test
    public void distinct(){

        String expected = "CF,CAM,LF,CM,GK,CDM";

        String allUniquePositionsOfMaleLessThan30y = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.MALE))
                .filter(footballer -> footballer.getAge() < 30)
                .map(Footballer::getPositions)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.joining(","));

        Assertions.assertEquals(expected, allUniquePositionsOfMaleLessThan30y);

    }

    @Test
    public void sorted() {

        List<Footballer>  sortByGenderAndName= footballerList.stream()
                .sorted(Comparator.comparing(Footballer::getGender).thenComparing(Footballer::getName))
                .collect(Collectors.toList());

        System.out.println(sortByGenderAndName);

        Assertions.assertEquals("Alexia", sortByGenderAndName.get(0).getName());
        Assertions.assertEquals("Jana", sortByGenderAndName.get(1).getName());
        Assertions.assertEquals("Arthur", sortByGenderAndName.get(3).getName());
        Assertions.assertEquals("Griezmann", sortByGenderAndName.get(4).getName());
        Assertions.assertEquals("Messi", sortByGenderAndName.get(5).getName());

    }

    @Test
    public void limit() {
        List<Footballer>  sortByGenderAndName = footballerList.stream()
                .sorted(Comparator.comparing(Footballer::getGender).thenComparing(Footballer::getName))
                .limit(2)
                .collect(Collectors.toList());

        Assertions.assertTrue(sortByGenderAndName.size() == 2);
        Assertions.assertEquals("Alexia", sortByGenderAndName.get(0).getName());
        Assertions.assertEquals("Jana", sortByGenderAndName.get(1).getName());
    }

    @Test
    public void skip(){

        List<Footballer>  sortByGenderAndNameSkipping5 = footballerList.stream()
                .sorted(Comparator.comparing(Footballer::getGender).thenComparing(Footballer::getName))
                .skip(5)
                .collect(Collectors.toList());


        Assertions.assertTrue(sortByGenderAndNameSkipping5.size() == 3);
        Assertions.assertEquals("Messi", sortByGenderAndNameSkipping5.get(0).getName());
        Assertions.assertEquals("Puig", sortByGenderAndNameSkipping5.get(1).getName());
        Assertions.assertEquals("Ter Stegen", sortByGenderAndNameSkipping5.get(2).getName());
    }

    @Test
    void takeWhile() {

        List<Integer> takeAWhile = Stream.of(2, 4, 6, 8, 9, 10, 11, 12)
                .takeWhile(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println("takeAWhile = " + takeAWhile);
        Assertions.assertEquals(List.of(2,4,6,8), takeAWhile);
    }

    @Test
    void dropWhile() {

        List<Integer> expected = List.of(9, 10, 11, 12);

        List<Integer> dropWhile = Stream.of(2, 4, 6, 8, 9, 10, 11, 12)
                .dropWhile(n -> n % 2 == 0)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, dropWhile);

    }

    @Test
    void count() {

        long malesPlayers = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.MALE))
                .count();

        Assertions.assertEquals(5, malesPlayers);
    }

    @Test
    void forEach() {

        List<Integer> expected = List.of(21, 24, 28, 29, 33);

        List<Footballer> playersSortedByAge = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.MALE))
                .sorted(Comparator.comparing(Footballer::getAge))
                .collect(Collectors.toList());

        playersSortedByAge.stream()
                .forEach(footballer -> footballer.increaseAge());

        List<Integer> ageList = playersSortedByAge.stream()
                .map(Footballer::getAge).collect(Collectors.toList());

        Assertions.assertEquals(expected, ageList);

    }

    @Test
    void forEachOrdered() {
        List<Integer> result = new ArrayList<>();
        List<Integer> expected = List.of(4, 1, 6, 7, 19, 2, 3, 64);

        List.of(4,1,6,7,19,2,3,81,64).stream()
                .filter(number -> number < 65)
                .forEachOrdered(number -> result.add(number));

        Assertions.assertEquals(expected, result);

    }

    @Test
    void toArray() {
        Footballer[] femaleFootballers = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.FEMALE))
                .toArray(Footballer[]::new);

        Assertions.assertTrue(femaleFootballers.length == 3);
    }

    @Test
    void min() {
        Integer minAge = footballerList.stream()
                .min(Comparator.comparing(Footballer::getAge))
                .map(Footballer::getAge)
                .get();

        Assertions.assertEquals(17, minAge);
    }

    @Test
    void minVersion2() {

        Integer minAge = footballerList.stream()
                .map(Footballer::getAge)
                .min(Integer::compare)
                .get();

        Assertions.assertEquals(17, minAge);
    }

    @Test
    void max() {

        Integer maxAge = footballerList.stream()
                .max(Comparator.comparing(Footballer::getAge))
                .map(Footballer::getAge)
                .get();

        Assertions.assertEquals(32, maxAge);
    }

    @Test
    void anyMatch() {
        boolean anyMatch = footballerList
                .stream()
                .anyMatch(footballer -> footballer.getAge() > 25);

        Assertions.assertTrue(anyMatch);
    }

    @Test
    void allMatch() {
        boolean allMatch = footballerList.stream()
                .allMatch(footballer -> footballer.getAge() > 25);

        Assertions.assertFalse(allMatch);
    }

    @Test
    void nonMatch() {
        boolean noneMatch = footballerList.stream()
                .noneMatch(footballer -> footballer.getAge() > 100);

        Assertions.assertTrue(noneMatch);
    }

    @Test
    void findFirst() {
        Integer findFirst = List.of(4, 1, 3, 7, 5, 6, 2, 28, 15, 29)
                .stream()
                .filter(number -> number > 5)
                .findFirst()
                .get();

        Assertions.assertEquals(7, findFirst);
    }

    /**
     * The behavior of this operation is explicitly nondeterministic; it is free to select any element in the stream.
     * This is to allow for maximal performance in parallel operations; the cost is that multiple invocations on the
     * same source may not return the same result.
     * (If a stable result is desired, use findFirst() instead.)
     */
    @Test
    void findAny() {
        Integer findAny = List.of(4, 1, 3, 7, 5, 6, 2, 28, 15, 29)
                .parallelStream()
                .filter(number -> number > 5)
                .findAny()
                .get();

        Assertions.assertNotNull(findAny);
    }


    /**
     * Performs a reduction on the elements of this stream, using an associative accumulation function,
     * and returns an Optional describing the reduced value, if any.
     * The accumulator function must be an associative function.
     * This is a terminal operation.
     */
    @Test
    void reduce() {
        Optional<String> longestName = footballerList.stream()
                .map(Footballer::getName)
                .reduce((name1, name2)
                        -> name1.length() > name2.length()
                        ? name1 : name2);


        Assertions.assertEquals("Ter Stegen", longestName.get());
    }

    /**
     * Performs a mutable reduction operation on the elements of this stream using a Collector.
     * A Collector encapsulates the functions used as arguments to collect(Supplier, BiConsumer, BiConsumer),
     * allowing for reuse of collection strategies and composition of collect operations such as multiple-level
     * grouping or partitioning.
     * If the stream is parallel, and the Collector is concurrent, and either the stream is unordered or the collector
     * is unordered, then a concurrent reduction will be performed (see Collector for details on concurrent reduction.)
     * This is a terminal operation.
     * When executed in parallel, multiple intermediate results may be instantiated, populated, and merged so as to
     * maintain isolation of mutable data structures. Therefore, even when executed in parallel with non-thread-safe
     * data structures (such as ArrayList), no additional synchronization is needed for a parallel reduction.
     */
    @Test
    void collect() {
        List<Footballer> collect = footballerList.stream()
                .filter(footballer -> footballer.getGender().equals(Gender.FEMALE))
                .filter(footballer -> footballer.getAge() > 25)
                .collect(Collectors.toList());

        Assertions.assertTrue(collect.size() == 1);
        Assertions.assertEquals("Jennifer", collect.get(0).getName());

    }

    private List<Footballer> getFootballers() {
        return List.of(
                new Footballer("Messi", 32, Gender.MALE, List.of("CF", "CAM", "RF")),
                new Footballer("Griezmann", 28, Gender.MALE, List.of("CF", "CAM", "LF")),
                new Footballer("Arthur", 23, Gender.MALE, List.of("CM", "CAM")),
                new Footballer("Ter Stegen", 27, Gender.MALE, List.of("GK")),
                new Footballer("Puig", 20, Gender.MALE, List.of("CM", "CDM")),
                new Footballer("Jennifer", 29, Gender.FEMALE, List.of("CF", "CAM")),
                new Footballer("Jana", 17, Gender.FEMALE, List.of("CB")),
                new Footballer("Alexia", 25, Gender.FEMALE, List.of("CAM", "RF", "LF"))
        );
    }
}
