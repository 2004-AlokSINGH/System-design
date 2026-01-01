Lets say we are creating multiple object of same class. and that object creation is too complex and time consuming stting all its params.

***The Prototype Design Pattern is a creational design pattern that lets you create new objects by cloning existing ones, instead of instantiating them from scratch.***

The Challenge of Cloning Objects-
Imagine you have an object in your system, and you want to create an exact copy of it. How would you do it?

Your first instinct might be to:

Create a new object of the same class.
Manually copy each field from the original object to the new one.

```java
A a1= new A(); // takes 3 sec
A a2= new A(); // takes 3 sec.
```

Problem 1: Encapsulation Gets in the Way

This approach assumes that all fields of the object are publicly accessible. But in a well-designed system, many fields are private and hidden behind encapsulation. That means your cloning logic can’t access them directly.
Problem 2: Class-Level Dependency

Even if you could access all the fields, you'd still need to know the concrete class of the object to instantiate a copy.
This tightly couples your cloning logic to the object's class, which introduces problems:

It violates the Open/Closed Principle.
It reduces flexibility if the object's implementation changes.
It becomes harder to scale when you work with polymorphism.


In many cases, your code doesn’t work with concrete classes at all—it works with interfaces.

For example:

public void processClone(Shape shape) {
    Shape cloned = ???; // we only know it implements Shape
}
Here, you know the object implements a certain interface (Shape), but you don’t know what class it is, let alone how to create a new instance of it. You’re stuck unless the object knows how to clone itself.



```java
A a1 = new A();
A a2= a1.copy();
```

copy constructor:- create a object of same class object using other object of same class.

implementation:- create a abstract class cloneble/prototype with one clone method.
and any class which implement this -- meaning that class has clone method and can be clone.


Prototype Design Pattern comes in.

Instead of having external code copy or recreate the object, the object itself knows how to create its clone. It exposes a clone() or copy() method that returns a new instance with the same data.


now let  you have game character to a FlyingEnemy. You might write code like this:

Enemy flying1 = new Enemy("Flying", 100, 10.5, false, "Laser");
Enemy flying2 = new Enemy("Flying", 100, 10.5, false, "Laser");


***But Here’s the Problem
Repetitive Code: You’re duplicating the same instantiation logic again and again.
Scattered Defaults: If the default speed or weapon of FlyingEnemy changes, you need to update it in every single place you created one.
Error-Prone: Forget to set one property? Use a wrong value? Bugs will creep in silently.
Cluttered Codebase: Your main game loop or spawn logic becomes bloated with object construction details.
Hard to manage and maintain.***


**Instead of configuring every new object line-by-line, we define a pre-configured prototype and simply clone it whenever we need a new instance.**



```
A Quick Note on Cloning:
Shallow Copy: This implementation performs a shallow copy. It’s fine if all fields are primitives or immutable (like String). But if Enemy had a field like a List, both the original and cloned enemies would share the same list object, which can cause subtle bugs.
Deep Copy: If your object contains mutable reference types, you should create a deep copy in the copy constructor.
```