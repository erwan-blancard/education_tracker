package education_tracker;

import java.util.Scanner;

public class StudentManagementSystem {
	
	DatabaseManager dbManager;
	
	private static Scanner sc;
	
	private final int CMD_HELP = 0;
	private final int CMD_CONNECT = 1;
	private final int CMD_DISCONNECT = 2;
	private final int CMD_EXIT = 3;
	private final int CMD_ADD = 4;
	private final int CMD_EDIT = 5;
	private final int CMD_DELETE = 6;
	private final int CMD_LIST = 7;
	private final int CMD_SEARCH = 8;
	
	private final String[] commands = {
			"help",
			"connect",
			"disconnect",
			"exit",
			"add",
			"edit",
			"delete",
			"list",
			"search"
	};
	
	private final String[] commandsDesc = {
			"Affiche cette liste",
			"Permet de se connecter à la base de données",
			"Permet de se déconnecter à la base de données",
			"Ferme le programme",
			"Ajouter un étudiant",
			"Modifier les attributs d'un étudiant",
			"Supprimer un étudiant",
			"Affiche une liste des étudiants",
			"Rechercher un étudiant"
	};
	
	public StudentManagementSystem() {
		printTitle();
		connectToDatabase();
		
		while (!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!false) {
			mainLoop();
		}
	}
	
	private void mainLoop() {
		System.out.print("> ");
		String command = askInput();
		
		if (command.isBlank()) { return; }
		
		boolean cmd_found = false;
		int i = 0;
		while (i < commands.length) {
			if (commands[i].equals(command)) {
				cmd_found = true;
				switch (i) {
				case CMD_ADD:
					addStudent();
					break;
				case CMD_CONNECT:
					if (dbManager != null && dbManager.isConnected()) {
						System.out.print("Vous êtes déjà connecté !\nVoulez-vous vous reconnecter ? [o/n] ");
						String response = askInput();
						if (response.toLowerCase().equals("o") || response.toLowerCase().equals("y")) {
							connectToDatabase();
						}
					} else {
						connectToDatabase();
					}
					break;
				case CMD_DELETE:
					deleteStudent();
					break;
				case CMD_DISCONNECT:
					disconnect();
					break;
				case CMD_EDIT:
					
					break;
				case CMD_EXIT:
					dbManager.closeConnection(true);
					System.exit(0);
					break;
				case CMD_HELP:
					displayHelp();
					break;
				case CMD_LIST:
					listStudents();
					break;
				case CMD_SEARCH:
					searchForStudent();
					break;

				default:
					System.out.println("\"" + command + "\" n'est pas une commande valide");
					break;
				}
			}
			i++;
		}
		
		if (!cmd_found) {
			System.out.println("\"" + command + "\" n'est pas une commande valide");
		}
	}
	
	private void deleteStudent() {
		if (dbManager != null && dbManager.isConnected()) {
			dbManager.askDeleteStudent();
		} else {
			System.out.println("Impossible d'exécuter la commande, vous n'êtes pas connecté !");
		}
	}

	private void searchForStudent() {
		if (dbManager != null && dbManager.isConnected()) {
			dbManager.askGetStudent();
		} else {
			System.out.println("Impossible de chercher un étudiant, vous n'êtes pas connecté !");
		}
	}

	private void listStudents() {
		if (dbManager != null && dbManager.isConnected()) {
			dbManager.listStudents();
		} else {
			System.out.println("Impossible de lister les étudiants, vous n'êtes pas connecté !");
		}
	}

	private void addStudent() {
		if (dbManager != null && dbManager.isConnected()) {
			dbManager.askAddStudent();
		} else {
			System.out.println("Impossible d'exécuter la commande, vous n'êtes pas connecté !");
		}
	}
	
	private void disconnect() {
		if (dbManager != null) {
			if (dbManager.closeConnection()) {
				dbManager = null;
				System.out.println("Déconnexion réussie !");
			} else {
				System.out.println("La déconnection à échoué !");
			}
		} else {
			System.out.println("Vous n'êtes pas connecté !");
		}
	}
	
	private void displayHelp() {
		for (int i = 0; i < commands.length; i++) { System.out.println(commands[i] + " - " + commandsDesc[i]+"."); }
	}

	public static String askInput() {
		/*
		 * Ask input using System.console(). If System.console() is null, use Scanner instead
		 */
		if (System.console() != null) {
			return System.console().readLine();
		} else {
			if (sc == null) { sc = new Scanner(System.in); }
			return sc.nextLine();
		}
	}
	
	private void connectToDatabase() {
		System.out.print("\nMot de passe: ");
		dbManager = new DatabaseManager("root", askInput());
		
		if (dbManager.isConnected()) {
			System.out.println("Connexion établie !\n");
		} else {
			System.out.println("Impossible d'établir la connexion avec la base de données.\n\nUtilisez la commande connect pour vous connecter.\n");
		}
	}
	
	private void printTitle() {
		System.out.println("Système de gestion d'étudiants\n------------------------------");
	}

	public static void main(String[] args) {
		new StudentManagementSystem();
	}

}
