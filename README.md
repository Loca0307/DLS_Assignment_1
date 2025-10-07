# Template for Assignment 1

This is a normal sbt project, like the one shown in Lecture 03. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL. 

## Authorship

Add your details in the `build.sbt` file in the developers field. The first element is your id, the second is the fullname, the third is the email. Leave the fourth as it is.

## Devcontainer

This version fixes the previous devcontainer which tried to install a bad version of java.
Using the devcontainer is not necessary to run the project. If you properly installed java, `sbt` and the `metals` extension for `vscode` (see lecture 03), you should be able to open the project and work on it without any issue. 


## Exercise 1

Put your diagrams inside the `./domaindatamodel` folder. The instruction in the assignment descriptions still hold. However, we have provided an additional way to submit the exercise, via plantuml. 

Plantuml is an external DSL that is normally used to describe UML diagrams. In this repository, we have provided a small theme (in the `domaindatamodel/domaindatamodeldiagram.puml`) for domain data models. 

If you decide to solve Exercise 1 with this notation, complete the diagrams in files `ex1.puml`. To edit `puml` files on `vscode`, install the `jebbs.plantuml` extension.

### Notation

The notation is very intuitive: you can define a set with the following syntax:

```
rectangle Name 
```

If you want to specify an abstract set:

```
rectangle MachineObject <<Abstract>>
```

Relations and multiplicities are specified via arrows:

```
MachineObject "!" --> "!" Name : name
```

* The first element is the domain of the relation;
* The second is the multiplicity on the domain side;
* The third is the arrow;
* The fourth is the multiplicity on the range side;
* The fifth is the range of the relation;
* The last element, after the column, is the relation name.

Specialization can be declared with an arrow in this form:

```
Event --|> AbstractEvent
```

### Layout

If you want to force the layout of elements, you can specify it in arrows:
```
-up->, -down->, -left->, -right->
```

This will try to position the target element of the relation in the direction specified by the arrow.

A full example of a domain data model specified with this notation is found in `domaindatamodel/gothicsecurity.puml`.

### Visualizing Domain Data Models

The development container in this repository contains the extension to visualize the diagrams created with plantuml, and all the dependent software (in particular, `graphviz`). To visualize a diagram, right-click on the editor when the file is open and select `Preview Current Diagram`.

## Exercise 2

Add your code in the `model` subpackage. Check the `FlightBookingSemanticModel.scala` file, and follow the instructions there.

## Exercise 3

Add your code in the `fluentapi` subpackage. Check the `FlightBookingFluentAPI.scala` file, and follow the instructions there.

