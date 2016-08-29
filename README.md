# Concurrency Example
A challenge to create a non-blocking task consumer with dependency between processes and tasks.  Written for Java 8, playing with streams and callable (from Concurrency library). 

Here is the challenge in original form:

```Question: There are two independent processes, Process A and Process B. Similarly there are five tasks, lets call them Task 1,2,3,4,5. All these tasks are independent of each other. Process A is supposed to complete all the five tasks however is dependent on Process B for Task 2. Once all the tasks are marked complete a message needs to be sent out(for now a simple system.out will do). Keep in mind this solution needs to be implemented for a trading application that witness vert high volumes and hence warrants to be non-blocking. This solution needs to be designed purely in core java.```
