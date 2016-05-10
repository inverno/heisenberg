# heisenberg
A reflective, observer library.

Heisenberg allows you to react to any invocation performed on an object you observe.

Assuming that catView.update() prints "Cat has Changed!" to console :
```java
Cat cat = when(cat).isInvoked(let(catView).update());
cat.doAnything();
//console : "Cat has Changed!"
```
