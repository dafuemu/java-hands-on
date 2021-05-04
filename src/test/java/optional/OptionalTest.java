package optional;


import data.Computer;
import data.SoundCard;
import data.USB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * https://www.oracle.com/technical-resources/articles/java/java8-optional.html
 */

public class OptionalTest {

    Logger logger = Logger.getLogger(String.valueOf(OptionalTest.class));

    @Test
    @DisplayName("Empty creation")
    public void emptyOptional() {
        Optional<Wizard> empty = Optional.empty();
        Assertions.assertFalse(empty.isPresent());
        Assertions.assertTrue(empty.isEmpty()); // Since java 11
    }

    @Test
    @DisplayName("Create With Static Method Of")
    public void ofOptional() {

        Optional<Wizard> wizard = Optional.of(getWizards().get(0));
        Assertions.assertTrue(wizard.isPresent());
    }

    @Test
    @DisplayName("Create Null With Method OfNullable")
    public void ofNullableOptional() {

        Optional<Wizard> wizard = Optional.ofNullable(null);

        Assertions.assertFalse(wizard.isPresent());
    }

    //dataset footballplayers better for both this test and the stream api test.


    @Test
    @DisplayName("ifPresent with a null value")
    public void ifPresentOptionalIsNull() {
        Optional<Wizard> empty = Optional.empty();
        empty.ifPresent(value -> System.out.println(value.getName()));

        Assertions.assertTrue(true);
        Assertions.assertThrows(NoSuchElementException.class, () -> empty.get());
    }

    @Test
    @DisplayName("ifPresent with non null value")
    public void ifPresentOptionalIsNotNull() {
        Optional<Wizard> wizard = Optional.of(getWizards().get(0));
        wizard.ifPresent(value -> System.out.println(value.getName()));

        Assertions.assertTrue(Objects.nonNull(wizard.get().getName()));

    }

    @Test
    @DisplayName("orElse with a null value. This null must be of a type Wizard")
    public void orELseOptionalWithNullWizard() {
        Wizard wizard_null = null;
        Wizard wizard = Optional.ofNullable(wizard_null)
                .orElse(getWizards().get(1));

        Assertions.assertTrue(Objects.nonNull(wizard.getName()));
    }

    @Test
    @DisplayName("orElse with a null value. This null must be of a type Wizard")
    public void orELseOptionalWithNotNullWizard() {
        Wizard wizard0 = getWizards().get(0);
        Wizard wizard = Optional.ofNullable(wizard0)
                .orElse(getWizards().get(1));

        Assertions.assertTrue(Objects.equals(wizard0, wizard));
    }

    @Test
    @DisplayName("orElseGet with a Wizard null")
    public void orElseGetWithNullWizard() {
        Wizard wizard_null = null;
        Wizard wizard = Optional.ofNullable(wizard_null)
                .orElseGet(() -> {
                    // n number of complicated lines
                    return getWizards().get(2);
                });
        Assertions.assertTrue(Objects.nonNull(wizard.getName()));

    }

    @Test
    @DisplayName("orElseGet with a Wizard not null")
    public void orElseGetWithNotNullWizard() {
        Wizard wizard0 = getWizards().get(0);
        Wizard wizard = Optional.ofNullable(wizard0)
                .orElseGet(() -> {
                    // n number of complicated lines
                    return getWizards().get(2);
                });

        Assertions.assertTrue(Objects.equals(wizard0, wizard));

    }

    @Test()
    @DisplayName("orElseThrow")
    public void orElseThrowOptional() {
        Wizard wizard_null = null;

        Assertions.assertThrows(IllegalAccessError.class,
                () -> Optional.ofNullable(wizard_null)
                        .orElseThrow(IllegalAccessError::new));
    }

    @Test
    @DisplayName("get from Optional")
    public void getOptional() {
        Optional<Wizard> wizard = Optional.ofNullable(getWizards().get(0));

        Assertions.assertTrue(wizard.get() instanceof Wizard);

    }

