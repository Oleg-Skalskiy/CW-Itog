import javax.management.RuntimeOperationsException;
import java.util.*;
import java.io.*;

public class Main implements AutoCloseable {

    private final List<Animal_Reg> animals = new ArrayList<>();

    private static final Count count = new Count();

    public void addNewAnimal_Reg(Animal_Reg animal) {
        animals.add(animal);
        count.add();
    }

    public void teachCommand(Animal_Reg animal, String command) {
        animal.setCommand(command);

        try (FileWriter writer = new FileWriter("Registry.txt", true)) {
            String animalType = getAnimal_RegType(animal);
            String animalName = animal.getName();
            String line = STR."\{animalType} > \{animalName} > \{command}\n";
            writer.write(line);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    private String getAnimal_RegType(Animal_Reg animal) {
        if (animal instanceof Dogs) {
            return "Собаки";
        } else if (animal instanceof Cats) {
            return "Кошки";
        } else if (animal instanceof Hamsters) {
            return "Хомячки";
        } else if (animal instanceof Horses) {
            return "Лошади";
        } else if (animal instanceof Camels) {
            return "Верблюды";
        } else if (animal instanceof Donkeys) {
            return "Ослы";
        }
        return "";
    }

    public List<String> getCommands(Animal_Reg animal) {
        List<String> commands = new ArrayList<>();
        commands.add(animal.getCommand());
        return commands;
    }

    @Override
    public String toString() {
        return "Main{" +
                "animals=" + animals +
                '}';
    }

    public void readRegistryFile() {
        File registryFile = new File("Registry.txt");
        if (!registryFile.exists()) {
            try {
                registryFile.createNewFile();
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(registryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String animalName = data[0];
                    String command = data[1];
                    animals.stream().filter(a -> a.getName().equals(animalName)).findFirst().ifPresent(animal -> animal.setCommand(command));
                }
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try (Main main = new Main()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Показать список животных");
                System.out.println("2. Добавить новое животное");
                System.out.println("3. Обучить команде");
                System.out.println("4. Получить команды животного");
                System.out.println("5. Выход");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader("Registry.txt"));
                            System.out.println(" ");
                            System.out.println("vvv Список всех животных и команд vvv");
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                                System.out.println("^^^ Список всех животных и команд ^^^");
                                System.out.println(" ");
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.fillInStackTrace();
                        }
                        break;
                    case 2:
                        System.out.println("Введите тип животного: ");
                        String type = scanner.nextLine();
                        System.out.println("Введите имя животного: ");
                        String name = scanner.nextLine();
                        Animal_Reg animal = switch (type) {
                            case "Собаки" -> new Dogs(name);
                            case "Кошки" -> new Cats(name);
                            case "Хомячки" -> new Hamsters(name);
                            case "Лошади" -> new Horses(name);
                            case "Верблюды" -> new Camels(name);
                            case "Ослы" -> new Donkeys(name);
                            default ->
                                    throw new IllegalStateException(STR."Неверный тип животного, повторите ввод: \{type}");
                        };
                        main.addNewAnimal_Reg(animal);
                        break;
                    case 3:
                        System.out.println("Введите имя животного: ");
                        String animalName = scanner.nextLine();
                        Animal_Reg foundAnimal_Reg = main.animals.stream()
                                .filter(a -> a.getName().equals(animalName))
                                .findFirst()
                                .orElse(null);
                        if (foundAnimal_Reg == null) {
                            System.out.println("Нет такого животного");
                            break;
                        }
                        System.out.println("Введите команду: ");
                        String command = scanner.nextLine();
                        main.teachCommand(foundAnimal_Reg, command);
                        break;
                    case 4:
                        System.out.println("Введите имя животного: ");
                        String aName = scanner.nextLine();
                        Animal_Reg fAnimal_Reg = main.animals.stream()
                                .filter(a -> a.getName().equals(aName))
                                .findFirst()
                                .orElse(null);
                        if (fAnimal_Reg == null) {
                            System.out.println("Нет такого животного");
                            break;
                        }
                        List<String> commands = main.getCommands(fAnimal_Reg);
                        for (String cmd : commands) {
                            System.out.println(cmd);
                        }
                        break;
                    case 5:
                        return;
                }
            }
        } catch (RuntimeException e) {
            throw new NullPointerException();
        }
    }

    @Override
    public void close() throws Exception {
        if (count.getCount() == 0) {
            throw new Exception("Счетчик не использовался");
        } else {
            count.resetCount();
        }
    }

    public List<Animal_Reg> getAnimals() {
        return animals;
    }
}

class Count {

    private int count;

    public void add() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }

    public void setCount(int count) {
        this.count = count;
    }
}