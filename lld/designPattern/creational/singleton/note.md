**In software development, we often require classes that can only have one object.**

Creating more than one objects of these could lead to issues such as incorrect program behavior, overuse of resources, or inconsistent results.

one iconic way - 
make private constructor
provide static method for external object.


```
The instance class variable hold one instance Singleton class only.

The Singleton() constructor is declared as private, preventing extenral class to create object.

The getInstance() method is a static class method, making it accessible to the external world.
```
