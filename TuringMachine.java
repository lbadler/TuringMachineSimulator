import java.util.*;
import java.io.*;
public class TuringMachine
{
	private static class Transition {
		public int move;
		public Character character;
		public String nextstate;
		public Transition(int m, Character c, String n) {
			move = m;
			character = c;
			nextstate = n;
		}
	}
	
	
	HashMap<String, Transition> transitions;
	HashMap<Integer, Character> tape;
	int index, minIndex, maxIndex;
	String currentState;
	String startState;
	List<String> acceptStates;
	List<String> rejectStates;
	
	void prepareMachine(String machine) {
		transitions = new HashMap<String, Transition>();
		String[] machineLines = machine.split("\n");
		String startLine = machineLines[0], acceptLine = machineLines[1], rejectLine = machineLines[2];
		startState = startLine.substring(7);
		acceptStates = Arrays.asList(acceptLine.substring(8).split(", "));
		rejectStates = Arrays.asList(rejectLine.substring(8).split(", "));
		for (int i = 3; i < machineLines.length; i++) {
			String currentLine = machineLines[i];
			if (currentLine.contains("->")) {
				String[] halves = currentLine.split(" -> ");
				String fromState = halves[0].split(", ")[0];
				char fromChar = halves[0].charAt(halves[0].length()-1);
				String[] results =  halves[1].split(", ");
				Transition result = new Transition(results[2].indexOf('r') - results[2].indexOf('l'), results[1].charAt(0), results[0]);
				transitions.put(fromState+fromChar, result);
			}
		}
	}
	
	void initializeTape(String initialValue) {
		tape = new HashMap<Integer, Character>();
		for (int i = 0; i < initialValue.length(); i++) {
			tape.put(i, initialValue.charAt(i));
			maxIndex = i;
		}
		minIndex = 0;
		
	}
	
	Character readTape(int position) {
		if (!tape.containsKey(position))
			tape.put(position, '_');
		return tape.get(position);
	}
	
	void modifyTape(int index, Character newChar) {
		tape.put(index, newChar);
		if (index < minIndex)
			minIndex = index;
		else if (index > maxIndex)
			maxIndex = index;
	}
	
	String printTape() {
		String output = "";
		for (int i = Math.min(minIndex, index); i <= Math.max(maxIndex, index); i++)
			if (i == index)
				output += "(" + readTape(i) + ")";
			else
				output += readTape(i);
		return output;
	}
	
	void run (String initialTape, long maxTime) {
		initializeTape(initialTape);
		currentState = startState;
		index = 0;
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < maxTime) {
			System.out.println(printTape() + ", " + currentState);
			Character currentChar = readTape(index);
			if (transitions.containsKey(currentState+currentChar)) {
				Transition toApply = transitions.get(currentState+currentChar);
				modifyTape(index, toApply.character);
				currentState = toApply.nextstate;
				index += toApply.move;
				if (acceptStates.contains(currentState)) {
					System.out.println("Machine accepted with state " + currentState);
					return;
				}
				if (rejectStates.contains(currentState)) {
					System.out.println("Machine rejected with state " + currentState);
					return;
				}
			} else {
				System.err.println("Undefined transition occured");
				return;
			}
		}
		System.err.println("Machine ran too long, maximum length " + maxTime + "ms");
		return;
	}
	
	
	public static void main(String[]args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("File to read machine from: ");
		String fileName = sc.nextLine();
		String machine = "";
		try {
			File machineFile = new File(fileName);
			Scanner fileReader = new Scanner(machineFile);
			machine = fileReader.nextLine();
			while (fileReader.hasNextLine())
				machine += "\n" + fileReader.nextLine();
		} catch (Exception e) {}
		TuringMachine runner = new TuringMachine();
		runner.prepareMachine(machine);
		System.out.print("Enter timeout (in ms): ");
		long timeout = sc.nextLong();
		sc.nextLine();
		System.out.print("Enter an input to run the machine on: ");
		String toRun = sc.nextLine();
		do {
			runner.run(toRun, timeout);
			System.out.print("Enter another input to run machine on, or q to quit: ");
			toRun = sc.nextLine();
		} while (!toRun.equals("q"));
	}
}