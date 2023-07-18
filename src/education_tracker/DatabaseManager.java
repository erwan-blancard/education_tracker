package education_tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager {

	private Connection conn = null;

	public DatabaseManager(String user, String password) {
		connect(user, password);
	}
	
	private void connect(String user, String password) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/education_tracker", user, password);

		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    //System.out.println("SQLState: " + ex.getSQLState());
		    //System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void listStudents() {
		try {
			Statement studentStatement = conn.createStatement();
			Statement gradesStatement = conn.createStatement();
			try {
		        ResultSet studentsRes = studentStatement.executeQuery("select * from students;");
		        ArrayList<Student> students = new ArrayList<Student>();
		        
		        // used for TAB alignment
		        /*
		        int maxNameLength = 0;
		        int maxLastnameLength = 0;
		        */
		        
		        while (studentsRes.next()) {
		        	int id = studentsRes.getInt("id");
		        	String name = studentsRes.getString("first_name");
		        	String lastname = studentsRes.getString("last_name");
		        	int age = studentsRes.getInt("age");
		        	
		        	ResultSet gradesRes = gradesStatement.executeQuery("select * from grades where id="+id+";");
		        	ArrayList<Integer> gradesArrayList = new ArrayList<Integer>();
		        	// put grades in arraylist
		        	while (gradesRes.next()) { gradesArrayList.add(gradesRes.getInt("grade")); }
		        	
		        	// convert arraylist to int array
		        	int[] grades = new int[gradesArrayList.size()];
		        	for (int i = 0; i < gradesArrayList.size(); i++) {
						grades[i] = gradesArrayList.get(i);
					}
		        	
		        	students.add(new Student(id, name, lastname, age, grades));
		        	
		        	/*
		        	if (name.length() > maxNameLength) { maxNameLength = name.length(); }
		        	if (lastname.length() > maxLastnameLength) { maxLastnameLength = lastname.length(); }
		        	*/
		        }
		        if (students.size() == 0) {
		        	System.out.println("Aucun étudiant n'est enregistré.");
		        } else {
		        	System.out.println("ID\t| Prénom\t| Nom\t| Âge\t| Notes\n");
		        	for(Student student : students) {
		        		System.out.println(student.getId() + "\t| "+student.getName()+"\t| "+student.getLastname()+"\t| "+student.getAge()+"\t| "+student.getGradeList());
		        	}
		        }
			} catch (SQLException e ) {
				System.out.println("Erreur lors de la récupération des étudiants: "+e.getMessage());
		    }
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'envoi de la requête: "+e.getMessage());
		}
	}

	public void askGetStudent() {
		System.out.print("Rechercher un étudiant par ID:\nID: ");
		String searchId = StudentManagementSystem.askInput();
		try {
			Statement studentStatement = conn.createStatement();
			Statement gradesStatement = conn.createStatement();
			try {
				ResultSet studentsRes = studentStatement.executeQuery("select first_name, last_name, age from students where id="+searchId+";");
				
				// if student is found
				if (studentsRes.next()) {
		        	String name = studentsRes.getString("first_name");
		        	String lastname = studentsRes.getString("last_name");
		        	int age = studentsRes.getInt("age");
					
					ResultSet gradesRes = gradesStatement.executeQuery("select * from grades where id="+searchId+";");
		        	ArrayList<Integer> gradesArrayList = new ArrayList<Integer>();
		        	// put grades in arraylist
		        	while (gradesRes.next()) { gradesArrayList.add(gradesRes.getInt("grade")); }
		        	
		        	// convert arraylist to int array
		        	int[] grades = new int[gradesArrayList.size()];
		        	for (int i = 0; i < gradesArrayList.size(); i++) {
						grades[i] = gradesArrayList.get(i);
					}
		        	
		        	// print student infos
		        	System.out.println("\nRésultat de la recherche:");
		        	new Student(Integer.parseInt(searchId), name, lastname, age, grades).printInfos();
				} else {
					System.out.println("\nAucun étudiant trouvé avec cet ID.");
				}
	        	
			} catch (SQLException | NumberFormatException e) {
				System.out.println("Erreur lors de la récupération de l'étudiant: "+e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'envoi de la requête: "+e.getMessage());
		}
	}

	public void askDeleteStudent() {
		System.out.print("Supprimer un étudiant par ID:\nID: ");
		String supprId = StudentManagementSystem.askInput();
		try {
			Statement studentStatement = conn.createStatement();
			Statement gradesStatement = conn.createStatement();
			try {
				int stateQueryStudent = studentStatement.executeUpdate("delete from students where id="+supprId+";");
				
				// if successful
				if (stateQueryStudent == 1) {
		        	System.out.println("\nÉtudiant supprimé avec succès !");
		        	
		        	// delete grades
					int stateQueryGrades = gradesStatement.executeUpdate("delete from grades where id="+supprId+";");
		        	
		        	// if successful
					if (stateQueryGrades == 1) {
			        	System.out.println("\nNotes de l'étudiant supprimées avec succès !");
					} else {
						System.out.println("\nPas de notes trouvées pour cet étudiant.");
					}
				} else {
					System.out.println("\nAucun étudiant trouvé avec cet ID.");
				}
	        	
			} catch (SQLException | NumberFormatException e) {
				System.out.println("Erreur lors de la suppression de l'étudiant: "+e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'envoi de la requête: "+e.getMessage());
		}
	}

	public void askAddStudent() {
		System.out.println("Ajouter un étudiant:");
		System.out.print("Prénom: ");
		String name = StudentManagementSystem.askInput();
		System.out.print("Nom: ");
		String lastname = StudentManagementSystem.askInput();
		System.out.print("Âge: ");
		int age = 0;
		boolean validAge = false;
		while (!validAge) {
			try {
				age = Integer.parseInt(StudentManagementSystem.askInput());
				if (age > 0) {
					validAge = true;
				}
			} catch (NumberFormatException e) {
				System.out.print("\nÂge invalide, réessayez: ");
			}
		}
		
		// do grades
		
		addStudent(name, lastname, age, null);
		
	}
	
	private void addStudent(String name, String lastname, int age, ArrayList<Integer> grades) {
		
	}
	
	public boolean isConnected() {
		return conn != null;
	}

	public boolean closeConnection() {
		return closeConnection(false);
	}

	public boolean closeConnection(boolean noVerbose) {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			if (noVerbose) { e.printStackTrace(); };
			return false;
		}
	}

}
