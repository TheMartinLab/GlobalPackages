/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;



public class UpdateRegisterObjectFile {

	private File objFile = new File("WorkPlayRegister.obj");
	private ObjectOutputStream oos;
	public UpdateRegisterObjectFile() throws IOException {
		FileOutputStream fos = new FileOutputStream("WorkPlayRegister_new.obj");
		oos = new ObjectOutputStream(fos);
		run();
	}
	@SuppressWarnings("unchecked")
	public void run() throws IOException {
		FileInputStream fis = new FileInputStream(objFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		Vector<OldRegister> oldWork = null, oldExercise = null, oldPlay = null;
		Vector<Register> newWork, newExercise = null, newPlay = null;
		try {
			oldWork = (Vector<OldRegister>) ois.readObject();
			oldExercise= (Vector<OldRegister>) ois.readObject();
			oldPlay = (Vector<OldRegister>) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String date;
		double hours;
		newWork = new Vector<Register>();
		OldRegister or;
		for(int i = 0; i < oldWork.size(); i++) {
			or = oldWork.get(i);
			date = or.getDate();
			hours = or.getMinutes();
			newWork.add(new Register(date, hours, ""));
		}
		oos.writeObject(newWork);

		newExercise = new Vector<Register>();
		for(int i = 0; i < oldExercise.size(); i++) {
			or = oldExercise.get(i);
			date = or.getDate();
			hours = or.getMinutes();
			newExercise.add(new Register(date, hours, ""));
		}
		oos.writeObject(newExercise);
		
		newPlay = new Vector<Register>();
		for(int i = 0; i < oldPlay.size(); i++) {
			or = oldPlay.get(i);
			date = or.getDate();
			hours = or.getMinutes();
			newPlay.add(new Register(date, hours, ""));
		}
		oos.writeObject(newPlay);
		
		oos.close();
		
		ois.close();
		fis.close();
	}
	public static void main(String[] args) {
		try {
			new UpdateRegisterObjectFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