    @Test
    @DisplayName("map applied to an Optional")
    public void mapOptional() {
        Optional<Wizard> wizard = Optional.of(getWizards().get(0));

        String upperCase = wizard.map(wizard1 -> wizard1.getName().toUpperCase())
                .orElse(getUpperCaseString()); // The method is evaluated even though the map gives a result

        Assertions.assertTrue(upperCase.equals("HARRY POTTER"));
    }

    @Test
    @DisplayName("map applied to an empty Optional")
    public void mapOptionalOrElse() {
        Optional<Wizard> wizard = Optional.ofNullable(null);

        String upperCase = wizard.map(wizard1 -> wizard1.getName().toUpperCase())
                .orElse(getUpperCaseString());

        Assertions.assertTrue(upperCase.equals("UPPERCASE"));
    }

    @Test
    @DisplayName("map applied with a null mapper Function")
    public void mapOptionalNullMapperGivesANullPointerException() {
        Optional<Wizard> wizard = Optional.of(getWizards().get(0));
        Function<Wizard, String> function = null;

        Assertions.assertThrows(NullPointerException.class, () -> wizard.map(function)
                .orElse(getUpperCaseString()));
    }

    @Test
    @DisplayName("Optional map with orElseGet improvement")
    public void mapOptionalOrElseGet() {
        Optional<Wizard> wizard = Optional.of(getWizards().get(0));

        String upperCase = wizard.map(wizard1 -> wizard1.getName().toUpperCase())
                .orElseGet(() -> getUpperCaseString()); // The method is NOT evaluated as with orElse

        Assertions.assertTrue(upperCase.equals("HARRY POTTER"));
    }


