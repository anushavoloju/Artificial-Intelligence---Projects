To run the java program, please follow the  below steps :


1. Navigate to the folder containing com, txtfiles and readme.


2. Open Input.txt file in txtfiles folder.


3. Update the input file with the required input by following the below format. 
Update only the values of the variables and save the file.

#size of the gridworld

size : 4 3

#list of location of walls

walls : 2 2

#list of terminal states (row,column,reward)

terminal_states : 4 2 -1 , 4 3 +1

#reward in non-terminal states

reward : -0.04

#transition probabilities

transition_probabilities : 0.8 0.1 0.1 0

discount_rate : 1

epsilon : 0.001


4. Give the below command to compile the files.
javac com/MDP/ValueIteration.java


5. Give the below command to run the file.
java com/MDP/ValueIteration


6. The output is displayed with the iterations and best policy.


7.If you want to give another input, follow the same steps from Step 2.


