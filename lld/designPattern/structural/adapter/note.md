The Adapter Design Pattern is a **structural design pattern** that allows incompatible interfaces to work together by converting the interface of one class into another that the client expects.


let say 2 different inteface/abstract want to talk and there we can use adapter in between.

like we are integrating 3rd party library in our codes now we add adapter in between so existing code and 3rd party can talk.

IReport - a abstract class that has one method getJSONData();

XMLDataProviderAdapter - a class which implements IReport

XMLDataProvvider - a 3rd party that will give data in XML format give XMLdata.



What is the Adapter Pattern?
The Adapter acts as a bridge between an incompatible interface and what the client actually expects.


Two Types of Adapters
There are two primary ways to implement an adapter, depending on the language and use case:

1. Object Adapter (Preferred in Java)
Uses composition: the adapter holds a reference to the adaptee (the object it wraps).
Allows flexibility and reuse across class hierarchies.
This is the most common and recommended approach in Java.
2. Class Adapter (Rare in Java)
Uses inheritance: the adapter inherits from both the target interface and the adaptee.
Requires multiple inheritance, which Java doesn’t support for classes.
More suitable for languages like C++.