    @Test
    @DisplayName("filter applied to an Optional")
    public void filterOptionalWithNotNullValue() {
        Optional<Wizard> wizardOptional = Optional.of(getWizards().get(0));
        Predicate<Wizard> predicate = wizard -> wizard.getName() == "Harry Potter";
        Optional<Wizard> result = wizardOptional.filter(predicate);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("filter applied to an Optional gives an empty optional")
    public void filterOptionalEmptyResult() {
        Optional<Wizard> wizardOptional = Optional.of(getWizards().get(0));
        Predicate<Wizard> predicate = wizard -> wizard.getName() == "Does not exist";
        Optional<Wizard> result = wizardOptional.filter(predicate);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("filter applied to an Optional gives an NullPointerException")
    public void filterOptionalWithNullPredicateGivesANullPointerException() {
        //https://docs.oracle.com/javase/9/docs/api/java/util/Optional.html#ifPresentOrElse-java.util.function.Consumer-java.lang.Runnable-
        Wizard wizardNull = null;
        Optional<Wizard> wizard = Optional.ofNullable(wizardNull);
        Predicate<Wizard> predicate = null;

        Assertions.assertThrows(NullPointerException.class, () -> wizard.filter(predicate));

    }

    @Test
    @DisplayName("ifPresentOrElse when there is a value and an action")
    public void ifPresentOrElseOptionalWhenValueExists(){
        // given
        Optional<String> value = Optional.of("properValue");
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger onEmptyOptionalCounter = new AtomicInteger(0);

        // when
        value.ifPresentOrElse(
                v -> successCounter.incrementAndGet(),
                onEmptyOptionalCounter::incrementAndGet);

        // then
        Assertions.assertEquals(1, successCounter.get());
        Assertions.assertEquals(0, onEmptyOptionalCounter.get());

    }

    @Test
    @DisplayName("ifPresentOrElse when there value does not exist")
    public void ifPresentOrElseOptionalWhenValueDoesNotExist(){
        // given
        Optional<String> value = Optional.empty();
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger onEmptyOptionalCounter = new AtomicInteger(0);

        // when
        value.ifPresentOrElse(
                v -> successCounter.incrementAndGet(),
                onEmptyOptionalCounter::incrementAndGet);

        // then
        Assertions.assertEquals(0, successCounter.get());
        Assertions.assertEquals(1, onEmptyOptionalCounter.get());

    }

    @Test
    @DisplayName("ifPresentOrElse throws Exception when there is no value but no empty action")
    public void ifPresentOrElseOptionalExceptionWhenValueExistsButNotAction(){
        // given
        Optional<String> value = Optional.empty();
        AtomicInteger successCounter = new AtomicInteger(0);

        Assertions.assertThrows(NullPointerException.class, () -> value.ifPresentOrElse(
                v -> successCounter.incrementAndGet(),
                null));
    }

    @Test
    @DisplayName("ifPresentOrElse throws Exception when there is value but no action")
    public void ifPresentOrElseOptionalExceptionWhenValueDoesNotExistsButNotActionEmpty(){
        // given
        Optional<String> value = Optional.of("Hello");
        AtomicInteger successCounter = new AtomicInteger(0);
        Consumer<String> consumer = null;
        AtomicInteger onEmptyOptionalCounter = new AtomicInteger(0);

        Assertions.assertThrows(NullPointerException.class, () -> value.ifPresentOrElse(
                consumer,
                onEmptyOptionalCounter::incrementAndGet));

    }

    @Test
    public void givenOptional_whenPresent_thenShouldTakeAValueFromIt() {
        //given
        String expected = "properValue";
        Optional<String> value = Optional.of(expected);
        Optional<String> defaultValue = Optional.of("default");

        //when
        Optional<String> result = value.or(() -> defaultValue);

        //then
        Assertions.assertEquals(expected, result.get());
    }

    @Test
    public void givenOptional_whenEmpty_thenShouldTakeAValueFromOr() {
        // given
        String defaultString = "default";
        Optional<String> value = Optional.empty();
        Optional<String> defaultValue = Optional.of(defaultString);

        // when
        Optional<String> result = value.or(() -> defaultValue);

        // then
        Assertions.assertEquals(defaultString, result.get());
    }

    /**
     * How the null check can be improved with the use of Optional. See the code snippet below from:
     * <pre>
     * String version = "UNKNOWN";
     * if(computer != null){
     *   SoundCard soundCard = computer.getSoundCard();
     *   if(soundCard != null){
     *     USB usb = soundCard.getUSB();
     *     if(usb != null){
     *       version = usb.getVersion();
     *     }
     *   }
     * }
     * </pre>
     * The use of flatMap instead of map is explained in the following link: https://www.oracle.com/technical-resources/articles/java/java8-optional.html
     */

    @Test
    public void improvedVersionOfNullChecking() {
        Optional<Computer> computer = Optional.of(new Computer());
        String name = computer.flatMap(Computer::getSoundCard)
                .flatMap(SoundCard::getUSB)
                .map(USB::getVersion)
                .orElse("UNKNOWN");

        Assertions.assertEquals("version 0.1", name);
    }

    @Test
    public void improvedVersionOfNullCheckingWithANullValue() {
        Optional<Computer> computer = Optional.empty();
        String name = computer.flatMap(Computer::getSoundCard)
                .flatMap(SoundCard::getUSB)
                .map(USB::getVersion)
                .orElse("UNKNOWN");

        Assertions.assertEquals("UNKNOWN", name);
    }





    private List<Wizard> getWizards() {
        return List.of(
                new Wizard("Gryfindor", "phoenix feather core", "Half Blood", "Male", "Harry Potter"),
                new Wizard("Gryfindor", "unicorn hair core", "Pure Blood", "Male", "Ronald Wisely"),
                new Wizard("Gryfindor", "dragon heart string core", "Mud Blood", "Female", "Hermione Granger"),
                new Wizard("Slytherin", "unicorn hair core", "Pure Blood", "Male", "Draco Malfoy"),
                new Wizard("Ravenclaw", "unicorn hair core", "Pure Blood", "Male", "Cedric Diggory")

        );
    }

    private class Wizard {

        private final String house;
        private final String description;
        private final String blood;
        private final String gender;
        private final String name;

        public Wizard(String house, String description, String blood, String gender, String name) {
            this.house = house;
            this.description = description;
            this.blood = blood;
            this.gender = gender;
            this.name = name;
        }

        public String getHouse() {
            return house;
        }

        public String getDescription() {
            return description;
        }

        public String getBlood() {
            return blood;
        }

        public String getGender() {
            return gender;
        }

        public String getName() {
            return name;
        }
    }

    public String getUpperCaseString() {
        logger.info("getRandomName() method - start");

        logger.info("getRandomName() method - end");
        return "UPPERCASE";
    }
}
