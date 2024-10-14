import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

// Project: Student Management System
// Developed by: Bhanu Pratap
// Description: This system manages student records, including scores and average calculation.

class Person {
    private String name; // Name of the person
    private final int id; // Unique identifier for the person

    // Constructor to initialize person attributes
    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Override toString for better display of person details
    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id;
    }
}

// Student class extends Person to represent student-specific attributes
class Student extends Person {
    private final LinkedList<Integer> scores; // List to hold student scores

    // Constructor to initialize student attributes
    public Student(String name, int id) {
        super(name, id);
        scores = new LinkedList<>();
    }

    // Method to add scores with validation
    public void addScore(int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        scores.add(score);
    }

    // Getter for scores
    public LinkedList<Integer> getScores() {
        return new LinkedList<>(scores); // Return a copy to maintain immutability
    }

    // Method to calculate average of scores
    public double calculateAverage() {
        if (scores.isEmpty()) {
            return 0; // Return 0 if no scores are present
        }
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return (double) sum / scores.size(); // Return average
    }

    // Override toString to include scores and average
    @Override
    public String toString() {
        return super.toString() + "\nScores: " + scores + ", Average: " + calculateAverage();
    }
}

// Class to manage student-related operations with error handling
class StudentManager {
    private final HashMap<Integer, Student> studentMap; // Map to hold student records

    // Constructor to initialize the student manager
    public StudentManager() {
        studentMap = new HashMap<>();
    }

    // Method to add a new student with ID uniqueness check
    public void addStudent(Student student) {
        if (studentMap.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }
        studentMap.put(student.getId(), student);
    }

    // Binary search for student by ID
    public Student binarySearch(int id) {
        ArrayList<Student> studentList = new ArrayList<>(studentMap.values());
        studentList.sort(Comparator.comparingInt(Student::getId)); // Sort students by ID

        int low = 0;
        int high = studentList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Student midStudent = studentList.get(mid);
            if (midStudent.getId() == id) {
                return midStudent; // Student found
            } else if (midStudent.getId() < id) {
                low = mid + 1; // Search in the right half
            } else {
                high = mid - 1; // Search in the left half
            }
        }
        return null; // Student not found
    }

    // Method to display all students
    public void displayAllStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("All Students:");
        for (Student student : studentMap.values()) {
            System.out.println(student); // Print each student
        }
    }

    // Method to get top-performing student in a subject
    public Student getTopStudent(int subjectIndex) {
        if (subjectIndex < 0) {
            throw new IllegalArgumentException("Subject index cannot be negative");
        }

        Student topStudent = null; // Variable to hold top student
        int topScore = -1; // Variable to track top score

        for (Student student : studentMap.values()) {
            LinkedList<Integer> scores = student.getScores();
            // Check if score for the subject exists and is higher than the current top score
            if (subjectIndex < scores.size() && scores.get(subjectIndex) > topScore) {
                topScore = scores.get(subjectIndex);
                topStudent = student; // Update top student
            }
        }

        return topStudent; // Return the top student
    }
}

// Main class to demonstrate the system
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        // Adding some default students
        Student student1 = new Student("Bhanu Pratap", 101);
        student1.addScore(80);
        student1.addScore(90);
        student1.addScore(85);
        manager.addStudent(student1);

        Student student2 = new Student("Harsh", 102);
        student2.addScore(95);
        student2.addScore(88);
        student2.addScore(92);
        manager.addStudent(student2);

        Student student3 = new Student("Badal", 103);
        student3.addScore(78);
        student3.addScore(85);
        student3.addScore(80);
        manager.addStudent(student3);

        // User interaction to manage students
        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Search Student by ID");
            System.out.println("3. Get Top Student in Subject");
            System.out.println("4. Add New Student");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.displayAllStudents(); // Display all students
                    break;
                case 2:
                    System.out.print("Enter Student ID to search: ");
                    int searchId = sc.nextInt();
                    Student foundStudent = manager.binarySearch(searchId);
                    if (foundStudent != null) {
                        System.out.println("Student found:\n" + foundStudent);
                    } else {
                        System.out.println("Student not found."); // Not found message
                    }
                    break;
                case 3:
                    System.out.print("Enter subject index (0 for first subject, 1 for second, etc.): ");
                    int subjectIndex = sc.nextInt();
                    try {
                        Student topStudent = manager.getTopStudent(subjectIndex);
                        if (topStudent != null) {
                            System.out.println("Top student in subject " + (subjectIndex + 1) + ":\n" + topStudent);
                        } else {
                            System.out.println("No top student found for that subject."); // No top student found
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage()); // Print error message
                    }
                    break;
                case 4:
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter student ID: ");
                    int id = sc.nextInt();
                    Student newStudent = new Student(name, id);
                    System.out.print("How many scores to add? ");
                    int scoreCount = sc.nextInt();
                    for (int i = 0; i < scoreCount; i++) {
                        System.out.print("Enter score " + (i + 1) + ": ");
                        int score = sc.nextInt();
                        newStudent.addScore(score); // Add each score
                    }
                    try {
                        manager.addStudent(newStudent); // Attempt to add new student
                        System.out.println("Student added successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage()); // Print error message
                    }
                    break;
                case 5:
                    System.out.println("Exiting..."); // Exit message
                    sc.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Try again."); // Invalid choice message
            }
        }
    }
}
