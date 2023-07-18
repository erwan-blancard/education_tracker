package education_tracker;

public class Student {
	
	private int id;
	private String name;
	private String lastname;
	private int age;
	private int[] grades;

	public Student(int id, String name, String lastname, int age, int[] grades) {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.age = age;
		this.grades = grades;
	}

	public int getId() { return id; }
	public String getName() { return name; }
	public String getLastname() { return lastname; }
	public int getAge() { return age; }
	public int[] getGrades() { return grades; }
	
	public String getGradeList() {
		if (grades != null && grades.length > 0) {
			String out = "";
			for (int i = 0; i < grades.length; i++) {
				String end = (i == grades.length-1)? "" : ", ";
				out += grades[i] + end;
			}
			return out;
		}
		return "Pas de notes";
	}

	public void printInfos() {
		System.out.println("ID: "+id+"\nPrénom: "+name+"\nNom: "+lastname+"\nÂge: "+age+"\nNotes: "+getGradeList());
	}

}
